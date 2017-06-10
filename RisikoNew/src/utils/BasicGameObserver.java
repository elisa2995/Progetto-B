package utils;

import shared.CountryInfo;

public interface BasicGameObserver {

    /**
     * Method that updates the observer as soon as the attacker has chosen which
     * country to attack.
     *
     * @param countryAttackerName
     * @param countryDefenderName
     * @param defenderPlayer
     * @param maxArmiesAttacker
     * @param maxArmiesDefender
     * @param reattack
     */
    //public void updateOnSetDefender(String countryAttackerName, String countryDefenderName, String defenderPlayer, int maxArmiesAttacker, int maxArmiesDefender, boolean reattack);
    public void updateOnSetDefender(CountryInfo[] fightingCountries, boolean reattack);
    /**
     * Method called on the victory of a player.
     *
     * @param winMessage message that notifies the victory.
     */
    public void updateOnVictory(String winMessage);

    /**
     * Method called whenever an attack produces a result.
     *
     * @param conquered
     * @param canAttackFromCountry
     * @param maxArmiesAttacker
     * @param maxArmiesDefender
     * @param attackerDice
     * @param defenderDice
     * @param artificialAttack
     * @param attackerCountryName
     * @param defenderCountryName
     * @param conqueredContinent
     */
    public void updateOnAttackResult(boolean conquered, boolean canAttackFromCountry, int maxArmiesAttacker, int maxArmiesDefender, int[] attackerDice, int[] defenderDice, boolean[] artificialAttack, String attackerCountryName, String defenderCountryName, String conqueredContinent);
    //public void updateOnAttackResult();
    /**
     * Method called as soon as the attacker has chosen how many armies to use
     * in a fight.
     *
     * @param defender
     * @param defenderCountry
     * @param attacker
     * @param attackerCountry
     * @param nrA
     * @param artificialPlayer
     */
    public void updateOnDefend(String defender, String defenderCountry, String attacker, String attackerCountry, int nrA, boolean artificialPlayer);

    /**
     * Method called to update the observer when a player (<code> defender</code>)
     * is eliminated from the game.
     * @param defenderName
     * @param artificialAttack 
     */
    public void updateOnElimination(String defenderName, boolean artificialAttack);

    /**
     * Method called to update the observer when the game ends.
     */
    public void updateOnEndGame();
}
