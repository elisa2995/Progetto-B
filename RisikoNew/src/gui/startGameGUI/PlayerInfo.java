/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * <code>colorComboBox</code>(JComboBox) to decide the color of the troops,
 * <code>removeButton</code>(JButton) to remove the player from the
 * <code>playersPanel</code>
 */
public class PlayerInfo extends JComponent {

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
     * It creates a new PlayerInfo
     *
     * @param index
     * @param types
     * @param colorListener
     * @param typeListener
     * @param removeListener
     */
    protected PlayerInfo(int index, String[] types, ColorBoxListener colorListener, SelectTypeListener typeListener, RemovePlayerListener removeListener) {
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
     * Initialization
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
     * It initializes the field where you can insert the name of the player
     */
    private void initPlayerName() {
        nameField.setOpaque(false);
        nameField.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 150)));
    }

    /**
     * It sets the layout and add the JComponents
     */
    private void initLayout() {
        this.setLayout(new GridLayout(1, 4));
        this.add(nameField);
        this.add(typeComboBox);
        this.add(colorComboBox);
        this.add(removeButton);
    }

    /**
     * It initializes <code>colorComboBox</code> inserting the icons that
     * display the colors and setting the selectedItem to a color that has not
     * already been assigned
     */
    private void initColorBox() {

        DefaultColor[] colors = DefaultColor.values();
        ImageIcon[] icons = new ImageIcon[colors.length];
        String[] colorNames = new String[colors.length];

        // Creo un array di icons (una per colore)
        for (int i = 0; i < icons.length; i++) {
            colorNames[i] = colors[i].toStringLC();
            icons[i] = new ImageIcon("images/" + colors[i].toStringLC() + ".png");
        }

        // Setto i colorBoxes
        colorComboBox.setModel(new DefaultComboBoxModel(colorNames));
        //colorComboBox.setSelectedItem(colorListener.getAvailableColor(index));
        colorComboBox.setSelectedItem(colorNames[index]);
        ComboBoxRenderer renderer = new ComboBoxRenderer(icons, colorNames);
        renderer.setPreferredSize(new Dimension(icons[0].getIconWidth(), icons[0].getIconHeight()));
        colorComboBox.setRenderer(renderer);
        colorComboBox.addActionListener(colorListener);

    }

    /**
     * It gets the <code>nameField</code>
     *
     * @return
     */
    public JTextField getNameField() {
        return nameField;
    }

    /**
     * It gets the player's name
     *
     * @return
     */
    public String getPlayerName() {
        return nameField.getText();
    }

    /**
     * It sets the player's name
     *
     * @param name
     */
    public void setPlayerName(String name) {
        nameField.setText(name);
    }

    /**
     * It sets the editability of <code>nameField</code>
     *
     * @param editable
     */
    public void setPlayerNameEditable(boolean editable) {
        nameField.setEditable(editable);
    }

    /**
     * It resets <code>nameField</code>
     */
    public void resetPlayerName() {
        nameField.setText("");
    }

    /**
     * It gets <code>typeComboBox</code>
     *
     * @return
     */
    public JComboBox getTypeComboBox() {
        return typeComboBox;
    }

    /**
     * It gets the type of the player
     *
     * @return
     */
    public String getType() {
        return (String) typeComboBox.getSelectedItem();
    }

    /**
     * It sets the type of the player
     *
     * @param type
     */
    public void setType(String type) {
        typeComboBox.setSelectedItem(type);
    }

    /**
     * It gets <code>colorComboBox</code>
     *
     * @return
     */
    public JComboBox getColorComboBox() {
        return colorComboBox;
    }

    /**
     * It gets the selected color in <code>colorComboBox</code>
     *
     * @return
     */
    public String getColor() {
        return (String) colorComboBox.getSelectedItem();
    }

    /**
     * It sets the selected color in <code>colorComboBox</code>
     *
     * @param color
     */
    public void setColor(String color) {
        colorComboBox.setSelectedItem(color);
    }

    /**
     * It gets the <code>removeButton</code>
     *
     * @return
     */
    public JButton getRemoveButton() {
        return removeButton;
    }

    /**
     * It determins if the player is removable
     *
     * @return
     */
    public boolean isRemovable() {
        return removable;
    }

    /**
     * It sets if the player is removable
     *
     * @param removable
     */
    public void setRemovable(boolean removable) {
        this.removable = removable;
        this.removeButton.setVisible(removable);
    }

    /**
     * It sets the visibility of all the JComponents
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
     * It gets the index of the player
     *
     * @return
     */
    public int getIndex() {
        return index;
    }

    /**
     * It set the index of the player
     *
     * @param index
     */
    public void setIndex(int index) {
        this.index = index;
        setLocation();
    }

    /**
     * It sets the location of all the JComponents
     */
    private void setLocation() {
        int y = getYPosition();
        this.nameField.setLocation(X[0], y);
        this.typeComboBox.setLocation(X[1], y);
        this.colorComboBox.setLocation(X[2], y);
        this.removeButton.setLocation(X[3], y);
    }

    /**
     * It retrives the vertical component of the position depending on the indx
     * of the player
     *
     * @return
     */
    private int getYPosition() {
        return (index + 1) * ROW_WIDTH;

    }

    /**
     * It adds <code>ColorBoxListener</code> to the <code>colorComboBox</code>
     *
     * @param colorListener
     */
    public void addColorListener(ColorBoxListener colorListener) {
        colorComboBox.addActionListener(colorListener);
    }

    /**
     * It determines if the player is logged 
     * @return 
     */
    public boolean isLogged() {
        return logged;
    }

    /**
     * It sets if the player is logged
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
     * It adds <code>SelectTypeListener</code> to the <code>typeComboBox</code>
     *
     * @param typeListener
     */
    protected void addSelectTypeListener(SelectTypeListener typeListener) {
        this.typeComboBox.addActionListener(typeListener);
    }

}
