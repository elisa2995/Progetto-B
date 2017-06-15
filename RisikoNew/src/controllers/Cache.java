package controllers;

import java.util.HashMap;
import java.util.Map;
import risiko.game.GameProxy;

/**
 * 
 */
public class Cache {

    private GameProxy game;
    private Map<String, Boolean> cacheMap;

    public Cache() {
        this.cacheMap = new HashMap<>();
    }

    public void setGame(GameProxy game) {
        this.game = game;
    }

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

    public void save(String country, boolean canBechosen) {
        cacheMap.put(country, canBechosen);
    }

    public boolean controlReinforce(String country) {
        return canBeChosen(country) || game.controlPlayer(country) && game.canReinforce();
    }

    public boolean controlAttack(String country) {
        return canBeChosen(country) || game.getAttackerCountryName() == null && game.controlAttacker(country);
    }

    public boolean controlDefense(String country) {
        return canBeChosen(country) || game.getAttackerCountryName() != null && game.controlDefender(country);
    }

    public boolean controlMoveFromCountry(String country) {
        return canBeChosen(country) || game.getFromCountryName() == null && game.controlFromCountryPlayer(country);
    }

    public boolean controlMoveToCountry(String country) {
        return canBeChosen(country) || game.getFromCountryName() != null && game.controlMovement(country);
    }

    public boolean canBeChosenAsAttacker(String country) {
        return game.getAttackerCountryName() == null && canBeChosen(country);
    }
    
    public boolean canBeChosenAsFromCountry(String country){
        return game.getFromCountryName() == null && canBeChosen(country);
    }
}
