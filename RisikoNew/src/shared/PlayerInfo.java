package shared;

/**
 * Class used to communicate between the model and the view the info of a
 * certain player.
 */
public class PlayerInfo {

    private String name;
    private String color;
    private int bonusArmies;
    private boolean artificial;

    public PlayerInfo(String name, String color, boolean artificial) {
        this.name = name;
        this.color = color;
        this.artificial = artificial;
    }

    /*public PlayerInfo(String name, String color, int bonus){
        this.name = name;
        this.color = color;
        this.bonusArmies = bonus;
    }*/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isArtificial() {
        return artificial;
    }

    public void setArtificial(boolean artificial) {
        this.artificial = artificial;
    }

}
