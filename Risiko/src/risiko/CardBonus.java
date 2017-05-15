package risiko;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 * @author andrea
 */
public enum CardBonus implements Comparable<CardBonus>{
    FANTE ("images/FANTE.png"),
    KNIGHT("images/KNIGHT.png"),
    CANNON("images/CANNON.png"),
    JOLLY ("images/JOLLY.png");

    private final BufferedImage image;
    
    CardBonus(String path){
        image=loadImage(path);
    }
 
    private BufferedImage loadImage(String path){
        try {
            return ImageIO.read(new File(path));
        } catch (Exception ex) {
            return null;
        }
    }
    
    public static CardBonus giveRandomCard() {
        switch ((int) (Math.random() * 4)) {
            case (0):
                return FANTE;
            case (1):
                return KNIGHT;
            case (2):
                return CANNON;
            case (3):
                return JOLLY;        
        }
        return null;
    }

    public BufferedImage getImage() {
        return image;
    }  
}