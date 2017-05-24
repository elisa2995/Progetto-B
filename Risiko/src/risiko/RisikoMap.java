package risiko;

import risiko.players.Player;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RisikoMap {

    private final int DEFAULT_ARMIES = 3;
    private final String urlCountries = "files/territori.txt";
    //private final String urlCountries = "files/prova.txt";
    private final String urlMissions = "files/missions.txt";
    private Map<String,  List<Country>> continentCountries;
    private Map<Country, List<Country>> countryNeighbors;
    private List<Mission> missions;
    private Map<String,  Integer>       continentBonus;
    private Map<Country, Player>        countryPlayer;
    private Map<String,  Country>       nameCountry;
    
    public Map<Country, Player> getCountryPlayer() {
        return countryPlayer;
    }

    public RisikoMap() {
        this.continentCountries = new HashMap<>();
        this.countryNeighbors = new HashMap<>();
        this.continentBonus = new HashMap<>();
        this.countryPlayer = new HashMap<>();
        this.nameCountry = new HashMap<>();
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
        buildCountryPlayer();
        buildCountryNeighbors();
        buildContinentCountry();
        buildMissions();
    }

    /**
     * Legge il file specificato a urlCountries, di ogni riga letta prende solo
     * il primo token e builda la Country relativa e la mette in CountryPlayer,
     * crea anche l'ashMap nameCountry.
     *
     * @author Elisa
     * @throws qualche eccezione legata alla lettura del file
     */
    private void buildCountryPlayer() {
        try (BufferedReader br = new BufferedReader(new FileReader(urlCountries))) {
            String line;

            while ((line = br.readLine()) != null) {

                if (!line.startsWith("-")) {

                    String str[] = line.split(",");

                    Country country = new Country(str[0]);
                    country.setArmies(DEFAULT_ARMIES);
                    countryPlayer.put(country, null);
                    nameCountry.put(country.getName(), country);
                }
            }
        } catch (Exception ex) {
            System.out.println("Error in buildCountryPlayer");
        }
    }

    /**
     * Legge il file specificato da urlCountries, per ogni country costruisce
     * una List di vicini, salva in CountryNeighbor il territorio e la lista di
     * vicini.
     *
     * @throws qualche eccezione legata alla lettura del file
     * @author Elisa
     */
    private void buildCountryNeighbors() {
        try (BufferedReader br = new BufferedReader(new FileReader(urlCountries))) {
            String line;
            while ((line = br.readLine()) != null) {

                if (!line.startsWith("-")) {
                    String str[] = line.split(",");

                    List<Country> neighbours = new ArrayList<>();
                    for (int j = 1; j < str.length; j++) {
                        neighbours.add(nameCountry.get(str[j]));
                    }

                    countryNeighbors.put(nameCountry.get(str[0]), neighbours);
                }
            }
        } catch (Exception ex) {
            System.out.println("Error in buildCountryNeighbors");
        }
    }

    private void buildContinentCountry() {
        try (BufferedReader br = new BufferedReader(new FileReader(urlCountries))) {
            String line;
            List<Country> countries = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                
                if (line.startsWith("-")) {
                    String str[] = line.split("-");     //str[] sarà un'array di 3 stringhe, la prima sarà vuota, questo perchè line inizia con "-"
                    continentCountries.put(str[1], countries);                    
                    continentBonus.put(str[1],new Integer(str[2]));
                    countries = new ArrayList<>();
                } else {
                    String str[] = line.split(",");
                    countries.add(getCountryByName(str[0]));
                }
            }
        } catch (Exception ex) {
            System.out.println("Error in buildContinentCountry");
        }
    }
    
    /**
     * Legge il file specificato da urlMissions, da ogni riga estrae la description
     * di un obiettivo e crea gli oggetti Mission
     * 
     * @throws qualche eccezione legata alla lettura del file
     * @author Federico
     */
    private void buildMissions() {
        
        try (BufferedReader br = new BufferedReader(new FileReader(urlMissions))) {

            String line;
            Mission mission;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("=");
                mission = new Mission(tokens[1]);
                this.missions.add(mission);
                buildTargetListForMission(mission);
            }

        } catch (Exception ex) {
            System.out.println("File not found");
        }
    }
    
    /**
     * Costruisce la targetList (contenente i country da conquistare) di una mission
     * 
     * @param mission
     * @author Federico
     * 
     */
    private void buildTargetListForMission(Mission mission){
        
        String description = mission.getDescription();
        List<String> continents = new ArrayList<> (continentCountries.keySet());
        for (int i=0 ; i<continents.size() ; i++){
            if(description.contains(continents.get(i))){
            	mission.setTargetList(continentCountries.get(continents.get(i)));            
            }
        }
        if(mission.getTargetList().isEmpty()){
            mission.setNrCountryToConquer(24);
        }
    }
    
    /**
     * Effettua l'assegnazione degli obiettivi ai giocatori.
     * 
     * @param players
     * @author Federico
     */
    public void assignMissionToPlayers(List<Player> players) {
        Collections.shuffle(this.missions);
        for(int i=0;i<players.size();i++){
            players.get(i).setMission(missions.get(i));
        }
    }
    
    /**
     * Effettua l'assegnazione iniziale dei territori ai giocatori (random).
     *
     * @param players
     * @author Elisa
     */
    public void assignCountriesToPlayers(List<Player> players) throws Exception {

        List<Country> countries = getCountriesList();
        Collections.shuffle(countries);
        int nCountries = this.countryPlayer.size();
        int round = 0;
        while (nCountries != 0) {
            this.countryPlayer.put(countries.get(round), nextPlayer(players, round));
            //System.out.println(countries.get(round).getName()+" "+nextPlayer(players, round));
            round++;
            nCountries--;
        }

    }

    /**
     * Ritorna il giocatore successivo nella lista dei players a quello che si
     * passa; se il giocatore è l'ultimo della lista ritorna il primo.
     *
     * @param players
     * @param round
     * @return Player
     * @author Elisa
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
        return new ArrayList<>(countryPlayer.keySet());
    }

    /**
     * Ritorna il giocatore a cui appartiene quel territorio
     *
     * @param country
     * @return
     */
    public Player getPlayerByCountry(Country country) {
        return countryPlayer.get(country);
    }

    /**
     * Esegue la fase di rinforzo di inizio turno. Nr armate bonus =
     * ceil(nrTerritoriGiocatore/3) e le distribuisce a caso sui territori del
     * giocatore
     *
     * @param player il giocatore di turno
     * @author Elisa
     */
    void computeBonusArmies(Player player) {
        int bonus = 0;
        List<Country> countryOfThatPlayer = getMyCountries(player);
        Set<String> continentSet = continentCountries.keySet();
        for (String continent : continentSet) {
            if (countryOfThatPlayer.containsAll(continentCountries.get(continent)))
                bonus+=continentBonus.get(continent);
        }
        bonus += (int) Math.floor(getMyCountries(player).size() / 3);
        player.addBonusArmies(bonus);
    }

    /**
     * Ritorna una lista dei territori del giocatore
     *
     * @param player
     * @return
     * @author Alessandro
     */
    public List<Country> getMyCountries(Player player) {
        return this.countryPlayer.entrySet().stream().filter(mapEntry -> mapEntry.getValue().equals(player)).map(mapE -> mapE.getKey()).collect(Collectors.toList());
    }

    /**
     * Ritorna la lista dei vicini di un territorio.
     *
     * @param country
     */
    public List<Country> getNeighbors(Country country) {
        return countryNeighbors.get(country);
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
     * Controlla che l'attacco sia valido. return true se Country[0] è del
     * giocatore di turno e ha di un'armata, Country[1] è di un altro giocatore,
     * e i due territori sono confinanti, false altrimenti.
     *
     * @param countries array di territori, [0] attaccante, [1] attaccato
     * @param player il giocatore di turno.
     * @author Alessandro
     */
    /*public boolean verifyAttack(Country[] countries, Player player) {
        if (this.countryPlayer.get(countries[0]) != null && this.countryPlayer.get(countries[0]).equals(player) && countries[0].getArmies() > 1) {
            if (this.countryPlayer.get(countries[1]) != null && !this.countryPlayer.get(countries[1]).equals(player) ) {
                return true;
            }
        }
        return false;
    }*/
 /*
        Controlla che il territorio sia dell'active player e che si legale attaccare
     */
    public boolean controlAttacker(Country country, Player player) {
        return this.countryPlayer.get(country).equals(player) && country.getArmies() > 1;

    }

    public boolean controlPlayer(Country country, Player player) {
        return this.countryPlayer.get(country).equals(player);

    }

    /**
     * Controlla che il territorio non sia dell'active player e che sia un
     * confinante dell'attacker
     */
    public boolean controlDefender(Country attacker, Country defender, Player player) {

        return !this.countryPlayer.get(defender).equals(player) && this.getNeighbors(attacker).contains(defender);
    }
    /**
     * Controlla che toCountry  sia dell'active player e che sia un
     * confinante dell'fromCountry
     */
    public boolean controlMovement(Country fromCountry, Country toCountry, Player player) {

        return this.countryPlayer.get(toCountry).equals(player) && this.getNeighbors(fromCountry).contains(toCountry);
    }
    
    public void move(Country fromCountry, Country toCountry, int nrArmies){
        fromCountry.removeArmies(nrArmies);
        toCountry.addArmies(nrArmies);
    }

    /*
        Ridà il massimo numero di armate per lo spinner rispetto al tipo di country
     */
    public int getMaxArmies(Country country, boolean isAttacker) {

        if (isAttacker) {
            return Math.min(3, country.getArmies() - 1);
        }
        return Math.min(3, country.getArmies());
    }

    /**
     * Metodo chiamato nel caso in cui un giocatore abbia conquistato un
     * territorio. Setta il nuovo proprietario del territorio appena conquistato
     * (countryPlayer) e muove tante armate quante sono quelle con cui è stato
     * eseguito l'attacco dal territorio attaccante a quello conquistato.
     *
     * @param armies è il numero di armate con cui è stato sferrato l'attacco, e
     * in questa prima versione del gioco anche il numero di armate che vengono
     * spostate dal territorio attaccante a quello appena conquistato.
     * @author Alessandro
     */
    public void updateOnConquer(Country attackerCountry, Country defenderCountry, int armies) {
        Player attacker = this.countryPlayer.get(attackerCountry);
        this.countryPlayer.put(defenderCountry, attacker);
        attackerCountry.removeArmies(armies);
        defenderCountry.setArmies(armies);
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
        
        boolean result = false;
        
        if ( !player.getMission().getTargetList().isEmpty() ) {
            
            if ( getMyCountries(player).containsAll(player.getMission().getTargetList()) ) {
                result = true;
            }
            
        } else {
            
            if ( getMyCountries(player).size() == player.getMission().getNrCountryToConquer()){
                result = true;
            }
        }
        
        return result;
    }

    /**
     * Controlla se il territorio è stato conquistato
     *
     * @return true se non ha armate
     */
    public boolean isConquered(Country country) {
        return country.isConquered();
    }

    public Map<Country, List<Country>> getCountryNeighbors() {
        return this.countryNeighbors;
    }

    /*
     *   controlla se il difensore non ha più territori
     */
    public boolean hasLost(Player defenderPlayer) {
        return getMyCountries(defenderPlayer).isEmpty();
    }

    public void addArmies(Country country, int nArmies) {
        country.addArmies(nArmies);
    }

    public void removeArmies(Country country, int nArmies) {
        country.removeArmies(nArmies);
    }

    public boolean canAttackFromCountry(Country country) {
        return country.getArmies() > 1;
    }

    public Country getCountryByName(String countryName) {
        return nameCountry.get(countryName);
    }

    public String[] getCountriesColors() {
        String[] colors = new String[getCountriesList().size()];
        int i = 0;
        for (Country country : getCountriesList()) {
            colors[i] = getPlayerColorByCountry(country);
            i++;
        }
        return colors;
    }

    public String getPlayerColorByCountry(Country country) {
        return getPlayerByCountry(country).getColor();
    }

    public Map<String, List<Country>> getContinentCountries() {
        return continentCountries;
    }

    /**
     * Ritorna la lista delle countries che compongono un continent.
     *
     * @param continent
     * @return 
     */
    public List<Country> getCountriesByContinet(String continent) {
        return countryNeighbors.get(continent);
    }
}
