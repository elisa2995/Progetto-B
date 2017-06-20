package exceptions;

/**
 * This exception is throw when the player tries to go to the next phase of the
 * game but he can't because it has to finish some tasks in the curent phase.
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
