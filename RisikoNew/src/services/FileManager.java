package services;

import exceptions.FileManagerException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe che si occupa di gestire tutte le letture/scritture da/su file.
 * Implementato secondo il pattern singleton con una lazy initialization.
 */
public class FileManager {

    private static volatile FileManager instance;
    private final String PLAYERS = "src/resources/files/players.txt";
    // url file che fa da db per le info dei giocatori
    private final String COUNTRIES = "src/resources/files/countries.txt";
    private final String MISSIONS = "src/resources/files/missions.txt";
    private final String LABELS = "src/resources/files/countriesLabels.txt";
    private final String COLORS = "src/resources/files/countriesColors.txt";
    private final String TRIS = "src/resources/files/bonusTris.txt";
    private final String VOCABULARY = "src/resources/files/vocabulary";
    private final String INFO = "src/resources/files/info";

    private FileManager() {
    }

    /**
     * Ritorna l'istanza di FileManager. (Se non è ancora stata creata, la crea
     * - lazy initialization)
     *
     * @return
     */
    public static FileManager getInstance() {
        if (instance == null) {
            synchronized (FileManager.class) {
                if (instance == null) {
                    instance = new FileManager();
                }
            }
        }
        return instance;
    }

    //----------------------- countries.txt ----------------------------------//
    /**
     * Legge il file countries.txt per ricavare la lista di territori.
     *
     * @return una List contenente i nomi dei territori.
     */
    public List<String> getCountries() {
        List<String> countries = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(COUNTRIES))) {
            String line;
            String countryName;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("-")) {
                    countryName = line.split(",")[0];
                    countries.add(countryName);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return countries;
    }

    /**
     * Legge il file countries.txt per ricavare da ogni country la lista dei
     * suoi vicini.
     *
     * @return una Map che mappa un territorio con la lista dei suoi vicini.
     */
    public Map<String, List<String>> getCountryNeighbors() {

        Map<String, List<String>> countryNeighbors = new HashMap();
        try (BufferedReader br = new BufferedReader(new FileReader(COUNTRIES))) {
            String line;
            String subject;
            String[] tokens;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("-")) {
                    tokens = line.split(",");
                    subject = tokens[0];
                    countryNeighbors.put(subject, Arrays.asList(Arrays.copyOfRange(tokens, 1, tokens.length)));
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return countryNeighbors;
    }

    /**
     * Legge il file countries.txt per ricavare i continenti.
     *
     * @return una List di Map, di cui ogni elemento corrisponde a un
     * continente. La Map ha tre entrate: §1 name -> String - nome del
     * continente §2 countries -> List<String> - di territori che appartengono a
     * quel continente §3 bonus -> Integer - bonusArmies
     */
    public List<Map<String, Object>> getContinents() {

        List<Map<String, Object>> continents = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(COUNTRIES))) {
            String line;
            Map<String, Object> row;
            List<String> countries = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (line.startsWith("-")) { // Nuovo continente
                    row = new HashMap<>();
                    String tokens[] = line.split("-");     //tokens[] sarà un array di 3 stringhe, la prima sarà vuota, questo perchè line inizia con "-"
                    row.put("name", tokens[1]);
                    row.put("bonus", Integer.parseInt(tokens[2]));
                    row.put("countries", countries);
                    continents.add(row);
                    countries = new ArrayList<>();
                } else {
                    countries.add(line.split(",")[0]);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return continents;
    }

    //------------------------ missions.txt ----------------------------------//
    /**
     * Legge il file missions.txt.
     *
     * @return una List di Map, di cui ogni elemento corrisponde a una missione.
     * La Map ha tre entrate: §1 type -> String - tipo di missione ("Continent"
     * se consiste nel conquistre di continenti, "Countries" altrimenti) §2
     * points -> Integer - punti che si guadagnano nel caso la missione venga
     * completata. §3 description -> String - descrizione
     */
    public List<Map<String, Object>> getMissions() {

        List<Map<String, Object>> missions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(MISSIONS))) {
            Map<String, Object> mission;
            String line;
            String[] tokens;

            while ((line = br.readLine()) != null) {
                mission = new HashMap<>();
                tokens = line.split("=")[1].split("-");
                mission.put("type", line.split("=")[0]);
                mission.put("points", Integer.parseInt(tokens[1]));
                mission.put("description", tokens[0]);
                missions.add(mission);
            }

        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return missions;

    }

    //------------------------ players.txt -----------------------------------//
    /**
     * Aggiorna il file players.txt.
     *
     * @param username l'username del giocatore che ha vinto la partita
     * @param missionPoints i punti della missione completata.
     */
    public void recordGainedPoints(String username, int missionPoints) {

        String inputStr = readPlayersFile(username, missionPoints);
        writePlayersFile(inputStr);

    }

    /**
     * Legge il file dei players da cui costruisce una stringa. Aggiorna il
     * punteggio di player.
     *
     * @param player il giocatore che ha vinto la partita
     * @return il contenuto del file sotto forma di stringa.
     */
    private String readPlayersFile(String username, int missionPoints) {

        String inputStr = "";
        try (BufferedReader file = new BufferedReader(new FileReader(PLAYERS))) {
            String line;
            StringBuilder inputBuffer = new StringBuilder();
            int points;
            while ((line = file.readLine()) != null) {
                if (line.contains(username)) {
                    String[] tokens = line.split(";");
                    points = Integer.parseInt(tokens[2]) + missionPoints;
                    tokens[2] = Integer.toString(points);
                    line = tokens[0] + ";" + tokens[1] + ";" + tokens[2];
                }
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }
            inputStr = inputBuffer.toString();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return inputStr;
    }

    /**
     * Riscrive il contenuto del file player.txt con i dati aggioranti.
     *
     * @param urlFile il file players.txt
     * @param player il giocatore che ha vinto la partita
     */
    private void writePlayersFile(String content) {
        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream(PLAYERS);
            fileOut.write(content.getBytes());
            fileOut.close();
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Restituisce i punti di un giocatore.
     *
     * @param username
     * @return
     * @throws FileManagerException
     */
    public int getPlayerPoints(String username) throws FileManagerException {
        try (BufferedReader file = new BufferedReader(new FileReader(PLAYERS))) {
            String line;
            while ((line = file.readLine()) != null) {
                if (line.contains(username)) {
                    String[] tokens = line.split(";");
                    return Integer.parseInt(tokens[2]);
                }
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        throw new FileManagerException("No user found with username " + username);
    }

    /**
     * Controlla se le credenziali inserite sono corrette.
     *
     * @param username
     * @param password
     * @return true se sono corrette, false altrimenti.
     */
    public boolean checkCredentials(String username, String password) {
        
        try (BufferedReader br = new BufferedReader(new FileReader(PLAYERS))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tmp = line.split(";");
                byte[] encryptedBytes = tmp[1].getBytes();
                byte[] decryptedBytes = Base64.getDecoder().decode(encryptedBytes);
                String decryptedString = new String(decryptedBytes, "UTF-8");
                if (tmp[0].equals(username) && decryptedString.equals(password)) {
                    return true;
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    /**
     * Registra l'utente nel file players.txt.
     * @param username
     * @param encryptedPassword 
     */
    public void registerUser(String username, String encryptedPassword){
        
        String line = username+";"+encryptedPassword+";"+"0";
        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream(PLAYERS, true);
            fileOut.write(line.getBytes());
            fileOut.close();
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Legge il file players.txt e controla se lo username è presente tra quelli
     * dei giocatori già registrati
     *
     * @param username
     * @return true se lo username è presente nel file
     */
    public boolean checkUsernameInFile(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(PLAYERS))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] tmp = line.split(";");
                if (tmp[0].equals(username)) {
                    return false;
                }
            }
        } catch (IOException ex) { 
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    //----------------------------- countriesLabels.txt-------------------------//
    /**
     * Ritorna una List di Map, di cui ogni elemento contiene le proprietà di
     * una Label. La Map ha tre entrate: §1 country -> String - nome della
     * country §2 x -> Integer - la coordinata x della label §3 y -> Integer -
     * la coordinata y della label.
     *
     * @return
     */
    public List<Map<String, Object>> getLabelsProperties() {
        List<Map<String, Object>> labels = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(LABELS))) {
            Map<String, Object> row;
            String[] tokens;
            String line;
            while ((line = br.readLine()) != null) {
                row = new HashMap<>();
                tokens = line.split("\t");
                row.put("country", tokens[1]);
                row.put("x", Integer.parseInt(tokens[0].split(",")[0]));
                row.put("y", Integer.parseInt(tokens[0].split(",")[1]));
                labels.add(row);
            }

        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return labels;
    }

    //------------------------- countriesColors.txt --------------------------//
    /**
     * Legge il file countriesColors.txt.
     *
     * @return una lista di Map in cui ogni elemento corrisponde a un
     * territorio. La Map ha 4 entrate: § 1 - key : "country" -> String nome
     * della country § 2 - key : "R" -> Integer numero da 0 a 255 che
     * rappresenta il valore di Rosso nell'RGB della Country § 3 - key : "G" ->
     * Integer numero da 0 a 255 che rappresenta il valore di Verde nell'RGB
     * della country § 4 - key : "B" -> Integer numero da 0 a 255 che
     * rappresenta il valore di Blu nell'RGB della country
     */
    public List<Map<String, Object>> getCountriesColors() {

        List<Map<String, Object>> countriesColors = new ArrayList();
        try (BufferedReader br = new BufferedReader(new FileReader(COLORS))) {
            Map<String, Object> row;
            String[] tokens, RGB;
            String line;
            while ((line = br.readLine()) != null) {
                row = new HashMap<>();
                tokens = line.split("=");
                RGB = tokens[1].split(",");
                row.put("country", tokens[0]);
                row.put("R", Integer.parseInt(RGB[0].trim()));
                row.put("G", Integer.parseInt(RGB[1].trim()));
                row.put("B", Integer.parseInt(RGB[2].trim()));
                countriesColors.add(row);
            }
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return countriesColors;
    }

    //--------------------------- bonusTris.txt ------------------------------//
    /**
     * Legge il file bonusTris.txt.
     *
     * @return una List di Map, in cui ogni elemento corrisponde a un tris. La
     * Map ha 2 entrate: § key : "cards" -> String[] cards, le carte che
     * compongono il tris § key : "bonus" -> Integer bonus, il bonus di armate
     * per quel tris.
     */
    public List<Map<String, Object>> getTris() {

        List<Map<String, Object>> tris = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(TRIS))) {
            String line;
            HashMap<String, Object> row;
            while ((line = br.readLine()) != null) {
                row = new HashMap<>();
                row.put("cards", line.split("\t")[0].split(","));
                row.put("bonus", Integer.parseInt(line.split("\t")[1]));
                tris.add(row);
            }
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tris;

    }
    
    //---------------------------- vocabulary.txt --------------------------------//
    
    public String getWord(String word, String lang, boolean backwards) throws FileManagerException{
        
        String line;
        try(BufferedReader br = new BufferedReader(new FileReader(VOCABULARY+lang+".txt"))){
            while((line = br.readLine())!=null){
                int index = backwards ? 1:0;
                if(line.split("=")[index].trim().equals(word)){
                    return line;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new FileManagerException("Word "+word+" not found in vocabulary"+lang+".");
    
    }
    
    //---------------------------------- info.txt -------------------------------//
    
    public String getInfoFor(String phase, String lang) throws FileManagerException{
    
        String line;
        try(BufferedReader br = new BufferedReader(new FileReader(INFO+lang+".txt"))){
            while((line = br.readLine())!=null){
                if(line.split("=")[0].trim().equals(phase)){
                    return line.split("=")[1];
                }
            }
        
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new FileManagerException("No info found for "+phase);
    }
}
