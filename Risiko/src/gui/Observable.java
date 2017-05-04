package gui;

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
     * If this object has changed, as indicated by the <code>hasChanged</code>
     * method, then notify all of its observers and then call the
     * <code>clearChanged</code> method to indicate that this object has no
     * longer changed.
     * <p>
     * Each observer has its <code>update</code> method called with two
     * arguments: this observable object and the <code>arg</code> argument.
     *
     * @param arg any object.
     * @see java.util.Observable#clearChanged()
     * @see java.util.Observable#hasChanged()
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
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

    public void notifyAttackResult(String attackResultInfo, boolean isConquered, boolean canAttackFromCountry, int maxArmiesAttacker, int maxArmiesDefender) {
        synchronized (this) {
            if (!changed) {
                return;
            }
            clearChanged();
        }

        for (GameObserver ob : this.obs) {
            ob.updateOnAttackResult(attackResultInfo, isConquered, canAttackFromCountry, maxArmiesAttacker, maxArmiesDefender);
        }
    }

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
