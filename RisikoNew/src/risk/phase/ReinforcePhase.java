/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package risk.phase;

import risiko.map.Country;
import risiko.map.RisikoMap;

/**
 *
 * @author emanuela
 */
public class ReinforcePhase extends Phase{
    
    public ReinforcePhase(RisikoMap map) {
        super(map);
        this.index = REINFORCE_INDEX;
    }
    
    @Override
    public String toString(){
        return "REINFORCE";
    }

    public void reinforce(Country country) {
        map.reinforce(country);
    }
    
}
