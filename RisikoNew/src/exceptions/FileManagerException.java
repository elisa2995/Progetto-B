/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;


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
