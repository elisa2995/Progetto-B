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

    private static final int DEFAULT_DELAY = 10, LOOP_DELAY = 100;
    private final Object maxArmiesLock = new Object();
    private int maxArmiesAttack;
    private int maxArmiesDefense;
    private boolean maxArmiesSet;
    private boolean canAttack;
    private Action currentAction;
    private ArtificialPlayerSettings setting;

    /**
     * Create a new artificial player.
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

    public synchronized void setSetting(ArtificialPlayerSettings setting) {
        this.setting = setting;
    }

    /**
     * Plays a tris. In case the player can play more than one tris, it chooses
     * the best one (the one that awards more armies).
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
            tryNextPhase();
        }

    }

    /**
     * Moves armies between two adjacent territories.
     */
    private synchronized void moveArmies() {
        if (new Random().nextBoolean()) {

            List<String> myCountries = game.getMyCountries(this);
            String fromCountry = myCountries.get(new Random().nextInt(myCountries.size()));
            List<String> neighbors = game.getNeighbors(this, fromCountry);
            String toCountry = neighbors.get(new Random().nextInt(neighbors.size()));

            int max = game.getMaxArmiesForMovement(fromCountry, this);
            if (max > 0) {
                int nArmies = new Random().nextInt(max) + 1;
                game.move(fromCountry, toCountry, nArmies, this);
                return;
            }
        }
        tryNextPhase();
    }

    /**
     * Uses its bonusArmies to reinforce random territories. If the player runs
     * out of bonus armies, it changes the phase.
     */
    private synchronized void randomReinforce() {
        if (bonusArmies == 0) {
            tryNextPhase();
            return;
        }
        List<String> myCountries = game.getMyCountries(this);
        for (int i = 0; i < bonusArmies; i++) {
            game.reinforce(myCountries.get(new Random().nextInt(myCountries.size())), this);
            waitMs(setting.getReinforceDelay());
        }
    }

    /**
     * Executes a series of random attacks.
     */
    private synchronized void randomAttack() {
        int nrAttacks = new Random().nextInt(setting.getBaseAttack()) + 1;
        while (nrAttacks > 0 && game.getAllAttackers(this).length != 0) {
            if (canAttack) {
                randomSingleAttack();
                nrAttacks--;
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

        index = new Random().nextInt(myCountries.length);
        game.setAttackerCountry(myCountries[index], this);
        opponentCountries = game.getAllDefenders(myCountries[index]);

        defendIndex = new Random().nextInt(opponentCountries.length);
        game.setDefenderCountry(opponentCountries[defendIndex], this);

        waitMs(DEFAULT_DELAY);

        if (maxArmiesSet) {
            int nrA = new Random().nextInt(maxArmiesAttack) + 1;
            game.setAttackerArmies(nrA, this);
        } else {
            game.setAttackerArmies(-1, this);
        }

        game.declareAttack(this);

        waitMs(setting.getAttackDeclarationDelay());
    }

    /**
     * Chooses the number of armies for the defense and confirms the attack.
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
     * Contains the loop for the artificial player to act. The player keeps
     * checking if it's its turn and in case acts accodingly to the phase of the
     * game.
     */
    @Override
    public void run() {
        synchronized (this) {
            waitMs(LOOP_DELAY);
        }
        while (currentAction != Action.ENDGAME) {
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
                        tryNextPhase();
                        break;
                    case "MOVE":
                        moveArmies();
                        synchronized (this) {
                            waitMs(LOOP_DELAY);
                        }
                        break;
                }
            } else if (this.currentAction == Action.DEFEND) {
                this.defend();
            }
        }
    }

    /**
     * Updates maxArmiesAttack and maxArmiesDefense.
     *
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
     * Updates currentAction. If it has conquered a country moves some armies
     * from attacker to defender.
     *
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
     * Updates currentAction to <code>ENDGAME</code> if someone wins.
     *
     * @param winner
     */
    @Override
    public void updateOnVictory(String winner) {
        this.currentAction = Action.ENDGAME;
    }

    /**
     * Updates currentAction to <code>DEFEND</code> when ArtificialPlayer is
     * attacked.
     *
     * @param defenderCountryInfo
     */
    @Override
    public void updateOnDefend(CountryInfo defenderCountryInfo) {
        if (this.getName().equals(defenderCountryInfo.getPlayerName())) {
            this.currentAction = Action.DEFEND;
        }
    }

    /**
     * Updates currentAction to <code>ENDGAME</code> when ArtificialPlayer is
     * eliminated.
     *
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
     * Updates currentAction when the game ends.
     */
    @Override
    public void updateOnEndGame() {
        this.currentAction = Action.ENDGAME;
    }

    /**
     * Tries to change the phase.
     */
    private void tryNextPhase() {
        try {
            game.nextPhase(this);
        } catch (PendingOperationsException ex) {
        }
    }

    /**
     * Waits <code>ms</code> milliseconds.
     *
     * @param ms
     */
    private void waitMs(int ms) {
        try {
            this.wait(ms);
        } catch (InterruptedException ex) {
        }
    }

}
