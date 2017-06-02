package risiko.missions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    public void buildTarget(Map<String, List<Country>> continentCountries) {
        List<String> continents = new ArrayList<>(continentCountries.keySet());
        for (String continent : continents) {
            if (description.contains(continent)) {
                this.setTargetList(continentCountries.get(continent));
            }
        }
    }
}
