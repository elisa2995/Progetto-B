package gui.mainGui.cards;

import javax.swing.SwingUtilities;
/**
 * Animation of the <code>cardPanel</code> that goes up and down. 
 */
public class PanelAnimation extends Thread {

    private CardPanel cardPanel;
    private int x, y, toY;

    /**
     * Creates a new PanelAnimation
     * @param cardPanel
     * @param toY 
     */
    public PanelAnimation(CardPanel cardPanel, int toY) {
        this.cardPanel = cardPanel;
        this.x = cardPanel.getX();
        this.y = cardPanel.getY();
        this.toY = toY;
    }

    /**
     * Moves the panel up or down
     */
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

    /**
     * Sets the new location of the panel.
     */
    private void redraw() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                cardPanel.setLocation(x, y);
            }
        });
    }

}
