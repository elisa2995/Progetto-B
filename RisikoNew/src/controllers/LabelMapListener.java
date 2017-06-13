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
import risiko.players.ArtificialPlayer;

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

    public LabelMapListener(JLabel mapLabel, Map<Color, String> ColorNameCountry, GUI gui) {
        this.gui = gui;
        this.mapLabel = mapLabel;
        this.bufferedImage = convertToBufferedImage(mapLabel);
        this.ColorNameCountry = ColorNameCountry;
        this.cache = new HashMap<>();
    }

    public void setGame(GameProxy game) {
        this.game = game;
    }

    /**
     * Decide se scatenare o meno l'evento relativo alla corrente fase del
     * gioco, a seconda che il punto cliccato sia valido o meno.
     *
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        String country = getCountryFromClick(e);
        switch (game.getPhase()) {
            case PLAY_CARDS:
                PlayAudio.play("src/resources/sounds/clickOff.wav");
                break;
            case REINFORCE:
                if (country == null) {
                    PlayAudio.play("src/resources/sounds/clickOff.wav");
                    return;
                }

                if (canBeChosen(country)) {
                    //Ho ancora bonus armies e sono su un mio territorio
                    game.reinforce(country);
                    //reinforce chiama notify(), la gui si aggiorna
                    PlayAudio.play("src/resources/sounds/clickOn.wav");
                    break;
                }

                PlayAudio.play("src/resources/sounds/clickOff.wav");
                break;
            case FIGHT:
                if (country == null) {
                    PlayAudio.play("src/resources/sounds/clickOff.wav");
                    game.resetFightingCountries();
                    resetCache();
                    return;
                }

                if (game.getAttackerCountryName() == null && canBeChosen(country)) {
                    //Devo scegliere l'attaccante, sono su un mio territorio da cui posso attaccare
                    game.setAttackerCountry(country);
                    PlayAudio.play("src/resources/sounds/clickOn.wav");
                    break;
                }
                if (canBeChosen(country)) {
                    //Devo scegliere il difensore, sono su un territorio confinante attaccabile
                    game.setDefenderCountry(country);
                    PlayAudio.play("src/resources/sounds/clickOn.wav");
                    gui.setAttackerDialogVisible(true);
                    break;
                }
                //Sono su un territorio non valido per attaccare nè per difendere
                PlayAudio.play("src/resources/sounds/clickOff.wav");
                game.resetFightingCountries();
                resetCache();
                break;

            case MOVE:
                if (country == null) {
                    PlayAudio.play("src/resources/sounds/clickOff.wav");
                    game.resetFightingCountries();
                    resetCache();
                    return;
                }

                if (game.getAttackerCountryName() == null && (canBeChosen(country))) {

                    //Devo scegliere territorio da cui voglio iniziare lo spostamento, sono su un mio territorio da cui posso spostarmi
                    game.setFromCountry(country);
                    PlayAudio.play("src/resources/sounds/clickOn.wav");
                    break;
                }
                if (canBeChosen(country)) {
                    //Devo scegliere il terriotrio in cui spostarmi, sono su un territorio confinante in cui posso spostarmi
                    MoveDialog moveDialog = new MoveDialog(game, game.getAttackerCountryName(), country);
                    moveDialog.setVisible(true);
                    PlayAudio.play("src/resources/sounds/clickOn.wav");
                    game.resetFightingCountries();
                    break;
                }
                //Sono su un territorio non valido per spostarmi
                PlayAudio.play("src/resources/sounds/clickOff.wav");
                game.resetFightingCountries();
                resetCache();
                break;

        }
    }

    /**
     * Check in the cache wheter the <code>country</code> can be chosen in the
     * current phase.
     */
    private boolean canBeChosen(String country) {
        return cache.containsKey(country) && cache.get(country);
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
        if (game.getActivePlayer() instanceof ArtificialPlayer) //Affinchè la posizione del mouse non interferisca sui coni di luce dei giocatori artificiali
        {
            return;
        }
        String country = getCountryFromClick(e);
        /*Cache reset: a tutti i cambiamenti di fase, dopo ogni attacco se c'è 
         stata una conquista oppure è rimasta solo un armata, quando setto l'attaccante,
         (quando setto il difensore: no perchè posso sceglierne un altro quindi rimango
         con le stesse possibilità)
         quando resetto le fighting countries in labelMaplistener(cioè qui)
         */
        if (country == null) {
            drowCone(e);
            // Non sono su alcun territorio
            e.getComponent().setCursor(Cursor.getDefaultCursor());
            return;
        }
        JLabel label = gui.getLabelByCountry(country);
        mapLabel.setToolTipText(country);

        switch (game.getPhase()) {
            case PLAY_CARDS:
                return;
            case REINFORCE:
                if (canBeChosen(country) || game.controlPlayer(country) && game.canReinforce()) {
                    //Ho ancora bonus armies e sono su un mio territorio
                    setHandCursor(e.getComponent(), label);
                    cache.put(country, true);

                } else {
                    //Non ho più bonusArmies oppure non sono sul mio territorio
                    setDefaultCursor(e.getComponent(), label);
                    cache.put(country, false);
                }
                break;
            case FIGHT:
                drowCone(e);
                if (canBeChosen(country) || game.getAttackerCountryName() == null && game.controlAttacker(country)) {
                    //Devo scegliere l'attaccante, sono su un mio territorio da cui posso attaccare
                    setHandCursor(e.getComponent(), label);
                    cache.put(country, true);
                    break;
                }
                if (canBeChosen(country) || game.getAttackerCountryName() != null && game.controlDefender(country)) {
                    //Devo scegliere il difensore, sono su un territorio confinante attaccabile
                    setHandCursor(e.getComponent(), label);
                    cache.put(country, true);
                    break;
                }
                //Sono su un territorio non valido per attaccare nè per difendere
                setDefaultCursor(e.getComponent(), label);
                cache.put(country, false);
                break;
            case MOVE:
                drowCone(e);
                if (canBeChosen(country) || game.getAttackerCountryName() == null && game.controlFromCountryPlayer(country)) {
                    //Devo scegliere territorio da cui voglio iniziare lo spostamento, sono su un mio territorio da cui posso spostarmi
                    setHandCursor(e.getComponent(), label);
                    cache.put(country, true);
                    break;
                }
                if (canBeChosen(country)|| game.getAttackerCountryName() != null && game.controlMovement(country)) {
                    //Devo scegliere il terriotrio in cui spostarmi, sono su un territorio confinante in cui posso spostarmi
                    setHandCursor(e.getComponent(), label);
                    cache.put(country, true);
                    break;
                }
                //Sono su un territorio non valido per spostarmi
                setDefaultCursor(e.getComponent(), label);
                cache.put(country, false);
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

    public void drowCone(MouseEvent e) {
        if (game.getAttackerCountryName() != null) {
            //imposto il cono di luce dall'attackerCountry alla posizione del Mouse
            ((GraphicsJLabel) mapLabel).drawCone(gui.getAttackerCountry(), new Rectangle(e.getX(), e.getY(), 2, 2));
        }
    }

}
