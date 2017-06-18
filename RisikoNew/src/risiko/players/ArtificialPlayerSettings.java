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
    private int attackDelay;
    private int reinforceDelay;
    private int attackDeclarationDelay;

    public ArtificialPlayerSettings(int attackDelay, int reinforceDelay, int attackDeclarationDelay, int reinforceDistribution) {
        this.baseAttack = 10;
        this.attackDelay = attackDelay;
        this.reinforceDelay = reinforceDelay;
        this.attackDeclarationDelay = attackDeclarationDelay;
    }

    public ArtificialPlayerSettings() {
        
    }
    
    public int getBaseAttack() {
        return baseAttack;
    }

    public int getAttackDelay() {
        return attackDelay;
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

    public void setAttackDelay(int attackDelay) {
        this.attackDelay = attackDelay;
    }

    public void setReinforceDelay(int reinforceDelay) {
        this.reinforceDelay = reinforceDelay;
    }

    public void setAttackDeclarationDelay(int attackDeclarationDelay) {
        this.attackDeclarationDelay = attackDeclarationDelay;
    }
   
}