package utils;

/**
 * A class can implement the <code>GameObserver</code> interface when it wants
 * to be informed of changes in <code>utils.Observable</code> objects.
 */
public interface GameObserver extends BasicGameObserver {

    /**
     * Method called whenever a country gets reinforced.
     *
     * @param bonusArmies the remaining bonusArmies of the activePlayer.
     */
    public void updateOnReinforce(int bonusArmies);

    /**
     * Updates the observer when the phase changes.
     * @param player
     * @param phase
     * @param color
     * @param bonusArmies
     */
    public void updateOnPhaseChange(String player, String phase, String color, int bonusArmies);

    /**
     * Updates the observer whenever the active player chooses the attacker for
     * a fight.
     * @param countryName
     * @param maxArmiesAttacker
     * @param attacker
     * @param color
     */
    public void updateOnSetAttacker(String countryName, int maxArmiesAttacker, String attacker, String color);

    /**
     * Informs the observer of the initial country assignment.
     */
    public void updateOnCountryAssignment(String[] countries, int[] armies, String[] colors);

    /**
     * Method called whenever the number of armies of a country changes.
     */
    public void updateOnArmiesChange(String country, int armies, String color);

    /**
     * Updates the observer when the activePlayer passes its turn.
     */
    public void updateOnNextTurn();

    /**
     * Updates the observer when the activePlayer has chosen from which country
     * to move its armies.
     */
    public void updateOnSetFromCountry(String country);
    
    /**
     * Tells the observer which card has just been drawn.
     * @param cardName 
     */
    public void updateOnDrawnCard(String card, boolean isArtificialPlayer);
}
