package risiko;

import java.awt.Color;
import java.util.ArrayList;

public class Player {
    private ArrayList<CardBonus> cardBonus;
    private String  name;
    private Color   color;
    private int     contaCarte[];
    private int     bonusArmies;
    private boolean justDrowCard;
    
    public Player(String name, Color color) {
        this.justDrowCard=false;
        this.bonusArmies = 0;
        this.contaCarte  = new int[4];
        this.cardBonus   = new ArrayList<>();
        this.color       = color;
        this.name        = name;
    }

    public String getName() {
        return name;
    }

    public int getBonusArmies() {
        return bonusArmies;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBonusArmies(int bonusArmies) {
        this.bonusArmies = bonusArmies;
    }
    
    public void decrementBonusArmies(int bonusArmies){
        this.bonusArmies-=bonusArmies;
    }
    
    public Color getColor(){
        return this.color;
    }

    public ArrayList<CardBonus> getCardBonus() {
        return cardBonus;
    }

    public void countCardsByType(){
        for (int i = 0; i < 4; i++) 
            contaCarte[i]=0;
        for (CardBonus cb : cardBonus) 
            contaCarte[cb.ordinal()]++;
    }
        
    public void playTris(CardBonus cardBonus1, CardBonus cardBonus2, CardBonus cardBonus3, int bonusArmiesTris){
        cardBonus.remove(cardBonus1);
        cardBonus.remove(cardBonus2);
        cardBonus.remove(cardBonus3);
        bonusArmies+=bonusArmiesTris;
    }
        
    public boolean canPlay_3Cannon_Tris(){
        return contaCarte[2]>=3;
    }
    
    public boolean canPlay_3Fants_Tris(){
        return contaCarte[0]>=3;
    }
    
    public boolean canPlay_3Knight_Tris(){
        return contaCarte[1]>=3;
    }
    
    public boolean canPlay_Fants_Knight_Cannon_Tris(){
        return (contaCarte[0]>=1 && contaCarte[1]>=1 && contaCarte[2]>=1);
    }
    
    public boolean canPlay_Jolly_2Fants_Tris(){
        return (contaCarte[3]>=1 && contaCarte[0]>=2);
    }
    
    public boolean canPlay_Jolly_2Cannon_Tris(){
        return (contaCarte[3]>=1 && contaCarte[2]>=2);
    }
    
    public boolean canPlay_Jolly_2Knight_Tris(){
        return (contaCarte[3]>=1 && contaCarte[1]>=2);
    }
    
    public void drowBonusCard(){
        this.cardBonus.add(CardBonus.giveRandomCard());
        this.justDrowCard=true;
    }
    
    public ArrayList<CardBonus> getAllBonusCard(){
        return this.cardBonus;
    }

    public boolean havejustDrowCardBonus() {
        return justDrowCard;
    }

    public void setJustDrowCard(boolean justDrowCard) {
        this.justDrowCard = justDrowCard;
    }
}
