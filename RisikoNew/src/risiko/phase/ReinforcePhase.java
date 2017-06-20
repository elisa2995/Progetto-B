package risiko.phase;

import risiko.map.Country;
import risiko.map.RisikoMap;

/**
 * Class that represents the phase of the game in which the player can reinforce
 * its countries with its bonusArmies.
 */
public class ReinforcePhase extends Phase {

    public ReinforcePhase(RisikoMap map) {
        super(map);
        this.index = REINFORCE_INDEX;
    }

    public void reinforce(Country country) {
        country.reinforce();
    }

    @Override
    public String toString() {
        return "REINFORCE";
    }

}
