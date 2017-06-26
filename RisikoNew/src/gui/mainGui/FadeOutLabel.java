package gui.mainGui;

import gui.mainGui.GUI;
import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * Fading out JLabel. This class can be used to show images that fade out in
 * RUNNING_TIME ms, thanks to a timer that updates its transparency.
 */
public class FadeOutLabel extends JLabel {

    public static final long RUNNING_TIME = 3000;

    private BufferedImage outImage;
    private float alpha = 0f;
    private long startTime = -1;
    private final Timer timer;
    private final GUI gui;

    /**
     * Creates a new FadeOutLabel
     *
     * @param gui
     */
    public FadeOutLabel(GUI gui) {
        this.gui = gui;
        String url = "src/resources/images/REINFORCE.png";
        try {
            outImage = ImageIO.read(new File(url));
        } catch (IOException exp) {
            System.out.println(url + " not found");
        }
        this.timer = new Timer(40, new ActionListener() {
            @Override
            public synchronized void actionPerformed(ActionEvent e) {
                if (startTime < 0) {
                    startTime = System.currentTimeMillis();

                } else {

                    long time = System.currentTimeMillis();
                    long duration = time - startTime;

                    if (duration >= RUNNING_TIME) {
                        startTime = -1;
                        ((Timer) e.getSource()).stop();
                        alpha = 1f;
                        gui.moveToBack();
                    } else {
                        alpha = ((float) duration / (float) RUNNING_TIME);
                    }
                    repaint();
                }
            }
        });

    }

    /**
     * Returns the Dimension of the setted image.
     *
     * @return
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(outImage.getWidth(), outImage.getHeight());
    }

    /**
     * Paints the label with the current alpha coefficient.
     *
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setComposite(AlphaComposite.SrcOver.derive(1f - alpha));
        int x = (getWidth() - outImage.getWidth()) / 2;
        int y = (getHeight() - outImage.getHeight()) / 2;
        g2d.drawImage(outImage, x, y, this);
        g2d.dispose();
    }

    /**
     * Starts fading out of the FadeOutLabel.
     *
     * @param url
     */
    public void startFadeOut(String url) {
        try {
            outImage = ImageIO.read(new File(url));
            alpha = 1f;
            timer.start();
            this.setOpaque(false);
        } catch (IOException exp) {
            System.out.println(url + " not found");
        }
    }

}
