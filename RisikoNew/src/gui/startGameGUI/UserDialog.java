package gui.startGameGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import services.FileManager;

/**
 * Dialog for registration and login.
 * In the registration mode it allows to register a new loggable player while in
 * the login mode it allow to login
 * 
 */
public class UserDialog extends javax.swing.JDialog {

    private final StartGameGUI gui;
    private int index;
    private List<String> players;
    private List<PlayerInfo> playerRows;
    private boolean isRegistration;

    /**
     * It creates new form RegistrationDialog
     *
     * @param gui
     * @param playerRows
     * @param isRegistration
     */
    public UserDialog(StartGameGUI gui,List<PlayerInfo> playerRows, boolean isRegistration) {
        initComponents();
        this.gui = gui;
        this.playerRows=playerRows;        
        this.isRegistration = isRegistration;
        setWindowSettings();
        setMode(isRegistration);
    }
    
    
    /**
     * It sets the dimensions of the window, the background of <code>commentsText</code>
     * and the behavior when the window is closed
     */
    private void setWindowSettings() {
        Dimension dim = getToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getWidth() / 2, dim.height / 2 - this.getHeight() / 2);
        this.setResizable(false);
        
        commentsText.setBackground(new Color(240, 240, 240));
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeDialog();
            }

        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        usernameText = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        password2Label = new javax.swing.JLabel();
        saveUserButton = new javax.swing.JButton();
        loginButton = new javax.swing.JButton();
        passwordText = new javax.swing.JPasswordField();
        password2Text = new javax.swing.JPasswordField();
        commentsText = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Username");

        jLabel2.setText("Password");

        password2Label.setText("Ripeti la password");

        saveUserButton.setText("Registra");
        saveUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveUserButtonActionPerformed(evt);
            }
        });

        loginButton.setText("Login");
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saveUserButton, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(password2Label)
                        .addComponent(jLabel2)
                        .addComponent(jLabel1)
                        .addComponent(usernameText, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                        .addComponent(passwordText)
                        .addComponent(password2Text)
                        .addComponent(commentsText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(51, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(usernameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(passwordText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(password2Label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(password2Text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(commentsText, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(loginButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveUserButton)
                .addGap(30, 30, 30))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * It registers a new loggable player if all the fields are filled, the inserted passwords matched and 
     * the username choosen is not already used.
     *
     * @param evt
     */
    private void saveUserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveUserButtonActionPerformed
        if (usernameText.getText().length() == 0 || passwordText.getPassword().length == 0 || password2Text.getPassword().length == 0) {
            commentsText.setText("Compila tutti i campi");
            return;
        }
        if (!String.valueOf(passwordText.getPassword()).equals(String.valueOf(password2Text.getPassword()))) {
            commentsText.setText("Le password non coincidono");
            return;
        }

        if (FileManager.getInstance().checkUsernameInFile(usernameText.getText())) {
            try {
                registerUser(usernameText.getText(), String.valueOf(passwordText.getPassword()));
                JOptionPane.showMessageDialog(null, "Utente " + usernameText.getText() + " registrato correttamente");
            } catch (IOException ex) {
                Logger.getLogger(UserDialog.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                closeDialog();
            }
        } else {
            commentsText.setText("Username già usato");
        }

    }//GEN-LAST:event_saveUserButtonActionPerformed

    /**
     * It closes the dialog and makes the gui visible again; if the closing operation is done
     * after an attempt of login, it resets the type of the player
     */
    private void closeDialog() {
        if(!isRegistration){
            playerRows.get(index).setType("Normale");
        }
        this.dispose();
        gui.setVisible(true);
        gui.setEnabled(true);
    }

    /**
     * It insertes in <code>playerRow</code> a logged player if 
     * the player is not already logged and the inserted username and password
     * are correct
     *
     * @param evt
     */
    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        String url = "files/players.txt";
        String username = usernameText.getText();
        String password = String.valueOf(passwordText.getPassword());
        if (checkUsername(username)) {
            try (BufferedReader br = new BufferedReader(new FileReader(url))) {
                String line;

                while ((line = br.readLine()) != null) {
                    String[] tmp = line.split(";");
                    byte[] encryptedBytes = tmp[1].getBytes();
                    byte[] decryptedBytes = Base64.getDecoder().decode(encryptedBytes);
                    String decryptedString = new String(decryptedBytes, "UTF-8");
                    if (tmp[0].equals(username) && decryptedString.equals(password)) {
                        JOptionPane.showMessageDialog(null, "Utente " + usernameText.getText() + " inserito correttamente");
                        this.setVisible(false);
                        playerRows.get(index).setPlayerName(username);
                        playerRows.get(index).setLogged(true);
                        gui.setVisible(true);
                        gui.setEnabled(true);
                        return;
                    }

                }

            } catch (FileNotFoundException ex) {
                System.out.println("File not found");
            } catch (IOException ex) {
                Logger.getLogger(UserDialog.class.getName()).log(Level.SEVERE, null, ex);
            }

            commentsText.setText("Nome giocatore o password errati");
        } else {
            commentsText.setText("Giocatore già presente nel gioco");
        }
    }//GEN-LAST:event_loginButtonActionPerformed

    /**
     * It saves in the players.txt the username and the encripted password 
     * of the player 
     *
     * @param username
     * @param password
     * @throws IOException
     */
    private void registerUser(String username, String password) throws IOException {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encryptedBytes = encoder.encode(password.getBytes());
        String encryptedString = new String(encryptedBytes, "UTF-8");
        try (PrintWriter out = new PrintWriter(new FileOutputStream("files/players.txt", true))) {
            out.println(username + ";" + encryptedString);
        }
    }

    /**
     * it sets the dialog components depending on the purpose of the 
     * dialog(registration of a new user or login)
     *
     * @param isRegistration
     */
    private void setMode(boolean isRegistration) {
        String title = isRegistration ? "Registrazione" : "Login";
        setTitle(title);
        password2Text.setVisible(isRegistration);
        loginButton.setVisible(!isRegistration);
        password2Label.setVisible(isRegistration);
        saveUserButton.setVisible(isRegistration);
    }

    /**
     * It sets the index of the playerRow from which the login is requested
     *
     * @param index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    
    /**
     * It saves the list of the already logged players
     *
     * @param list
     */
    public void setPlayers(List<String> list) {
        this.players = list;
    }

    /**
     * It checks if the chosen username has already been used to login
     *
     * @param username
     * @return false if the username is already used
     */
    private boolean checkUsername(String username) {
        for (String s : players) {
            if (s.equals(username)) {
                return false;
            }
        }
        return true;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel commentsText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton loginButton;
    private javax.swing.JLabel password2Label;
    private javax.swing.JPasswordField password2Text;
    private javax.swing.JPasswordField passwordText;
    private javax.swing.JButton saveUserButton;
    private javax.swing.JTextField usernameText;
    // End of variables declaration//GEN-END:variables

}