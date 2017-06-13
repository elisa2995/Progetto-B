/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package risiko;

import risiko.map.Country;
import risiko.map.RisikoMap;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import risiko.players.Player;

/**
 *
 * @author Elisa
 */
public class RisikoMapTest {

    private final int N_COUNTRIES=42;
    private List<Player> players;
    private RisikoMap instance;

    public RisikoMapTest() {

        players = new ArrayList<>();
        players.add(new Player("player1", "RED"));
        players.add(new Player("player2", "GREEN"));
        instance = new RisikoMap();
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

//    /**
//     * Test of getPlayerByCountry method, of class RisikoMap.
//     */
//    @Test
//    public void testGetPlayerByCountry() {
//        System.out.println("getPlayerByCountry");
//        Country country = null;
//        RisikoMap instance = new RisikoMap();
//        Player expResult = null;
//        Player result = instance.getPlayerByCountry(country);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of computeBonusArmies method, of class RisikoMap.
//     */
//    @Test
//    public void testComputeBonusArmies() {
//        System.out.println("computeBonusArmies");
//        Player player = null;
//        RisikoMap instance = new RisikoMap();
//        instance.computeBonusArmies(player);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of hasConqueredContinent method, of class RisikoMap.
//     */
//    @Test
//    public void testHasConqueredContinent() {
//        System.out.println("hasConqueredContinent");
//        Player player = null;
//        Country justConqueredCountry = null;
//        RisikoMap instance = new RisikoMap();
//        boolean expResult = false;
//        boolean result = instance.hasConqueredContinent(player, justConqueredCountry);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getContinentByCountry method, of class RisikoMap.
//     */
//    @Test
//    public void testGetContinentByCountry() {
//        System.out.println("getContinentByCountry");
//        Country country = null;
//        RisikoMap instance = new RisikoMap();
//        Continent expResult = null;
//        Continent result = instance.getContinentByCountry(country);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getMyCountries method, of class RisikoMap.
//     */
//    @Test
//    public void testGetMyCountries() {
//        System.out.println("getMyCountries");
//        Player player = null;
//        RisikoMap instance = new RisikoMap();
//        List<Country> expResult = null;
//        List<Country> result = instance.getMyCountries(player);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getNeighbors method, of class RisikoMap.
//     */
//    @Test
//    public void testGetNeighbors() {
//        System.out.println("getNeighbors");
//        Country country = null;
//        RisikoMap instance = new RisikoMap();
//        List<Country> expResult = null;
//        List<Country> result = instance.getNeighbors(country);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of playerCanAttack method, of class RisikoMap.
//     */
//    @Test
//    public void testPlayerCanAttack() {
//        System.out.println("playerCanAttack");
//        Player player = null;
//        RisikoMap instance = new RisikoMap();
//        boolean expResult = false;
//        boolean result = instance.playerCanAttack(player);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of controlAttacker method, of class RisikoMap.
//     */
//    @Test
//    public void testControlAttacker() {
//        System.out.println("controlAttacker");
//        Country country = null;
//        Player player = null;
//        RisikoMap instance = new RisikoMap();
//        boolean expResult = false;
//        boolean result = instance.controlAttacker(country, player);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of controlFromCountryPlayer method, of class RisikoMap.
//     */
//    @Test
//    public void testControlFromCountryPlayer() {
//        System.out.println("controlFromCountryPlayer");
//        Country country = null;
//        Player player = null;
//        RisikoMap instance = new RisikoMap();
//        boolean expResult = false;
//        boolean result = instance.controlFromCountryPlayer(country, player);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of controlPlayer method, of class RisikoMap.
//     */
//    @Test
//    public void testControlPlayer() {
//        System.out.println("controlPlayer");
//        Country country = null;
//        Player player = null;
//        RisikoMap instance = new RisikoMap();
//        boolean expResult = false;
//        boolean result = instance.controlPlayer(country, player);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of controlDefender method, of class RisikoMap.
//     */
//    @Test
//    public void testControlDefender() {
//        System.out.println("controlDefender");
//        Country attacker = null;
//        Country defender = null;
//        Player player = null;
//        RisikoMap instance = new RisikoMap();
//        boolean expResult = false;
//        boolean result = instance.controlDefender(attacker, defender, player);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of controlMovement method, of class RisikoMap.
//     */
//    @Test
//    public void testControlMovement() {
//        System.out.println("controlMovement");
//        Country fromCountry = null;
//        Country toCountry = null;
//        Player player = null;
//        RisikoMap instance = new RisikoMap();
//        boolean expResult = false;
//        boolean result = instance.controlMovement(fromCountry, toCountry, player);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of move method, of class RisikoMap.
//     */
//    @Test
//    public void testMove() {
//        System.out.println("move");
//        Country fromCountry = null;
//        Country toCountry = null;
//        int nrArmies = 0;
//        RisikoMap instance = new RisikoMap();
//        instance.move(fromCountry, toCountry, nrArmies);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getMaxArmies method, of class RisikoMap.
//     */
//    @Test
//    public void testGetMaxArmies() {
//        System.out.println("getMaxArmies");
//        Country country = null;
//        boolean isAttacker = false;
//        RisikoMap instance = new RisikoMap();
//        int expResult = 0;
//        int result = instance.getMaxArmies(country, isAttacker);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of updateOnConquer method, of class RisikoMap.
//     */
//    @Test
//    public void testUpdateOnConquer() {
//        System.out.println("updateOnConquer");
//        Country attackerCountry = null;
//        Country defenderCountry = null;
//        int armies = 0;
//        RisikoMap instance = new RisikoMap();
//        instance.updateOnConquer(attackerCountry, defenderCountry, armies);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of checkIfWinner method, of class RisikoMap.
//     */
//    @Test
//    public void testCheckIfWinner() {
//        System.out.println("checkIfWinner");
//        Player player = null;
//        RisikoMap instance = new RisikoMap();
//        boolean expResult = false;
//        boolean result = instance.checkIfWinner(player);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of isConquered method, of class RisikoMap.
//     */
//    @Test
//    public void testIsConquered() {
//        System.out.println("isConquered");
//        Country country = null;
//        RisikoMap instance = new RisikoMap();
//        boolean expResult = false;
//        boolean result = instance.isConquered(country);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of hasLost method, of class RisikoMap.
//     */
//    @Test
//    public void testHasLost() {
//        System.out.println("hasLost");
//        Player defenderPlayer = null;
//        RisikoMap instance = new RisikoMap();
//        boolean expResult = false;
//        boolean result = instance.hasLost(defenderPlayer);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of addArmies method, of class RisikoMap.
//     */
//    @Test
//    public void testAddArmies() {
//        System.out.println("addArmies");
//        Country country = null;
//        int nArmies = 0;
//        RisikoMap instance = new RisikoMap();
//        instance.addArmies(country, nArmies);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of removeArmies method, of class RisikoMap.
//     */
//    @Test
//    public void testRemoveArmies() {
//        System.out.println("removeArmies");
//        Country country = null;
//        int nArmies = 0;
//        RisikoMap instance = new RisikoMap();
//        instance.removeArmies(country, nArmies);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of canAttackFromCountry method, of class RisikoMap.
//     */
//    @Test
//    public void testCanAttackFromCountry() {
//        System.out.println("canAttackFromCountry");
//        Country country = null;
//        RisikoMap instance = new RisikoMap();
//        boolean expResult = false;
//        boolean result = instance.canAttackFromCountry(country);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getCountryByName method, of class RisikoMap.
//     */
//    @Test
//    public void testGetCountryByName() {
//        System.out.println("getCountryByName");
//        String countryName = "";
//        RisikoMap instance = new RisikoMap();
//        Country expResult = null;
//        Country result = instance.getCountryByName(countryName);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getCountriesColors method, of class RisikoMap.
//     */
//    @Test
//    public void testGetCountriesColors() {
//        System.out.println("getCountriesColors");
//        RisikoMap instance = new RisikoMap();
//        String[] expResult = null;
//        String[] result = instance.getCountriesColors();
//        assertArrayEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getPlayerColorByCountry method, of class RisikoMap.
//     */
//    @Test
//    public void testGetPlayerColorByCountry() {
//        System.out.println("getPlayerColorByCountry");
//        Country country = null;
//        RisikoMap instance = new RisikoMap();
//        String expResult = "";
//        String result = instance.getPlayerColorByCountry(country);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of changeOwner method, of class RisikoMap.
//     */
//    @Test
//    public void testChangeOwner() {
//        System.out.println("changeOwner");
//        Player oldOwner = null;
//        Player newOwner = null;
//        RisikoMap instance = new RisikoMap();
//        instance.changeOwner(oldOwner, newOwner);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

}