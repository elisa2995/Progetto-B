/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

/**
 *
 * @author Elisa
 */
public class AIListener implements ActionListener {
    
    private JTextField[] players;
    private JCheckBox[] aiChecks;
    private JButton[] logins;

    public AIListener(JTextField[] players, JCheckBox[] aiChecks,JButton[] logins) {
        this.players=players;
        this.aiChecks=aiChecks;
        this.logins=logins;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    
        JCheckBox checkBox=(JCheckBox)e.getSource();
        int index=Arrays.asList(aiChecks).indexOf(checkBox);
        if(checkBox.isSelected()){            
            players[index].setText("GiocatoreArt"+(index+1));
            players[index].setEditable(false);
            logins[index].setText("Login");
            logins[index].setVisible(false);
        }else{
            logins[index].setVisible(true);   
            players[index].setEditable(true);
            players[index].setText("");
        }
    }
    
}
