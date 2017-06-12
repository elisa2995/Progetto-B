package risiko;

import gui.GUI;
import gui.StartGameGUI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        StartGameGUI start = new StartGameGUI();
        start.setVisible(true);
        /* GUI gui = new GUI();
        gui.setVisible(true);*/
    }

}
