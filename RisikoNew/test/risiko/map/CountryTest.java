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
    private String NAME_INSTANCE = "instance";
    private Country instance;
    List<Country> neighbors;

    public CountryTest() {
        players = new ArrayList<>();
        players.add(new Player("playerInstance", "BLUE"));

        neighbors = new ArrayList<>();

        instance = new Country(NAME_INSTANCE);
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
        assertNotNull(instance.getNeighbors());
    }

    /**
     * Test of setNeighbors method, of class Country.
     */
    @Test
    public void testSetNeighbors() {
        System.out.println("setNeighbors");
        instance.setNeighbors(null);
        assertNull(instance.getNeighbors());

        neighbors = new ArrayList<>();
        neighbors.add(new Country("Country0"));
        neighbors.add(new Country("Country1"));
        instance.setNeighbors(neighbors);
        assertNotNull(instance.getNeighbors());
    }

    /**
     * Test of getName method, of class Country.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        assertNotNull(instance.getName());
    }

    /**
     * Test of setArmies method, of class Country.
     */
    @Test
    public void testSetArmies() {
        System.out.println("setArmies");
        instance.setArmies(5);
        assertEquals(instance.getArmies(), 5);
    }

    /**
     * Test of getArmies method, of class Country.
     */
    @Test
    public void testGetArmies() {
        System.out.println("getArmies");
        instance.setArmies(3);
        assertEquals(instance.getArmies(), 3);
    }

    /**
     * Test of removeArmies method, of class Country.
     */
    @Test
    public void testRemoveArmies() {
        System.out.println("removeArmies");
        int nArmies = instance.getArmies();
        instance.removeArmies(nArmies);
        assertEquals(instance.getArmies(), 0);
    }

    /**
     * Test of addArmies method, of class Country.
     */
    @Test
    public void testAddArmies() {
        System.out.println("addArmies");
        instance.setArmies(2);
        instance.addArmies(5);
        assertEquals(instance.getArmies(), 7);
    }

    /**
     * Test of isConquered method, of class Country.
     */
    @Test
    public void testIsConquered() {
        System.out.println("isConquered");
        instance.setArmies(0);
        assertTrue(instance.isConquered());
        instance.setArmies(2);
        assertFalse(instance.isConquered());
    }

    /**
     * Test of compareTo method, of class Country.
     */
    @Test
    public void testCompareTo() {
        System.out.println("compareTo");
        instance.setArmies(10);
        neighbors.add(new Country("countryRND"));
        Country neighbor0 = instance.getNeighbors().get(0);
        neighbor0.setArmies(5);
        assertTrue(instance.compareTo(neighbor0) > 0);

        neighbor0.setArmies(10);
        assertTrue(instance.compareTo(neighbor0) == 0);

        neighbor0.setArmies(50);
        assertTrue(instance.compareTo(neighbor0) < 0);
    }

    /**
     * Test of toString method, of class Country.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        assertEquals(instance.getName(), NAME_INSTANCE);
    }

    /**
     * Test of getMaxArmies method, of class RisikoMap.
     */
    @Test
    public void testGetMaxArmies() {
        System.out.println("getMaxArmies");
        instance.setArmies(3);
        int result0 = instance.getMaxArmies(true);
        assertEquals(2, result0);

        int result1 = instance.getMaxArmies(false);
        assertEquals(3, result1);
    }

    /**
     * Test of getOwner method, of class Country.
     */
    @Test
    public void testGetOwner() {
        System.out.println("getOwner");
        Player owner = new Player("PlayerInstance", "BLUE");
        instance.setOwner(owner);
        assertEquals(owner, instance.getOwner());
    }

    /**
     * Test of setOwner method, of class Country.
     */
    @Test
    public void testSetOwner() {
        System.out.println("setOwner");
        Player owner = new Player("PlayerInstance", "BLUE");
        instance.setOwner(owner);
        assertNotNull(instance.getOwner());
    }

    /**
     * Test of reinforce method, of class Country.
     */
    @Test
    public void testReinforce() {
        System.out.println("reinforce");
        instance.setArmies(0);
        Player owner = instance.getOwner();
        owner.addBonusArmies(1);
        instance.reinforce();
        assertTrue(instance.getArmies() == 1);
    }

    /**
     * Test of controlMovement method, of class RisikoMap.
     */
    @Test
    public void testControlMovement() {
        System.out.println("controlMovement");
        Player owner = instance.getOwner();
        Country toCountry = new Country("toCountry");
        assertFalse(instance.canMoveTo(toCountry));
        toCountry.setOwner(owner);
        assertFalse(instance.canMoveTo(toCountry));
        neighbors.add(toCountry);
        assertTrue(instance.canMoveTo(toCountry));
    }

    /**
     * Test of controlPlayer method, of class RisikoMap.
     */
    @Test
    public void testIsMyOwner() {
        System.out.println("isMyOwner");
        Player owner = new Player("playerTmp", "BLUE");
        assertFalse(instance.isMyOwner(owner));
        instance.setOwner(owner);
        assertTrue(instance.isMyOwner(owner));
    }

    /**
     * Test of controlDefender method, of class RisikoMap. FAILED :(
     */
    @Test
    public void testControlDefender() {
        System.out.println("controlDefender");
        Player theSameOwner = instance.getOwner();
        Country defender = new Country("defender");
        defender.setOwner(theSameOwner);
        assertFalse(instance.controlDefender(defender));

        neighbors.add(defender);
        assertFalse(instance.controlDefender(defender));

        defender.setOwner(new Player("notTheSamePlayer", "BLUE"));
        assertTrue(instance.controlDefender(defender));
    }

    /**
     * Test of updateOnConquer method, of class RisikoMap.
     */
    @Test
    public void testConquer() {
        System.out.println("conquer");
        Country defender = new Country("defender");
        Player owner = instance.getOwner();
        assertFalse(owner.hasConqueredACountry());
        assertNotEquals(owner, defender.getOwner());

        instance.conquer(defender);
        assertTrue(owner.hasConqueredACountry());
        assertEquals(owner, defender.getOwner());
    }

    /**
     * Test of controlAttacker method, of class RisikoMap.
     */
    @Test
    public void testControlAttacker() {
        System.out.println("controlAttacker");
        Player random = new Player("random", "BLUE");
        Player owner = instance.getOwner();

        instance.setArmies(1);
        assertFalse(instance.controlAttacker(random));
        assertFalse(instance.controlAttacker(owner));
        instance.setArmies(2);
        assertFalse(instance.controlAttacker(random));
        assertTrue(instance.controlAttacker(owner));
    }

    /**
     * Test of controlFromCountryPlayer method, of class RisikoMap.
     */
    @Test
    public void testCanMove() {//qualcosa non torna
        System.out.println("canMove");
        Player random = new Player("random", "BLUE");
        Player owner = instance.getOwner();
        Country neighbor = new Country("neighbor");
        neighbor.setOwner(random);

        instance.setArmies(1);
        assertFalse(instance.canMove(random));
        assertFalse(instance.canMove(owner));
        instance.setArmies(2);
        assertFalse(instance.canMove(random));
        assertFalse(instance.canMove(owner));
        neighbors.add(neighbor);
        assertFalse(instance.canMove(random));
        assertFalse(instance.canMove(owner));
        neighbor.setOwner(owner);
        assertFalse(instance.canMove(random));
        assertTrue(instance.canMove(owner));
    }

    /**
     * Test of canAttackFromCountry method, of class RisikoMap.
     */
    @Test
    public void testCanAttack() {
        System.out.println("canAttack");
        Player random = new Player("random", "BLUE");
        Player owner = instance.getOwner();
        Country neighbor = new Country("neighbor");
        neighbor.setOwner(owner);
        neighbors.add(neighbor);

        instance.setArmies(1);
        assertFalse(instance.canAttack());
        instance.setArmies(2);
        assertFalse(instance.canAttack());
        neighbor.setOwner(random);
        assertTrue(instance.canAttack());
    }
}