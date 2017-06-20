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
 * Listener for the movement of the cards. Depending on the position of the
 * cursor it puts a card in front of the others; when a card is clicked it moves
 * it to a destination that depends on the previous position.
 */
public class CardListener extends MouseInputAdapter {

    private final String PLAY_CARDS = "PLAY_CARDS";
    private CardPanel cardPanel;
    private GameProxy game;
    private static final int CHOSEN_OFFSET = 670;
    private static final int LOW_Y = 20;
    private static final int HIGH_Y = 15;

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
     * When the cursor enters an image of a card, the image on which it is goes
     * to the front.
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
     * When the cursor exits an image that has been entered, the visualization
     * of the cards return to the original one.
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
     * When the image of a card is clicked it changes position depending on the
     * previous position. If the card was among the selectable ones and the
     * chosen cards are less than three, it goes to the chosen ones; if it was
     * among the chosen ones it comes back to the selectable ones.
     *
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
     * Returns the final position to the movement of a card.
     *
     * @param label
     * @return
     */
    private int getToX(JLabel label) {
        if ((boolean) label.getClientProperty("chosen")) {
            return 10 + 50 * cardPanel.getNrCards();
        }
        return CHOSEN_OFFSET + cardPanel.getNrChosenCards() * 50;

    }
}
