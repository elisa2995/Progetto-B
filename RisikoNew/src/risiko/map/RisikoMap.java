package risiko.map;

import risiko.players.Player;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import services.FileManager;
import risiko.missions.Mission;

/**
 * Class that initializes the game and handles continents, countries and
 * missions.
 */
public class RisikoMap {

    private final int DEFAULT_ARMIES = 3;
    private final List<Continent> continents;

    public RisikoMap() {
        this.continents = new ArrayList<>();
        init();
    }

    /**
     * Initializes the map.
     *
     */
    private void init() {
        buildContinent();
        setAllCountries();
        //buildMissions();
    }

    /**
     * Builds the list of continents.
     */
    private void buildContinent() {

        for (Map<String, Object> tmpContinent : FileManager.getInstance().getContinents()) {

            List<Country> countriesOfThatContinent = new ArrayList<>();
            for (String countryName : (List<String>) tmpContinent.get("countries")) {
                countriesOfThatContinent.add(new Country(countryName));
            }

            String continentName = (String) tmpContinent.get("name");
            Integer bonus = (Integer) tmpContinent.get("bonus");
            Continent continent = new Continent(continentName, countriesOfThatContinent, bonus);
            continents.add(continent);
        }
    }

    /**
     * Asks the FileManager the list of countries and their neighbors
     * (represented by their names), and turns it into an HashMap that maps a
     * Country to its list of neighbors.
     *
     */
    private void setAllCountries() {
        Map<String, List<String>> countryNeighborsNames = FileManager.getInstance().getCountryNeighbors();
        for (Map.Entry<String, List<String>> row : countryNeighborsNames.entrySet()) {

            List<Country> neighbors = new ArrayList<>();
            for (String neighbor : row.getValue()) {
                neighbors.add(getCountryByName(neighbor));
            }

            Country country = getCountryByName(row.getKey());
            country.setArmies(DEFAULT_ARMIES);
            country.setNeighbors(neighbors);
        }
    }

    /**
     * Builds the list of missions.
     *
     */
    private List<Mission> buildMissions() {
        List<Mission> missions=new ArrayList<>();
        Constructor constructor;
        Mission m;
        String description;
        int points;
        String packagePath = "risiko.missions.";
        for (Map<String, Object> mission : FileManager.getInstance().getMissions()) {
            try {
                points = (Integer) mission.get("points");
                description = (String) mission.get("description") + "\n (" + points + "punti)";
                constructor = Class.forName(packagePath + (String) mission.get("type") + "Mission").getConstructor(String.class, Integer.TYPE);
                m = (Mission) constructor.newInstance(description, points);
                missions.add(m);
                m.buildTarget(continents);
            } catch (Exception ex) {
                Logger.getLogger(RisikoMap.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return missions;
    }

    /**
     * Initializes the game.
     *
     * @param players
     */
    public void initGame(List<Player> players) {
        assignCountriesToPlayers(players);
        assignMissionToPlayers(players);
    }

    /**
     * Assigns a mission to each player randomly.
     *
     * @param players
     */
    private void assignMissionToPlayers(List<Player> players) {
        List<Mission> missions = buildMissions();
        Collections.shuffle(missions);
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setMission(missions.get(i));
        }
    }

    /**
     * Assigns some countries to each player randomly.
     *
     * @param players
     */
    public void assignCountriesToPlayers(List<Player> players) {
        List<Country> countries = getCountriesList();
        Collections.shuffle(countries);
        int round = 0;
        for (Country country : countries) {
            country.setOwner(nextPlayer(players, round++));
        }
    }

    /**
     * Returns the next player in the list of players.
     *
     */
    private Player nextPlayer(List<Player> players, int round) {
        return players.get(round % (players.size()));
    }

    /**
     * Returns a list containing all the countries.
     *
     * @return
     */
    public List<Country> getCountriesList() {
        List<Country> countries = new ArrayList<>();
        for (Continent continent : continents) {
            for (Country country : continent.getCountries()) {
                countries.add(country);
            }
        }
        return countries;
    }

    /**
     * Computes the number of bonus armies awarded to <code>player</code> for
     * owning its countries.
     *
     * @param player
     */
    public void computeBonusArmies(Player player) {

        int bonus = 0;
        for (Continent continent : continents) {
            if (ownsContinent(player, continent)) {
                bonus += continent.getBonus();
            }
        }

        bonus += (int) Math.floor(getMyCountries(player).size() / 3);
        player.addBonusArmies(bonus);
    }

    /**
     * Returns true if the player <code> player</code> owns the continent
     * <code>continent</code>.
     *
     * @param player
     * @param continent
     * @return
     */
    private boolean ownsContinent(Player player, Continent continent) {
        return getMyCountries(player).containsAll(continent.getCountries());
    }

    /**
     * Returns true if the player, after the conquest of a Country
     * <code>conqueredCountry</code> owns the entire continent of that Country.
     *
     * @param conqueredCountry
     * @return
     */
    public boolean hasConqueredContinent(Country conqueredCountry) {
        Player player = conqueredCountry.getOwner();
        Continent continent = getContinentByCountry(conqueredCountry);
        return ownsContinent(player, continent);
    }

    /**
     * Returns the continent to which the Country <code>country</code> belongs.
     *
     * @param country
     * @return
     */
    public Continent getContinentByCountry(Country country) {
        for (Continent continent : continents) {
            if (continent.containsCountry(country)) {
                return continent;
            }
        }
        return null;
    }

    /**
     * Returns the list of player's countries.
     *
     * @param player
     * @return
     */
    public List<Country> getMyCountries(Player player) {
        List<Country> myCountries = new ArrayList<>();
        for (Country country : getCountriesList()) {
            if (country.getOwner().equals(player)) {
                myCountries.add(country);
            }
        }
        return myCountries;
    }

    /**
     * Returns the list of country's neighbors.
     *
     * @param country
     * @return
     */
    public List<Country> getNeighbors(Country country) {
        return country.getNeighbors();
    }

    /**
     * Checks if player has just won.
     *
     * @param player
     * @return
     */
    public boolean checkIfWinner(Player player) {
        return player.checkIfWinner(getMyCountries(player));
    }

    /**
     * Checks if country is conquered.
     *
     * @param country
     * @return
     */
    public boolean isConquered(Country country) {
        return country.isConquered();
    }

    /**
     * Checks if the player <code>defenderPlayer</code> has no more countries.
     *
     * @param defenderPlayer
     * @return
     */
    public boolean hasLost(Player defenderPlayer) {
        return getMyCountries(defenderPlayer).isEmpty();
    }

    /**
     * Returns the Country by its name <code>countryName</code>.
     *
     * @param countryName
     * @return
     */
    public Country getCountryByName(String countryName) {
        for (Country country : getCountriesList()) {
            if (country.getName().equals(countryName)) {
                return country;
            }
        }
        return null;
    }

    /**
     * Assigns the countries that belonged to<code>oldOwner</code> to
     * <code>newOwner</code>.
     *
     * @param oldOwner previous owner of the territories
     * @param newOwner new owner of the territories
     */
    public void changeOwner(Player oldOwner, Player newOwner) {
        for (Country country : getCountriesList()) {
            if (country.getOwner().equals(oldOwner)) {
                country.setOwner(newOwner);
            }
        }
    }
}
