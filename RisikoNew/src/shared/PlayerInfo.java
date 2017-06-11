package shared;

/**
 * Class used to communicate between the model and the view the info of a
 * certain player.
 */
public class PlayerInfo {

    private final String name;
    private final String color;
    private final int bonusArmies;
    private final boolean artificial;

    public PlayerInfo(String name, String color, int bonus, boolean artificial) {
        this.name = name;
        this.color = color;
        this.bonusArmies = bonus;
        this.artificial = artificial;
    }
    
    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public boolean isArtificial() {
        return artificial;
    }
    
    public int getBonusArmies(){
        return bonusArmies;
    }

}
