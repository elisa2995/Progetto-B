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

    public AttackResultInfo(CountryInfo[] countries, int[][] dice, boolean conquered, String conqueredContinent) {
        this.countries = countries;
        this.dice = dice;
        this.conquered = conquered;
        this.conqueredContinent = conqueredContinent;
    }
    
    public CountryInfo getAttackerInfo(){
        return countries[0];
    }
    
    public CountryInfo getDefenderInfo(){
        return countries[1];
    }
    
    public String getAttackerCountryName(){
        return getAttackerInfo().getName();
    }
    
    public String getDefenderCountryName(){
        return getDefenderInfo().getName();
    }
    
    public int getMaxArmiesAttacker(){
        return getAttackerInfo().getMaxArmies();
    }
    
    public int getMaxArmiesDefender(){
        return getDefenderInfo().getMaxArmies();
    }

    public int[][] getDice() {
        return dice;
    }

    public boolean hasConquered() {
        return conquered;
    }

    public String getConqueredContinent() {
        return conqueredContinent;
    }
    
    public boolean areBothArtificial(){
        return countries[0].hasArtificialOwner() && countries[1].hasArtificialOwner();
    }
    
    public boolean isAttackerArtificial(){
        return countries[0].hasArtificialOwner();
    }
    
    public boolean canAttackFromCountry(){
        return countries[0].canAttackFromHere();
    }
}
