package risiko.phase;

import risiko.map.Country;
import risiko.map.RisikoMap;

/**
 * Class that represents the phase of the game in which the active player can
 * move its armies from one of its countries to another adjacent country.
 */
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

    /**
     * Moves <code> nrArmies</code> from <code>fromCountry</code> to
     * <code>toCountry</code>.
     *
     * @param nrArmies
     */
    public void move(int nrArmies) {
        fromCountry.removeArmies(nrArmies);
        toCountry.addArmies(nrArmies);
    }

    /**
     * Moves <code>nrArmies</code> from <code>fromCountry</code> to
     * <code>toCountry</code>.
     *
     */
    public void move(Country fromCountry, Country toCountry, int nrArmies) {
        this.fromCountry = fromCountry;
        this.toCountry = toCountry;
        move(nrArmies);
    }

    /**
     * Checks if it is legal to move from <code>fromCountry</code> to
     * <code>toCountry</code>.
     *
     * @return
     */
    public boolean controlMovement() {
        return fromCountry.canMoveTo(toCountry);
    }

    /**
     * Checks if it is legal to move from <code>fromCountry</code> to
     * <code>toCountry</code>.
     *
     * @return
     */
    public boolean controlMovement(Country toCountry) {
        return fromCountry.canMoveTo(toCountry);
    }

    @Override
    public void clear() {
        fromCountry = null;
        toCountry = null;
    }

    @Override
    public String toString() {
        return "MOVE";
    }

}
