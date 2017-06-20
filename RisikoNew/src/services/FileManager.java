package services;

import exceptions.FileManagerException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class used to write to/read from files.
 */
public class FileManager {

    private static volatile FileManager instance;
    private final String USERS = "files/players.txt";
    private final String COUNTRIES = "resources/files/countries.txt";
    private final String MISSIONS = "resources/files/missions.txt";
    private final String LABELS = "resources/files/countriesLabels.txt";
    private final String COLORS = "resources/files/countriesColors.txt";
    private final String TRIS = "resources/files/bonusTris.txt";
    private final String VOCABULARY = "resources/files/vocabulary";
    private final String INFO = "resources/files/info";

    private FileManager() {
    }

    /**
     * Returns an instance of FileManager. (If the instance hasn't been
     * initialized yet, it creates it. - lazy initialization)
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

    //----------------------- COUNTRIES ----------------------------------//
    /**
     * Reads the file at url <code>COUNTRIES</code> and builds an HashMap to map
     * each country with the list of its neighbors.
     *
     * @return
     */
    public Map<String, List<String>> getCountryNeighbors() {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(COUNTRIES);
        Map<String, List<String>> countryNeighbors = new HashMap();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
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
     * Reads the file at url <code>COUNTRIES</code> and retrieves the list of
     * continents.
     *
     * @return a List of Maps<String, Object>. Each element of the list
     * represents a continent and has 3 entries: §1 K : String "name" -> V :
     * String - name of the continent §2 K : String "countries" -> V :
     * List<String> - the continent's countries §3 K : String "bonus" -> V: the
     * number of bonusArmies awarded to the player if it conquers the whole
     * continent.
     */
    public List<Map<String, Object>> getContinents() {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(COUNTRIES);
        List<Map<String, Object>> continents = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            Map<String, Object> row;
            List<String> countries = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (line.startsWith("-")) { // It's a new continent
                    row = new HashMap<>();
                    String tokens[] = line.split("-");
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

    //------------------------ MISSIONS ----------------------------------//
    /**
     * Reads the file at url <code>MISSIONS</code>.
     *
     * @return a List of Maps. Each element of the list represents a different
     * mission. The map has 3 entries: § K : String "type" -> String - the type
     * of mission("Continent" if the mission is to conquer specific
     * continents,"Countries" otherwise) §2 K : String "points" -> Integer -
     * points awarded to the player if it completes the mission. §3 K : String
     * "description"-> String - mission description.
     */
    public List<Map<String, Object>> getMissions() {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(MISSIONS);
        List<Map<String, Object>> missions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
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

    //------------------------ PLAYERS -----------------------------------//
    /**
     * Update the file <code>PLAYERS</code>.
     *
     * @param username the username of the user that won the game.
     * @param missionPoints the points awarded to the player.
     */
    public void recordGainedPoints(String username, int missionPoints) {
        String inputStr = readPlayersFile(username, missionPoints);
        writePlayersFile(inputStr);

    }

    /**
     * Build a String with the content of the file <code>PLAYERS</code>. Updates
     * the points of the player.
     *
     * @param player the winner
     * @return the content of the file as a String
     */
    private String readPlayersFile(String username, int missionPoints) {
        String inputStr = "";
        try (BufferedReader br = new BufferedReader(new FileReader(USERS))) {
            String line;
            StringBuilder inputBuffer = new StringBuilder();
            int points;
            while ((line = br.readLine()) != null) {
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
     * Rewrites the content of the file.
     *
     * @param urlFile
     */
    private void writePlayersFile(String content) {
        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream(USERS);
            fileOut.write(content.getBytes());
            fileOut.close();
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns the points of the player whose username is <code>username</code>.
     *
     * @param username
     * @return
     * @throws FileManagerException
     */
    public int getPlayerPoints(String username) throws FileManagerException {
        try (BufferedReader br = new BufferedReader(new FileReader(USERS))) {
            String line;
            while ((line = br.readLine()) != null) {
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
     * Checks if the credentials are correct.
     *
     * @param username
     * @param password
     * @return true if they're correct, false otherwise.
     */
    public boolean checkCredentials(String username, String password) {

        try (BufferedReader br = new BufferedReader(new FileReader(USERS))) {
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
     * Records the user into <code>PLAYERS</code>.
     *
     * @param username
     * @param encryptedPassword
     */
    public void registerUser(String username, String encryptedPassword) {

        String line = "\n" + username + ";" + encryptedPassword + ";" + "0";
        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream(USERS, true);
            fileOut.write(line.getBytes());
            fileOut.close();
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Checks if the chosen username is available.
     *
     * @param username
     * @return true if the username is available, false otherwise.
     */
    public boolean checkUsernameInFile(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(USERS))) {
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

    //---------------------------- COUNTRIES -----------------------------//
    /**
     * Returns a List of Maps. Each element contains the property of a different
     * Label. The Map has 3 entries: §1 K String "country" -> V String - the
     * name of the country to which the label refers §2 K String "x" -> V
     * Integer - the x of the Label §3 String "y" -> V Integer - the y of the
     * label.
     *
     * @return
     */
    public List<Map<String, Object>> getLabelsProperties() {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(LABELS);

        List<Map<String, Object>> labels = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
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

    //------------------------- COLORS.txt --------------------------//
    /**
     * Reads the file <code>COLORS</code>.
     *
     * @return a List of Maps. Each element of the list represents a Country.
     * The map has 4 entries:§ 1 - K : String "country" --> Name of the country
     * § 2 - K : Stirng "R" -> Integer, number between 0 and 255 that represents
     * the amount of Red in the RGB representation of the color of the country.
     * § 3 - K : String "G" -> Integer, number between 0 and 255 that represents
     * the amount of Green in the RGB representation of the color of the
     * country. § 4 - K : String "B" -> Integer, number between 0 and 255 that
     * represents the amount of Green in the RGB representation of the color of
     * the country.
     */
    public List<Map<String, Object>> getCountriesColors() {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(COLORS);

        List<Map<String, Object>> countriesColors = new ArrayList();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
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

    //--------------------------- TRIS ------------------------------//
    /**
     * Reads <code> TRIS </code>.
     *
     * @return a List of Maps. Eache elements represents a tris. The map has 2
     * entries: §1 K String : "cards" -> String[] the set of cards which make up
     * the tris. tris § K String : "bonus" -> Integer bonus, the bonus awarded
     * for that tris.
     */
    public List<Map<String, Object>> getTris() {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(TRIS);

        List<Map<String, Object>> tris = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
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

    //---------------------------- VOCABULARY --------------------------------//
    /**
     * Looks for the word <code> word</code> into the vocabulary.
     *
     * @param word
     * @param lang
     * @param reverse
     * @return
     * @throws FileManagerException
     */
    public String getWord(String word, String lang, boolean reverse) throws FileManagerException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(VOCABULARY + lang + ".txt");

        String line;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
            while ((line = br.readLine()) != null) {
                int index = reverse ? 1 : 0;
                if (line.split("=")[index].trim().equals(word)) {
                    return line;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new FileManagerException("Word " + word + " not found in vocabulary" + lang + ".");

    }

    //---------------------------------- INFO -------------------------------//
    /**
     * Looks for the info corresponding to the phase <code>phase</code>.
     * @param phase
     * @param lang
     * @return
     * @throws FileManagerException 
     */
    public String getInfoFor(String phase, String lang) throws FileManagerException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(INFO + lang + ".txt");

        String line;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
            while ((line = br.readLine()) != null) {
                if (line.split("=")[0].trim().equals(phase)) {
                    return line.split("=")[1];
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new FileManagerException("No info found for " + phase);
    }
}
