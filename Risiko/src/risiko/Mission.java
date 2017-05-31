/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package risiko;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author feded
 */
public class Mission {
    private String description;
    private List<Country> targetList;
    private int nrCountryToConquer;
    private int points;
    
    public Mission(String description, int points) {
        this.description = description;
        this.points = points;
        this.targetList = new ArrayList<>();
        this.nrCountryToConquer = 0;
    }

    public String getDescription() {
        return description;
    }

    public int getNrCountryToConquer() {
        return nrCountryToConquer;
    }

    public List<Country> getTargetList() {
        return targetList;
    }

    public void setTargetList(List<Country> targetList) {
        this.targetList.addAll(targetList);
    }

    public void setNrCountryToConquer(int nrCountryToConquer) {
        this.nrCountryToConquer = nrCountryToConquer;
    }
    
    public int getPoints(){
        return this.points;
    }
}
