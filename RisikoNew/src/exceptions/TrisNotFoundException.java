package exceptions;

/**
 * This exception is thrown when the system is looking for the bonus awarded for
 * a tris that is not valid.
 */
public class TrisNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>TrisNotFoundException</code> without
     * detail message.
     */
    public TrisNotFoundException() {
    }

    /**
     * Constructs an instance of <code>TrisNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public TrisNotFoundException(String msg) {
        super(msg);
    }
}
