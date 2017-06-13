package gui;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JComponent;

/**
 * A Component that can be set as ContentPane of any other JComponent. The image
 * <code>image</code> passed in the constructor serves as background of the
 * panel.
 */
public class BackgroundPane extends JComponent {

    private final Image image;

    public BackgroundPane(Image image) {
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }

}
