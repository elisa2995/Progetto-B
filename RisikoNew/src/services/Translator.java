package services;

import exceptions.FileManagerException;
import exceptions.TranslationException;

public class Translator {

    private static volatile Translator instance;

    private Translator() {
    }

    public static Translator getInstance() {
        if (instance == null) {
            synchronized (Translator.class) {
                if (instance == null) {
                    instance = new Translator();
                }
            }
        }
        return instance;
    }

    /**
     * Translates the word <code>source</code>. If backwards is true, it
     * translates it from the language <code> lang</code> to English, otherwise
     * it translates it from English to <code>lang</code>.
     *
     * @param source
     * @param lang
     * @param backwards
     * @return
     * @throws TranslationException
     */
    public String translate(String source, String lang, boolean backwards) throws TranslationException {
        try {
            int index = backwards? 0:1;
            String row = FileManager.getInstance().getWord(source, lang, backwards);
            return row.split("=")[index];
        } catch (FileManagerException ex) {
            throw new TranslationException(ex.getMessage());
        }
    }

}
