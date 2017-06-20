package exceptions;

/**
 * Exception that is thrown when, searching in a file, <code>FileManager</code>
 * doesn't find the content that is looking for.
 */

public class FileManagerException extends Exception {

    /**
     * Creates a new instance of <code>DBException</code> without detail
     * message.
     */
    public FileManagerException() {
    }

    /**
     * Constructs an instance of <code>DBException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public FileManagerException(String msg) {
        super(msg);
    }
}
