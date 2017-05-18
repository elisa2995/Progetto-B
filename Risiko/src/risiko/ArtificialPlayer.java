/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package risiko;

import exceptions.PendingOperationsException;
import gui.GameObserver;
import java.awt.Color;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author emanuela
 */
public class ArtificialPlayer extends Player implements Runnable, GameObserver{
    private Game game;
    private int action=0;
    
    /**
     * 
     * @param name
     * @param color 
     */
    public ArtificialPlayer(String name, Color color, Game game) {
        super(name, color);
        this.game = game;
    }

    /**
     * aggiunge armate ai territori posseduti fino a che non sono esaurite
     */
     private synchronized  void  randomReinforce(){
        RisikoMap map = game.getMap();
        List<Country> myCountries = game.getMap().getMyCountries(this);
        
        while(game.canReinforce(1, this)){
            int index = new Random().nextInt(myCountries.size());
            game.reinforce(myCountries.get(index).getName(), 1, this);
            try {
                this.wait(450);           
            } catch (InterruptedException ex) {
                
            }
        }
        
        
            
    }
    
    /**
     * esegue il turno del giocatore artificiale
     */
    @Override
    public void run() {
        while(true){
            try {
                //if(game.getActivePlayer().equals(this)){
                switch (game.getPhase()) {
                    case REINFORCE:
                        randomReinforce();
                        break;
                    case FIGHT:
                        break;
                }
                
                game.nextPhase(this);
                
            } catch (PendingOperationsException ex) {
                //l'eccezione delle armate ancora da assegnare viene data quando si sovrappongono le operazioni di 2 giocatori
                Logger.getLogger(ArtificialPlayer.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }

    @Override
    public void updateOnReinforce(String countryName, int bonusArmies) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateOnSetAttacker(String countryName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateOnSetDefender(String countryAttackerName, String countryDefenderName, String defenderPlayer, int maxArmiesAttacker, int maxArmiesDefender) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateOnAttackResult(String attackResultInfo, boolean isConquered, boolean canAttackFromCountry, int maxArmiesAttacker, int maxArmiesDefender, int[] attackerDice, int[] defenderDice) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateOnVictory(String winner) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateOnCountryAssignment(String[] countries, int[] armies, Color[] colors) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateOnArmiesChange(String country, int armies, Color color) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateOnNextTurn(Player activePlayer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateOnSetFromCountry(String countryName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateOnDrowCardBonus(CardBonus lastCardDrowed) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateOnPhaseChange(String player, String phase, Color color) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
