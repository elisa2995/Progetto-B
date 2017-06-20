package gui.startGameGUI;

import controllers.ColorBoxListener;
import controllers.RemovePlayerListener;
import controllers.SelectTypeListener;
import exceptions.TranslationException;
import gui.DefaultColor;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JPanel;
import services.Translator;
import shared.PlayerInfo;

/**
 * Panel for the insertion of the players. It's a table with a row for each
 * player and and a column for each JComponent that sets a property to the
 * player(name, type, color of troops)
 *
 */
public class PlayersPanel extends JPanel {

    public static final int N_PLAYERS_MIN = 2;
    public static final int N_PLAYERS_MAX = 6;
    private List<PlayerInfoRow> players;
    private JComponent header;
    private final static String[] TYPES = {"Normale", "Artificiale", "Loggato"};
    private ColorBoxListener colorListener;
    private SelectTypeListener typeListener;
    private RemovePlayerListener removeListener;
    private final StartGameGUI gui;
    private final String LANG = "ITA";

    /**
     * It creates a new PlayersPanel and initializes it
     *
     * @param gui
     */
    public PlayersPanel(StartGameGUI gui) {
        players = new ArrayList<>();
        this.gui = gui;
        init();
    }

    /**
     * Initialization
     */
    private void init() {
        this.setLayout(new GridLayout(7, 1));
        initListeners();
        initHeader();
        initPlayerRows();
    }

    /**
     * It sets the header of the table
     */
    private void initHeader() {
        header = new PlayerHeader();
        addToPanel(header);
    }

    /**
     * It adds to the table as many rows as the minimun number of players
     */
    private void initPlayerRows() {
        for (int i = 0; i < N_PLAYERS_MIN; i++) {
            createPlayerRow(i);
        }
    }

    /**
     * It initializates the listeners
     */
    private void initListeners() {
        DefaultColor[] colors = DefaultColor.values();
        String[] colorNames = new String[colors.length];
        for (int i = 0; i < colors.length; i++) {
            colorNames[i] = colors[i].toStringLC();
        }
        colorListener = new ColorBoxListener(players, colorNames);
        typeListener = new SelectTypeListener(gui, players);
        removeListener = new RemovePlayerListener(this, players);
    }

    /**
     * It adds a JComponent to the panel
     *
     * @param component
     */
    private void addToPanel(JComponent component) {
        this.add(component);
        component.setVisible(true);
    }

    /**
     * It adds a row in the table
     */
    public void addPlayerRow() {
        if (players.size() >= N_PLAYERS_MAX) {
            return;
        }
        createPlayerRow(players.size());
        setRemovable(true);
        gui.setAddButtonVisible(players.size() < N_PLAYERS_MAX);

    }

    /**
     * It creates a new row and it adds it to the panel
     *
     * @param index
     */
    private void createPlayerRow(int index) {
        PlayerInfoRow player = new PlayerInfoRow(index, TYPES, colorListener, typeListener, removeListener);
        players.add(player);
        addToPanel((JComponent) player);

    }

    /**
     * It sets the visibility of the remove button of every row
     *
     * @param removable
     */
    public void setRemovable(boolean removable) {
        for (PlayerInfoRow p : players) {
            p.setRemovable(removable);
        }
    }

    /**
     * It removes a row from the tables
     *
     * @param player
     */
    public void removePlayer(PlayerInfoRow player) {
        this.remove(player);
        repaint();
        updateGUI();
    }

    /**
     * It updates the GUI after the addiction of a new row; it sets the
     * visibility of the <code>removeButton</code> of each row and of the
     * <code>addButton</code> of StartGameGUI; it also updates the indexes of
     * the rows
     */
    private void updateGUI() {
        setRemovable(players.size() > N_PLAYERS_MIN);
        gui.setAddButtonVisible(players.size() < N_PLAYERS_MAX);
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setIndex(i);
        }
        revalidate();
    }

    /**
     * It retrives all the inserted names
     *
     * @return
     */
    public List<String> getPlayerNames() {
        List<String> names = new ArrayList<>();
        for (PlayerInfoRow player : players) {
            names.add(player.getPlayerName());
        }
        return names;
    }

    /**
     * It gets all the information about the players
     *
     * @return
     */
    public List<PlayerInfo> getAllplayers() {
        List<PlayerInfo> list = new ArrayList<>();
        for (PlayerInfoRow player : players) {
            list.add(new PlayerInfo(player.getPlayerName(), player.getColor(), getFormattedType(player.getType().toLowerCase())));
        }
        return list;
    }

    /**
     * It formatts the type of the player.
     *
     * @param type
     * @return
     */
    private String getFormattedType(String type) {
        try {
            return Translator.translate(type, LANG, true);
        } catch (TranslationException ex) {
            return "";
        }
    }
}
