package controllers;

import java.util.HashMap;
import java.util.Map;
import risiko.game.GameProxy;

/**
 * Memorizes wether a country is clickable or not in that specific phase of the
 * game.
 *
 */
public class Cache {

    private GameProxy game;
    private Map<String, Boolean> cacheMap;

    /**
     * Creates a new Cache.
     */
    public Cache() {
        this.cacheMap = new HashMap<>();
    }

    /**
     * Sets the <code>GameProxy</code>.
     *
     * @param game
     */
    public void setGame(GameProxy game) {
        this.game = game;
    }

    /**
     * Resets the cache.
     */
    public void resetCache() {
        this.cacheMap = new HashMap<>();
    }

    /**
     * Checks in the cache wheter the <code>country</code> can be chosen in the
     * current phase.
     *
     * @param country
     * @return
     */
    public boolean canBeChosen(String country) {
        return cacheMap.containsKey(country) && cacheMap.get(country);
    }

    /**
     * Checks in the cache wheter the <code>country</code> can be chosen in the
     * current phase as attacker.
     *
     * @param country
     * @return
     */
    public boolean canBeChosenAsAttacker(String country) {
        return game.getAttackerCountryName() == null && canBeChosen(country);
    }

    /**
     * Checks in the cache wheter the <code>country</code> can be chosen in the
     * current phase as country from which to move the armies.
     *
     * @param country
     * @return
     */
    public boolean canBeChosenAsFromCountry(String country) {
        return game.getFromCountryName() == null && canBeChosen(country);
    }

    /**
     * Saves the country and if the <code>country</code> can be chosen in the
     * current phase.
     *
     * @param country
     * @param canBechosen
     */
    public void save(String country, boolean canBechosen) {
        cacheMap.put(country, canBechosen);
    }

    /**
     * Returns wheter the country can be reinforced by the player or not. If the
     * county is already in the cache, returns the value in the cache, otherwise
     * it asks it to <code>GameProxy</code>.
     *
     * @param country
     * @return
     */
    public boolean controlReinforce(String country) {
        return canBeChosen(country) || game.isCountryOwner(country) && game.canReinforce();
    }

    /**
     * Returns wheter the country can be chosen as attacker by the player or
     * not. If the country is already in the cache, returns the value in the
     * cache, otherwise it asks it to <code>GameProxy</code>.
     *
     * @param country
     * @return
     */
    public boolean controlAttack(String country) {
        return canBeChosen(country) || game.getAttackerCountryName() == null && game.controlAttacker(country);
    }

    /**
     * Returns wheter the country can be chosen as defender by the player or
     * not. If the country is already in the cache, returns the value in the
     * cache, otherwise it asks it to <code>GameProxy</code>.
     *
     * @param country
     * @return
     */
    public boolean controlDefense(String country) {
        return canBeChosen(country) || game.getAttackerCountryName() != null && game.controlDefender(country);
    }

    /**
     * Returns wheter the country can be chosen as coutry from which to move
     * armies. If the country is already in the cache, returns the value in the
     * cache, otherwise it asks it to <code>GameProxy</code>.
     *
     * @param country
     * @return
     */
    public boolean controlMoveFromCountry(String country) {
        return canBeChosen(country) || game.getFromCountryName() == null && game.canMoveFromHere(country);
    }

    /**
     * Returns wheter the country can be chosen as coutry to which to move
     * armies. If the country is already in the cache, returns the value in the
     * cache, otherwise it asks it to <code>GameProxy</code>.
     *
     * @param country
     * @return
     */
    public boolean controlMoveToCountry(String country) {
        return canBeChosen(country) || game.getFromCountryName() != null && game.controlMovement(country);
    }

}
