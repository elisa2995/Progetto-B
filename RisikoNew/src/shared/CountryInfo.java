package shared;

/**
 * Class used to communicate between the model and the view the info of a
 * certain country that may have changed.
 */
public class CountryInfo {

    private String name;
    private PlayerInfo player;
    private int armies;

    public CountryInfo(String name, int armies, PlayerInfo player) {
        this.name = name;
        this.armies = armies;
        this.player = player;
    }

    public CountryInfo(String name, int armies) {
        this.name = name;
        this.armies = armies;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getArmies() {
        return armies;
    }

    public void setArmies(int armies) {
        this.armies = armies;
    }

    public PlayerInfo getPlayer() {
        return player;
    }

    public void setPlayer(PlayerInfo player) {
        this.player = player;
    }

    public String getPlayerName() {
        return player.getName();
    }

    public String getPlayerColor() {
        return player.getColor();
    }
}
