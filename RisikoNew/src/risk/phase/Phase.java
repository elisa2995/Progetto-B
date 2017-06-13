package risk.phase;

import risiko.map.RisikoMap;

public abstract class Phase {

    protected static final int CARD_INDEX = 0, REINFORCE_INDEX = 1, FIGHT_INDEX = 2, MOVE_INDEX = 3;
    protected int index;
    protected RisikoMap map;

    public Phase(RisikoMap map) {
        this.map = map;
    }

    public int getIndex(){
        return this.index;
    }
}
