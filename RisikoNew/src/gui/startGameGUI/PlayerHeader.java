package gui.startGameGUI;

import java.awt.GridLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * Header of the table contained in <code>playersPanel</code>.
 */
public class PlayerHeader extends JComponent {

    private final JLabel playerName;
    private final JLabel playerType;
    private final JLabel playerColor;

    /**
     * Creates a new PlayerHeader.
     */
    public PlayerHeader() {
        playerName = new JLabel("Giocatore");
        playerType = new JLabel("Tipo di giocatore");
        playerColor = new JLabel("Colore armate");        
        initLayout();
    }

    /**
     * Initialization of the layout and stting of the JComponents.
     */
    private void initLayout() {
        this.setLayout(new GridLayout(1, 4));
        this.add(playerName);
        this.add(playerType);
        this.add(playerColor);
        this.add(new JLabel());
    }

}
