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
 * Class that init the game and builds continent, country and missions.
 */
public class RisikoMap {

    private final int DEFAULT_ARMIES = 3;
    private final List<Mission> missions;
    private final List<Continent> continents;

    public RisikoMap() {
        this.continents = new ArrayList<>();
        this.missions = new ArrayList<>();
        init();
    }

    /**
     * Inits the map. 
     *
     */
    private void init() {
        buildContinent();
        setAllCountries();
        buildMissions();
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
     * Traduce la Map <code>countryNeighborsNames</code> (Map di String - nomi
     * dei territori) nella mappa <Country, List<Country>> corrispondente,
     * recuperando dal nome del territorio l'oggetto Country corrispondente.
     *
     * @author Elisa
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
            country.setNeighbors(neighbors);    //Setto i neighbors ai Countries contenuti in countryNeighbors
        }
    }

    /**
     * Builds the list of missions.
     *
     */
    private void buildMissions() {
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
                this.missions.add(m);
                m.buildTarget(continents);
            } catch (Exception ex) {
                Logger.getLogger(RisikoMap.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
     * Assigns randomly a mission to each player.
     *
     * @param players
     */
    public void assignMissionToPlayers(List<Player> players) {
        Collections.shuffle(this.missions);
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setMission(missions.get(i));
        }
    }

    /**
     * Assigns randomly countries to each player.
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
     * Returns next player in the list of players.
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
     * Computes the number of player's bonus armies due.
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
     * Returns true if the player owns the continent.
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
     * <code>conqueredCountry</code> owns the entire continent
     * relative to the Country.
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
     * Returns the continent whom country belongs.
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
     * Check if player has just won.
     *
     * @param player
     * @return
     */
    public boolean checkIfWinner(Player player) {
        return player.checkIfWinner(getMyCountries(player));
    }

    /**
     * Check if country is conquered.
     *
     * @param country
     * @return 
     */
    public boolean isConquered(Country country) {
        return country.isConquered();
    }

    /**
     * Check if the defender player has lost every countries
     * and so if he has lost.
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
     * Assigns the countries of a player to another.
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
