package gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.MouseInputAdapter;
import risiko.Game;
import utils.PlayAudio;

/**
 *
 * @author andrea
 */
public class LabelMapListener extends MouseInputAdapter {

    private Game game;
    private final BufferedImage bufferedImage;
    private final Map<Color, String> ColorNameCountry;
    private JLabel mapLabel;

    public LabelMapListener(JLabel mapLabel, Map<Color, String> ColorNameCountry, Game game) {
        this.game = game;
        this.mapLabel = mapLabel;
        this.bufferedImage = convertToBufferedImage(mapLabel);
        this.ColorNameCountry = ColorNameCountry;
    }

    /**
     * Decide se scatenare o meno l'evento relativo alla corrente fase del
     * gioco, a seconda che il punto cliccato sia valido o meno.
     *
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        String countryName = getCountryFromClick(e);
        if (countryName == null) {
            PlayAudio.play("sounds/clickOffShort.wav");
            game.resetFightingCountries();
            return;
        }
        PlayAudio.play("sounds/clickOnShort.wav");
        switch (game.getPhase()) {
            case REINFORCE:
                if (game.controlPlayer(countryName) && game.canReinforce(1)) {
                    //Ho ancora bonus armies e sono su un mio territorio
                    game.reinforce(countryName, 1);
                    //reinforce chiama notify(), la gui si aggiorna
                }
                break;
            case FIGHT:
                if (game.getAttackerCountryName() == null && game.controlAttacker(countryName)) {
                    //Devo scegliere l'attaccante, sono su un mio territorio da cui posso attaccare
                    game.setAttackerCountry(countryName);
                    break;
                }
                if (game.getAttackerCountryName() != null && game.controlDefender(countryName)) {
                    //Devo scegliere il difensore, sono su un territorio confinante attaccabile
                    game.setDefenderCountry(countryName);
                    break;
                }
                //Sono su un territorio non valido per attaccare nè per difendere
                game.resetFightingCountries();
                break;
        }
        // se ultimo reinforce metti nella textArea
    }

    /**
     * Setta il cursore a "manina" nel caso in cui il territorio su cui esso si
     * trova è valido nel contesto dell'attuale fase di gioco, a "freccina" se
     * non lo è.
     *
     * @param e
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        String countryName = getCountryFromClick(e);
        if (countryName == null) {
            // Non sono su alcun territorio
            e.getComponent().setCursor(Cursor.getDefaultCursor());
            return;
        }
        mapLabel.setToolTipText(countryName);
        switch (game.getPhase()) {
            case REINFORCE:
                if (game.controlPlayer(countryName) && game.canReinforce(1)) {
                    //Ho ancora bonus armies e sono su un mio territorio
                    e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    //Non ho più bonusArmies oppure non sono sul mio territorio
                    e.getComponent().setCursor(Cursor.getDefaultCursor());
                }
                break;
            case FIGHT:
                if (game.getAttackerCountryName() == null && game.controlAttacker(countryName)) {
                    //Devo scegliere l'attaccante, sono su un mio territorio da cui posso attaccare
                    e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    break;
                }
                if (game.getAttackerCountryName() != null && game.controlDefender(countryName)) {
                    //Devo scegliere il difensore, sono su un territorio confinante attaccabile
                    e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    break;
                }
                //Sono su un territorio non valido per attaccare nè per difendere
                e.getComponent().setCursor(Cursor.getDefaultCursor());
                break;
        }
    }

    /**
     * Ritirna il nome della Country a cui appartiene il pixel su cui si è
     * cliccato. (null se il pixel non appartiene a nessuna Country). In
     * particolare: Prende le coordinate del click, trova il Color di quel pixel
     * dall'immagine, e restituisce la stringa che corrisponde al nome della
     * Country corrispondente mappata in ColorNameCountry.
     *
     * @param e
     * @return
     */
    public String getCountryFromClick(MouseEvent e) {
        int x = e.getPoint().x;
        int y = e.getPoint().y;
        return ColorNameCountry.get(new Color(bufferedImage.getRGB(x, y)));
    }

    /**
     * Metodo necessario per convertire la labelMap in una bufferedMap a causa
     * della GUI che è stata creata con DESIGN
     *
     * @param labelMap
     * @return
     */
    private static BufferedImage convertToBufferedImage(JLabel labelMap) {
        ImageIcon imgIcon = ((ImageIcon) labelMap.getIcon());
        Image image = imgIcon.getImage();
        BufferedImage newImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }

}
