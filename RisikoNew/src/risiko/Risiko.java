package risiko;

import gui.GUI;
import exceptions.LastPhaseException;
import gui.startGameGUI.StartGameGUI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Elisa
 */
public class Risiko {

    /**
     * @param args the command line arguments creare frame che contiene la gui e
     * passare alla gui game
     */
    public static void main(String[] args) throws Exception {

        try {
            //UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
            // Nero, troppo nero. (L'unico con i jcombobox neri) - brutto rendering dadi/coni dadi

            //UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel"); 
            // (Nero, troppo nero. (L'unico con i jcombobox neri))^2.Un po' più di arancione in giro, scritte più bianche
            
            UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel"); 
            // Grigio scuro, bottoni bocciatissimi. (forse il mio preferito? Forse serve qualcosa che faccia contrasto? boh)
            
            //UIManager.setLookAndFeel("com.jtattoo.plaf.texture.TextureLookAndFeel");
            // Grigio, bello se non fosse per i jlist/bottoni (elegante)
            
            //UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
            // Non mi piacciono i jdialog/jlist

            //UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel"); 
            // Nero, brutti i bottoni e combobox
            
            //UIManager.setLookAndFeel("com.jtattoo.plaf.luna.LunaLookAndFeel"); 
            // Bleah
            
            //UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel"); 
            // Poverata:se vogliamo fingere di avere il mac
            
            //UIManager.setLookAndFeel("com.jtattoo.plaf.mint.MintLookAndFeel"); 
            // Brutta selezione bottoni
            
            //UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel"); 
            // Brutti bottoni 
            
            //UIManager.setLookAndFeel("com.jtattoo.plaf.bernstein.BernsteinLookAndFeel"); 
            // Giallo
            //UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel"); 
            // Blu
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        StartGameGUI start = new StartGameGUI();
        start.setVisible(true);
        /* GUI gui = new GUI();
        gui.setVisible(true);*/
    }

}
