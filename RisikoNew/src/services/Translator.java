package services;

import exceptions.FileManagerException;
import exceptions.TranslationException;

public class Translator {
    
    private static volatile Translator instance;
    
    private Translator(){}
    
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
    
    public String translate(String source, String lang) throws TranslationException{
        try{
            String row = FileManager.getInstance().getWord(source, lang);
            return row.split("=")[1];
        }catch(FileManagerException ex){
            throw new TranslationException(ex.getMessage());
        }
    }
}
