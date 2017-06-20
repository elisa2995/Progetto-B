package utils;

import java.util.List;
import shared.CountryInfo;
import shared.PlayerInfo;

public class Observable extends BasicObservable {

    /**
     * Construct an Observable with zero Observers.
     */
    public Observable() {
        super();
    }

    /**
     * Notifies the initial country assignment.
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
     * Notifies that the bonusArmies of the current active player have changed.
     *
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
     * Notifies that tha active player has chosen a country as attacker.
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
     * Notifies that the active player has chosen from which country to move its
     * armies.
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
     * Notifies that the phase has changed.
     *
     * @param player
     * @param phase
     */
    public void notifyPhaseChange(PlayerInfo player, String phase) {
        for (BasicGameObserver ob : this.obs) {
            if (ob instanceof GameObserver) {
                ((GameObserver) ob).updateOnPhaseChange(player, phase);
            }
        }
    }

    /**
     * Notifies a change in some country's armies.
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
     * Notifies the observable that the active player has some cards to play.
     *
     * @param cards
     */
    public void notifyPlayCards(List<String> cards) {
        for (BasicGameObserver ob : this.obs) {
            if (ob instanceof GameObserver) {
                ((GameObserver) ob).updateOnPlayCards(cards);
            }
        }
    }

    /**
     * Notifies that the player has drawn a card.
     *
     * @param cardName
     * @param isArtificialPlayer
     */
    public void notifyDrawnCard(String cardName, boolean isArtificialPlayer) {
        for (BasicGameObserver ob : this.obs) {
            if (ob instanceof GameObserver) {
                ((GameObserver) ob).updateOnDrawnCard(cardName, isArtificialPlayer);
            }
        }
    }

    /**
     * Notifies that the player has played a tris.
     */
    public void notifyPlayedTris() {
        for (BasicGameObserver ob : this.obs) {
            if (ob instanceof GameObserver) {
                ((GameObserver) ob).updateOnPlayedTris();
            }
        }
    }
}
