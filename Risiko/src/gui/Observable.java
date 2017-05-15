package gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Observable {

    private boolean changed = false;
    private List<GameObserver> obs;

    /**
     * Construct an Observable with zero Observers.
     */
    public Observable() {
        obs = new ArrayList<>();
    }

    //-------------------------- Notify-----------------------------------

    /**
     * Notifica un cambiamento dopo la fase di rinforzo
     * @param countryName
     * @param bonusArmies 
     */
    public void notifyReinforce(String countryName, int bonusArmies) {
        synchronized (this) {
            if (!changed) {
                return;
            }
            clearChanged();
        }

        for (GameObserver ob : this.obs) {
            ob.updateOnReinforce(countryName, bonusArmies);
        }

    }

    /**
     * Notifica un cambiamento dopo che l'attaccante è stato settato
     * @param countryName 
     */
    public void notifySetAttacker(String countryName) {
        synchronized (this) {
            if (!changed) {
                return;
            }
            clearChanged();
        }

        for (GameObserver ob : this.obs) {
            ob.updateOnSetAttacker(countryName);
        }

    }

    /**
     * Notifica un cambiamento dopo che il difensore è stato settato
     * @param countryAttackerName
     * @param countryDefenderName
     * @param defenderPlayer
     * @param maxArmiesAttacker
     * @param maxArmiesDefender 
     */
    public void notifySetDefender(String countryAttackerName, String countryDefenderName, String defenderPlayer, int maxArmiesAttacker, int maxArmiesDefender) {
        synchronized (this) {
            if (!changed) {
                return;
            }
            clearChanged();
        }

        for (GameObserver ob : this.obs) {
            ob.updateOnSetDefender(countryAttackerName, countryDefenderName, defenderPlayer, maxArmiesAttacker, maxArmiesDefender);
        }
    }

    /**
     * Notifica un cambiamento dopo un attacco
     * @param attackResultInfo
     * @param isConquered
     * @param canAttackFromCountry
     * @param maxArmiesAttacker
     * @param maxArmiesDefender
     * @param attackerDice
     * @param defenderDice 
     */
    public void notifyAttackResult(String attackResultInfo, boolean isConquered, boolean canAttackFromCountry, int maxArmiesAttacker, int maxArmiesDefender, int[] attackerDice, int[] defenderDice) {
        synchronized (this) {
            if (!changed) {
                return;
            }
            clearChanged();
        }

        for (GameObserver ob : this.obs) {
            ob.updateOnAttackResult(attackResultInfo, isConquered, canAttackFromCountry, maxArmiesAttacker, maxArmiesDefender, attackerDice, defenderDice);
        }
    }

    /**
     * Notifica quando cambia la fase del gioco
     * @param player
     * @param phase 
     */
    public void notifyPhaseChange(String player, String phase) {
        synchronized (this) {
            if (!changed) {
                return;
            }
            clearChanged();
        }
        for (GameObserver ob : this.obs) {
            ob.updateOnPhaseChange(player, phase);
        }
    }

    /**
     * Notifica un cambiamento quando c'è la vittoria di un giocatore
     * @param winner 
     */
    public void notifyVictory(String winner) {
        synchronized (this) {
            if (!changed) {
                return;
            }
            clearChanged();
        }

        for (GameObserver ob : this.obs) {
            ob.updateOnVictory(winner);
        }
    }

    /**
     * Notifica un cambiamento dopo l'asseganzione dei territori
     * @param countries
     * @param armies
     * @param colors 
     */
    public void notifyCountryAssignment(String[] countries, int[] armies, Color[] colors) {
        synchronized (this) {
            if (!changed) {
                return;
            }
            clearChanged();
        }

        for (GameObserver ob : this.obs) {
            ob.updateOnCountryAssignment(countries, armies, colors);
        }

    }
    
    /**
     * Notifica un cambiamento dopo uno spostamento
     * @param country
     * @param armies
     * @param color 
     */
    public void notifyArmiesChange(String country, int armies, Color color) {
        synchronized (this) {
            if (!changed) {
                return;
            }
            clearChanged();
        }

        for (GameObserver ob : this.obs) {
            ob.updateOnArmiesChange(country, armies, color);
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
    public synchronized void addObserver(GameObserver o) {
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
    public synchronized void deleteObserver(GameObserver o) {
        obs.remove(o);
    }

    /**
     * Clears the observer list so that this object no longer has any observers.
     */
    public synchronized void deleteObservers() {
        obs.clear();
    }

    /**
     * Marks this <tt>Observable</tt> object as having been changed; the
     * <tt>hasChanged</tt> method will now return <tt>true</tt>.
     */
    protected synchronized void setChanged() {
        changed = true;
    }

    /**
     * Indicates that this object has no longer changed, or that it has already
     * notified all of its observers of its most recent change, so that the
     * <tt>hasChanged</tt> method will now return <tt>false</tt>. This method is
     * called automatically by the <code>notifyObservers</code> methods.
     *
     * @see java.util.Observable#notifyObservers()
     * @see java.util.Observable#notifyObservers(java.lang.Object)
     */
    protected synchronized void clearChanged() {
        changed = false;
    }

    /**
     * Tests if this object has changed.
     *
     * @return  <code>true</code> if and only if the <code>setChanged</code>
     * method has been called more recently than the <code>clearChanged</code>
     * method on this object; <code>false</code> otherwise.
     * @see java.util.Observable#clearChanged()
     * @see java.util.Observable#setChanged()
     */
    public synchronized boolean hasChanged() {
        return changed;
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
