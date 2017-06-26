package gui.startGameGUI;

import java.awt.Dimension;
import gui.BackgroundPane;
import gui.mainGui.GUI;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import shared.PlayerInfo;

/**
 * Initial GUI. It lets the user set the info for a new game: the number of
 * players, their type(normal, artificial, logged) and the color of their troops
 * From this GUI is also possible to register new users.
 *
 */
public class StartGameGUI extends JFrame {

    private PlayersPanel playersPanel;

    /**
     * Creates a new form startGame; it calls all the initializazion functions.
     */
    public StartGameGUI() {
        initBackground();
        initComponents();
        init();
    }

    /**
     * Sets the dimension of the dialog and adds <code>playerPanel</code> to the
     * JFrame.
     */
    private void init() {

        Dimension dim = getToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getWidth() / 2, dim.height / 2 - this.getHeight() / 2);
        this.setResizable(false);

        addPlayersPanel();
    }

    /**
     * Adds <code>playerPanel</code> to the dialog.
     */
    private void addPlayersPanel() {
        playersPanel = new PlayersPanel(this);
        playersPanel.setBounds(200, 210, 410, 236);
        playersPanel.setOpaque(false);
        this.add(playersPanel);
    }

    /**
     * Sets the background to the dialog.
     */
    private void initBackground() {
        BufferedImage backgroundImage;
        try {
            backgroundImage = ImageIO.read(new File("src/resources/images/loginBackground.png"));
            this.setContentPane(new BackgroundPane(backgroundImage));
        } catch (IOException ex) {
            System.err.println("loginBackground.png not found");
        }
    }

    /**
     * Setting of all the JComponents.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        jToggleButton1 = new javax.swing.JToggleButton();
        startButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        registrationButton = new javax.swing.JButton();
        commentsLabel = new javax.swing.JLabel();
        rankingButton = new javax.swing.JButton();

        jRadioButtonMenuItem1.setSelected(true);
        jRadioButtonMenuItem1.setText("jRadioButtonMenuItem1");

        jToggleButton1.setText("jToggleButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        startButton.setFont(new java.awt.Font("Calibri Light", 1, 14)); // NOI18N
        startButton.setText("Gioca");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        addButton.setText("Aggiungi giocatore");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        registrationButton.setFont(new java.awt.Font("Calibri Light", 1, 14)); // NOI18N
        registrationButton.setText("Registra nuovo giocatore");
        registrationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registrationButtonActionPerformed(evt);
            }
        });

        commentsLabel.setFont(new java.awt.Font("Calibri Light", 1, 14)); // NOI18N
        commentsLabel.setForeground(new java.awt.Color(153, 0, 0));
        commentsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        rankingButton.setFont(new java.awt.Font("Calibri Light", 1, 14)); // NOI18N
        rankingButton.setText("Classifica");
        rankingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rankingButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(150, 150, 150))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(165, 165, 165)
                        .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(144, 144, 144)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(rankingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(commentsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(registrationButton))))
                .addContainerGap(224, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(157, 157, 157)
                .addComponent(registrationButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 255, Short.MAX_VALUE)
                .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(startButton, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(commentsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankingButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                .addGap(66, 66, 66))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Adds a player to the <code>playerPanel</code>.
     *
     * @param evt
     */
    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        playersPanel.addPlayerRow();
    }//GEN-LAST:event_addButtonActionPerformed

    /**
     * Creates the GUI of the game if all the field are filled and the chosen
     * names for the players are different among each other.
     *
     * @param evt
     */
    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed

        List<String> playerNames = playersPanel.getPlayerNames();
        if (hasEmptyFields(playerNames) && isAlreadyUsedUsername(playerNames)) {
            return;
        }

        GUI gui;
        List<PlayerInfo> players = playersPanel.getAllplayers();

        try {
            gui = new GUI(players);
            gui.setVisible(true);
            this.dispose();
        } catch (Exception ex) {
            Logger.getLogger(StartGameGUI.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_startButtonActionPerformed

    /**
     * It checks if there are empty fields and eventually shows a warning
     * message.
     *
     * @param playerNames
     * @return true if there are empty fields
     */
    private boolean hasEmptyFields(List<String> playerNames) {

        for (String name : playerNames) {
            if (name.length() == 0) {
                commentsLabel.setText("Inserisci i nomi di tutti i giocatori");
                return true;
            }
        }

        return false;
    }

    /**
     * Opens a <code>userDialog</code> in the registration mode, allowing to
     * register a new user.
     *
     * @param evt
     */
    private void registrationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registrationButtonActionPerformed
        UserDialog userDialog = new UserDialog(this, null, true);
        userDialog.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_registrationButtonActionPerformed

    /**
     * Shows the rankings of the logged players.
     *
     * @param evt
     */
    private void rankingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rankingButtonActionPerformed
        RankingDialog rankingDialog = new RankingDialog(this, true);
        rankingDialog.setVisible(true);
    }//GEN-LAST:event_rankingButtonActionPerformed

    /**
     * Checks if the names of the players are different from each other.
     *
     * @param players
     * @return true if there are at least two equal usernames
     */
    private boolean isAlreadyUsedUsername(List<String> players) {
        Object[] players1 = players.toArray();
        Object[] players2 = players1.clone();
        for (int i = 0; i < players1.length; i++) {
            for (int j = 0; j < players2.length; j++) {
                if (i != j && players1[i].equals(players2[j])) {
                    commentsLabel.setText("I nomi dei giocatori devono essere diversi tra loro");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Sets the visibility of <code>addButton</code>.
     *
     * @param visible
     */
    public void setAddButtonVisible(boolean visible) {
        this.addButton.setVisible(visible);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JLabel commentsLabel;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JButton rankingButton;
    private javax.swing.JButton registrationButton;
    private javax.swing.JButton startButton;
    // End of variables declaration//GEN-END:variables

}
