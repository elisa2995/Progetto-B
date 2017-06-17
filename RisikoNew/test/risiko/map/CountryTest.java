/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package risiko.map;

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
 * @author andrea
 */
public class CountryTest {

    private List<Player> players;
    private Country instance;

    public CountryTest() {
        players = new ArrayList<>();
        players.add(new Player("player0", "RED"));
        players.add(new Player("player1", "GREEN"));

        List<Country> neighbors = new ArrayList<>();
        neighbors.add(new Country("CountryOfPlayer0"));
        neighbors.get(0).addArmies(3);
        neighbors.get(0).setOwner(players.get(0));
        neighbors.add(new Country("CountryOfPlayer1"));
        neighbors.get(1).addArmies(3);
        neighbors.get(1).setOwner(players.get(1));

        instance = new Country("instance");
        instance.addArmies(3);
        instance.setOwner(players.get(0));
        instance.setNeighbors(neighbors);
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
     * Test of getNeighbors method, of class Country.
     */
    @Test
    public void testGetNeighbors() {
        System.out.println("getNeighbors");
        Country instance = null;
        List<Country> expResult = null;
        List<Country> result = instance.getNeighbors();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setNeighbors method, of class Country.
     */
    @Test
    public void testSetNeighbors() {
        System.out.println("setNeighbors");
        List<Country> neighbors = null;
        Country instance = null;
        instance.setNeighbors(neighbors);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getName method, of class Country.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        Country instance = null;
        String expResult = "";
        String result = instance.getName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setArmies method, of class Country.
     */
    @Test
    public void testSetArmies() {
        System.out.println("setArmies");
        int armies = 0;
        Country instance = null;
        instance.setArmies(armies);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getArmies method, of class Country.
     */
    @Test
    public void testGetArmies() {
        System.out.println("getArmies");
        Country instance = null;
        int expResult = 0;
        int result = instance.getArmies();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeArmies method, of class Country.
     */
    @Test
    public void testRemoveArmies() {
        System.out.println("removeArmies");
        int armies = 0;
        Country instance = null;
        instance.removeArmies(armies);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addArmies method, of class Country.
     */
    @Test
    public void testAddArmies() {
        System.out.println("addArmies");
        int armies = 0;
        Country instance = null;
        instance.addArmies(armies);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of incrementArmies method, of class Country.
     */
    @Test
    public void testIncrementArmies() {
        System.out.println("incrementArmies");
        Country instance = null;
        instance.incrementArmies();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isConquered method, of class Country.
     */
    @Test
    public void testIsConquered() {
        System.out.println("isConquered");
        Country instance = null;
        boolean expResult = false;
        boolean result = instance.isConquered();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of compareTo method, of class Country.
     */
    @Test
    public void testCompareTo() {
        System.out.println("compareTo");
        Country o = null;
        Country instance = null;
        int expResult = 0;
        int result = instance.compareTo(o);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class Country.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Country instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMaxArmies method, of class RisikoMap.
     */
    @Test
    public void testGetMaxArmies() {
        System.out.println("getMaxArmies");
        boolean isAttacker = true;
        int expResult0 = 2;
        int result0 = instance.getMaxArmies(isAttacker);
        assertEquals(expResult0, result0);

        int expResult1 = 3;
        isAttacker = false;
        int result1 = instance.getMaxArmies(isAttacker);
        assertEquals(expResult1, result1);
    }

    /**
     * Test of getOwner method, of class Country.
     */
    @Test
    public void testGetOwner() {
        System.out.println("getOwner");
        Country instance = null;
        Player expResult = null;
        Player result = instance.getOwner();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setOwner method, of class Country.
     */
    @Test
    public void testSetOwner() {
        System.out.println("setOwner");
        Player owner = null;
        Country instance = null;
        instance.setOwner(owner);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of reinforce method, of class Country.
     */
    @Test
    public void testReinforce() {
        System.out.println("reinforce");
        Country instance = null;
        instance.reinforce();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

//    /**
//     * Test of controlMovement method, of class RisikoMap.
//     */
//    @Test
//    public void testControlMovement() {
//        System.out.println("controlMovement");
//        Country fromCountry = null;
//        Country toCountry = null;
//        Player player = null;
//        boolean expResult = false;
//        boolean result = instance.controlMovement(toCountry);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

//    /**
//     * Test of controlPlayer method, of class RisikoMap.
//     */
//    @Test
//    public void testControlPlayer() {
//        System.out.println("controlPlayer");
//        Player player0 = players.get(0);
//        boolean expResult = player0Countries.contains(country);
//        boolean result = instance.controlPlayer(player0);
//        assertEquals(expResult, result);
//    }

//    /**
//     * Test of controlDefender method, of class RisikoMap. FAILED :(
//     */
//    @Test
//    public void testControlDefender() {
//        System.out.println("controlDefender");
//        for (int i = 0; i < 1000; i++) {
//            Country alaska = instance.getCountryByName("Alaska");
//            Country alberta = instance.getCountryByName("Alberta");
//            Country mongolia = instance.getCountryByName("Mongolia");
//            Player alaskaPlayer = alaska.getOwner();
//            Player albertaPlayer = alberta.getOwner();
//
//            if (alaskaPlayer.equals(albertaPlayer)) {
//                assertFalse(instance.controlDefender(alaska, alberta));
//            } else {
//                assertTrue(instance.controlDefender(alaska, alberta));
//            }
//            assertFalse(instance.controlDefender(alaska, mongolia));
//        }
//    }

//    /**
//     * Test of updateOnConquer method, of class RisikoMap.
//     */
//    @Test
//    public void testUpdateOnConquer() {
//        System.out.println("updateOnConquer");
//        instance.initGame(players);
//        instance.updateOnConquer(defenderCountry);
//        Player player0 = attackerCountry.getOwner();
//        Player player1 = defenderCountry.getOwner();
//        assertEquals(player0, player1);
//    }

//    /**
//     * Test of controlAttacker method, of class RisikoMap.
//     */
//    @Test
//    public void testControlAttacker() {
//        System.out.println("controlAttacker");
//        instance.initGame(players);
//        Player player0 = players.get(0);
//        Player player1 = players.get(1);
//        for (int i = 0; i < instance.getMyCountries(player0).size(); i++) {
//            Country countryOfPlayer0 = instance.getMyCountries(player0).get(i);
//            countryOfPlayer0.setArmies(3);
//            assertTrue(instance.controlAttacker(countryOfPlayer0, player0));
//        }
//        for (int i = 0; i < instance.getMyCountries(player0).size(); i++) {
//            Country countryOfPlayer0 = instance.getMyCountries(player0).get(i);
//            countryOfPlayer0.setArmies(3);
//            assertFalse(instance.controlAttacker(countryOfPlayer0, player1));
//        }
//        for (int i = 0; i < instance.getMyCountries(player0).size(); i++) {
//            Country countryOfPlayer0 = instance.getMyCountries(player0).get(i);
//            countryOfPlayer0.setArmies(1);
//            assertFalse(instance.controlAttacker(countryOfPlayer0, player0));
//        }
//        for (int i = 0; i < instance.getMyCountries(player0).size(); i++) {
//            Country countryOfPlayer0 = instance.getMyCountries(player0).get(i);
//            countryOfPlayer0.setArmies(1);
//            assertFalse(instance.controlAttacker(countryOfPlayer0, player1));
//        }
//    }

//    /**
//     * Test of controlFromCountryPlayer method, of class RisikoMap.
//     */
//    @Test
//    public void testControlFromCountryPlayer() {//qualcosa non torna
//        System.out.println("controlFromCountryPlayer");
//        instance.initGame(players);
//        Country country = instance.getCountryByName("Alaska");
//        Player player0 = players.get(0);
//
//        boolean expResult = false;
//        Player subjectPlayer = country.getOwner();
//        for (Country neighbor : country.getNeighbors()) {
//            Player neighborPlayer = neighbor.getOwner();
//            if (neighborPlayer.equals(subjectPlayer)) {
//                expResult = true;
//            }
//        }
//        expResult = country.getArmies() > 1 && subjectPlayer.equals(player0);
//        boolean result = instance.controlFromCountryPlayer(country, player0);
//        assertEquals(expResult, result);
//    }

//    /**
//     * Test of canAttackFromCountry method, of class RisikoMap.
//     */
//    @Test
//    public void testCanAttackFromCountry() {
//        System.out.println("canAttackFromCountry");
//        boolean result;
//        for (Country country : instance.getCountriesList()) {
//            Player player = country.getOwner();
//            if (instance.getMyCountries(player).containsAll(country.getNeighbors())) {
//                result = instance.canAttackFromCountry(country);
//                assertFalse(result);
//                break;
//            }
//            for (Country c : country.getNeighbors()) {
//                The neighbor is not a country of the player
//                's
//                if (c.getOwner() != player) {
//                    result = instance.canAttackFromCountry(country);
//                    assertTrue(result);
//                    The country has only 1 army country
//                    .removeArmies(country.getArmies() - 1);
//                    result = instance.canAttackFromCountry(country);
//                    assertFalse(result);
//                    break;
//                }
//
//            }
//
//            for (Country c : country.getNeighbors()) {
//                The neighbor is a country of the player
//                's
//                if (c.getOwner() == country.getOwner()     {
//                    result = instance.canAttackFromCountry(country);
//                    assertFalse(result);
//                    break;
//                }
//
//            }
//        }
//
//    }
//
}