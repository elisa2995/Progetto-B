package exceptions;

/**
 * This exception is thrown when the bonus deck doesn't found the tris that is
 * passed to it among the playable ones.
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
