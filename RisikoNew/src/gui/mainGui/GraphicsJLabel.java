package gui.mainGui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import java.util.Map;
import javax.swing.JLabel;

/**
 * JLabel on which colored cones are shown depending on the interaction of the
 * user with it.
 */
public class GraphicsJLabel extends JLabel {

    private double[] xx;
    private double[] yy;
    private static Paint GRADIENT_PAINT;
    private static final int DEFAULT_WIDTH = 15;
    private Path2D myPath;
    private Map<String, JLabel> countryLabel;

    /**
     * Creates a new GraphicsJLabel
     */
    public GraphicsJLabel() {
        super();
        myPath = new Path2D.Double();
        xx = new double[]{0, 0, 0};
        yy = new double[]{0, 0, 0};
    }

    /**
     * Sets countryLabel.
     *
     * @param countryLabel
     */
    public void setCountryLabel(Map<String, JLabel> countryLabel) {
        this.countryLabel = countryLabel;
    }

    /**
     * Paints the JLabel.
     *
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        myPath.moveTo(xx[0], yy[0]);
        myPath.lineTo(xx[0], yy[0]);
        myPath.lineTo(xx[1], yy[1]);
        myPath.lineTo(xx[2], yy[2]);
        myPath.closePath();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(GRADIENT_PAINT);
        g2.fill(myPath);
        myPath.reset();
    }

    /**
     * Draws a black triangle-shaped shadow with the vertix on the attacker's
     * label and the base on the defender's label.
     *
     * @param attacker
     * @param defender
     */
    public void drawCone(String attacker, String defender) {
        draw(countryLabel.get(attacker).getBounds(), countryLabel.get(defender).getBounds());
    }

    /**
     * Draws a black triangle-shaped shadow with the vertix on the attacker's
     * label and the base at mousePosition.
     *
     * @param attacker
     * @param mousePosition
     */
    public void drawCone(String attacker, Rectangle mousePosition) {
        draw(countryLabel.get(attacker).getBounds(), mousePosition);
    }

    /**
     * Draws a black triangle-shaped shadow with the vertix in the middle of the
     * Rectangle <code> from</code> and the base centred in the middle of the
     * Rectangle <code>to</code>. The base width is determined by the parameter
     * <code>defaultWidth</code>. the base at <code>base</code>.
     *
     * @param vertix
     * @param base
     */
    private void draw(Rectangle from, Rectangle to) {
        GraphicsJLabel label = this;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                xx[0] = from.getX() + from.getWidth() / 2;
                yy[0] = from.getY() + from.getHeight() / 2;
                xx[1] = to.getX() + to.getWidth() / 2;
                yy[1] = to.getY() + to.getHeight() / 2;
                double alpha = atan(Math.abs((xx[1] - xx[0]) / (yy[1] - yy[0])));
                xx[2] = xx[1] + DEFAULT_WIDTH * cos(alpha);
                yy[2] = yy[1] + DEFAULT_WIDTH * sin(alpha);
                label.updateUI();
            }
        });

    }

    /**
     * Removes the shadow.
     */
    public void resetCone() {
        GraphicsJLabel label = this;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                xx = new double[]{0, 0, 0};
                yy = new double[]{0, 0, 0};
                label.updateUI();
            }
        });
    }

    public static void setGradientPaint(Color color1, Color color2) {
        GRADIENT_PAINT = new GradientPaint(0, 0, color1, 20, 20, color2);
    }
}