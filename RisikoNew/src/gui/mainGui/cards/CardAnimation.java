package gui.mainGui.cards;

import gui.PlayAudio;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 * Thread in which a card is moved from x to toX.
 */
public class CardAnimation extends Thread {

    private CardPanel cardPanel;
    private boolean destination;
    private int x, y, toX;
    private JLabel card;

    /**
     * Creates a new CardAnimation
     *
     * @param cardPanel
     * @param x
     * @param y
     * @param toX
     * @param card
     */
    public CardAnimation(CardPanel cardPanel, int x, int y, int toX, JLabel card) {
        this.cardPanel = cardPanel;
        this.x = x;
        this.y = y;
        this.toX = toX;
        this.card = card;
    }

    /**
     * Changes the position of a card, moving it fluently from the set of
     * selectable cards to the chosen one or vice versa.
     */
    @Override
    public void run() {

        PlayAudio.play("src/resources/sounds/moveCard.wav");

        cardPanel.switchCard(card);
        while (x != toX) {

            if (x < toX) {
                ++x;
            } else if (x > toX) {
                --x;
            }
            redraw();

            try {
                sleep(0, 1);
            } catch (InterruptedException ie) {
                /* ignore */ }
        }
        cardPanel.addCardsToPane();
        cardPanel.addChosenCardsToPane();

    }

    /**
     * Redraws the JLabel.
     */
    private void redraw() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                cardPanel.moveCard(x, y, card);
            }
        });
    }

}
