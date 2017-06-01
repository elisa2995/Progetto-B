/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Elisa
 */
public class BasicObservable {
    
    protected List<BasicGameObserver> obs;

    /**
     * Construct an Observable with zero Observers.
     */
    public BasicObservable() {
        obs = new ArrayList<>();
    }

    //------------------Notify----------------------------------------------
    /**
     * notificha che chi viene attaccato deve scegliere con quante armate deve
     * difendersi
     *
     * @param defender giocatore attaccato
     * @param defenderCountry territorio attaccato
     * @param attacker attaccante
     * @param attackerCountry territorio attaccante
     * @param nrA numero di armate da cui si viene attaccati
     */
    public void notifyDefender(String defender, String defenderCountry, String attacker, String attackerCountry, int nrA, boolean isArtificialPlayer) {
        for (BasicGameObserver ob : this.obs) {
            ob.updateOnDefend(defender, defenderCountry, attacker, attackerCountry, nrA, isArtificialPlayer);
        }

    }

    /**
     * Notifica un cambiamento dopo un attacco
     *
     * @param attackResultInfo
     * @param isConquered
     * @param canAttackFromCountry
     * @param maxArmiesAttacker
     * @param maxArmiesDefender
     * @param attackerDice
     * @param defenderDice
     */
    public void notifyAttackResult(String attackResultInfo, boolean isConquered, boolean canAttackFromCountry, int maxArmiesAttacker, int maxArmiesDefender, int[] attackerDice, int[] defenderDice) {
        for (BasicGameObserver ob : this.obs) {
            ob.updateOnAttackResult(attackResultInfo, isConquered, canAttackFromCountry, maxArmiesAttacker, maxArmiesDefender, attackerDice, defenderDice);
        }
    }

    /**
     * Notifica un cambiamento quando c'è la vittoria di un giocatore
     *
     * @param winner
     */
    public void notifyVictory(String winMessage) {
        
        for (BasicGameObserver ob : this.obs) {
            ob.updateOnVictory(winMessage);
        }
    }

    /**
     * Notifica un cambiamento dopo che il difensore è stato settato
     *
     * @param countryAttackerName
     * @param countryDefenderName
     * @param defenderPlayer
     * @param maxArmiesAttacker
     * @param maxArmiesDefender
     */
    public void notifySetDefender(String countryAttackerName, String countryDefenderName, String defenderPlayer, int maxArmiesAttacker, int maxArmiesDefender) {
        for (BasicGameObserver ob : this.obs) {
            
            ob.updateOnSetDefender(countryAttackerName, countryDefenderName, defenderPlayer, maxArmiesAttacker, maxArmiesDefender);
        }
    }

    /**
     * Adds an observer to the set of observers for this object, provided that
     * it is not the same as some observer already in the set. The order in
     * which notifications will be delivered to multiple observers is not
     * specified. See the class comment.
     *
     * @param o an observer to be added.
     * @throws NullPointerException if the parameter o is null.
     */
    public synchronized void addObserver(BasicGameObserver o) {
        if (o == null) {
            throw new NullPointerException();
        }
        if (!obs.contains(o)) {
            obs.add(o);
        }
    }

    /**
     * Deletes an observer from the set of observers of this object. Passing
     * <CODE>null</CODE> to this method will have no effect.
     *
     * @param o the observer to be deleted.
     */
    public synchronized void deleteObserver(BasicGameObserver o) {
        obs.remove(o);
    }

    /**
     * Clears the observer list so that this object no longer has any observers.
     */
    public synchronized void deleteObservers() {
        obs.clear();
    }

    /**
     * Returns the number of observers of this <tt>Observable</tt> object.
     *
     * @return the number of observers of this object.
     */
    public synchronized int countObservers() {
        return obs.size();
    }

}
