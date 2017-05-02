package risiko;

import java.util.Random;

public class Player {
    private String name;
    private int bonusArmies;

    public Player(String name) {
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
    
}
