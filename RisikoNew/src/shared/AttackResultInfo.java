package shared;

/**
 * Class used to communicate between the model and the view the result of a
 * fight.
 */
public class AttackResultInfo {

    private final CountryInfo[] countries;
    /*Array with 2 elements, the first represents the attacker, the second reperesents the defender.
    Their armies represent the maximum number of armies that can be used for an hypothetical future attack.*/
    private final int[][] dice;
    private final boolean conquered;
    private final String conqueredContinent;
    private final boolean canAttackFromCountry; // true if the attacker can fire another attack from the same country
    private final boolean[] artificialAttack;

    public AttackResultInfo(CountryInfo[] countries, int[][] dice, boolean conquered, String conqueredContinent, boolean canAttackFromCountry, boolean[] artificialAttack) {
        this.countries = countries;
        this.dice = dice;
        this.conquered = conquered;
        this.conqueredContinent = conqueredContinent;
        this.canAttackFromCountry = canAttackFromCountry;
        this.artificialAttack = artificialAttack;
    }
    
    public CountryInfo getAttackerInfo(){
        return countries[0];
    }
    
    public CountryInfo getDefenderInfo(){
        return countries[1];
    }
    
    public String getAttackerName(){
        return getAttackerInfo().getName();
    }
    
    public String getDefenderName(){
        return getDefenderInfo().getName();
    }
    
    public int getMaxArmiesAttacker(){
        return getAttackerInfo().getArmies();
    }
    
    public int getMaxArmiesDefender(){
        return getDefenderInfo().getArmies();
    }

    public int[][] getDice() {
        return dice;
    }

    public boolean isConquered() {
        return conquered;
    }

    public String getConqueredContinent() {
        return conqueredContinent;
    }

    public boolean isCanAttackFromCountry() {
        return canAttackFromCountry;
    }

    public boolean[] getArtificialAttack() {
        return artificialAttack;
    }

}
