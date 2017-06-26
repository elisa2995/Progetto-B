package risiko.map;

import java.util.List;

/**
 * Class that represents a continent. The continent is described by its name,
 * its countries and the number of bonus armies awarded to a player in case it
 * helds all its countries.
 */
public class Continent {

    private String name;
    private List<Country> Countries;
    private int bonus;

    public Continent(String name, List<Country> Countries, int bonus) {
        this.name = name;
        this.bonus = bonus;
        this.Countries = Countries;
    }

    public List<Country> getCountries() {
        return Countries;
    }

    public void setCountries(List<Country> Countries) {
        this.Countries = Countries;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Checks if country is contained in the continent.
     *
     * @param country
     * @return
     */
    public boolean containsCountry(Country country) {
        return this.getCountries().contains(country);
    }

    @Override
    public String toString() {
        return this.name;
    }

}
