package risiko.phase;

import risiko.map.Country;
import risiko.map.RisikoMap;

public class MovePhase extends Phase {

    private Country fromCountry, toCountry;

    public MovePhase(RisikoMap map) {
        super(map);
        this.index = MOVE_INDEX;
    }

    public int getMaxArmiesForMovement(Country country) {
        return country.getArmies() - 1;
    }

    public Country getFromCountry() {
        return fromCountry;
    }

    public void setFromCountry(Country fromCountry) {
        this.fromCountry = fromCountry;
    }

    public Country getToCountry() {
        return toCountry;
    }

    public void setToCountry(Country toCountry) {
        this.toCountry = toCountry;
    }

    public void move(int nrArmies) {
        fromCountry.removeArmies(nrArmies);
        toCountry.addArmies(nrArmies);
    }
    
    @Override
    public void clear(){
        fromCountry = null;
        toCountry = null;
    }

    @Override
    public String toString() {
        return "MOVE";
    }

    public boolean controlMovement() {
        return map.controlMovement(fromCountry, toCountry);
    }
    
    public boolean controlMovement(Country toCountry){
        return map.controlMovement(fromCountry, toCountry);
    }

}
