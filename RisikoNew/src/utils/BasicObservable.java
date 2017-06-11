/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import risiko.players.ArtificialPlayer;
import shared.AttackResultInfo;
import shared.CountryInfo;

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
     * @param artificialAttack
     */
    public void notifyAttackResult(AttackResultInfo attackResult) {
        for (BasicGameObserver ob : this.obs) {
            ob.updateOnAttackResult(attackResult);
        }
    }

    /**
     * Notifica un cambiamento quando c'è la vittoria di un giocatore
     *
     * @param winner
     */
    public void notifyVictory(String winner) {
        this.obs.sort(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if (o1 instanceof ArtificialPlayer) {
                    return -1;
                }
                return 1;
            }

        });
        for (BasicGameObserver ob : this.obs) {
            ob.updateOnVictory(winner);
        }
    }

    /**
     * Notifies the observer that the defender has chosen how many armies to use
     * for the defense.
     *
     * @param attacker
     * @param defender
     * @param reattack
     */
    public void notifySetDefender(CountryInfo[] fightingCountries, boolean reattack) {
        for (BasicGameObserver ob : this.obs) {
            ob.updateOnSetDefender(fightingCountries, reattack);
        }
    }

    /*public void notifySetDefender(String countryAttackerName, String countryDefenderName, String defenderPlayer, int maxArmiesAttacker, int maxArmiesDefender, boolean reattack) {
        for (BasicGameObserver ob : this.obs) {
            ob.updateOnSetDefender(countryAttackerName, countryDefenderName, defenderPlayer, maxArmiesAttacker, maxArmiesDefender, reattack);
        }
    }*/
    public void notifyElimination(String defenderName, boolean artificialAttack) {
        for (BasicGameObserver ob : this.obs) {
            ob.updateOnElimination(defenderName, artificialAttack);
        }
    }

    public void notifyEndGame() {
        this.obs.sort(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if (o1 instanceof ArtificialPlayer) {
                    return -1;
                }
                return 1;
            }

        });
        for (BasicGameObserver ob : this.obs) {
            ob.updateOnEndGame();
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
