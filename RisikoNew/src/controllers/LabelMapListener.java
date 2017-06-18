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
import java.awt.Rectangle;
import risiko.game.GameProxy;

/**
 * Listens to the movement and the click of the mouse and processes it
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
     * Resets to the original settings
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
        if (game.getPhase().equals("MOVE")) {
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
                checkReinforce(country, e.getComponent(), label);
                break;
            case "FIGHT":
                drawCone(e);
                checkFight(country, e.getComponent(), label);
                break;
            case "MOVE":
                checkMove(country, e.getComponent(), label);
                break;
        }
    }

    /**
     * Checks if the country can be reinforced; if this can be done it sets the
     * cursor to hand cursor. It saves the country and wheter it can bereinforcd
     * in the cache.
     *
     * @param country
     * @param component
     * @param label
     */
    private void checkReinforce(String country, Component component, JLabel label) {
        if (cache.controlReinforce(country)) {
            //Ho ancora bonus armies e sono su un mio territorio
            setHandCursor(component, label);
            cache.save(country, true);
            return;
        }
        //Non ho più bonusArmies oppure non sono sul mio territorio
        setDefaultCursor(component, label);
        cache.save(country, false);

    }

    /**
     * Checks if the country can be choosen as attacker/defender(depending on
     * the moment of the fight in which the game is); if this can be done it
     * sets the hand cursor. It saves the country and wheter it can bereinforcd
     * in the cache.
     *
     * @param country
     * @param component
     * @param label
     */
    private void checkFight(String country, Component component, JLabel label) {
        if (cache.controlAttack(country)) {

            //Devo scegliere l'attaccante, sono su un mio territorio da cui posso attaccare
            setHandCursor(component, label);
            cache.save(country, true);
            return;
        }
        if (cache.controlDefense(country)) {

            //Devo scegliere il difensore, sono su un territorio confinante attaccabile
            setHandCursor(component, label);
            cache.save(country, true);
            return;
        }
        //Sono su un territorio non valido per attaccare nè per difendere
        setDefaultCursor(component, label);
        cache.save(country, false);
    }

    /**
     * Checks if the country can be choosen as country from/to which the
     * movement take place(depending on the moment of the movement in which the
     * game is); if this can be done it sets the hand cursor. It saves the
     * country and wheter it can bereinforcd in the cache.
     *
     * @param country
     * @param component
     * @param label
     */
    private void checkMove(String country, Component component, JLabel label) {
        if (cache.controlMoveFromCountry(country)) {

            //Devo scegliere territorio da cui voglio iniziare lo spostamento, sono su un mio territorio da cui posso spostarmi
            setHandCursor(component, label);
            cache.save(country, true);
            return;
        }
        if (cache.controlMoveToCountry(country)) {

            //Devo scegliere il terriotrio in cui spostarmi, sono su un territorio confinante in cui posso spostarmi
            setHandCursor(component, label);
            cache.save(country, true);
            return;
        }
        //Sono su un territorio non valido per spostarmi
        setDefaultCursor(component, label);
        cache.save(country, false);

    }

    /**
     * Returns the name of the country that ows the clicked pixel. Returns null
     * if no country ows the pixel. In particular, it takes the coordinates of
     * the pixel, retrieves the <code>Color</code> of that pixel from the image
     * and returns the country name that corresponds to that color in
     * ColorNameCountry.
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
     * Converts the icon in labelMap in a buffered image
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
     * Sets the hand cursor
     *
     * @param component
     * @param label
     */
    private void setHandCursor(Component component, JLabel label) {
        component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /**
     * Sets the default cursor
     *
     * @param component
     * @param label
     */
    private void setDefaultCursor(Component component, JLabel label) {
        component.setCursor(Cursor.getDefaultCursor());
        label.setCursor(Cursor.getDefaultCursor());
    }

    /**
     * Resets the content of the cache.
     */
    public void resetCache() {
        cache.resetCache();
    }

    /**
     * Draws a cone that starts from the selected country and arrives to the
     * position of the cursor.
     *
     * @param e
     */
    public void drawCone(MouseEvent e) {
        if (game.getAttackerCountryName() != null) {
            //imposto il cono di luce dall'attackerCountry alla posizione del Mouse
            ((GraphicsJLabel) mapLabel).drawCone(gui.getAttackerCountry(), new Rectangle(e.getX(), e.getY(), 2, 2));
        }
    }

}
