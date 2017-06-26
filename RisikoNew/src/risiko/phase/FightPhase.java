package risiko.phase;

import exceptions.PlayerLossException;
import exceptions.WrongCallerException;
import risiko.equipment.Dice;
import risiko.map.Country;
import risiko.map.RisikoMap;
import risiko.players.ArtificialPlayer;

/**
 * Class that represents the phase of the game in which the active player can
 * attack its enemies.
 */
public class FightPhase extends Phase {

    private Country attackerCountry, defenderCountry;
    private boolean reattack, attackInProgress;
    private int nrA, nrD;
    private Dice dice;

    public FightPhase(RisikoMap map) {
        super(map);
        dice = new Dice();
        this.index = FIGHT_INDEX;
    }

    public Country getAttackerCountry() {
        return attackerCountry;
    }

    public Country getDefenderCountry() {
        return defenderCountry;
    }

    public int getDefenderArmies() {
        return nrD;
    }

    public int getAttackerArmies() {
        return nrA;
    }

    /**
     * Sets the attacker.
     *
     * @param attackerCountryName
     */
    public void setAttackerCountry(String attackerCountryName) {
        this.attackerCountry = map.getCountryByName(attackerCountryName);
    }

    /**
     * Sets the defender.
     *
     * @param defenderCountryName
     */
    public void setDefenderCountry(String defenderCountryName) {
        this.defenderCountry = map.getCountryByName(defenderCountryName);
    }

    /**
     * Sets the attribute <code>reattack</code>.
     *
     * @param reattack
     */
    public void setReattack(boolean reattack) {
        this.reattack = reattack;
    }

    /**
     * Resets the fighting countries.
     */
    public void resetFightingCountries() {
        clear();
    }

    /**
     * Simulates a battle between 2 armies. It removes the armies lost from each
     * country.
     *
     */
    private void fight() {
        int lostArmies[] = computeLostArmies();
        attackerCountry.removeArmies(lostArmies[0]);
        defenderCountry.removeArmies(lostArmies[1]);
    }

    /**
     * Computes the number of armies lost during a battle.
     *
     * @return 2 elements array of which the first one represents the number of
     * armies lost by the attacker, while the second one represents the number
     * of armies lost by the defende.
     */
    private int[] computeLostArmies() {
        dice.rollAllDice(nrA, nrD);
        int lostArmies[] = new int[2];
        for (int i = 0; i < Math.min(nrA, nrD); i++) {
            if (dice.getAttackerDice()[i] > dice.getDefenderDice()[i]) {
                lostArmies[1]++;
            } else {
                lostArmies[0]++;
            }
        }
        return lostArmies;
    }

    /**
     * Sets the number of armies for the defense.
     *
     * @param nrD
     * @param aiCaller
     */
    public void setDefenderArmies(int nrD, ArtificialPlayer... aiCaller) {
        if (!canCallDefenseMethods(aiCaller)) {
            return;
        }

        if (nrD == -1) {
            this.nrD = defenderCountry.getMaxArmies(false);
        } else {
            this.nrD = nrD;
        }
    }

    /**
     * Sets the number of armies for the attack.
     *
     * @param nrA
     */
    public void setAttackerArmies(int nrA) {
        if (nrA == -1) {
            this.nrA = attackerCountry.getMaxArmies(true);
        } else {
            this.nrA = nrA;
        }
    }

    /**
     * Declares an attack from <code>attackerCountry</code> to
     * <code>defenderCountry</code>. Notifies the defender so that it can choose
     * the number of armies for the defense.
     *
     * @param aiCaller
     */
    public void declareAttack(ArtificialPlayer... aiCaller) {
        attackInProgress = true;
    }

    /**
     * Performs the attack.
     *
     * @param nrD
     * @param aiCaller
     * @throws exceptions.WrongCallerException
     * @throws exceptions.PlayerLossException
     */
    public void confirmAttack(int nrD, ArtificialPlayer... aiCaller) throws WrongCallerException, PlayerLossException {
        if (!canCallDefenseMethods(aiCaller)) {
            throw new WrongCallerException();
        }
        attackInProgress = false;
        this.nrD = nrD;
        fight();
        checkCountryConquest();
    }

    /**
     * Returns the 2 sets of dice.
     *
     * @return
     */
    public int[][] getDice() {
        return dice.getDice();
    }

    /**
     * Returns true if <code>defenderCountry</code> has been defeated.
     *
     * @return
     */
    public boolean hasConquered() {
        return map.isConquered(defenderCountry);
    }

    /**
     * Tells wheter the caller of the method has the right to confirm the attack
     * (it has to be the owner of defenderCountry).
     *
     * @param aiCaller
     * @return true if it has the right to call confirmAttack, false otherwise.
     */
    private boolean canCallDefenseMethods(ArtificialPlayer... aiCaller) {
        boolean artificialDefender = defenderCountry.getOwner() instanceof ArtificialPlayer;
        boolean rightCaller = (aiCaller.length == 0) ? !artificialDefender : artificialDefender && aiCaller[0].equals(defenderCountry.getOwner());
        return attackInProgress && rightCaller;
    }

    /**
     * Checks if the country in defense has been conquered and acts accordingly.
     */
    private void checkCountryConquest() throws PlayerLossException {
        if (map.isConquered(defenderCountry)) {
            String defender = defenderCountry.getOwner().toString();
            attackerCountry.conquer(defenderCountry);
            throw new PlayerLossException(defender);
        }
    }

    /**
     * Returns the name of the contintnet that has just been conquered (or null
     * if no continent was conquered).
     *
     * @return
     */
    public String checkContinentConquest() {
        String continent = null;
        if (map.hasConqueredContinent(defenderCountry)) {
            continent = map.getContinentByCountry(defenderCountry).toString();
        }
        return continent;
    }

    /**
     * Returns true if the current phase of the game is fightPhase and if both
     * attackerCountry and defenderCountry have been set.
     *
     * @param index
     * @return
     */
    public boolean isReadyToFight(int index) {
        return index == this.index && attackerCountry != null && defenderCountry != null;
    }

    /**
     * Returns <code>attackInProgress</code>.
     *
     * @return
     */
    public boolean isAttackInProgress() {
        return attackInProgress;
    }

    /**
     * Checks if the defender is valid.
     *
     * @param defenderCountryName
     * @return
     */
    public boolean controlDefender(String defenderCountryName) {
        return attackerCountry.controlDefender(map.getCountryByName(defenderCountryName));
    }

    public boolean reattack() {
        return reattack;
    }

    public int getMaxArmies(Country country, boolean isAttacker) {
        return country.getMaxArmies(isAttacker);
    }

    @Override
    public String toString() {
        return "FIGHT";
    }

    @Override
    public void clear() {
        this.attackerCountry = null;
        this.defenderCountry = null;
    }
}
