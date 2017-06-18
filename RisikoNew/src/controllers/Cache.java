package controllers;

import java.util.HashMap;
import java.util.Map;
import risiko.game.GameProxy;

/**
 * Contains the countries in which the mouse has passed and wheter they can be
 * choosen in the curreunt phase.
 *
 */
public class Cache {

    /*Cache reset: a tutti i cambiamenti di fase, dopo ogni attacco se c'è 
         stata una conquista oppure è rimasta solo un armata, quando setto l'attaccante,
         (quando setto il difensore: no perchè posso sceglierne un altro quindi rimango
         con le stesse possibilità)
         quando resetto le fighting countries in labelMaplistener(cioè qui)
     */
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
     * Resets the cache that contains all the countries wheter they can be
     * chosen in the current phase.
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
     * current phase as attacker
     *
     * @param country
     * @return
     */
    public boolean canBeChosenAsAttacker(String country) {
        return game.getAttackerCountryName() == null && canBeChosen(country);
    }

    /**
     * Checks in the cache wheter the <code>country</code> can be chosen in the
     * current phase as country from which the movement starts
     *
     * @param country
     * @return
     */
    public boolean canBeChosenAsFromCountry(String country) {
        return game.getFromCountryName() == null && canBeChosen(country);
    }

    /**
     * Saves the country and wheter the <code>country</code> can be chosen in
     * the current phase.
     *
     * @param country
     * @param canBechosen
     */
    public void save(String country, boolean canBechosen) {
        cacheMap.put(country, canBechosen);
    }

    /**
     * If the country is already in the cache it returns wheter it can be chosen
     * in the reinforce phase; if the country has not been saved yet it asks the
     * <code>GameProxy</code> wheter it can be chosen.
     *
     * @param country
     * @return
     */
    public boolean controlReinforce(String country) {
        return canBeChosen(country) || game.isCountryOwner(country) && game.canReinforce();
    }

    /**
     * If the country is already in the cache it returns wheter it can be chosen
     * as attacker; if the country has not been saved yet it asks the
     * <code>GameProxy</code> wheter it can be chosen.
     *
     * @param country
     * @return
     */
    public boolean controlAttack(String country) {
        return canBeChosen(country) || game.getAttackerCountryName() == null && game.controlAttacker(country);
    }

    /**
     * If the country is already in the cache it returns wheter it can be chosen
     * as defender; if the country has not been saved yet it asks the
     * <code>GameProxy</code> wheter it can be chosen.
     *
     * @param country
     * @return
     */
    public boolean controlDefense(String country) {
        return canBeChosen(country) || game.getAttackerCountryName() != null && game.controlDefender(country);
    }

    /**
     * If the country is already in the cache it returns wheter it can be chosen
     * as country from which the movement starts; if the country has not been
     * saved yet it asks the <code>GameProxy</code> wheter it can be chosen.
     *
     * @param country
     * @return
     */
    public boolean controlMoveFromCountry(String country) {
        return canBeChosen(country) || game.getFromCountryName() == null && game.canMoveFromHere(country);
    }

    /**
     * If the country is already in the cache it returns wheter it can be chosen
     * as country to which the movement ends; if the country has not been saved
     * yet it asks the <code>GameProxy</code> wheter it can be chosen.
     *
     * @param country
     * @return
     */
    public boolean controlMoveToCountry(String country) {
        return canBeChosen(country) || game.getFromCountryName() != null && game.controlMovement(country);
    }

}
