package risiko.missions;

import java.util.List;
import risiko.map.Continent;
import risiko.map.Country;

/**
 * Class that represent a mission. 
 * 
 */
public abstract class Mission {

    protected String description;
    private final int points;

    public Mission(String description, int points) {
        this.description = description;
        this.points = points;
    }

    public String getDescription() {
        return description;
    }

    public int getPoints() {
        return this.points;
    }

    /**
     * Checks if the mission is completed.
     * @param countries
     * @return 
     */
    public abstract boolean isCompleted(List<Country> countries);

    /**
     * Builds <code>targetList</code>.
     * @param continents 
     */
    public abstract void buildTarget(List<Continent> continents);
}
