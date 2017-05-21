/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package risiko;

/**
 *
 * @author alessandro
 */
public class ArtificialPlayerSettings {
    private int attackRate;
    private int attackDelay;
    private int reinforceDelay;
    private int attackDeclarationDelay;
    private int reinforceDistribution;

    public ArtificialPlayerSettings(int attackRate, int attackDelay, int reinforceDelay, int attackDeclarationDelay, int reinforceDistribution) {
        this.attackRate = attackRate;
        this.attackDelay = attackDelay;
        this.reinforceDelay = reinforceDelay;
        this.attackDeclarationDelay = attackDeclarationDelay;
        this.reinforceDistribution = reinforceDistribution;
    }

    public int getAttackRate() {
        return attackRate;
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

    public int getReinforceDistribution() {
        return reinforceDistribution;
    }

    public void setAttackRate(int attackRate) {
        this.attackRate = attackRate;
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

    public void setReinforceDistribution(int reinforceDistribution) {
        this.reinforceDistribution = reinforceDistribution;
    }
    
    
    
}
