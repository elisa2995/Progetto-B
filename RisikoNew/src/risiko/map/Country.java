package risiko.map;

import java.util.ArrayList;
import java.util.List;


public class Country implements Comparable<Country>{
    
    private final String name;
    private int armies;
    private List<Country> neighbors;
    
    public Country(String name){
        this.name= name;
        this.neighbors=new ArrayList<>();
    }

    public List<Country> getNeighbors() {
        return neighbors;
    }
    
    public void setNeighbors(List<Country> neighbors){
        this.neighbors = neighbors;
    }
    
    public String getName(){
        return this.name;
    }
    
    public void setArmies(int armies){
        this.armies = armies;
    }
    
    public int getArmies(){
        return this.armies;
    }
    
    public void removeArmies(int armies){
       this.armies-= armies;
    }
    
    public void addArmies(int armies){
        this.armies+=armies;
    }
    
    public void incrementArmies(){
        this.armies++;
    }
    
    public boolean isConquered(){
        return (armies==0);
    }

    @Override
    public int compareTo(Country o) {
        return this.armies-o.getArmies();
    }
    
    /**
     * questo metodo seve per mostrare il nome nella combobox
     * @return il nome del territorio
     */
    @Override
    public String toString(){
        return this.name;
    }
}
