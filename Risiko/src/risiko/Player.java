package risiko;

import java.awt.Color;
import java.util.ArrayList;

public class Player {
    private Mission mission;
    private ArrayList<CardBonus> cardBonus;    
    private String  name;
    private Color   color;
    private int     contaCarte[];
    private int     bonusArmies;
    private boolean justDrowCard;
    
    public Player(String name, Color color) {
        this.justDrowCard=false;
        this.bonusArmies = 0;
        this.mission = null;
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

    public void addBonusArmies(int bonusArmies) {
        this.bonusArmies += bonusArmies;
    }
    
    public void decrementBonusArmies(int bonusArmies){
        this.bonusArmies-=bonusArmies;
    }
    
    public Color getColor(){
        return this.color;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }
    
    public ArrayList<CardBonus> getCardBonus() {
        return cardBonus;
    }

        
    public void playTris(CardBonus cardBonus1, CardBonus cardBonus2, CardBonus cardBonus3, int bonusArmiesTris){
        if(cardBonus1==null && cardBonus2==null && cardBonus3==null)
            return;
        contaCarte[cardBonus1.ordinal()]--;
        contaCarte[cardBonus2.ordinal()]--;
        contaCarte[cardBonus3.ordinal()]--;
        cardBonus.remove(cardBonus1);
        cardBonus.remove(cardBonus2);
        cardBonus.remove(cardBonus3);
        bonusArmies+=bonusArmiesTris;
    }
    
    public boolean canPlayThisTris(CardBonus cardBonus1, CardBonus cardBonus2, CardBonus cardBonus3){
        int contaTris[]=new int[4];
        contaTris[cardBonus1.ordinal()]++;
        contaTris[cardBonus2.ordinal()]++;
        contaTris[cardBonus3.ordinal()]++;
        return ((contaCarte[0]>=contaTris[0]) && (contaCarte[1]>=contaTris[1]) && (contaCarte[2] >= contaTris[2]) && (contaCarte[3]>=contaTris[3]));
    }
    
    
    public void drowBonusCard(){
        CardBonus card =CardBonus.giveRandomCard();
        cardBonus.add(card);
        contaCarte[card.ordinal()]++;
        this.justDrowCard=true;
    }
    
    public boolean havejustDrowCardBonus() {
        return justDrowCard;
    }

    public void setJustDrowCard(boolean justDrowCard) {
        this.justDrowCard = justDrowCard;
    }

    public String getMissionDescription() {
        return mission.getDescription();
    }
}
