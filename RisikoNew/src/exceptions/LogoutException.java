package exceptions;

/**
 * Thrown when the user refuses to logout after being asked to.
 */
public class LogoutException extends Exception {

    /**
     * Creates a new instance of <code>LogoutException</code> without detail
     * message.
     */
    public LogoutException() {
    }

    /**
     * Constructs an instance of <code>LogoutException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public LogoutException(String msg) {
        super(msg);
    }
}
