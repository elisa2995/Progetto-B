package risiko.players;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import risiko.BonusDeck;
import risiko.Card;
import risiko.Mission;

public class PlayerTest {

    private Player player;

    public PlayerTest() {
        player = new Player("prova", "black");
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
     * Test of canPlayThisTris method, of class Player.
     */
    @Test
    public void testCanPlayThisTris() {
        System.out.println("canPlayThisTris");
        boolean expResult;
        boolean result;
        int nCards = Card.values().length;
        Card[][] tris = new Card[nCards][3];
        int k = 4;
        for (int i = 0; i < nCards; i++) {
            for (int j = 0; j < k; j++) {
                player.addCard(Card.values()[i]);
                // Metto 4 fanti, 3 cavalli, 2 cannoni e 1 jolly 
                //System.out.println(player.getLastDrawnCard());
            }
            k--;
        }

        // Tris di carte uguali, può giocare solo quelli di fanti e cavalli
        for (int i = 0; i < nCards; i++) {
            for (int j = 0; j < 3; j++) {
                tris[i][j] = Card.values()[i];
            }
            expResult = i < 2;
            result = player.canPlayThisTris(tris[i]);
            assertEquals(expResult, result);
        }
        
        player.playTris(new Card[]{Card.ARTILLERY, Card.CAVALRY, Card.INFANTRY}, 1);
        // Ora ha 3 fanti, 2 cavalli, 1 cannone e un jolly, può giocare solo il tris di fanti
        for (int i = 0; i < nCards; i++) {
            expResult = i < 1;
            result = player.canPlayThisTris(tris[i]);
            assertEquals(expResult, result);
        }

        // Tris misti, può giocare 2fanti+jolly, 2 cavalli + jolly
        for (int i = 0; i < nCards - 1; i++) {
            for (int j = 0; j < 3; j++) {
                tris[i][j] = (j == 2) ? Card.WILD : Card.values()[i];
            }
            expResult = i < 2;
            result = player.canPlayThisTris(tris[i]);
            assertEquals(expResult, result);
        }

        player.playTris(new Card[]{Card.INFANTRY, Card.INFANTRY, Card.WILD}, 1);
        // Ora ha 1 fante, 2 cavalli, 1 cannone e  0 jolly, non può giocare nessun tris misto con il jolly
        for (int i = 0; i < nCards - 1; i++) {
            expResult = false;
            result = player.canPlayThisTris(tris[i]);
            assertEquals(expResult, result);
        }
        
        Card[] tris2 = new Card[]{Card.INFANTRY, Card.CAVALRY, Card.ARTILLERY};
        expResult= true;
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
        
        for (int i = 0; i < nCards; i++) {
            for (int j = 0; j < k; j++) {
                player.addCard(Card.values()[i]);
                // Metto 3 fanti, 2 cavalli, 1 cannone e 0 jolly 
                //System.out.println(player.getLastDrawnCard());
            }
            k--;
        }
        System.out.println("Carte giocatore :");
        for(Card card: player.getBonusCards()){
            System.out.println(card.name()+" ");
        }
        System.out.println("Tris giocabili");
        /*
           Può giocare: tris uguali : 1 (3 fanti)
                        tris con jolly : 0
                        tris scala : 1
        */
        
        int expResult = 2;
        Map<Card[], Integer> tris = player.getPlayableTris((new BonusDeck()).getTris());
        for(Card[] cards : tris.keySet()){
            System.out.println(cards[0].name()+" "+cards[1].name()+" "+cards[2].name());
        }
        int result = tris.size();
        assertEquals(expResult, result);
        // ---------------------- Aggiungo il jolly-----------------
        player.addCard(Card.WILD);
        System.out.println("Carte giocatore :");
        for(Card card: player.getBonusCards()){
            System.out.println(card.name()+" ");
        }
        System.out.println("Tris giocabili");
        tris = player.getPlayableTris((new BonusDeck()).getTris());
        for(Card[] cards : tris.keySet()){
            System.out.println(cards[0].name()+" "+cards[1].name()+" "+cards[2].name());
        }
        /*
           Può giocare: tris uguali : 1 (3 fanti)
                        tris con jolly : 2 (2 fanti + jolly, 2 cavalli + jolly)
                        tris scala : 1
        */
        
        expResult = 4;
        result = tris.size();
        assertEquals(expResult, result);
    }

    
}
