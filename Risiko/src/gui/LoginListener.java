/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextField;

/**
 *
 * @author Elisa
 */
public class LoginListener implements ActionListener {

    private JButton[] logins;
    private UserDialog regDialog;
    private StartGameGUI gui;
    private JTextField[] playerTexts;

    public LoginListener(JButton[] logins, UserDialog regDialog, StartGameGUI gui, JTextField[] playerTexts) {
        this.logins = logins;
        this.regDialog = regDialog;
        this.gui = gui;
        this.playerTexts = playerTexts;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();
        int index = Arrays.asList(logins).indexOf(source);
        if (logins[index].getText().equals("Login")) {
            regDialog.setRegistrationMode(false);
            regDialog.setIndex(index);
            regDialog.setPlayers(getPlayers());
            regDialog.setVisible(true);
            gui.setVisible(false);
        } else {
            logins[index].setText("Login");
            playerTexts[index].setEditable(false);
            playerTexts[index].setText("");
        }
    }

    /**
     * Ritorna la lista dei giocatori già loggati
     *
     * @return
     */
    private List getPlayers() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < playerTexts.length; i++) {
            if (playerTexts[i].getText().length() != 0 && logins[i].getText().equals("Logout")) {
                list.add(playerTexts[i].getText());
            }
        }
        return list;
    }

}
