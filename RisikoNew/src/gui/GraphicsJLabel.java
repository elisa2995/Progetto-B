package gui;

import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.JLabel;

public class GraphicsJLabel extends JLabel {

    private double[] xx;
    private double[] yy;
    private static final Color COLOR_1 = new Color(0,0, 0, 225); // nero opaco
    private static final Color COLOR_2 = new Color(0, 0, 0, 120); // nero semitrasparente
    private static final Paint GRADIENT_PAINT = new GradientPaint(0, 0, COLOR_1, 20, 20, COLOR_2);
    private Path2D myPath = new Path2D.Double();

    public GraphicsJLabel() {
        super();
        xx = new double[]{0, 0, 0, 5};
        yy = new double[]{0, 0, 0, 0};
    }

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
     * Disegna un triangolo di luce (???) che parte dall'attaccante al difensore.
     * @param boundsAttacker i limiti della JLabel dell'attaccante
     * @param boundsDefender i limiti della JLabel del difensore
     */
    public void drawCone(Rectangle boundsAttacker, Rectangle boundsDefender) {
        GraphicsJLabel label = this;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                int defaultWidth = 15;
                xx[0] = boundsAttacker.getX() + boundsAttacker.getWidth() / 2;
                yy[0] = boundsAttacker.getY() + boundsAttacker.getHeight() / 2;
                xx[1] = boundsDefender.getX() + boundsDefender.getWidth() / 2;
                yy[1] = boundsDefender.getY() + boundsDefender.getHeight() / 2;
                double alpha = atan(Math.abs((xx[1] - xx[0]) / (yy[1] - yy[0])));
                xx[2] = xx[1] + defaultWidth * cos(alpha);
                yy[2] = yy[1] + defaultWidth * sin(alpha);
                //xx[2] = xx[1] + (xx[3] - xx[0]);
                //yy[2] = yy[1] + (yy[3] - yy[0]);
                label.updateUI();
            }
        });
    }
    
    /**
     * Cancella il cono.
     */
    public void resetCone() {
        GraphicsJLabel label = this;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               xx=new double[]{0,0,0};
               yy= new double[]{0,0,0};
               label.updateUI();
               
            }
        });
        
    }
}
