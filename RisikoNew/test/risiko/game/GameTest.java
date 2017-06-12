/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package risiko.game;

import gui.GUI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import risiko.Card;
import risiko.Phase;
import risiko.players.ArtificialPlayer;
import risiko.players.ArtificialPlayerSettings;
import risiko.players.Player;

/**
 *
 * @author Elisa
 */
public class GameTest {
    
    public GameTest() {
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
     * Test of canPlayThisTris method, of class Game.
     */
    @Test
    public void testCanPlayThisTris_StringArr_ArtificialPlayerArr() throws Exception {
        System.out.println("canPlayThisTris");
        String[] cardNames = new String[3];
        cardNames[0]="WILD";
        cardNames[1]="WILD";
        cardNames[2]="INFANTRY";
        ArtificialPlayer[] aiCaller = null;
        Map<String, String> playersMap = new HashMap<>();
        Map<String, String> colorsMap = new HashMap<>();
        playersMap.put("p1", "NORMAL");
        playersMap.put("p2", "NORMAL");
        colorsMap.put("p1", "RED");
        colorsMap.put("p2", "GREEN");
        GUI gui = new GUI(playersMap, colorsMap);

        Game instance = new Game(playersMap, colorsMap, gui);
        boolean expResult = false;
        boolean result = instance.canPlayThisTris(cardNames, aiCaller);
        assertEquals(expResult, result);
    }
    
}
