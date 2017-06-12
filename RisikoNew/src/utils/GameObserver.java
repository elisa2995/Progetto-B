package utils;

import java.awt.Color;
import java.util.List;
import risiko.players.Player;
import shared.CountryInfo;
import shared.PlayerInfo;

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
     */
    public void updateOnPhaseChange(PlayerInfo player, String phase);

    /**
     * Updates the observer whenever the active player chooses the attacker for
     * a fight.
     * @param attackerInfo
     */
    public void updateOnSetAttacker(CountryInfo attackerInfo);

    /**
     * Informs the observer of the initial country assignment.
     * @param countriesInfo
     */
    public void updateOnCountriesAssignment(CountryInfo[] countriesInfo);

    /**
     * Method called whenever the number of armies of a country changes.
     * @param country
     */
    public void updateOnArmiesChange(CountryInfo country);

    /**
     * Updates the observer when the activePlayer passes its turn.
     * @param cards
     */
    public void updateOnNextTurn(List<String> cards);
    
    /**
     * Updates the observer when the activePlayer plays a tris.
     */
    public void updateOnPlayedTris();

    /**
     * Updates the observer when the activePlayer has chosen from which country
     * to move its armies.
     * @param country
     */
    public void updateOnSetFromCountry(String country);
    
    /**
     * Tells the observer which card has just been drawn.
     * @param card
     * @param isArtificialPlayer 
     */
    public void updateOnDrawnCard(String card, boolean isArtificialPlayer);

}
