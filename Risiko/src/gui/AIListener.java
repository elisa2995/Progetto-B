/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

/**
 *
 * @author Elisa
 */
public class AIListener implements ActionListener {
    
    private JTextField[] players;
    private JCheckBox[] aiChecks;

    public AIListener(JTextField[] players, JCheckBox[] aiChecks) {
        this.players=players;
        this.aiChecks=aiChecks;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    
        JCheckBox checkBox=(JCheckBox)e.getSource();
        int index=Arrays.asList(aiChecks).indexOf(checkBox);
        if(checkBox.isSelected()){
            players[index].setVisible(false);
            players[index].setText(" ");
        }else{
            players[index].setVisible(true);
        }
    }
    
}
