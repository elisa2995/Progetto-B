package shared;

/**
 * Class used to communicate between the model and the view the info of a
 * certain player.
 */
public class PlayerInfo {

    private final String name;
    private String color;
    private int bonusArmies;
    private boolean artificial;
    private String type; // Artificial/Logged/Normal
    private int points;

    /**
     * Constructs a PlayerInfo with only its attributes <code>name</code>,
     * <code>color</code>, <code>bonusarmies</code> and <code>artificial</code>
     * set.
     *
     * @param name
     * @param color
     * @param bonus
     * @param artificial
     */
    public PlayerInfo(String name, String color, int bonus, boolean artificial) {
        this.name = name;
        this.color = color;
        this.bonusArmies = bonus;
        this.artificial = artificial;
    }

    /**
     * Constructs a PlayerInfo with only its attribute <code>name</code>,
     * <code>color</code> and <code>type</code> set.
     *
     * @param name
     * @param color
     * @param type
     */
    public PlayerInfo(String name, String color, String type) {
        this.name = name;
        this.color = color;
        this.type = type;
    }
    
    public PlayerInfo(String name, int points){
        this.name=name;
        this.points=points;
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
    
    public int getPoints(){
        return points;
    }
}
