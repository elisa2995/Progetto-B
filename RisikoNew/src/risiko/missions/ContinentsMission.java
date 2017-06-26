package risiko.missions;

import java.util.ArrayList;
import java.util.List;
import risiko.map.Continent;
import risiko.map.Country;

/**
 * A mission which consists in conquering countries in <code>targetList</code>.
 * 
 */
public class ContinentsMission extends Mission {

    private final List<Country> targetList;

    public ContinentsMission(String description, int points) {
        super(description, points);
        this.targetList = new ArrayList<>();
    }

    public List<Country> getTargetList() {
        return targetList;
    }

    public void setTargetList(List<Country> targetList) {
        this.targetList.addAll(targetList);
    }

    /**
     * Checks if the mission is completed. I.E. if <code>myCountries</code>
     * contains all the countries in <code>this.targetList</code>.
     *
     * @param myCountries
     * @return
     */
    @Override
    public boolean isCompleted(List<Country> myCountries) {
        return myCountries.containsAll(targetList);
    }

    /**
     * Builds <code>targetList</code>.
     *
     * @param continents
     */
    @Override
    public void buildTarget(List<Continent> continents) {
        for (Continent continent : continents) {
            if (description.contains(continent.getName())) {
                this.setTargetList(continent.getCountries());
            }
        }
    }
}
