package controllers;

import gui.GUI;
import gui.GraphicsJLabel;
import gui.MoveDialog;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.event.MouseInputAdapter;
import risiko.game.Game;
import gui.PlayAudio;
import java.util.HashMap;
import java.awt.Rectangle;
import risiko.game.GameProxy;

/**
 *
 * @author andrea
 */
public class LabelMapListener extends MouseInputAdapter {

    private GameProxy game;
    private final BufferedImage bufferedImage;
    private final Map<Color, String> ColorNameCountry;
    private JLabel mapLabel;
    private GUI gui;
    private Map<String, Boolean> cache;

    public LabelMapListener(JLabel mapLabel, Map<Color, String> ColorNameCountry, GameProxy game, GUI gui) {
        this.game = game;
        this.gui = gui;
        this.mapLabel = mapLabel;
        this.bufferedImage = convertToBufferedImage(mapLabel);
        this.ColorNameCountry = ColorNameCountry;
        this.cache = new HashMap<>();
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
        switch (game.getPhase()) {
            case REINFORCE:
                if (countryName == null) {
                    PlayAudio.play("sounds/clickOff.wav");
                    return;
                }

                if (cache.containsKey(countryName) && cache.get(countryName)) {
                    //Ho ancora bonus armies e sono su un mio territorio
                    game.reinforce(countryName);
                    //reinforce chiama notify(), la gui si aggiorna
                    PlayAudio.play("sounds/clickOn.wav");
                    break;
                }

                PlayAudio.play("sounds/clickOff.wav");
                break;
            case FIGHT:
                if (countryName == null) {
                    PlayAudio.play("sounds/clickOff.wav");
                    game.resetFightingCountries();
                    resetCache();
                    return;
                }

                if (game.getAttackerCountryName() == null && cache.containsKey(countryName) && cache.get(countryName)) {
                    //Devo scegliere l'attaccante, sono su un mio territorio da cui posso attaccare
                    game.setAttackerCountry(countryName);
                    PlayAudio.play("sounds/clickOn.wav");
                    break;
                }
                if (cache.containsKey(countryName) && cache.get(countryName)) {
                    //Devo scegliere il difensore, sono su un territorio confinante attaccabile
                    game.setDefenderCountry(countryName);
                    PlayAudio.play("sounds/clickOn.wav");
                    gui.setVisibleAttackerDialog(true);
                    break;
                }
                //Sono su un territorio non valido per attaccare nè per difendere
                PlayAudio.play("sounds/clickOff.wav");
                game.resetFightingCountries();
                resetCache();
                break;

            case MOVE:
                if (countryName == null) {
                    PlayAudio.play("sounds/clickOff.wav");
                    game.resetFightingCountries();
                    resetCache();
                    return;
                }


                if (game.getAttackerCountryName() == null && (cache.containsKey(countryName) && cache.get(countryName))) {

                    //Devo scegliere territorio da cui voglio iniziare lo spostamento, sono su un mio territorio da cui posso spostarmi
                    game.setFromCountry(countryName);
                    PlayAudio.play("sounds/clickOn.wav");
                    break;
                }
                if (cache.containsKey(countryName) && cache.get(countryName)) {
                    //Devo scegliere il terriotrio in cui spostarmi, sono su un territorio confinante in cui posso spostarmi
                    MoveDialog moveDialog = new MoveDialog(game, countryName);
                    moveDialog.setVisible(true);
                    PlayAudio.play("sounds/clickOn.wav");
                    break;
                }
                //Sono su un territorio non valido per spostarmi
                PlayAudio.play("sounds/clickOff.wav");
                game.resetFightingCountries();
                resetCache();
                break;

        }
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
        /*Cache reset: a tutti i cambiamenti di fase, dopo ogni attacco se c'è 
         stata una conquista oppure è rimasta solo un armata, quando setto l'attaccante,
         (quando setto il difensore: no perchè posso sceglierne un altro quindi rimango
         con le stesse possibilità)
         quando resetto le fighting countries in labelMaplistener(cioè qui)
         */
        if (countryName == null) {
            // Non sono su alcun territorio
            e.getComponent().setCursor(Cursor.getDefaultCursor());
            return;
        }
        JLabel label = gui.getLabelByCountry(countryName);
        mapLabel.setToolTipText(countryName);

        switch (game.getPhase()) {

            case REINFORCE:
                if ((cache.containsKey(countryName) && cache.get(countryName)) || game.controlPlayer(countryName) && game.canReinforce()) {
                    //Ho ancora bonus armies e sono su un mio territorio
                    setHandCursor(e.getComponent(), label);
                    cache.put(countryName, true);

                } else {
                    //Non ho più bonusArmies oppure non sono sul mio territorio
                    setDefaultCursor(e.getComponent(), label);
                    cache.put(countryName, false);
                }
                break;
            case FIGHT:
                if ((cache.containsKey(countryName) && cache.get(countryName)) || game.getAttackerCountryName() == null && game.controlAttacker(countryName)) {
                    //Devo scegliere l'attaccante, sono su un mio territorio da cui posso attaccare
                    setHandCursor(e.getComponent(), label);
                    cache.put(countryName, true);
                    break;
                }
                if (game.getAttackerCountryName() != null) {
                    //imposto il cono di luce dall'attackerCountry alla posizione del Mouse
                    ((GraphicsJLabel) mapLabel).drawCone(gui.getAttackerCountryBounds(), new Rectangle(e.getX(), e.getY(), 2, 2));
                }
                if ((cache.containsKey(countryName) && cache.get(countryName)) || game.getAttackerCountryName() != null && game.controlDefender(countryName)) {
                    //Devo scegliere il difensore, sono su un territorio confinante attaccabile
                    setHandCursor(e.getComponent(), label);
                    cache.put(countryName, true);
                    break;
                }
                //Sono su un territorio non valido per attaccare nè per difendere
                setDefaultCursor(e.getComponent(), label);
                cache.put(countryName, false);
                break;
            case MOVE:
                if ((cache.containsKey(countryName) && cache.get(countryName)) || game.getAttackerCountryName() == null && game.controlFromCountryPlayer(countryName)) {
                    //Devo scegliere territorio da cui voglio iniziare lo spostamento, sono su un mio territorio da cui posso spostarmi
                    setHandCursor(e.getComponent(), label);
                    cache.put(countryName, true);
                    break;
                }
                if (game.getAttackerCountryName() != null) {
                    //imposto il cono di luce dall'attackerCountry alla posizione del Mouse
                    ((GraphicsJLabel) mapLabel).drawCone(gui.getAttackerCountryBounds(), new Rectangle(e.getX(), e.getY(), 2, 2));
                }
                if ((cache.containsKey(countryName) && cache.get(countryName)) || game.getAttackerCountryName() != null && game.controlMovement(countryName)) {
                    //Devo scegliere il terriotrio in cui spostarmi, sono su un territorio confinante in cui posso spostarmi
                    setHandCursor(e.getComponent(), label);
                    cache.put(countryName, true);
                    break;
                }
                //Sono su un territorio non valido per spostarmi
                setDefaultCursor(e.getComponent(), label);
                cache.put(countryName, false);
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

    /**
     * Setta il cursore a "manina".
     *
     * @param component
     * @param label
     */
    private void setHandCursor(Component component, JLabel label) {
        component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /**
     * Setta il cursore a freccia.
     *
     * @param component
     * @param label
     */
    private void setDefaultCursor(Component component, JLabel label) {
        component.setCursor(Cursor.getDefaultCursor());
        label.setCursor(Cursor.getDefaultCursor());
    }

    public void resetCache() {
        this.cache = new HashMap<>();
    }

}

