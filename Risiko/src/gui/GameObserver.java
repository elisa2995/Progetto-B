
package gui;

import java.awt.Color;

/**
 * A class can implement the <code>Observer</code> interface when it
 * wants to be informed of changes in observable objects.
 *
 * @author  Chris Warth
 * @see     java.util.Observable
 * @since   JDK1.0
 */
public interface GameObserver {

    /**
     * Metodo chiamato dopo la fase di rinforzo
     * @param countryName
     * @param bonusArmies 
     */
    public void updateOnReinforce(String countryName, int bonusArmies);

    /**
     * Metodo chiamato al cambiamento della fase di gioco
     * @param player
     * @param phase 
     */
    public void updateOnPhaseChange(String player, String phase);

    /**
     * Metodo chiamato quando viene settato l'attaccante
     * @param countryName 
     */
    public void updateOnSetAttacker(String countryName);

    /**
     * Metodo chiamato quando viene settato il difensore
     * @param countryAttackerName
     * @param countryDefenderName
     * @param defenderPlayer
     * @param maxArmiesAttacker
     * @param maxArmiesDefender 
     */
    public void updateOnSetDefender(String countryAttackerName, String countryDefenderName, String defenderPlayer, int maxArmiesAttacker, int maxArmiesDefender);

    /**
     * Metodo chiamato dopo un attacco
     * @param attackResultInfo
     * @param isConquered
     * @param canAttackFromCountry
     * @param maxArmiesAttacker
     * @param maxArmiesDefender
     * @param attackerDice
     * @param defenderDice 
     */
    public void updateOnAttackResult(String attackResultInfo, boolean isConquered, boolean canAttackFromCountry, int maxArmiesAttacker, int maxArmiesDefender, int[] attackerDice, int[] defenderDice);
    
    /**
     * Metodo chiamato in caso di vittoria
     * @param winner 
     */
    public void updateOnVictory(String winner);

    /**
     * Metodo chiamato dopo l'assegnazione delle country
     * @param countries
     * @param armies
     * @param colors 
     */
    public void updateOnCountryAssignment(String[] countries, int[] armies, Color[] colors);
    
    /**
     * Metodo chiamato dopo uno spostamento di armate
     * @param country
     * @param armies
     * @param color 
     */
    public void updateOnArmiesChange(String country, int armies, Color color);
    
    /**
     * metodo chiamato per richiedere al difensore con quante armate difendersi
     * @param defender
     * @param countryDefender
     * @param nrA 
     */
    public void updateOnDefend(String defender, String countryDefender, String attacker, String countryAttacker, int nrA);
}
