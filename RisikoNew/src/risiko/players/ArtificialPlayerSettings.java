package risiko.players;

/**
 * Class that represents the artificial players' settings. 
 */
public class ArtificialPlayerSettings {

    private int baseAttack; // the number of attacks
    private int reinforceDelay; 
    private int attackDeclarationDelay;

    /**
     * Creates a new ArtificialPlayerSettings.
     * @param speed 
     */
    public ArtificialPlayerSettings(String speed) {
        this.baseAttack = 5;
        setSettingsForSpeed(speed);
    }

    /**
     * Sets different settings relative to the speed.
     * @param speed 
     */
    private void setSettingsForSpeed(String speed) {
        switch (speed) {
            case "Veloce":             
                this.reinforceDelay = 100;
                this.attackDeclarationDelay = 100;
                break;
            case "Normale":
                this.reinforceDelay = 300;
                this.attackDeclarationDelay = 500;
                break;
            case "Lento":
                this.reinforceDelay = 800;
                this.attackDeclarationDelay = 2000;
                break;
        }
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public int getReinforceDelay() {
        return reinforceDelay;
    }

    public int getAttackDeclarationDelay() {
        return attackDeclarationDelay;
    }
}
