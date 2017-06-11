package utils;

import shared.AttackResultInfo;
import shared.CountryInfo;

public interface BasicGameObserver {

    /**
     * Method that updates the observer as soon as the attacker has chosen which
     * country to attack.
     *
     * @param fightingCountries
     * @param reattack
     */
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
     * @param attackResult
     */
    public void updateOnAttackResult(AttackResultInfo attackResult);
    
    /**
     * Method called as soon as the attacker has chosen how many armies to use
     * in a fight.
     *
     * @param defenderCountryInfo
     */
    public void updateOnDefend(CountryInfo defenderCountryInfo);

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
