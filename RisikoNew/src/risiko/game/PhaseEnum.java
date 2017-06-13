package risiko.game;

import exceptions.LastPhaseException;


public enum PhaseEnum {
    PLAY_CARDS, REINFORCE, FIGHT, MOVE;

    private final int value;

    private PhaseEnum() {
        this.value = ordinal();
    }

    /**
     * Passa alla fase successiva del turno.
     * @return la fase successiva.
     * @throws LastPhaseException se è l'ultima fase del turno.
     */
    public PhaseEnum next() throws LastPhaseException {
        
        try {
            return PhaseEnum.values()[this.value+1];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new LastPhaseException();
        }
    }
}
