package risiko.phase;

import risiko.map.RisikoMap;

public abstract class Phase implements Comparable<Phase> {

    public static final int CARD_INDEX = 0, REINFORCE_INDEX = 1, FIGHT_INDEX = 2, MOVE_INDEX = 3;
    protected int index;
    protected RisikoMap map;

    public Phase(RisikoMap map) {
        this.map = map;
    }

    public int getIndex() {
        return this.index;
    }

    public static String getName(int index) {
        switch (index) {
            case (0):
                return "PLAY_CARDS";
            case (1):
                return "REINFORCE";
            case (2):
                return "FIGHT";
            case (3):
                return "MOVE";
            default:
                return "ERROR";
        }
    }

    public void clear() {
    }

    @Override
    public int compareTo(Phase other) {
        return this.index - other.index;
    }
}
