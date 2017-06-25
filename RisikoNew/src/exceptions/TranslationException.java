package exceptions;

/**
 * Thrown when the <code>Translator</code> can't find a word in the vocabulary.
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
