/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package risiko.players;

/**
 *
 * @author alessandro
 */
public class ArtificialPlayerSettings {

    private int baseAttack;
    private int reinforceDelay;
    private int attackDeclarationDelay;

    public ArtificialPlayerSettings(String speed) {
        this.baseAttack = 5;
        setSettingsForSpeed(speed);
    }

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

    public void setBaseAttack(int baseAttack) {
        this.baseAttack = baseAttack;
    }

    public void setReinforceDelay(int reinforceDelay) {
        this.reinforceDelay = reinforceDelay;
    }

    public void setAttackDeclarationDelay(int attackDeclarationDelay) {
        this.attackDeclarationDelay = attackDeclarationDelay;
    }

}
