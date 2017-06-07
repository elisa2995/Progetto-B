
import java.util.List;
import risiko.Continent;
import risiko.Country;
import risiko.RisikoMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author andrea
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RisikoMap risikoMap = new RisikoMap();
        
        //TESTO COUNTRIES
//        List<Country> countries = risikoMap.getCountries();
//        for (Country country : countries) {
//            System.out.println(country);
//            for (Country neighbor : country.getNeighbors()) {
//                System.out.println("--"+neighbor);               
//            }
//            
//        }
        
        //TESTO CONTINENT
        List<Continent> continents = risikoMap.getContinents();
        for (Continent continent : continents) {
            System.out.println(continent.getName());
            System.out.println(continent.getBonus());
            for (Country country : continent.getCountries()) {
                System.out.println("--"+country);               
            }
            
        }
    }
    
}
