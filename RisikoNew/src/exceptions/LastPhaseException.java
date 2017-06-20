package exceptions;

/**
 * This exception is thrown when the game exits the last phase of a turn, so
 * <code>MovePhase</code>.
 */
public class LastPhaseException extends Exception {

    /**
     * Creates a new instance of <code>LastPhaseException</code> without detail
     * message.
     */
    public LastPhaseException() {
    }

    /**
     * Constructs an instance of <code>LastPhaseException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public LastPhaseException(String msg) {
        super(msg);
    }
}
