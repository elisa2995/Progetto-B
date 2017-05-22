/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author Elisa
 */
public class ChangeTypeListener implements ActionListener {

    private JTextField[] playerTexts;
    private JButton[] logins;
    private JButton[] changePlayers;
    private JLabel[] playerTypes;

    public ChangeTypeListener(JTextField[] playerTexts, JButton[] logins, JButton[] changeTypes, JLabel[] playerTypes) {
        this.playerTexts = playerTexts;
        this.logins = logins;
        this.changePlayers = changeTypes;
        this.playerTypes = playerTypes;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();
        int index = Arrays.asList(changePlayers).indexOf(source);
        String type = playerTypes[index].getText().toLowerCase();

        switch (type) {
            case "normale":
                playerTypes[index].setText("Artificiale");
                playerTypes[index].setVisible(true);
                playerTexts[index].setEditable(false);
                playerTexts[index].setText("GiocatoreIA" + getIndex());
                logins[index].setVisible(false);
                break;
            case "artificiale":
                playerTypes[index].setText("Loggato");
                playerTypes[index].setVisible(true);
                playerTexts[index].setEditable(false);
                playerTexts[index].setText("");
                logins[index].setVisible(true);
                break;
            case "loggato":
                playerTypes[index].setText("Normale");
                playerTypes[index].setVisible(true);
                playerTexts[index].setEditable(true);
                playerTexts[index].setText("");
                logins[index].setVisible(false);
                break;

        }
    }

    private int getIndex() {
        List<String> names = new ArrayList<>();
        for (JLabel playerType : playerTypes) {
            names.add(playerType.getText());
        }
        return Collections.frequency(names, "Artificiale");
    }

}
