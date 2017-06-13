package controllers;

import gui.startGameGUI.PlayerInfoRow;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JComboBox;

public class ColorBoxListener implements ActionListener {

    private List<PlayerInfoRow> players;
    private String[] defaultColors;

    public ColorBoxListener(List<PlayerInfoRow> players, String[] colors) {
        this.defaultColors = colors;
        this.players=players;
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
        
        List<JComboBox> colorBoxes=getColorBoxes();
        String newColor = (String) colorBox.getSelectedItem();
        int index = colorBoxes.indexOf(colorBox);
        /*String oldColor = savedColors[index]; // colore precedente
        if (!oldColor.equals(newColor)) {  // se ha cambiato colore
            savedColors[index] = newColor;     // assegno il colore nuovo  */
            // controlliamo se qualcuno ha il colore selezionato, in tal caso scambiamo i colori
            for (JComboBox other : colorBoxes) {
                if (other != colorBox && ((String) other.getSelectedItem()).equals(newColor)) {
                    //switchColors(other, oldColor);
                    other.setSelectedItem(getAvailableColor());
                }
            }

        //}
    }
    
    private List<JComboBox> getColorBoxes(){ 
        List<JComboBox> colorBoxes=new ArrayList<>();
        for(PlayerInfoRow player:players){
            colorBoxes.add(player.getColorComboBox());
        }
        return colorBoxes;
    }
    

    private void switchColors(JComboBox other, String oldColor) {
        other.setSelectedItem(oldColor);
        //savedColors[getColorBoxes().indexOf(other)] = oldColor;
        }

    public String getAvailableColor() {
        
        for(String defaultColor : defaultColors){
            if(!getChosenColors().contains(defaultColor)){
                return defaultColor;
            }
        }
        
        return null; //non dovrebbe mai arrivarci;
       
    }


    public List<String> getChosenColors(){
        List<String> chosen = new ArrayList<>();
        for(PlayerInfoRow player: players){
            chosen.add(player.getColor());
        }
        return chosen;
    }

}
