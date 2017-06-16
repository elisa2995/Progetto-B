package gui.mainGui.cards;

import gui.PlayAudio;
import javax.swing.SwingUtilities;

public class PanelAnimation extends Thread {

    private CardPanel cardPanel;
    private int x, y, toY;

    public PanelAnimation(CardPanel cardPanel, int toY) {
        this.cardPanel = cardPanel;
        this.x = cardPanel.getX();
        this.y = cardPanel.getY();
        this.toY = toY;
    }

    @Override
    public void run() {
       
        while (y != toY) {

            if (y < toY) {
                ++y;
            } else if (y > toY) {
                --y;
            }
            redraw();

            try {
                sleep(0, 1);
            } catch (InterruptedException ie) {
                /* ignore */ }
        }
    }

    private void redraw() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                cardPanel.setLocation(x, y);
            }
        });
    }

}
