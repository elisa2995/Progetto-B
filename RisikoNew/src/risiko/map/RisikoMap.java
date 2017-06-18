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
     * Inizializza la mappa leggendo il file specificato in urlCountries (in
     * countryPlayer il player è inizializzato a null).
     *
     * @throws qualche eccezione relativa alla lettura del file
     */
    private void init() {
        buildContinent();
        setAllCountries();
        buildMissions();
    }

    /**
     * Builda le Map ContinentCountries e continentBonus.
     */
    private void buildContinent() {
        //Itero su ogni continente
        for (Map<String, Object> tmpContinent : FileManager.getInstance().getContinents()) {

            List<Country> countriesOfThatContinent = new ArrayList<>();
            for (String countryName : (List<String>) tmpContinent.get("countries")) {
                countriesOfThatContinent.add(new Country(countryName));
            }

            String    continentName = (String)  tmpContinent.get("name");  //Prendo il nome del continente
            Integer   bonus         = (Integer) tmpContinent.get("bonus");
            Continent continent     = new       Continent(continentName, countriesOfThatContinent, bonus);
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
     * Traduce la List di Map <code>fileManager.getMissions()</code>, in cui
     * ogni elemento corrispone a una missione, negli oggetti Mission
     * corrisponenti.
     *
     * @author Federico
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
     * Effettua l'assegnazione degli obiettivi ai giocatori.
     *
     * @param players
     * @author Federico
     */
    public void assignMissionToPlayers(List<Player> players) {
        Collections.shuffle(this.missions);
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setMission(missions.get(i));
        }
    }

    /**
     * Effettua l'assegnazione iniziale dei territori ai giocatori (random).
     *
     * @param players
     * @author Elisa
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
     * Ritorna il giocatore successivo nella lista dei players a quello che si
     * passa; se il giocatore è l'ultimo della lista ritorna il primo.

     */
    private Player nextPlayer(List<Player> players, int round) {
        return players.get(round % (players.size()));
    }

    /**
     * Ritorna una lista con tutti i countries
     *
     *
     * @return List
     * @author Elisa
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
     * Esegue la fase di rinforzo di inizio turno. Nr armate bonus =
     * ceil(nrTerritoriGiocatore/3) e le distribuisce a caso sui territori del
     * giocatore
     *
     * @param player il giocatore di turno
     * @author Elisa
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
     * Ritorna true se il player possiede il continente.
     *
     * @param player
     * @param continent
     * @return
     */
    private boolean ownsContinent(Player player, Continent continent) {
        return getMyCountries(player).containsAll(continent.getCountries());
    }

    /**
     * Ritorna true se il player dopo la conquiesta della Country
     * <code>justConqueredCountry</code> possiede l'intero continente.

     * @return
     */
    public boolean hasConqueredContinent(Country conqueredCountry) {
        Player player = conqueredCountry.getOwner();
        Continent continent = getContinentByCountry(conqueredCountry);
        return ownsContinent(player, continent);
    }

    /**
     * Ritorna il continente a cui appartiene una country.
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
        return null; //non dovrebbe mai arrivarci
    }

    /**
     * Ritorna una lista dei territori del giocatore
     *
     * @param player
     * @return
     * @author Alessandro
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
     * Ritorna la lista dei vicini di un territorio.
     *
     * @param country
     */
    public List<Country> getNeighbors(Country country) {
        return country.getNeighbors();
    }

    /**
     * Controlla se il giocatore può ancora attaccare da uno dei suoi territori.
     *
     * @return true nel caso in cui almeno uno dei territori di ActivePlayer
     * abbia più di un'armata, false in caso contrario.
     * @author Alessandro QUESTIONE: forse dovremmo controllare che la country
     * eventualmente trovata con più di un'armata, abbia dei vicini attaccabili
     * (non suoi)
     */
    public boolean playerCanAttack(Player player) {
        List<Country> availableCountries = this.getMyCountries(player);
        for (Country c : availableCountries) {
            if (c.getArmies() > 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Metodo chiamato nel caso in cui un giocatore abbia conquistato un
     * territorio. La mappa controlla se ha raggiunto il suo obbiettivo.
     *
     * @param player il giocatore di turno
     * @return true se il giocatore ha vinto, false altrimenti.
     * @author Federico
     */
    public boolean checkIfWinner(Player player) {
        return player.checkIfWinner(getMyCountries(player));
    }

    /**
     * Controlla se il territorio è stato conquistato
     *
     * @return true se non ha armate
     */
    public boolean isConquered(Country country) {
        return country.isConquered();
    }

    /**
     * controlla se il difensore non ha più territori
     */
    public boolean hasLost(Player defenderPlayer) {
        return getMyCountries(defenderPlayer).isEmpty();
    }

    public Country getCountryByName(String countryName) {
        for (Country country : getCountriesList()) {
            if (country.getName().equals(countryName)) {
                return country;
            }
        }
        return null;
    }



    /**
     * Assigns the countries of a player to another
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
