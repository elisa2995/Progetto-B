package risiko.missions;

import java.util.List;
import java.util.Map;
import risiko.map.Continent;
import risiko.map.Country;

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

    public abstract boolean isCompleted(List<Country> countries);

    /**
     * Costruisce la targetList (contenente i country da conquistare) di una
     * mission
     *
     * @param mission
     * @author Federico
     *
     */
    public abstract void buildTarget(List<Continent> continents);
}
