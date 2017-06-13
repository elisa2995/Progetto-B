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
public class TranslationException extends Exception {

    /**
     * Creates a new instance of <code>TranslationException</code> without
     * detail message.
     */
    public TranslationException() {
    }

    /**
     * Constructs an instance of <code>TranslationException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public TranslationException(String msg) {
        super(msg);
    }
}
