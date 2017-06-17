package risiko.map;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import risiko.missions.ContinentsMission;
import risiko.players.Player;

public class RisikoMapTest {

    private final int N_COUNTRIES = 42;
    private List<Player> players;
    private RisikoMap instance;

    public RisikoMapTest() {
        players = new ArrayList<>();
        players.add(new Player("player1", "RED"));
        players.add(new Player("player2", "GREEN"));
        instance = new RisikoMap();
        instance.initGame(players);
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of assignMissionToPlayers method, of class RisikoMap.
     */
    @Test
    public void testAssignMissionToPlayers() {
        System.out.println("assignMissionToPlayers");
        instance.assignMissionToPlayers(players);
        players.stream().forEach((player) -> {
            assertNotNull(player.getMission());
        });
    }

    /**
     * Test of assignCountriesToPlayers method, of class RisikoMap.
     */
    @Test
    public void testAssignCountriesToPlayers() {
        System.out.println("assignCountriesToPlayers");
        instance.assignCountriesToPlayers(players);
        players.stream().forEach((player) -> {
            assertNotNull(instance.getMyCountries(player));
        });

    }

    /**
     * Test of getCountriesList method, of class RisikoMap.
     */
    @Test
    public void testGetCountriesList() {
        System.out.println("getCountriesList");
        List<Country> result = instance.getCountriesList();
        assertNotNull(result);
        assertEquals(result.size(), N_COUNTRIES);
    }

    /**
     * Test of computeBonusArmies method, of class RisikoMap.
     */
    @Test
    public void testComputeBonusArmies() {
        instance.initGame(players);
        System.out.println("computeBonusArmies");
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            instance.computeBonusArmies(player);
            double bonusArmies = player.getBonusArmies();
            double nrCountries = instance.getMyCountries(player).size();
            assertTrue((bonusArmies) >= Math.floor(nrCountries / 3));
            //Maggiore uguale perchè non calcolo il bonus derivante dai continenti
        }
    }

    /**
     * Test of hasConqueredContinent method, of class RisikoMap.
     */
    @Test
    public void testHasConqueredContinent() {
        System.out.println("hasConqueredContinent");
        Player player = players.get(0);
        Country country = instance.getMyCountries(player).get(0);
        Continent continent = instance.getContinentByCountry(country);
        for (Country c : continent.getCountries()) {
            if (c.getOwner() != player) {
                country.updateOnConquer(c);
            }
        }

        boolean result = instance.hasConqueredContinent(country);
        assertTrue(result);
        Player otherPlayer = players.get(1);
        Country otherCountry = instance.getMyCountries(otherPlayer).get(0);

        for (Country c : continent.getCountries()) {
            if (c != country) {
                otherCountry.updateOnConquer(c);
                break;
            }
        }
        result = instance.hasConqueredContinent(country);
        assertFalse(result);
    }

    /**
     * Test of getContinentByCountry method, of class RisikoMap.
     */
    @Test
    public void testGetContinentByCountry() {
        System.out.println("getContinentByCountry");
        instance.initGame(players);
        String[] countries = {"Alaska", "Venezuela", "Ucraina", "Madagascar", "India", "Indonesia"};
        String[] continents = {"Nord America", "Sud America", "Europa", "Africa", "Asia", "Oceania"};
        int i = 0;
        for (String countryName : countries) {
            Country country = instance.getCountryByName(countryName);
            Continent continent = instance.getContinentByCountry(country);
            assertEquals(continents[i], continent.getName());
            i++;
        }
    }

    /**
     * Test of getMyCountries method, of class RisikoMap.
     */
    @Test
    public void testGetMyCountries() {
        System.out.println("getMyCountries");
        List<Player> newPlayers = (List<Player>) ((ArrayList) players).clone();
        for (int i = 0; i < 4; i++) {
            newPlayers.add(new Player("", ""));
            instance.initGame(newPlayers);
            int total = 0;
            for (Player player : newPlayers) {
                List<Country> myCountries = instance.getMyCountries(player);
                total += myCountries.size();
                assertNotNull(myCountries);
                for (Country country : myCountries) {
                    assertNotNull(country);
                }
                assertTrue(Math.abs(myCountries.size() - N_COUNTRIES / (newPlayers.size())) <= 1);
            }
            assertEquals(N_COUNTRIES, total);
        }
    }

    /**
     * Test of getNeighbors method, of class RisikoMap.
     */
    @Test
    public void testGetNeighbors() {
        System.out.println("getNeighbors");
        instance.initGame(players);
        Country country = instance.getCountryByName("Alaska");
        List<Country> result = instance.getNeighbors(country);
        assertNotNull(result);
    }

    /**
     * Test of playerCanAttack method, of class RisikoMap.
     */
    @Test
    public void testPlayerCanAttack() {
        System.out.println("playerCanAttack");
        instance.initGame(players);

        for (Country country : instance.getCountriesList()) {
            country.setArmies(1);
        }
        assertFalse(instance.playerCanAttack(players.get(0)));
        assertFalse(instance.playerCanAttack(players.get(1)));

        for (Country country : instance.getCountriesList()) {
            country.setArmies(3);
        }
        assertTrue(instance.playerCanAttack(players.get(0)));
        assertTrue(instance.playerCanAttack(players.get(1)));

    }

    /**
     * Test of hasLost method, of class RisikoMap.
     */
    @Test
    public void testHasLost() {
        System.out.println("hasLost");
        Player player = players.get(0);
        Player otherPlayer = players.get(1);
        Country attackerCountry = instance.getMyCountries(otherPlayer).get(0);
        for (Country country : instance.getCountriesList()) {
            if (country.getOwner() == player) {
                attackerCountry.updateOnConquer(country);
            }
        }
        boolean result = instance.hasLost(player);
        assertTrue(result);

        result = instance.hasLost(otherPlayer);
        assertFalse(result);
    }

    /**
     * Test of checkIfWinner method, of class RisikoMap.
     */
    @Test

    public void testCheckIfWinner() {
        System.out.println("checkIfWinner");
        instance.initGame(players);
        Player player = players.get(0);
        List<Country> targetList = instance.getMyCountries(player);
        ContinentsMission mission = new ContinentsMission("test", 0);
        mission.setTargetList(targetList);
        player.setMission(mission);
        boolean expResult = true;
        boolean result = instance.checkIfWinner(player);
        assertEquals(expResult, result);

    }

    /**
     * Test of isConquered method, of class RisikoMap.
     */
    @Test
    public void testIsConquered() {
        System.out.println("isConquered");
        instance.initGame(players);
        Country country = instance.getCountryByName("Egitto");
        country.setArmies(0);

        boolean expResult = true;
        boolean result = instance.isConquered(country);
        assertEquals(expResult, result);
    }

    /**
     * Test of getCountryByName method, of class RisikoMap.
     */
    @Test
    public void testGetCountryByName() {
        System.out.println("getCountryByName");
        instance.initGame(players);
        String countryName = "Alaska";
        Country result = instance.getCountryByName(countryName);
        assertNotNull(result);

    }

    /**
     * Test of changeOwner method, of class RisikoMap.
     */
    @Test
    public void testChangeOwner() {
        System.out.println("changeOwner");
        instance.initGame(players);
        Country alaska = instance.getCountryByName("Alaska");

        Player oldOwner = alaska.getOwner();
        List<Country> countriesOfOldOwner = instance.getMyCountries(oldOwner);
        players.remove(oldOwner);
        Player newOwner = players.get(0);
        instance.changeOwner(oldOwner, newOwner);

        for (Country country : countriesOfOldOwner) {
            assertEquals(country.getOwner(),newOwner);
        }
    }
}
