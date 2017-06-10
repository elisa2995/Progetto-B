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
    public void notifyReinforce(int bonusArmies) {
        for (BasicGameObserver ob : this.obs) {
            if (ob instanceof GameObserver) {
                ((GameObserver) ob).updateOnReinforce(bonusArmies);
            }
        }

    }

    /**
     * Notifica un cambiamento dopo che l'attaccante è stato settato
     *
     * @param countryName
     * @param maxArmiesAttacker
     */
    public void notifySetAttacker(String countryName, int maxArmiesAttacker, String attacker, String color) {
        for (BasicGameObserver ob : this.obs) {
            if (ob instanceof GameObserver) {
                ((GameObserver) ob).updateOnSetAttacker(countryName, maxArmiesAttacker, attacker, color);
            }
        }

    }

    /**
     * Notifica un cambiamento dopo che l'attaccante è stato settato
     *
     * @param countryName
     */
    public void notifySetFromCountry(String countryName) {
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
    public void notifyPhaseChange(String player, String phase, String color, int bonusArmies) {
        for (BasicGameObserver ob : this.obs) {
            if (ob instanceof GameObserver) {
                ((GameObserver) ob).updateOnPhaseChange(player, phase, color, bonusArmies);
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
        for (BasicGameObserver ob : this.obs) {
            if (ob instanceof GameObserver) {
                ((GameObserver) ob).updateOnNextTurn();
            }
        }
    }

    /**
     * Notifica il............... di una carta.
     */
    public void notifyDrawnCard(String cardName, boolean isArtificialPlayer) {
        for (BasicGameObserver ob : this.obs) {
            if (ob instanceof GameObserver) {
                ((GameObserver) ob).updateOnDrawnCard(cardName, isArtificialPlayer);
            }
        }
    }
}
