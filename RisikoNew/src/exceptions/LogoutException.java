/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author Elisa
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
