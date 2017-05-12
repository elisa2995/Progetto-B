package risiko;

import java.awt.Color;
import java.util.Random;

public class Player {
    private String name;
    private int bonusArmies;
    private Color color;

    public Player(String name, Color color) {
        this.color = color;
        this.name = name;
        this.bonusArmies = 0;
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
    
}
