package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import risiko.game.GameProxy;

/**
 * @author andrea
 */
public class CardBonusDialog extends JDialog {

    private final GameProxy game;
    private final JPanel imagesPanel;
    private final JPanel buttonPanel;
    private String drawnCard;
    private final int WIDTH = 200;
    private final int HEIGHT = 320;

    public CardBonusDialog(GameProxy game) {
        this.game = game;
        this.imagesPanel = new JPanel();
        this.buttonPanel = new JPanel();

        initImagesPanel();
        initButtonPanel();
        setLayout(new BorderLayout());
        add(imagesPanel, BorderLayout.WEST);
        add(buttonPanel, BorderLayout.EAST);

        pack();
        setModalityType(DEFAULT_MODALITY_TYPE);
        //setVisible(true);
    }

    //Creo un JPanel che mostra le immagini di tutte le bonusCard del player, 
    //Con questo panel non si può interagire, è a solo fini illustrativi.
    public void initImagesPanel() {
        imagesPanel.removeAll();
        String path;
        int cards = game.getCardsNames().size();
        int rows = cards / 5 + 1;
        int cols = Math.min(cards, 5);
        this.setSize(WIDTH*5, HEIGHT*rows);
        imagesPanel.setLayout(new GridLayout(rows, 5));
        for (String card : game.getCardsNames()) {
            path = "images/" + card + ".png";
            imagesPanel.add(new JLabel(new ImageIcon(path)));
        }
    }

    //Creo un JPanel nel quale ci sarà un bottone per ogni tris che le cardBonus del player gli permetteranno di giocare 
    public void initButtonPanel() {
        buttonPanel.removeAll();
        buttonPanel.setLayout(new GridLayout(5, 1));
        buttonPanel.add(createButtonTris("Giocherò il TRIS la prossima volta", null, 0));

        Map<String[], Integer> playableTris = game.getPlayableTris();
        String[] c;
        String message;
        for (Map.Entry<String[], Integer> entry : playableTris.entrySet()) {
            c = entry.getKey();
            message = "Gioca : "+getFormattedString(c)+", ("+entry.getValue()+" armate)";
            buttonPanel.add(createButtonTris(message, c, entry.getValue()));
        }
    }

    private JButton createButtonTris(String text, String[] cards, int bonusArmiesTris) {
        JButton buttonTris = new JButton(text);
        CardBonusDialog dialog = this;
        buttonTris.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.playTris(cards, bonusArmiesTris);
                dialog.setVisible(false);
            }
        });

        /*buttonTris.addActionListener((ActionEvent e) -> {
            game.playTris(cards, bonusArmiesTris);
            this.setVisible(false);
        });*/
        return buttonTris;
    }

    /**
     *
     * @param c
     * @return
     */
    private String getFormattedString(String[] c) {
        return getFormattedName(c[0]) + "-" + getFormattedName(c[1]) + "-" + getFormattedName(c[2]);
    }

    /**
     * Ritorna i nomi in italiano delle carte.
     *
     * @param c
     * @return
     */
    private String getFormattedName(String c) {
        switch (c) {
            case "INFANTRY":
                return "Fante";
            case "CAVALRY":
                return "Cavaliere"; // o cavallo??
            case "ARTILLERY":
                return "Cannone";
            case "WILD":
                return "Jolly";
            default:
                System.out.println("Errore in getFormattedName"); // temporaneo
                return "";
        }
    }

    public void setDrawnCard(String card) {
        this.drawnCard = card;
    }
}
