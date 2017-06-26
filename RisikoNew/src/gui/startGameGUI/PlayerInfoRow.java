package gui.startGameGUI;

import controllers.ColorBoxListener;
import controllers.RemovePlayerListener;
import controllers.SelectTypeListener;
import gui.DefaultColor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 * JComponent that contains all the information about a player. It contains:
 * <code>nameField</code>(JTextField) to insert the name of the player,
 * <code>typeComboBox</code>(JComboBox) to determine the type of the player,
 * <code>colorComboBox</code>(JComboBox) to choose the color of the troops,
 * <code>removeButton</code>(JButton) to remove the player from the
 * <code>playersPanel</code>.
 */
public class PlayerInfoRow extends JComponent {

    private final static int[] X = {10, 120, 250, 360};
    private final static int ROW_WIDTH = 35;

    private JTextField nameField;
    private JComboBox typeComboBox;
    private JComboBox colorComboBox;
    private JButton removeButton;
    private boolean removable;
    private int index;
    private boolean logged;
    private ColorBoxListener colorListener;

    /**
     * Creates a new PlayerInfo.
     *
     * @param index
     * @param types
     * @param colorListener
     * @param typeListener
     * @param removeListener
     */
    protected PlayerInfoRow(int index, String[] types, ColorBoxListener colorListener, SelectTypeListener typeListener, RemovePlayerListener removeListener) {
        this.nameField = new JTextField();
        this.typeComboBox = new JComboBox(new DefaultComboBoxModel(types));
        this.typeComboBox.addActionListener(typeListener);
        this.colorComboBox = new JComboBox();
        this.removeButton = new JButton("-");
        this.removeButton.addActionListener(removeListener);
        this.removable = false;
        this.logged = false;
        this.colorListener = colorListener;
        init(index);
    }

    /**
     * Initialization.
     *
     * @param index
     */
    private void init(int index) {
        setIndex(index);
        initColorBox();
        initLayout();
        initPlayerName();
    }

    /**
     * Initializes the field used to enter the name of the player.
     */
    private void initPlayerName() {
        nameField.setOpaque(false);
        nameField.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 150)));
    }

    /**
     * Sets the layout and adds the JComponents.
     */
    private void initLayout() {
        this.setLayout(new GridLayout(1, 4));
        this.add(nameField);
        this.add(typeComboBox);
        this.add(colorComboBox);
        this.add(removeButton);
    }

    /**
     * Initializes <code>colorComboBox</code> inserting the icons that
     * display the colors and setting the selectedItem to a color that has not
     * been assigned yet.
     */
    private void initColorBox() {

        DefaultColor[] colors = DefaultColor.values();
        ImageIcon[] icons = new ImageIcon[colors.length];
        String[] colorNames = new String[colors.length];

        
        for (int i = 0; i < icons.length; i++) {
            colorNames[i] = colors[i].toStringLC();
            icons[i] = new ImageIcon("src/resources/images/" + colors[i].toStringLC() + ".png");
        }

        colorComboBox.setModel(new DefaultComboBoxModel(colorNames));
        colorComboBox.setSelectedItem(colorListener.getAvailableColor());
        
        ComboBoxRenderer renderer = new ComboBoxRenderer(icons, colorNames);
        renderer.setPreferredSize(new Dimension(icons[0].getIconWidth(), icons[0].getIconHeight()));
        colorComboBox.setRenderer(renderer);
        colorComboBox.addActionListener(colorListener);

    }

    /**
     * Gets the <code>nameField</code>.
     *
     * @return
     */
    public JTextField getNameField() {
        return nameField;
    }

    /**
     * Gets the player's name.
     *
     * @return
     */
    public String getPlayerName() {
        return nameField.getText();
    }

    /**
     * Sets the player's name.
     *
     * @param name
     */
    public void setPlayerName(String name) {
        nameField.setText(name);
    }

    /**
     * Sets the editability of <code>nameField</code>.
     *
     * @param editable
     */
    public void setPlayerNameEditable(boolean editable) {
        nameField.setEditable(editable);
    }

    /**
     * Resets <code>nameField</code>.
     */
    public void resetPlayerName() {
        nameField.setText("");
    }

    /**
     * Gets <code>typeComboBox</code>.
     *
     * @return
     */
    public JComboBox getTypeComboBox() {
        return typeComboBox;
    }

    /**
     * Gets the type of the player.
     *
     * @return
     */
    public String getType() {
        return (String) typeComboBox.getSelectedItem();
    }

    /**
     * Sets the type of the player.
     *
     * @param type
     */
    public void setType(String type) {
        typeComboBox.setSelectedItem(type);
    }

    /**
     * Gets <code>colorComboBox</code>.
     *
     * @return
     */
    public JComboBox getColorComboBox() {
        return colorComboBox;
    }

    /**
     * Gets the selected color in <code>colorComboBox</code>.
     *
     * @return
     */
    public String getColor() {
        return (String) colorComboBox.getSelectedItem();
    }

    /**
     * Sets the selected color in <code>colorComboBox</code>.
     *
     * @param color
     */
    public void setColor(String color) {
        colorComboBox.setSelectedItem(color);
    }

    /**
     * Gets the <code>removeButton</code>.
     *
     * @return
     */
    public JButton getRemoveButton() {
        return removeButton;
    }

    /**
     * Returns if the player is removable.
     *
     * @return
     */
    public boolean isRemovable() {
        return removable;
    }

    /**
     * Sets if the player is removable.
     *
     * @param removable
     */
    public void setRemovable(boolean removable) {
        this.removable = removable;
        this.removeButton.setVisible(removable);
    }

    /**
     * Sets the visibility of every JComponent of this row.
     *
     * @param visible
     */
    @Override
    public void setVisible(boolean visible) {
        nameField.setVisible(visible);
        colorComboBox.setVisible(visible);
        typeComboBox.setVisible(visible);
        removeButton.setVisible(visible && removable);
    }

    /**
     * Gets the index of the player.
     *
     * @return
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the index of the player and updates its location accordingly.
     *
     * @param index
     */
    public void setIndex(int index) {
        this.index = index;
        setLocation();
    }

    /**
     * Sets the location of all the JComponents according to the index of the player.
     */
    private void setLocation() {
        int y = getYPosition();
        this.nameField.setLocation(X[0], y);
        this.typeComboBox.setLocation(X[1], y);
        this.colorComboBox.setLocation(X[2], y);
        this.removeButton.setLocation(X[3], y);
    }

    /**
     * Retrieves the vertical component of the position depending on the index
     * of the player.
     *
     * @return
     */
    private int getYPosition() {
        return (index + 1) * ROW_WIDTH;

    }

    /**
     * Adds a <code>ColorBoxListener</code> to the <code>colorComboBox</code>.
     *
     * @param colorListener
     */
    public void addColorListener(ColorBoxListener colorListener) {
        colorComboBox.addActionListener(colorListener);
    }

    /**
     * Determines if the player is logged or not. 
     * @return 
     */
    public boolean isLogged() {
        return logged;
    }

    /**
     * Sets if the player is logged.
     * @param logged 
     */
    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    /**
     * It determines if the player is artificial
     * @return 
     */
    public boolean isArtificial() {
        return this.getType().equals("Artificiale");
    }

    /**
     * It adds <code>SelectTypeListener</code> to the <code>typeComboBox</code>.
     *
     * @param typeListener
     */
    protected void addSelectTypeListener(SelectTypeListener typeListener) {
        this.typeComboBox.addActionListener(typeListener);
    }

}
