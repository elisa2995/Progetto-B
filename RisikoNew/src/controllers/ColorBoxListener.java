/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JComboBox;

/**
 *
 * @author Elisa
 */
public class ColorBoxListener implements ActionListener {

    private final String[] defaultColors;
    private List<JComboBox> colorBoxes;
    private String[] savedColors;

    public ColorBoxListener(String[] colors) {
        this.defaultColors = colors;
        this.colorBoxes = new ArrayList<>();
        this.savedColors = colors.clone();
    }

    /**
     * Se un giocatore cambia il suo colore di default, quel colore viene
     * assegnato al giocatore che possedeva in precedenza il nuovo colore
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox comboBox = (JComboBox) e.getSource();
        updateColors(comboBox);
    }

    public void updateColors(JComboBox colorBox) {

        String newColor = (String) colorBox.getSelectedItem();
        int index = colorBoxes.indexOf(colorBox);
        String oldColor = savedColors[index]; // colore precedente

        if (!oldColor.equals(newColor)) {  // se ha cambiato colore
            savedColors[index] = newColor;     // assegno il colore nuovo  
            // controlliamo se qualcuno ha il colore selezionato, in tal caso scambiamo i colori
            for (JComboBox other : colorBoxes) {
                if (other != colorBox && ((String) other.getSelectedItem()).equals(newColor)) {
                    switchColors(other, oldColor);
                }
            }

        }
    }

    private void switchColors(JComboBox other, String oldColor) {
        other.setSelectedItem(oldColor);
        savedColors[colorBoxes.indexOf(other)] = oldColor;
    }

    public String getAvailableColor(int index) {

        List<String> usedColors = Arrays.asList(savedColors).subList(0, index);
        for (String color : defaultColors) {
            if (!usedColors.contains(color)) {
                return color;
            }
        }
        return null; // Non dovrebbe mai arrivarci    
    }

    /**
     * Ridà la lista dei colori ordinata rispetto alle scelte dei giocatori
     *
     * @param length
     * @return
     */
    public String[] getSavedColors(int length) {
        return Arrays.copyOfRange(savedColors, 0, length);
        //return savedColors.subList(0, length);
    }

    public void addColorBox(JComboBox colorBox) {
        colorBoxes.add(colorBox);
        //savedColors.add((String)colorBox.getSelectedItem());
    }

    public void addColorBox(JComboBox colorBox, int index) {
        colorBoxes.add(index, colorBox);
        //savedColors.add(index,(String)colorBox.getSelectedItem());
    }

    public void removeColorBox(JComboBox colorBox) {
        colorBoxes.remove(colorBox);
        //savedColors.remove((String)colorBox.getSelectedItem());
    }

    public void updateSavedColors() {

    }

}
