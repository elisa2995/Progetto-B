package controllers;

import gui.startGameGUI.PlayerInfoRow;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;

/**
 * Process the events of the JComboBoxes in which the user can choose the color
 * of the armies.
 */
public class ColorBoxListener implements ActionListener {

    private List<PlayerInfoRow> players;
    private String[] defaultColors;

    /**
     * Creates a new ColorBoxListener
     *
     * @param players
     * @param colors
     */
    public ColorBoxListener(List<PlayerInfoRow> players, String[] colors) {
        this.defaultColors = colors;
        this.players = players;
    }

    /**
     * It updates the colors of the JComboBoxes
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox comboBox = (JComboBox) e.getSource();
        updateColors(comboBox);
    }

    /**
     * If the color in the <code>JComboBox</code> was already selected in
     * another <code>JComboBox</code> it sets a new color in it.
     *
     * @param colorBox
     */
    public void updateColors(JComboBox colorBox) {

        List<JComboBox> colorBoxes = getColorBoxes();
        String newColor = (String) colorBox.getSelectedItem();
        for (JComboBox other : colorBoxes) {
            if (other != colorBox && ((String) other.getSelectedItem()).equals(newColor)) {
                other.setSelectedItem(getAvailableColor());
            }
        }
    }

    /**
     * Return the list of the JComboBoxes of the visible player rows
     *
     * @return
     */
    private List<JComboBox> getColorBoxes() {
        List<JComboBox> colorBoxes = new ArrayList<>();
        for (PlayerInfoRow player : players) {
            colorBoxes.add(player.getColorComboBox());
        }
        return colorBoxes;
    }

    /**
     * Returns a color that has not already been choosen in one of the visible
     * JComboBoxes.
     *
     * @return
     */
    public String getAvailableColor() {

        for (String defaultColor : defaultColors) {
            if (!getChosenColors().contains(defaultColor)) {
                return defaultColor;
            }
        }

        return null;

    }

    /**
     * Returns the list of the colors chosen in the visibile JComboBoxes.
     *
     * @return
     */
    public List<String> getChosenColors() {
        List<String> chosen = new ArrayList<>();
        for (PlayerInfoRow player : players) {
            chosen.add(player.getColor());
        }
        return chosen;
    }

}
