package risiko.missions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import risiko.Continent;
import risiko.Country;

/**
 * Missione per cui bisogna conquistare i territori indicati in
 * <code>targetList</code>
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

    @Override
    public boolean isCompleted(List<Country> myCountries) {
        return myCountries.containsAll(targetList);
    }

    @Override
    public void buildTarget(List<Continent> continents) {
        for (Continent continent : continents) {
            if (description.contains(continent.getName())) {
                this.setTargetList(continent.getCountries());
            }
        }
    }
}
