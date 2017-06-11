/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import gui.startGameGUI.PlayerInfo;
import gui.startGameGUI.PlayersPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;

/**
 *
 * @author Elisa
 */
public class RemovePlayerListener implements ActionListener {

    private List<PlayerInfo> players;
    private PlayersPanel playersPanel;

    public RemovePlayerListener(PlayersPanel playersPanel, List<PlayerInfo> players) {
        this.players = players;
        this.playersPanel = playersPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source=(JButton) e.getSource();
        PlayerInfo player = getPlayerByButton(source);
        // Elimino questa riga e aggiorno la posizione delle righe successive a questa
        players.remove(player);
        playersPanel.removePlayer(player);
    }

    private PlayerInfo getPlayerByButton(JButton removeButton) {
        for (PlayerInfo player : players) {
            if (removeButton.equals(player.getRemoveButton())) {
                return player;
            }
        }
        return null; // non dovrebbe mai arrivarci
    }   

}
