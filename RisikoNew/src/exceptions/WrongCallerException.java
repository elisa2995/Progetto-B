package exceptions;

/**
 * Thrown when defense methods are called by a player that doesn't have the
 * right to do it.
 */
public class WrongCallerException extends Exception {

    /**
     * Creates a new instance of <code>WrongCallerException</code> without
     * detail message.
     */
    public WrongCallerException() {
    }

    /**
     * Constructs an instance of <code>WrongCallerException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public WrongCallerException(String msg) {
        super(msg);
    }
}
