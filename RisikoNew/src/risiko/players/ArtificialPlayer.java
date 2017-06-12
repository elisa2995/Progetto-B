package risiko.players;

import exceptions.PendingOperationsException;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import risiko.game.GameProxy;
import shared.AttackResultInfo;
import shared.CountryInfo;
import utils.BasicGameObserver;

public class ArtificialPlayer extends Player implements Runnable, BasicGameObserver {

    private GameProxy game;

    private int maxArmiesAttack;
    private int maxArmiesDefense;
    private boolean maxArmiesSet;
    private boolean canAttack;
    private Action currentAction;
    private ArtificialPlayerSettings setting;
    private int reinforceSpeed;
    private int attacksRemaining;

    public void setSetting(ArtificialPlayerSettings setting) {
        this.setting = setting;
    }

    /**
     * crea un delay tra successivi rinforzi di un giocatore artificiale
     *
     * @param reinforceSpeed tempo in ms tra un rinforzo e l'altro
     */
    public void setReinforceSpeed(int reinforceSpeed) {
        this.reinforceSpeed = reinforceSpeed;
    }

    /**
     * create a new artificial player
     * @param name name of the player
     * @param color color assigned to the player
     * @param game proxy used to call the various action
     */
    public ArtificialPlayer(String name, String color, GameProxy game) {
        super(name, color);
        this.game = game;
        currentAction = Action.NOACTION;
        setting = new ArtificialPlayerSettings();
        setting.setBaseAttack(5);
        setting.setAttackDeclarationDelay(100);
        setting.setReinforceDelay(100);
        setting.setAttackDelay(100);
    }

    /**
     * tries to play a tris from the best to the worst
     */
    private synchronized void playHighestTris() {
        int max = -1;
        String[] trisToPlay = null;

        Map<String[], Integer> playableTris = game.getPlayableTris();
        for (Map.Entry<String[], Integer> entry : playableTris.entrySet()) {
            if (max < entry.getValue()) {
                max = entry.getValue();
                trisToPlay = entry.getKey();
            }
        }
        if (trisToPlay != null) {
            game.playTris(trisToPlay, this);
        }else{
            try {
                game.nextPhase(this);
            } catch (PendingOperationsException ex) {
                Logger.getLogger(ArtificialPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    /**
     * moves armies between territories
     */
    private synchronized void moveArmies(){
        String[] myCountries = game.getMyCountries(this);
        int from = new Random().nextInt(myCountries.length);
        int to = new Random().nextInt(myCountries.length);

        if (from != to) {
            int max = game.getMaxArmiesForMovement(myCountries[from], this);
            if (max > 0) {
                int nArmies = new Random().nextInt(max);
                if(nArmies != 0){
                    game.move(myCountries[from], myCountries[to], nArmies, this);
                }
            }
        }

    }
    

    /**
     * add armies to the territories until the are exhausted
     */
    private synchronized void randomReinforce() {
        if (bonusArmies == 0) {
            try {
                game.nextPhase(this);
            } catch (PendingOperationsException ex) {
                //Logger.getLogger(ArtificialPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        int index;
        String[] myCountries = game.getMyCountries(this);
        for (int i = 0; i < bonusArmies; i++) {
            index = new Random().nextInt(myCountries.length);
            game.reinforce(myCountries[index], this);

            try {
                this.wait(setting.getReinforceDelay());
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * executes a series of attacks
     */
    private synchronized void randomAttack() {
        int i = setting.getBaseAttack();

        while (i > 0 && game.getAllAttackers(this).length != 0 ) {
            if (canAttack) {
                randomSingleAttack();
                i--;
            }
        }
    }

    /**
     * execute a single attack
     */
    private synchronized void randomSingleAttack() {
        maxArmiesSet = false;
        canAttack = false;
        String[] myCountries = game.getAllAttackers(this);
        String[] opponentCountries;
        int index, defendIndex;
        int tries = 10;

        do {
            //set country attacco
            index = new Random().nextInt(myCountries.length);
            game.setAttackerCountry(myCountries[index], this);

            //set country difesa
            opponentCountries = game.getAllDefenders(myCountries[index]);
            tries--;
        } while (opponentCountries.length == 0 && tries > 0);
        /* Esco quando ho trovato un mio territorio con almeno un territorio
        attaccabile come vicino */
        if (tries > 0) {
            defendIndex = new Random().nextInt(opponentCountries.length);
            game.setDefenderCountry(opponentCountries[defendIndex], this);
            //aspetta la risposta da game per il settaggio di max armies
            try {
                this.wait(10);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            //set armate attacco
            //int nrA = new Random().nextInt(game.getMaxArmies(myCountries[index], true, this));
            //game.setAttackerArmies(nrA, this);
            if (maxArmiesSet) { // Perché?
                int nrA = new Random().nextInt(maxArmiesAttack) + 1;
                game.setAttackerArmies(nrA, this);
            } else {
                //se maxarmiesset non è true attacco con il massimo numero di armate
                game.setAttackerArmies(-1, this);
            }

            game.declareAttack(this);

            try {
                this.wait(setting.getAttackDeclarationDelay());
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * let the artificial player defend itself from an attack
     */
    private synchronized void defend() {
        if (!maxArmiesSet) {
            return;
        }
        /*if (maxArmiesSet) {
            int nrD = new Random().nextInt(this.maxArmiesDefense)+1;
            game.setDefenderArmies(nrD, this);
        } else {
            //se maxarmiesset non è true difendo con il massimo numero di armate
            game.setDefenderArmies(-1, this);
        }*/
        int nrD = new Random().nextInt(this.maxArmiesDefense) + 1;
        game.setDefenderArmies(nrD, this);
        game.confirmAttack(this);
        this.currentAction = Action.NOACTION;
        try {
            this.wait(setting.getAttackDelay());
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        maxArmiesSet=false;
    }

    /**
     * contains the loop for the artificial player to act
     */
    @Override
    public void run() {
        while (currentAction != Action.ENDGAME) {

            try {
                if (game.getActivePlayer().equals(this)) {
                    switch (game.getPhase()) {
                        case PLAY_CARDS:
                            playHighestTris();
                            break;
                        case REINFORCE:
                            randomReinforce();
                            break;
                        case FIGHT:
                            canAttack = true;  
                            this.randomAttack();
                            game.nextPhase(this);
                            break;
                        case MOVE:
                            moveArmies();
                            synchronized(this) {
                                this.wait(100);
                            }
                            break;
                    }
                } else if (this.currentAction == Action.DEFEND) {
                    this.defend();
                }
            } catch (PendingOperationsException ex) {
                //l'eccezione delle armate ancora da assegnare viene data quando si sovrappongono le operazioni di 2 giocatori
                //Logger.getLogger(ArtificialPlayer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(ArtificialPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void updateOnSetDefender(CountryInfo[] countries, boolean reattack) {
        this.maxArmiesAttack = countries[0].getMaxArmies();
        this.maxArmiesDefense = countries[1].getMaxArmies();
        this.maxArmiesSet = true;
    }

    @Override
    public void updateOnAttackResult(AttackResultInfo ar) {
        if (this.currentAction == Action.DEFEND) {
            // Se ero in difesa una volta ricevuto il risultato dell'attacco, passo alla fase di no-action
            this.currentAction = Action.NOACTION;
        }
        if(ar.hasConquered()){
            game.move(ar.getAttackerCountryName(), ar.getDefenderCountryName(), ar.getMaxArmiesAttacker(), this);
        }
        canAttack = true;
    }

    @Override
    public void updateOnVictory(String winner) {
        this.currentAction = Action.ENDGAME;
    }

    @Override
    public void updateOnDefend(CountryInfo defenderCountryInfo) {
        if (this.getName().equals(defenderCountryInfo.getPlayerName())) {
            this.currentAction = Action.DEFEND;
        }

    }

    @Override
    public void updateOnElimination(String defenderName, boolean artificialAttack) {
        if (this.getName().equals(defenderName)) {
            this.currentAction = Action.ENDGAME;
        }
    }

    @Override
    public void updateOnEndGame() {
        this.currentAction = Action.ENDGAME;
    }

}
