package risiko.players;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import risiko.BonusDeck;
import risiko.Card;
import risiko.Country;
import risiko.missions.ContinentsMission;
import risiko.missions.CountriesMission;
import risiko.missions.Mission;

public class PlayerTest {

    private Player instance;
    private BonusDeck deck;

    public PlayerTest() {
        instance = new Player("Foo", "black");
        deck = new BonusDeck();
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
     * Test of getName method, of class Player.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        String expResult = "Foo";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getBonusArmies method, of class Player.
     */
    @Test
    public void testGetBonusArmies() {
        System.out.println("getBonusArmies");
        for (int i = 0; i < 5; i++) {
            instance.addBonusArmies(i);
        }
        int expResult = 1 + 2 + 3 + 4;
        int result = instance.getBonusArmies();
        assertEquals(expResult, result);
    }

    /**
     * Test of addBonusArmies method, of class Player.
     */
    @Test
    public void testAddBonusArmies() {
        System.out.println("addBonusArmies");
        instance = new Player("Foo", "black");
        int tmp = 0, randomArmies = 0;
        for (int i = 0; i < 10; i++) {
            randomArmies = new Random().nextInt(i+1);
            tmp += randomArmies;
            instance.addBonusArmies(randomArmies);
        }
        assertEquals(tmp, instance.getBonusArmies());
    }

    /**
     * Test of decrementBonusArmies method, of class Player.
     */
    @Test
    public void testDecrementBonusArmies() {
        System.out.println("decrementBonusArmies");
        Player instance = new Player("Foo", "black");
        int bonusArmies = 10;
        instance.addBonusArmies(bonusArmies);
        int counter = 0;
        while (instance.getBonusArmies() > 0) {
            instance.decrementBonusArmies();
            counter++;
        }
        assertEquals(bonusArmies, counter);
    }

    /**
     * Test of getColor method, of class Player.
     */
    @Test
    public void testGetColor() {
        System.out.println("getColor");
        Player instance;
        String[] colors = {"red", "black", "purple", "green", "blue", "yellow"};
        for (String color : colors) {
            instance = new Player("Foo", color);
            assertEquals(color, instance.getColor());
        }
    }

    /**
     * Test of getMission method, of class Player.
     */
    @Test
    public void testGetMission() {
        System.out.println("getMission");
        String description = "Lorem ipsum";
        int answer = 42;
        instance.setMission(new CountriesMission(description, answer));
        String result = instance.getMissionDescription();
        assertEquals(description, result);
        assertEquals(answer, instance.getMission().getPoints());
    }

    /**
     * Test of setMission method, of class Player.
     */
    @Test
    public void testSetMission() {
        System.out.println("setMission");
        String description = "Lorem ipsum";
        int answer = 42;
        Mission mission = null;
        instance.setMission(mission);
        assertNull(instance.getMission());

        mission = new ContinentsMission(description, answer);
        instance.setMission(mission);
        assertNotNull(instance.getMission());
    }

    /**
     * Test of getBonusCards method, of class Player.
     */
    @Test
    public void testGetBonusCards() {
        System.out.println("getBonusCards");
        ArrayList<Card> expResult = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Card card = deck.drawCard();
            expResult.add(card);
            instance.addCard(card);
        }
        ArrayList<Card> result = instance.getBonusCards();
        assertEquals(expResult, result);
    }

    /**
     * Test of playTris method, of class Player.
     */
    @Test
    public void testPlayTris() {
        System.out.println("playTris");
        Player instance = new Player("Foo", "black");
        int N_CARDS = 10;
        for (int i = 0; i < N_CARDS; i++) {
            instance.addCard(deck.drawCard());
        }
        Card[] cards = instance.getPlayableTris(deck.getTris()).keySet().iterator().next();
        instance.playTris(cards, 10);
        assertEquals(instance.getBonusCards().size(), N_CARDS - cards.length);
    }

    /**
     * Test of canPlayThisTris method, of class Player.
     */
    @Test
    public void testCanPlayThisTris() {
        System.out.println("canPlayThisTris");

        Player player = new Player("Foo", "black");
        boolean expResult;
        boolean result;
        int nCards = Card.values().length;
        
        // The player has no cards yet-> he can play no tris.
        for(Card[] tris : deck.getTris().keySet()){
            assertEquals(false, player.canPlayThisTris(tris));
        }
        
        Card[][] tris = new Card[nCards][3];
        int k = 4;
        for (int i = 0; i < nCards; i++) {
            for (int j = 0; j < k; j++) {
                player.addCard(Card.values()[i]);
                // I put 4 infantry, 3 cavalry, 2 artillery e 1 wild card 
            }
            k--;
        }

        // Three of a kind, he can play the infantry and cavalry ones
        for (int i = 0; i < nCards; i++) {
            for (int j = 0; j < 3; j++) {
                tris[i][j] = Card.values()[i];
            }
            expResult = i < 2;
            result = player.canPlayThisTris(tris[i]);
            assertEquals(expResult, result);
        }

        player.playTris(new Card[]{Card.ARTILLERY, Card.CAVALRY, Card.INFANTRY}, 1);
        // He now has 3 infantry, 2 cavalry, 1 artillery and 1 wild card. He can only play the infantry one.
        for (int i = 0; i < nCards; i++) {
            expResult = i < 1;
            result = player.canPlayThisTris(tris[i]);
            assertEquals(expResult, result);
        }

        // Mixed tris. He can play 2 infantry + 1 wild, 2 cavalry + 1 wild.
        for (int i = 0; i < nCards - 1; i++) {
            for (int j = 0; j < 3; j++) {
                tris[i][j] = (j == 2) ? Card.WILD : Card.values()[i];
            }
            expResult = i < 2;
            result = player.canPlayThisTris(tris[i]);
            assertEquals(expResult, result);
        }

        player.playTris(new Card[]{Card.INFANTRY, Card.INFANTRY, Card.WILD}, 1);
        // He now has 1 infantry, 2 cavalry, 1 artillery and no wild cards. 
        // He can't play any of the tris with the wild card.
        for (int i = 0; i < nCards - 1; i++) {
            expResult = false;
            result = player.canPlayThisTris(tris[i]);
            assertEquals(expResult, result);
        }

        Card[] tris2 = new Card[]{Card.INFANTRY, Card.CAVALRY, Card.ARTILLERY};
        expResult = true;
        result = player.canPlayThisTris(tris2);
        assertEquals(expResult, result);

    }

    /**
     * Test of getPlayableTris method, of class Player.
     */
    @Test
    public void testGetPlayableTris() {
        System.out.println("getPlayableTris");
        int nCards = Card.values().length;
        int k = 3;
        Player player = new Player("Foo", "black");
        
        for (int i = 0; i < nCards; i++) {
            for (int j = 0; j < k; j++) {
                player.addCard(Card.values()[i]);
                // 3 infantry, 2 cavalry, 1 artillery, no wild card
            }
            k--;
        }
        /*System.out.println("Cards:");
        for(Card card: player.getBonusCards()){
            System.out.println(card.name()+" ");
        }
        System.out.println("Playable tris");*/
        /*
           He can play: three of a kind : 1 (3 infantry)
                        with a wild card: 0
                        straight : 1
        */
        
        int expResult = 2;
        Map<Card[], Integer> tris = player.getPlayableTris((new BonusDeck()).getTris());
        /*for(Card[] cards : tris.keySet()){
            System.out.println(cards[0].name()+" "+cards[1].name()+" "+cards[2].name());
        }*/
        int result = tris.size();
        assertEquals(expResult, result);
        // ---------------------- I add a wild card -----------------
        player.addCard(Card.WILD);
        /*System.out.println("Player cards:");
        for(Card card: player.getBonusCards()){
            System.out.println(card.name()+" ");
        }
        System.out.println("Playable tris");*/
        tris = player.getPlayableTris((new BonusDeck()).getTris());
        /*for(Card[] cards : tris.keySet()){
            System.out.println(cards[0].name()+" "+cards[1].name()+" "+cards[2].name());
        }*/
        /*
           He can play : three of a kind: 1 (3 fanti)
                        with a wild card: 2 (2 fanti + jolly, 2 cavalli + jolly)
                        straight: 1
        */
        
        expResult = 4;
        result = tris.size();
        assertEquals(expResult, result);
    }

    /**
     * Test of addCard method, of class Player.
     */
    @Test
    public void testAddCard() {
        System.out.println("addCard");
        Card card = deck.drawCard();
        instance.addCard(card);
        assertEquals(card, instance.getLastDrawnCard());
    }

    /**
     * Test of getLastDrawnCard method, of class Player.
     */
    @Test
    public void testGetLastDrawnCard() {
        System.out.println("getLastDrawnCard");
        instance.addCard(Card.WILD);
        for(Card card : Card.values()){
            instance.addCard(card);
            Card result = instance.getLastDrawnCard();
            assertEquals(card, result);
        }
    }

    /**
     * Test of getMissionDescription method, of class Player.
     */
    @Test
    public void testGetMissionDescription() {
        System.out.println("getMissionDescription");
        String description = "Lorem ipsum";
        instance.setMission(new CountriesMission(description, 42));
        String expResult = description;
        String result = instance.getMissionDescription();
        assertEquals(expResult, result);
    }

    /**
     * Test of checkIfWinner method, of class Player.
     */
    @Test
    public void testCheckIfWinner() {
        System.out.println("checkIfWinner");
        List<Country> myCountries = new ArrayList<>();
        int N_COUNTRIES = 24;
        for(int i = 0; i<N_COUNTRIES; i++){
            myCountries.add(new Country(""));
        }
        Mission mission = new CountriesMission("Conquer "+N_COUNTRIES+" territories", 1);
        mission.buildTarget(null);
        instance.setMission(mission);
        boolean expResult = true;
        boolean result = instance.checkIfWinner(myCountries);
        assertEquals(expResult, result);
        
        myCountries.add(new Country(""));
        expResult = true;
        result = instance.checkIfWinner(myCountries);
        assertEquals(expResult, result);
        
        myCountries.remove(myCountries.size()-1);
        myCountries.remove(myCountries.size()-1);
        expResult = false;
        result = instance.checkIfWinner(myCountries);
        assertEquals(expResult, result);
    }

    /**
     * Test of setConqueredACountry method, of class Player.
     */
    @Test
    public void testSetConqueredACountry() {
        System.out.println("setConqueredACountry");
        boolean flag, expResult, result;
        for(int i = 0; i<20; i++){
            flag = new Random().nextBoolean();
            instance.setConqueredACountry(flag);
            expResult = flag;
            result = instance.hasConqueredACountry();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of hasConqueredACountry method, of class Player.
     */
    @Test
    public void testHasConqueredACountry() {
        System.out.println("hasConqueredACountry");
        boolean flag, expResult, result;
        for(int i = 0; i<20; i++){
            flag = new Random().nextBoolean();
            instance.setConqueredACountry(flag);
            expResult = flag;
            result = instance.hasConqueredACountry();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of toString method, of class Player.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        String expResult = "Foo";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

}
