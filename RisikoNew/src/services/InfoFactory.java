package services;

import java.util.List;
import risiko.map.Country;
import risiko.map.RisikoMap;
import risiko.phase.FightPhase;
import risiko.players.ArtificialPlayer;
import risiko.players.Player;
import shared.AttackResultInfo;
import shared.CountryInfo;
import shared.PlayerInfo;

/**
 * Useful class for the communication between the model and the view. Builds
 * shared.info objects from model's ones.
 */
public class InfoFactory {

    /**
     * Builds an array of 2 elements of <code>CountryInfo</code>. The element at
     * index 0 represent the attacker, the one at index 1 the defender.
     *
     * @return
     */
    public static CountryInfo[] buildFightingCountriesInfo(FightPhase fightPhase, RisikoMap map) {
        CountryInfo attackerCountryInfo = buildCountryInfo(true, fightPhase, map);
        attackerCountryInfo.canAttackFromHere(map.canAttackFromCountry(fightPhase.getAttackerCountry()));
        CountryInfo defenderCountryInfo = buildCountryInfo(false, fightPhase, map);
        return new CountryInfo[]{attackerCountryInfo, defenderCountryInfo};
    }

    /**
     * Builds an object <code>CountryInfo</code> which contains the info about
     * the attacker/defender.
     *
     * @param isAttacker
     * @param fightPhase
     * @param map
     * @return
     */
    public static CountryInfo buildCountryInfo(boolean isAttacker, FightPhase fightPhase, RisikoMap map) {
        Country country = (isAttacker) ? fightPhase.getAttackerCountry() : fightPhase.getDefenderCountry();
        Player player = map.getPlayerByCountry(country);
        return new CountryInfo(country.toString(), map.getMaxArmies(country, isAttacker), buildPlayerInfo(player));
    }

    /**
     * Builds an object <code>CountryInfo</code> from an object of type
     * <code>Country</code>.
     *
     * @param country
     * @param map
     * @return
     */
    public static CountryInfo buildCountryInfo(Country country, RisikoMap map) {
        return new CountryInfo(buildPlayerInfo(map.getPlayerByCountry(country)), country.toString(), country.getArmies());
    }

    /**
     * Builds an object <code>PlayerInfo</code> which containts the info about a
     * certain player.
     *
     * @param player
     * @return
     */
    public static PlayerInfo buildPlayerInfo(Player player) {
        return new PlayerInfo(player.toString(), player.getColor(), player.getBonusArmies(), player instanceof ArtificialPlayer);
    }

    /**
     * Builds an array of CountryInfo, containing the info about every country
     * on the map.
     *
     * @param map
     * @return
     */
    public static CountryInfo[] buildAllCountryInfo(RisikoMap map) {
        Country country;
        List<Country> countries = map.getCountriesList();
        CountryInfo[] countriesInfo = new CountryInfo[countries.size()];
        for (int i = 0; i < countriesInfo.length; i++) {
            country = countries.get(i);
            countriesInfo[i] = new CountryInfo(buildPlayerInfo(map.getPlayerByCountry(country)), country.getName(), country.getArmies());
        }
        return countriesInfo;
    }

    /**
     * Builds an object that represents the result of an attack.
     *
     * @return
     */
    public static AttackResultInfo buildAttackResultInfo(FightPhase fightPhase, RisikoMap map) {
        return new AttackResultInfo(buildFightingCountriesInfo(fightPhase, map), fightPhase.getDice(), fightPhase.hasConquered(), fightPhase.checkContinentConquest());
    }

}
