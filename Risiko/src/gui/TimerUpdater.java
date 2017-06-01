/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import risiko.Game;

/**
 *
 * @author alessandro
 */
public class TimerUpdater implements Runnable{
    JButton nextTurn;
    Game game;
    boolean gameEnd = false;

    public void setGameEnd(boolean gameEnd) {
        this.gameEnd = gameEnd;
    }

    public TimerUpdater(JButton nextTurn, Game game) {
        this.nextTurn = nextTurn;
        this.game = game;
    }

    private String toMinutes(long seconds) {
        long min, sec;
        min = seconds / 60;
        sec = seconds % 60;
        if (sec < 10) {
            return "" + min + ":0" + sec;
        } else {
            return "" + min + ":" + sec;
        }
    }
    
    @Override
    public void run() {
        //nextTurn.setText("nextPhase - " + game.getTimeRemaining());
        while (!gameEnd) {
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    nextTurn.setText("nextPhase - " + toMinutes(game.getTimeRemaining()));
                }
            });
            //senza un wait si blocca l'edt per le troppe richieste fatte 
            synchronized (this) {
                try {
                    this.wait(800);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TimerUpdater.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    
}
