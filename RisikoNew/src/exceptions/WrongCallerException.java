/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author emanuela
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
