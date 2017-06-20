package shared;

/**
 * Class used to communicate between the model and the view the info of a
 * certain country that may have changed.
 */
public class CountryInfo {

    private String name;
    private PlayerInfo player;
    private int maxArmies;
    private int armies;
    private boolean canAttackFromHere;

    /* In case the active player is attacking from this country, this parameter
    tells wheter the player can choose again this country for a new fight */
    /**
     * Constructs a CountryInfo with only its attributes name, maxArmies, player
     * and canAttackFromHere set.
     *
     * @param name
     * @param maxArmies
     * @param player
     * @param canAttackFromHere
     */
    public CountryInfo(String name, int maxArmies, PlayerInfo player, boolean canAttackFromHere) {
        this.name = name;
        this.maxArmies = maxArmies;
        this.player = player;
        this.canAttackFromHere = canAttackFromHere;
    }

    /**
     * Constructs a CountryInfo with only its attributes <code>name</code>,
     * <code>maxArmies</code>, <code>player</code> set.
     *
     * @param name
     * @param maxArmies
     * @param player
     */
    public CountryInfo(String name, int maxArmies, PlayerInfo player) {
        this.name = name;
        this.maxArmies = maxArmies;
        this.player = player;
    }

    /**
     * Constructs a CountryInfo with only its attributes <code>player</code>,
     * <code>name</code> and <code>armies</code> set.
     *
     * @param player
     * @param name
     * @param armies
     */
    public CountryInfo(PlayerInfo player, String name, int armies) {
        this.player = player;
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

    public int getMaxArmies() {
        return maxArmies;
    }

    public void setMaxArmies(int maxArmies) {
        this.maxArmies = maxArmies;
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

    public boolean canAttackFromHere() {
        return canAttackFromHere;
    }

    public void canAttackFromHere(boolean canAttackFromHere) {
        this.canAttackFromHere = canAttackFromHere;
    }

    public boolean hasArtificialOwner() {
        return this.player.isArtificial();
    }
}
