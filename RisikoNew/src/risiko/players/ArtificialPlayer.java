package risiko.players;

import exceptions.PendingOperationsException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import risiko.game.GameProxy;
import shared.AttackResultInfo;
import shared.CountryInfo;
import utils.BasicGameObserver;

/**
 * An IA player.
 */
public class ArtificialPlayer extends Player implements Runnable, BasicGameObserver {

    private GameProxy game;

    private final Object maxArmiesLock = new Object();
    private int maxArmiesAttack;
    private int maxArmiesDefense;
    private boolean maxArmiesSet;
    private boolean canAttack;
    private Action currentAction;
    private ArtificialPlayerSettings setting;

    public void setSetting(ArtificialPlayerSettings setting) {
        this.setting = setting;
    }

    /**
     * Create a new artificial player
     *
     * @param name name of the player
     * @param color color assigned to the player
     * @param game proxy used to call the various action
     */
    public ArtificialPlayer(String name, String color, GameProxy game) {
        super(name, color);
        this.game = game;
        currentAction = Action.NOACTION;
        setting = new ArtificialPlayerSettings("Lento");
    }

    /**
     * Tries to play a tris from the best to the worst.
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
        } else {
            try {
                game.nextPhase(this);
            } catch (PendingOperationsException ex) {
                Logger.getLogger(ArtificialPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * Moves armies between two territories.
     */
    private synchronized void moveArmies() {
        if (new Random().nextBoolean()) {
            
            List<String> myCountries = game.getMyCountries(this);
            int from = new Random().nextInt(myCountries.size());
            String fromCountry = myCountries.get(from);
            List<String> neighbors = game.getNeighbors(this, fromCountry);
            int to = new Random().nextInt(neighbors.size());
            String toCountry = neighbors.get(to);

            int max = game.getMaxArmiesForMovement(fromCountry, this);
            if (max > 0) {
                int nArmies = new Random().nextInt(max) + 1;
                game.move(fromCountry, toCountry, nArmies, this);
            }
        }
    }

    /**
     * Adds armies to the territories until the are exhausted.
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
        List<String> myCountries = game.getMyCountries(this);
        for (int i = 0; i < bonusArmies; i++) {
            index = new Random().nextInt(myCountries.size());
            game.reinforce(myCountries.get(index), this);

            try {
                this.wait(setting.getReinforceDelay());
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Executes a series of attacks.
     */
    private synchronized void randomAttack() {
        int i = new Random().nextInt(setting.getBaseAttack());
        i++;
        while (i > 0 && game.getAllAttackers(this).length != 0) {
            if (canAttack) {
                randomSingleAttack();
                i--;
            }
        }
    }

    /**
     * Executes a single attack.
     */
    private synchronized void randomSingleAttack() {
        setMaxArmiesSet(false);
        canAttack = false;
        String[] myCountries = game.getAllAttackers(this);
        String[] opponentCountries;
        int index, defendIndex;
        int tries = 10;

        do {

            index = new Random().nextInt(myCountries.length);
            game.setAttackerCountry(myCountries[index], this);

            opponentCountries = game.getAllDefenders(myCountries[index]);
            tries--;
        } while (opponentCountries.length == 0 && tries > 0);

        if (tries > 0) {
            defendIndex = new Random().nextInt(opponentCountries.length);
            game.setDefenderCountry(opponentCountries[defendIndex], this);

            try {
                this.wait(10);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            if (maxArmiesSet) {
                int nrA = new Random().nextInt(maxArmiesAttack) + 1;
                game.setAttackerArmies(nrA, this);
            } else {
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
     * Lets the artificial player defend itself from an attack. It sets the
     * number of armies with which the artificial player defends and confim the
     * attack.
     */
    private synchronized void defend() {
        synchronized (maxArmiesLock) {
            if (!maxArmiesSet) {
                return;
            }
            int nrD = new Random().nextInt(this.maxArmiesDefense) + 1;
            game.confirmAttack(nrD, this);
            this.currentAction = Action.NOACTION;

            setMaxArmiesSet(false);
        }
    }

    /**
     * Contains the loop for the artificial player to act.
     */
    @Override
    public void run() {
        while (currentAction != Action.ENDGAME) {

            try {
                if (game.checkMyIdentity(this)) {
                    switch (game.getPhase()) {
                        case "PLAY_CARDS":
                            playHighestTris();
                            break;
                        case "REINFORCE":
                            randomReinforce();
                            break;
                        case "FIGHT":
                            canAttack = true;
                            this.randomAttack();
                            game.nextPhase(this);
                            break;
                        case "MOVE":
                            moveArmies();
                            synchronized (this) {
                                this.wait(100);
                            }
                            break;
                    }
                } else if (this.currentAction == Action.DEFEND) {
                    this.defend();
                }
            } catch (PendingOperationsException ex) {
            } catch (InterruptedException ex) {
                Logger.getLogger(ArtificialPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Updates maxArmiesAttack and maxArmiesDefense
     * @param countries
     * @param reattack 
     */
    @Override
    public void updateOnSetDefender(CountryInfo[] countries, boolean reattack) {
        this.maxArmiesAttack = countries[0].getMaxArmies();
        this.maxArmiesDefense = countries[1].getMaxArmies();
        setMaxArmiesSet(true);
    }

    private void setMaxArmiesSet(boolean cond) {
        synchronized (maxArmiesLock) {
            this.maxArmiesSet = cond;
        }
    }

    /**
     * Updates currentAction. If has conquered a country moves armies from attacker to defender.
     * @param ar 
     */
    @Override
    public void updateOnAttackResult(AttackResultInfo ar) {
        if (this.currentAction == Action.DEFEND) {
            this.currentAction = Action.NOACTION;
        }
        if (ar.hasConquered()) {
            game.move(ar.getAttackerCountryName(), ar.getDefenderCountryName(), ar.getMaxArmiesAttacker(), this);
        }
        canAttack = true;
    }

    /**
     * Updates currentAction to ENDGAME if someone wins.
     * @param winner 
     */
    @Override
    public void updateOnVictory(String winner) {
        this.currentAction = Action.ENDGAME;
    }

    /**
     * Updates currentAction to DEFEND when ArtificialPlayer is attacked.
     * @param defenderCountryInfo 
     */
    @Override
    public void updateOnDefend(CountryInfo defenderCountryInfo) {
        if (this.getName().equals(defenderCountryInfo.getPlayerName())) {
            this.currentAction = Action.DEFEND;
        }
    }

    /**
     * Updates currentAction to ENDGAME when ArtificialPlayer is eliminated.
     * @param defenderName
     * @param artificialAttack 
     */
    @Override
    public void updateOnElimination(String defenderName, boolean artificialAttack) {
        if (this.getName().equals(defenderName)) {
            this.currentAction = Action.ENDGAME;
        }
    }

    /**
     * Updates currentAction when game ends.
     */
    @Override
    public void updateOnEndGame() {
        this.currentAction = Action.ENDGAME;
    }

}
