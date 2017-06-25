package controllers;

import gui.mainGui.cards.CardAnimation;
import gui.mainGui.cards.CardPanel;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.event.MouseInputAdapter;
import risiko.game.GameProxy;

/**
 * Listener for the movement of the cards. When the mouse hovers over a card,
 * the card is moved in front of the others; when the user clicks on a card, it
 * is moved to the set of chosen cards, or back to the set of the player's
 * cards.
 */
public class CardListener extends MouseInputAdapter {

    private final String PLAY_CARDS = "PLAY_CARDS";
    private CardPanel cardPanel;
    private GameProxy game;
    private static final int CHOSEN_OFFSET = 670;
    private static final int LOW_Y = 20, HIGH_Y = 15;
    private static final int X_OFFSET=10, HOR_OVERLAP=50;

    /**
     * Creates a new CardListener
     *
     * @param cardPanel
     * @param game
     */
    public CardListener(CardPanel cardPanel, GameProxy game) {
        this.cardPanel = cardPanel;
        this.game = game;
    }

    /**
     * When the cursor hovers over a card, the card is moved to the front.
     *
     * @param e
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        if (game.getPhase().equals(PLAY_CARDS)) {
            JLabel label = (JLabel) e.getComponent();
            cardPanel.getCardsPane().setLayer(label, 1);
            String card = (String) label.getClientProperty("name");
            label.setIcon(new ImageIcon("src/resources/images/" + card + "_BLACK.png"));
            Point p = label.getLocation();
            Point p1 = new Point(p.x, HIGH_Y);
            label.setLocation(p1);
            cardPanel.updateUI();
        }
    }

    /**
     * When the cursor exits a card, the card is moved back to its original
     * depth.
     *
     * @param e
     */
    @Override
    public void mouseExited(MouseEvent e) {
        if (game.getPhase().equals(PLAY_CARDS)) {
            JLabel label = (JLabel) e.getComponent();
            cardPanel.getCardsPane().setLayer(label, cardPanel.getLabelLayer(label));
            String card = (String) label.getClientProperty("name");
            label.setIcon(new ImageIcon("src/resources/images/" + card + ".png"));
            Point p = label.getLocation();
            Point p1 = new Point(p.x, LOW_Y);
            label.setLocation(p1);
            cardPanel.updateUI();
        }
    }

    /**
     * Toggles the position of a card between the two sets of card on the board
     * (not selected/selected). 
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (game.getPhase().equals(PLAY_CARDS)) {
            JLabel label = (JLabel) e.getComponent();
            if (!(boolean) label.getClientProperty("chosen")) {
                if (cardPanel.getNrChosenCards() < 3) {
                    String card = (String) label.getClientProperty("name");
                    label.setIcon(new ImageIcon("src/resources/images/" + card + "_GREEN.png"));
                    CardAnimation animation = new CardAnimation(cardPanel, label.getX(), HIGH_Y, getToX(label), label);
                    animation.start();
                    cardPanel.updateUI();
                }
            } else {
                String card = (String) label.getClientProperty("name");
                label.setIcon(new ImageIcon("src/resources/images/" + card + "_GREEN.png"));
                CardAnimation animation = new CardAnimation(cardPanel, label.getX(), HIGH_Y, getToX(label), label);
                animation.start();
                cardPanel.updateUI();
            }
        }
    }

    /**
     * Returns the final position for the movement of a card.
     *
     * @param label
     * @return
     */
    private int getToX(JLabel label) {
        if ((boolean) label.getClientProperty("chosen")) {
            return X_OFFSET + HOR_OVERLAP * cardPanel.getNrCards();
        }
        return CHOSEN_OFFSET + cardPanel.getNrChosenCards() * HOR_OVERLAP;

    }
}
