package utils;

import utils.GameObserver;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import risiko.players.Player;

public class Observable extends BasicObservable {

    /**
     * Construct an Observable with zero Observers.
     */
    public Observable() {
        super();
    }

    //-------------------------- Notify-----------------------------------
    /**
     * Notifica un cambiamento dopo la fase di rinforzo
     *
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

        for (BasicGameObserver ob : this.obs) {
            if (ob instanceof GameObserver) {
                ((GameObserver) ob).updateOnReinforce(countryName, bonusArmies);
            }
        }

    }

    /**
     * Notifica un cambiamento dopo che l'attaccante è stato settato
     *
     * @param countryName
     */
    public void notifySetAttacker(String countryName) {
        synchronized (this) {
            if (!changed) {
                return;
            }
            clearChanged();
        }

        for (BasicGameObserver ob : this.obs) {
            if (ob instanceof GameObserver) {
                ((GameObserver) ob).updateOnSetAttacker(countryName);
            }
        }

    }

    /**
     * Notifica un cambiamento dopo che l'attaccante è stato settato
     *
     * @param countryName
     */
    public void notifySetFromCountry(String countryName) {
        synchronized (this) {
            if (!changed) {
                return;
            }
            clearChanged();
        }

        for (BasicGameObserver ob : this.obs) {
            if (ob instanceof GameObserver) {
                ((GameObserver) ob).updateOnSetFromCountry(countryName);
            }
        }

    }

    /**
     * Notifica quando cambia la fase del gioco
     *
     * @param player
     * @param phase
     * @param color
     */
    public void notifyPhaseChange(String player, String phase, String color) {
        synchronized (this) {
            if (!changed) {
                return;
            }
            clearChanged();
        }
        for (BasicGameObserver ob : this.obs) {
            if (ob instanceof GameObserver) {
                ((GameObserver) ob).updateOnPhaseChange(player, phase, color);
            }
        }
    }

    /**
     * Notifica un cambiamento dopo l'asseganzione dei territori
     *
     * @param countries
     * @param armies
     * @param colors
     */
    public void notifyCountryAssignment(String[] countries, int[] armies, String[] colors) {
        synchronized (this) {
            if (!changed) {
                return;
            }
            clearChanged();
        }

        for (BasicGameObserver ob : this.obs) {
            if (ob instanceof GameObserver) {
                ((GameObserver) ob).updateOnCountryAssignment(countries, armies, colors);
            }
        }

    }

    /**
     * Notifica un cambiamento dopo uno spostamento
     *
     * @param country
     * @param armies
     * @param color
     */
    public void notifyArmiesChange(String country, int armies, String color) {
        synchronized (this) {
            if (!changed) {
                return;
            }
            clearChanged();
        }

        for (BasicGameObserver ob : this.obs) {
            if (ob instanceof GameObserver) {
                ((GameObserver) ob).updateOnArmiesChange(country, armies, color);
            }
        }

    }

    /**
     * Notifica all'observable che il giocatore del nuovo turno ha delle carte
     * da giocare.
     */
    public void notifyNextTurn() {
        synchronized (this) {
            if (!changed) {
                return;
            }
            clearChanged();
        }

        for (BasicGameObserver ob : this.obs) {
            if (ob instanceof GameObserver) {
                ((GameObserver) ob).updateOnNextTurn();
            }
        }
    }

    /**
     * Notifica il............... di una carta.
     */
    public void notifyDrawnCard(String cardName) {
        synchronized (this) {
            if (!changed) {
                return;
            }
            clearChanged();
        }

        for (BasicGameObserver ob : this.obs) {
            if (ob instanceof GameObserver) {
                ((GameObserver) ob).updateOnDrawnCard(cardName);
            }
        }
    }
}
