package shared;

/**
 * Class used to communicate between the model and the view the info of a
 * certain player.
 */
public class PlayerInfo {

    private final String name;
    private final String color;
    private int bonusArmies;
    private boolean artificial;
    private String type;

    public PlayerInfo(String name, String color, int bonus, boolean artificial) {
        this.name = name;
        this.color = color;
        this.bonusArmies = bonus;
        this.artificial = artificial;
    }

    public PlayerInfo(String name, String color, String type) {
        this.name = name;
        this.color = color;
        this.type = type;
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

    public int getBonusArmies() {
        return bonusArmies;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}