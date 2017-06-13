/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package risk.phase;

import risiko.map.RisikoMap;

public class CardPhase extends Phase{

    public CardPhase(RisikoMap map) {
        super(map);
        this.index = CARD_INDEX;
    }
    
    @Override
    public String toString(){
        return "PLAY_CARDS";
    }
    
}
