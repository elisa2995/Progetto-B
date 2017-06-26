package services;

import exceptions.FileManagerException;
import exceptions.TranslationException;

/**
 * Interface used to translate words from/to English.
 */
public interface Translator {

    /**
     * Translates the word <code>source</code>. If reverse is true, it
     * translates it from the language <code>lang</code> to English, otherwise
     * it translates it from English to <code>lang</code>.
     *
     * @param source
     * @param lang
     * @param reverse
     * @return
     * @throws TranslationException if no translation is found 
     */
    public static String translate(String source, String lang, boolean reverse) throws TranslationException {
        try {
            int index = reverse? 0:1;
            String row = FileManager.getInstance().getWord(source, lang, reverse);
            return row.split("=")[index];
        } catch (FileManagerException ex) {
            throw new TranslationException(ex.getMessage());
        }
    }

}
