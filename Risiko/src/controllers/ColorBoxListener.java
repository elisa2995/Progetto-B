/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 *
 * @author Elisa
 */
public class ColorBoxListener implements ActionListener {

    private String[] updateColors;
    private JComboBox[] colorBoxes;

    public ColorBoxListener(JComboBox[] colorBoxes, String[] updateColors) {
        this.updateColors = updateColors;
        this.colorBoxes = colorBoxes;
    }

    /**
     * Se un giocatore cambia il suo colore di default, quel colore viene 
     * assegnato al giocatore che possedeva in precedenza il nuovo colore
     * @param e 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox comboBox = (JComboBox) e.getSource();
        String selected = (String) comboBox.getSelectedItem();
        
        int index = Arrays.asList(colorBoxes).indexOf(comboBox);
        if(!updateColors[index].equals(selected)){ 
            String colorTmp = updateColors[index]; 
            updateColors[index] = selected;       
            for (int j = 0; j < colorBoxes.length; j++) {
                    if (colorBoxes[j] != comboBox && colorBoxes[j].getSelectedItem().equals(selected)) {
                        colorBoxes[j].setSelectedItem(colorTmp);    
                        updateColors[j] = colorTmp;
                    }
                }
        }
    }

    /**
     * Ridà la lista dei colori ordinata rispetto alle scelte dei giocatori
     * @param length
     * @return 
     */
    public String[] getUpdateColors(int length) {
        return Arrays.copyOfRange(updateColors, 0, length);
    }

}
