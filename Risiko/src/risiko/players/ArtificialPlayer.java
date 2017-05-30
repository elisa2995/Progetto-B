package risiko.players;

import exceptions.PendingOperationsException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import risiko.Action;
import risiko.GameProxy;
import utils.BasicGameObserver;

public class ArtificialPlayer extends Player implements Runnable, BasicGameObserver {

    private GameProxy game;

    int maxArmiesAttack;
    int maxArmiesDefense;
    boolean maxArmiesSet;
    boolean canAttack;

    private int attackRate = 2000;
    private Action currentAction;
    private int reinforceSpeed = 2000;
    private ArtificialPlayerSettings setting;
    private int declareSpeed = 1000;
    private int attackSpeed = 1000;

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
     *
     * @param name
     * @param color
     * @param game
     */
    public ArtificialPlayer(String name, String color, GameProxy game) {
        super(name, color);
        this.game = game;
        currentAction = Action.NOACTION;
        setting = new ArtificialPlayerSettings();
        setting.setBaseAttack(5);
        setting.setAttackDeclarationDelay(500);
        setting.setReinforceDelay(100);
        setting.setAttackDelay(500);
    }

    /**
     * aggiunge armate ai territori posseduti fino a che non sono esaurite
     */
    private synchronized void randomReinforce() {

        if (bonusArmies == 0) {
            try {
                game.nextPhase(this);
            } catch (PendingOperationsException ex) {
                Logger.getLogger(ArtificialPlayer.class.getName()).log(Level.SEVERE, null, ex);
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
     * esegue un attacco
     */
    private synchronized void randomAttack() {
        int i = setting.getBaseAttack();

        while (i > 0 && game.getAllAttackers(this).length != 0 && canAttack) {
            randomSingleAttack();
            i--;
        }
    }

    /**
     * esegue un attacco per turno
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
        try {
            this.wait(setting.getAttackDelay());
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * esegue il turno del giocatore artificiale
     */
    @Override
    public void run() {
        while (currentAction != Action.ENDGAME) {
            try {
                if (game.getActivePlayer().equals(this)) {
                    //System.out.println(game.getPhase()+this.getName());
                    switch (game.getPhase()) {
                        case REINFORCE:
                            randomReinforce();
                            break;
                        case FIGHT:
                            canAttack = true;
                            this.randomAttack();
                            game.nextPhase(this);
                            break;
                        case MOVE:
                            game.nextPhase(this);
                            break;
                    }
                } else if (this.currentAction == Action.DEFEND) {
                    //System.out.println(Action.DEFEND);
                    this.defend();
                }
            } catch (PendingOperationsException ex) {
                //l'eccezione delle armate ancora da assegnare viene data quando si sovrappongono le operazioni di 2 giocatori
                Logger.getLogger(ArtificialPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void updateOnSetDefender(String countryAttackerName, String countryDefenderName, String defenderPlayer, int maxArmiesAttacker, int maxArmiesDefender) {
        this.maxArmiesAttack = maxArmiesAttacker;
        this.maxArmiesDefense = maxArmiesDefender;
        this.maxArmiesSet = true;
    }

    @Override
    public void updateOnAttackResult(String attackResultInfo, boolean isConquered, boolean canAttackFromCountry, int maxArmiesAttacker, int maxArmiesDefender, int[] attackerDice, int[] defenderDice) {
        if (this.currentAction == Action.DEFEND) {
            // Se ero in difesa una volta ricevuto il risultato dell'attacco, passo alla fase di no-action
            this.currentAction = Action.NOACTION;
        }
        canAttack = true;
    }

    @Override
    public void updateOnVictory(String winner) {
        this.currentAction = Action.ENDGAME;
    }

    public void updateOnDefend(String defender, String countryDefender, String attacker, String countryAttacker, int nrA, boolean isArtificialPlayer) {
        if (this.getName().equals(defender)) {
            this.currentAction = Action.DEFEND;
        }

    }

}
