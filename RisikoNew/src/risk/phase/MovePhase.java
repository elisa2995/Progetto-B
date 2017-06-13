package risk.phase;

import risiko.map.Country;
import risiko.map.RisikoMap;

public class MovePhase extends Phase {

    public MovePhase(RisikoMap map) {
        super(map);
        this.index = MOVE_INDEX;
    }

    @Override
    public String toString() {
        return "MOVE";
    }

    public void resetMoveCountries() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getFromCountryName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setFromCountry(String fromCountryName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean controlMovement(Country countryByName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
