package controllers;

import gui.mainGui.GUI;
import gui.mainGui.GraphicsJLabel;
import gui.mainGui.dialogs.MoveDialog;
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
import gui.PlayAudio;
import java.util.HashMap;
import java.awt.Rectangle;
import risiko.game.GameProxy;

/**
 * Listens to the movement and the click of the mouse and it processes it
 * dependending on the current phase.
 */
public class LabelMapListener extends MouseInputAdapter {

    private GameProxy game;
    private final BufferedImage bufferedImage;
    private final Map<Color, String> ColorNameCountry;
    private JLabel mapLabel;
    private GUI gui;
    private Cache cache;

    /**
     * Creates a new LabelMapListener
     *
     * @param mapLabel
     * @param ColorNameCountry
     * @param gui
     */
    public LabelMapListener(JLabel mapLabel, Map<Color, String> ColorNameCountry, GUI gui) {
        this.gui = gui;
        this.mapLabel = mapLabel;
        this.bufferedImage = convertToBufferedImage(mapLabel);
        this.ColorNameCountry = ColorNameCountry;
        this.cache = new Cache();
    }

    /**
     * Sets the <code>GameProxy</code>
     *
     * @param game
     */
    public void setGame(GameProxy game) {
        this.game = game;
        cache.setGame(game);
    }

    /**
     *
     * If the click is on a country it calls the actions related to the current
     * phase; if the click is not on a country it resets all.
     *
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        String country = getCountryFromClick(e);
        if (country == null) {
            reset();
            return;
        }
        switch (game.getPhase()) {
            case "PLAY_CARDS":
                PlayAudio.play("src/resources/sounds/clickOff.wav");
                break;
            case "REINFORCE":
                tryReinforce(country);
                break;
            case "FIGHT":
                tryFight(country);
                break;
            case "MOVE":
                tryMove(country);
                break;
        }
    }

    /**
     * If the country is valid to be chosen(so it has bonus armies left and the
     * country is one of the active player's), it reinforces it.
     *
     * @param country
     */
    private void tryReinforce(String country) {
        if (cache.canBeChosen(country)) {
            //Ho ancora bonus armies e sono su un mio territorio
            game.reinforce(country);
            //reinforce chiama notify(), la gui si aggiorna
            PlayAudio.play("src/resources/sounds/clickOn.wav");
            return;
        }

        reset();
    }

    /**
     * If the attacker/defender country has to be set, it controls if the
     * country is valid to be chosen; if it is it sets the country as the
     * attacker/defender.
     *
     * @param country
     */
    private void tryFight(String country) {
        if (cache.canBeChosenAsAttacker(country)) {
            //Devo scegliere l'attaccante, sono su un mio territorio da cui posso attaccare
            game.setAttackerCountry(country);
            PlayAudio.play("src/resources/sounds/clickOn.wav");
            return;
        }
        if (cache.canBeChosen(country)) {
            //Devo scegliere il difensore, sono su un territorio confinante attaccabile
            game.setDefenderCountry(country);
            PlayAudio.play("src/resources/sounds/clickOn.wav");
            gui.setAttackerDialogVisible(true);
            return;
        }
        //Sono su un territorio non valido per attaccare nè per difendere
        reset();
    }

    /**
     * If the country from/to which the movement has to take place has to be
     * set, it controls if the country is valid to be chosen; if it is it sets
     * the country as the fromCountry/toCountry.
     *
     * @param country
     */
    private void tryMove(String country) {

        if (cache.canBeChosenAsFromCountry(country)) {

            //Devo scegliere territorio da cui voglio iniziare lo spostamento, sono su un mio territorio da cui posso spostarmi
            game.setFromCountry(country);
            PlayAudio.play("src/resources/sounds/clickOn.wav");
            return;
        }
        if (cache.canBeChosen(country)) {
            //Devo scegliere il terriotrio in cui spostarmi, sono su un territorio confinante in cui posso spostarmi
            MoveDialog moveDialog = new MoveDialog(game, game.getFromCountryName(), country);
            moveDialog.setVisible(true);
            PlayAudio.play("src/resources/sounds/clickOn.wav");
            game.resetMoveCountries();
            return;
        }
        //Sono su un territorio non valido per spostarmi
        reset();
    }

    /**
     * Resets all the original settings
     */
    private void reset() {
        PlayAudio.play("src/resources/sounds/clickOff.wav");
        if (!game.getPhase().equals("REINFORCE")) {
            resetCache();
        }

        if (game.getPhase().equals("FIGHT")) {
            game.resetFightingCountries();
            return;
        }
        if (game.getPhase().equals("FIGHT")) {
            game.resetMoveCountries();
        }
    }

    /**
     * If the country on which the mouse is situated is valid on the current
     * phase of the game, it sets the hand cursor; if not it sets the default
     * cursor.
     *
     * @param e
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        if (!game.checkMyIdentity()) { //Affinchè la posizione del mouse non interferisca sui coni di luce dei giocatori artificiali
            return;
        }
        String country = getCountryFromClick(e);

        if (country == null) {
            // Non sono su alcun territorio
            e.getComponent().setCursor(Cursor.getDefaultCursor());
            return;
        }

        JLabel label = gui.getLabelByCountry(country);
        mapLabel.setToolTipText(country);

        switch (game.getPhase()) {
            case "PLAY_CARDS":
                setDefaultCursor(e.getComponent(), label);
                break;
            case "REINFORCE":
                if (cache.controlReinforce(country)) {
                    //Ho ancora bonus armies e sono su un mio territorio
                    setHandCursor(e.getComponent(), label);
                    cache.save(country, true);

                } else {
                    //Non ho più bonusArmies oppure non sono sul mio territorio
                    setDefaultCursor(e.getComponent(), label);
                    cache.save(country, false);
                }
                break;
            case "FIGHT":
                drawCone(e);
                if (cache.controlAttack(country)) {

                    //Devo scegliere l'attaccante, sono su un mio territorio da cui posso attaccare
                    setHandCursor(e.getComponent(), label);
                    cache.save(country, true);
                    break;
                }
                if (cache.controlDefense(country)) {

                    //Devo scegliere il difensore, sono su un territorio confinante attaccabile
                    setHandCursor(e.getComponent(), label);
                    cache.save(country, true);
                    break;
                }
                //Sono su un territorio non valido per attaccare nè per difendere
                setDefaultCursor(e.getComponent(), label);
                cache.save(country, false);
                break;
            case "MOVE":
                if (cache.controlMoveFromCountry(country)) {

                    //Devo scegliere territorio da cui voglio iniziare lo spostamento, sono su un mio territorio da cui posso spostarmi
                    setHandCursor(e.getComponent(), label);
                    cache.save(country, true);
                    break;
                }
                if (cache.controlMoveToCountry(country)) {

                    //Devo scegliere il terriotrio in cui spostarmi, sono su un territorio confinante in cui posso spostarmi
                    setHandCursor(e.getComponent(), label);
                    cache.save(country, true);
                    break;
                }
                //Sono su un territorio non valido per spostarmi
                setDefaultCursor(e.getComponent(), label);
                cache.save(country, false);
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
        cache.resetCache();
    }

    public void drawCone(MouseEvent e) {
        if (game.getAttackerCountryName() != null) {
            //imposto il cono di luce dall'attackerCountry alla posizione del Mouse
            ((GraphicsJLabel) mapLabel).drawCone(gui.getAttackerCountry(), new Rectangle(e.getX(), e.getY(), 2, 2));
        }
    }

}
