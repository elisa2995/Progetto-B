package risiko.missions;

import java.util.List;
import risiko.map.Continent;
import risiko.map.Country;

/**
 * A mission which consits in conquering <code>nrCountriesToConquer</code>
 * countries.
 *
 */
public class CountriesMission extends Mission {

    private int nrCountriesToConquer;

    public CountriesMission(String description, int points) {
        super(description, points);
    }

    public int getNrCountriesToConquer() {
        return this.nrCountriesToConquer;
    }

    /**
     * Checks if the mission is completed, i.e. if <code>countries</code>
     * contains at least <code> nrCountriesToConquer</code> countries.
     *
     * @return
     */
    @Override
    public boolean isCompleted(List<Country> countries) {
        return countries.size() >= nrCountriesToConquer;
    }

    /**
     * Builds <code>targetList</code>.
     *
     * @param continents
     */
    @Override
    public void buildTarget(List<Continent> continents) {
        this.nrCountriesToConquer = Integer.parseInt(description.split(" ")[1]);
    }
}
