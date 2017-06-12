package utils;

import utils.GameObserver;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import risiko.players.Player;
import shared.CountryInfo;
import shared.PlayerInfo;

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
     * @param attackerInfo
     */
    public void notifySetAttacker(CountryInfo attackerInfo) {
        for (BasicGameObserver ob : this.obs) {
            if (ob instanceof GameObserver) {
                ((GameObserver) ob).updateOnSetAttacker(attackerInfo);
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
    public void notifyPhaseChange(PlayerInfo player, String phase) {
        for (BasicGameObserver ob : this.obs) {
            if (ob instanceof GameObserver) {
                ((GameObserver) ob).updateOnPhaseChange(player, phase);
            }
        }
    }

    /**
     * Notifica un cambiamento dopo l'asseganzione dei territori
     *
     * @param countriesInfo
     */
    public void notifyCountriesAssignment(CountryInfo[] countriesInfo) {
        for (BasicGameObserver ob : this.obs) {
            if (ob instanceof GameObserver) {
                ((GameObserver) ob).updateOnCountriesAssignment(countriesInfo);
            }
        }

    }

    /**
     * Notifies that the number of armies of a country has changed.
     *
     * @param countryInfo
     */
    public void notifyArmiesChange(CountryInfo countryInfo) {
        for (BasicGameObserver ob : this.obs) {
            if (ob instanceof GameObserver) {
                ((GameObserver) ob).updateOnArmiesChange(countryInfo);
            }
        }

    }

    /**
     * Notifica all'observable che il giocatore del nuovo turno ha delle carte
     * da giocare.
     */
    public void notifyNextTurn(List<String> cards) {
        for (BasicGameObserver ob : this.obs) {
            if (ob instanceof GameObserver) {
                ((GameObserver) ob).updateOnNextTurn(cards);
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

    public void notifyPlayedTris() {
        for (BasicGameObserver ob : this.obs) {
            if (ob instanceof GameObserver) {
                ((GameObserver) ob).updateOnPlayedTris();
            }
        }
    }
}
