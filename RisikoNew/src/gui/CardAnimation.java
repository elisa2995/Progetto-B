/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 *
 * @author feded
 */
public class CardAnimation extends Thread {

    private CardPanel cardPanel;
    private boolean destination;
    private int x, y, toX;
    private JLabel card;

    public CardAnimation(CardPanel cardPanel, int x, int y, int toX, JLabel card) {
        this.cardPanel = cardPanel;
        this.x = x;
        this.y = y;
        this.toX = toX;
        this.card = card;
    }

    @Override
    public void run() {

        PlayAudio.play("sounds/moveCard.wav");

        cardPanel.switchCard(card);
        destination = false;
        while (!destination) {

            if (x < toX) {
                ++x;
            } else if (x > toX) {
                --x;
            } else {
                stopAnimation();
            }
            redraw();

            try {
                sleep(0, 100);
            } catch (InterruptedException ie) {
                /* ignore */ }
        }
        cardPanel.addCardsToPane();
        cardPanel.addChosenCardsToPane();

    }

    private void stopAnimation() {
        destination = true;
    }

    private void redraw() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                cardPanel.moveCard(x, y, card);
            }
        });
    }

}
