/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import exceptions.LogoutException;
import gui.startGameGUI.PlayerInfo;
import gui.startGameGUI.PlayersPanel;
import gui.startGameGUI.StartGameGUI;
import gui.startGameGUI.UserDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 *
 * @author Elisa
 */
public class SelectTypeListener implements ActionListener {

    private List<PlayerInfo> players;
    private StartGameGUI gui;

    public SelectTypeListener(StartGameGUI gui,List<PlayerInfo> players) {
        this.players = players;
        this.gui = gui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JComboBox source = (JComboBox) e.getSource();
        PlayerInfo player = getSelectedRow(source);
        String type = ((String) source.getSelectedItem()).toLowerCase();
        // Se il player era loggato, lo slogghiamo
        if (player.isLogged()) {
            try {
                askLogout(player);
            } catch (LogoutException ex) {
                return;
            }
        }

        switch (type) {
            case "normale":
                player.resetPlayerName();
                player.setPlayerNameEditable(true);
                break;
            case "artificiale":
                player.setPlayerNameEditable(false);
                player.setPlayerName(getAIName());
                break;
            case "loggato":
                player.resetPlayerName();
                player.setPlayerNameEditable(false);
                showLogin(players.indexOf(player));
                break;

        }
        // }
    }

    private void askLogout(PlayerInfo player) throws LogoutException {

        int decision = JOptionPane.showConfirmDialog(gui, "Logout di " + player.getPlayerName());
        if (decision != 0) { // Non vuole sloggarsi
            player.setType("Loggato");
            throw new LogoutException("Player doesn't want to logout");
        }
        player.setLogged(false);

    }

    private void showLogin(int index) {
        UserDialog userDialog = new UserDialog(gui, players, false);
        userDialog.setIndex(index);
        userDialog.setPlayers(getAllLoggedPlayers());
        userDialog.setVisible(true);
        gui.setEnabled(false);
    }

    private PlayerInfo getSelectedRow(JComboBox type) {
        for (PlayerInfo info : players) {
            if (info.getTypeComboBox().equals(type)) {
                return info;
            }
        }
        return null; // Non dovrebbe mai arrivarci
    }

    private String getAIName() {

        String name;
        for (int i = 0; i < PlayersPanel.N_PLAYERS_MAX; i++) {
            name = "GiocatoreIA" + (i + 1);
            if (!getAllAINames().contains(name)) {
                return name;
            }
        }
        return null; // Non dovrebbe mai arrivarci
    }

    private List<String> getAllAINames() {

        List<String> names = new ArrayList<>();
        for (PlayerInfo player : players) {
            if (player.isArtificial()) {
                names.add(player.getPlayerName());
            }
        }
        return names;
    }

    /**
     * Ritorna la lista dei giocatori già loggati
     *
     * @return
     */
    private List getAllLoggedPlayers() {
        List<String> list = new ArrayList<>();
        for (PlayerInfo player : players) {
            if (player.isLogged()) {
                list.add(player.getPlayerName());
            }
        }
        return list;
    }

}
