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

    @Override
    public void run() {
        while(!gameEnd){
        nextTurn.setText("nextPhase - "+ game.getTimeRemaining());
//        try {
//          
//            this.wait(1000);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(TimerUpdater.class.getName()).log(Level.SEVERE, null, ex);
//        }
        }
    }
    
    
}
