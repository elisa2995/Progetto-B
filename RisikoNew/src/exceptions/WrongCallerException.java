package exceptions;

/**
 * This exception is thrown when an artificial player has not the right to call
 * a method.
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
