/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Elisa
 */
public interface BasicGameObserver {

    public void updateOnSetDefender(String countryAttackerName, String countryDefenderName, String defenderPlayer, int maxArmiesAttacker, int maxArmiesDefender);

    public void updateOnVictory(String winMessage);

    public void updateOnAttackResult(String attackResultInfo, boolean conquered, boolean canAttackFromCountry, int maxArmiesAttacker, int maxArmiesDefender, int[] attackerDice, int[] defenderDice);

    public void updateOnDefend(String defender, String defenderCountry, String attacker, String attackerCountry, int nrA, boolean artificialPlayer);
    
}
