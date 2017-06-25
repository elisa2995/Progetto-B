package exceptions;

/**
 * Thrown when the player tries to change the phase of the game even if there
 * still are some tasks to do to complete the current one.
 *
 */
public class PendingOperationsException extends Exception {

    /**
     * Creates a new instance of <code>PendingOperationsException</code> without
     * detail message.
     */
    public PendingOperationsException() {
    }

    /**
     * Constructs an instance of <code>PendingOperationsException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public PendingOperationsException(String msg) {
        super(msg);
    }
}
