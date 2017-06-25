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
     * phase; otherwise it resets all.
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
     * If the country can be chosen, it reinforces it; otherwise it resets the
     * cache.
     *
     * @param country
     */
    private void tryReinforce(String country) {
        if (cache.canBeChosen(country)) {
            game.reinforce(country);
            PlayAudio.play("src/resources/sounds/clickOn.wav");
            return;
        }

        reset();
    }

    /**
     * Sets the attacker/defender (if the chosen country is valid).
     *
     * @param country
     */
    private void tryFight(String country) {
        // The player has to choose a country as attacker and is clicking on a valid one
        if (cache.canBeChosenAsAttacker(country)) {
            game.setAttackerCountry(country);
            PlayAudio.play("src/resources/sounds/clickOn.wav");
            return;
        }

        // The player has to choose a country as defender and is clicking on a valid one
        if (cache.canBeChosen(country)) {
            game.setDefenderCountry(country);
            PlayAudio.play("src/resources/sounds/clickOn.wav");
            gui.setAttackerDialogVisible(true);
            return;
        }
        // The country is not valid
        reset();
    }

    /**
     * Sets the country from/to which to move the armies (if it is valid).
     *
     * @param country
     */
    private void tryMove(String country) {

        // The player has to set the country from which to move its armies, and the country is a valid one.
        if (cache.canBeChosenAsFromCountry(country)) {

            game.setFromCountry(country);
            PlayAudio.play("src/resources/sounds/clickOn.wav");
            return;
        }

        // The player has to set the country to which to move its armies, and the country is a valid one.
        if (cache.canBeChosen(country)) {
            MoveDialog moveDialog = new MoveDialog(game, game.getFromCountryName(), country);
            moveDialog.setVisible(true);
            PlayAudio.play("src/resources/sounds/clickOn.wav");
            game.resetMoveCountries();
            return;
        }
        // The country is not valid.
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
     * If the country can be chosen in the current phase of the game, it sets
     * the hand cursor, otherwise sets the default cursor.
     *
     * @param e
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        if (!game.checkMyIdentity()) {
            return;
        }
        String country = getCountryFromClick(e);

        if (country == null) {
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
     * If the country can be reinforced, sets the hand cursor, otherwises sets
     * the default cursor. It then saves this info into the cache.
     *
     * @param country
     * @param component
     * @param label
     */
    private void checkReinforce(String country, Component component, JLabel label) {
        if (cache.controlReinforce(country)) {
            setHandCursor(component, label);
            cache.save(country, true);
            return;
        }
        setDefaultCursor(component, label);
        cache.save(country, false);

    }

    /**
     * If the country can be chosen as attacker/defender, sets the hand cursor,
     * otherwise sets the default cursor. It then saves this info into the
     * cache.
     *
     * @param country
     * @param component
     * @param label
     */
    private void checkFight(String country, Component component, JLabel label) {
        // The player has to choose a country as attacker and the cursor is on a valid one        
        if (cache.controlAttack(country)) {
            setHandCursor(component, label);
            cache.save(country, true);
            return;
        }

        // The player has to choose a country as defender and the cursor  is on a valid one
        if (cache.controlDefense(country)) {
            setHandCursor(component, label);
            cache.save(country, true);
            return;
        }
        // The country is not valid
        setDefaultCursor(component, label);
        cache.save(country, false);
    }

    /**
     * If the player can choose this country as country from/to which to move
     * its armies, sets the hand cursor, otherwise sets the default cursor.It
     * saves this info into the cache.
     *
     * @param country
     * @param component
     * @param label
     */
    private void checkMove(String country, Component component, JLabel label) {
        // The player has to choose a country from which start the movement and the cursor is on a selectable one
        if (cache.controlMoveFromCountry(country)) {
            setHandCursor(component, label);
            cache.save(country, true);
            return;
        }
        // The player has to choose a country in which the movement ends and the cursor is on a selectable one
        if (cache.controlMoveToCountry(country)) {
            setHandCursor(component, label);
            cache.save(country, true);
            return;
        }
        // The cursor is not on a selectable country
        setDefaultCursor(component, label);
        cache.save(country, false);

    }

    /**
     * Returns the name of the country that corresponds to the pixel pointed by
     * the cursor. In particular, it takes the coordinates of the pixel,
     * retrieves the <code>Color</code> of that pixel from the image and returns
     * the name of the country mapped to that color in <code>ColorNameCountry</code>.
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
     * Converts the icon in labelMap in a buffered image.
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
     * Sets the hand cursor.
     *
     * @param component
     * @param label
     */
    private void setHandCursor(Component component, JLabel label) {
        component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /**
     * Sets the default cursor.
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
            ((GraphicsJLabel) mapLabel).drawCone(gui.getAttackerCountry(), new Rectangle(e.getX(), e.getY(), 2, 2));
        }
    }

}
