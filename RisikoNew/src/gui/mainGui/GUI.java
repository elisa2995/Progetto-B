package gui.mainGui;

import gui.startGameGUI.StartGameGUI;
import controllers.LabelMapListener;
import exceptions.FileManagerException;
import utils.GameObserver;
import exceptions.PendingOperationsException;
import exceptions.TranslationException;
import gui.mainGui.dialogs.AttackerDialog;
import gui.BackgroundPane;
import gui.mainGui.cards.CardPanel;
import gui.DefaultColor;
import gui.mainGui.dialogs.DefenseDialog;
import gui.mainGui.dialogs.DiceDialog;
import gui.mainGui.dialogs.MoveDialog;
import gui.PlayAudio;
import gui.mainGui.dialogs.SettingsDialog;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import risiko.game.Game;
import risiko.game.GameInvocationHandler;
import risiko.game.GameProxy;
import services.FileManager;
import services.Translator;
import shared.AttackResultInfo;
import shared.CountryInfo;
import shared.PlayerInfo;

/**
 * Main GUI.
 */
public class GUI extends JFrame implements GameObserver {

    private GameProxy game;
    private Map<Color, String> colorCountryNameMap;
    private final Map<String, JLabel> countryLabelMap;
    private DefenseDialog defenseDialog;
    private AttackerDialog attackerDialog;
    private DiceDialog diceDialog;
    private LabelMapListener labelMapListener;
    private FadeOutLabel fadeOutLabel;
    private CardPanel cardPanel;
    private final int PREFERRED_WIDTH = 400;
    private final int PREFERRED_HEIGHT = 192;
    private final String LANG = "ITA";

    public GUI(List<PlayerInfo> players) {
        initBackground();
        initComponents();
        labelMap.setIcon(new ImageIcon("src/resources/images/risiko.png"));
        countryLabelMap = new HashMap<>();
        initColorCountryNameMap();
        init(players);
    }

    private void initBackground() {
        BufferedImage backgroundImage;
        try {
            backgroundImage = ImageIO.read(new File("src/resources/images/background.jpg"));
            this.setContentPane(new BackgroundPane(backgroundImage));
        } catch (IOException ex) {
            System.out.println("loginBackground.png not found");
        }
    }

    /**
     * Inizializza la gui e il game.
     */
    private void init(List<PlayerInfo> players) {

        // Image fading out
        fadeOutLabel = new FadeOutLabel(this);
        fadeOutLabel.setOpaque(true);
        fadeOutLabel.setBounds(334, 233, 332, 46);
        mapLayeredPane.add(fadeOutLabel, 1000);

        // Labels
        initLabels();
        ((GraphicsJLabel) labelMap).setCountryLabel(countryLabelMap);
        mapLayeredPane.setComponentZOrder(labelMap, mapLayeredPane.getComponentCount() - 1);
        textAreaInfo.setText("Clicca su un tuo territorio per rinforzarlo con 1 armata");
        playerLabel.setFont(new Font("Calibri", Font.BOLD, 24));
        phaseLabel.setFont(new Font("Calibri", Font.BOLD, 24));
        buildLabelPlayers(players);

        // Mouse Listeners
        labelMapListener = new LabelMapListener(labelMap, colorCountryNameMap, this);
        labelMap.addMouseListener(labelMapListener);
        labelMap.addMouseMotionListener(labelMapListener);

        // Game
        game = (GameProxy) Proxy.newProxyInstance(GameProxy.class.getClassLoader(),
                new Class<?>[]{GameProxy.class},
                new GameInvocationHandler(new Game(players, this)));

        labelMapListener.setGame(game);

        // Dialogs
        defenseDialog = new DefenseDialog(game, this, true);
        attackerDialog = new AttackerDialog(game, this, true);
        attackerDialog.setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
        diceDialog = new DiceDialog(game, this, true);

        // CardPanel
        cardPanel = new CardPanel(game);
        this.add(cardPanel, 0);
        cardPanel.setBounds(10, 530, 1200, 300);
        cardPanel.setOpaque(false);
        // Setting
        Dimension dim = getToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getWidth() / 2, dim.height / 2 - this.getHeight() / 2);

    }

    private void buildLabelPlayers(List<PlayerInfo> players) {
        JLabel[] labelPlayers = {labelPlayer1, labelPlayer2, labelPlayer3, labelPlayer4, labelPlayer5, labelPlayer6};
        for (int i = 0; i < 6; i++) {
            if (i < players.size()) {
                labelPlayers[i].setFont(new Font("Serif", Font.BOLD, 18));
                labelPlayers[i].setText(players.get(i).getName());
                labelPlayers[i].setForeground(DefaultColor.valueOf(players.get(i).getColor().toUpperCase()).getColor());
            } else {
                labelPlayers[i].setVisible(false);
            }
        }
    }

    /**
     * Creates an HashMap which maps a <code>java.awt.Color</code> to a country.
     */
    private void initColorCountryNameMap() {
        List<Map<String, Object>> countriesColors = FileManager.getInstance().getCountriesColors();
        colorCountryNameMap = new HashMap<>();
        Color color;
        int R, G, B;
        String country;
        for (Map<String, Object> row : countriesColors) {
            country = (String) row.get("country");
            R = (Integer) row.get("R");
            G = (Integer) row.get("G");
            B = (Integer) row.get("B");
            color = new Color(R, G, B);
            colorCountryNameMap.put(color, country);
        }
    }

    /**
     * Initializes the JLabels.
     *
     * @param src
     */
    private void initLabels() {

        List<Map<String, Object>> labels = FileManager.getInstance().getLabelsProperties();
        String country;
        int x, y;
        for (Map<String, Object> label : labels) {
            country = (String) label.get("country");
            x = (Integer) label.get("x");
            y = (Integer) label.get("y");
            createLabel(country, x, y);
        }
    }

    /**
     * Creates a label for the country <code>countryName</code> and adds it to
     * countryLabelMap.
     *
     * @param countryName
     * @param x
     * @param y
     */
    private void createLabel(String countryName, int x, int y) {

        JLabel label = new JLabel();
        label.setFont(new Font("Serif", Font.BOLD, 16));
        label.setBounds(x, y, 30, 30);
        mapLayeredPane.add(label);
        mapLayeredPane.setComponentZOrder(label, 1);
        countryLabelMap.put(countryName, label);
    }

    /**
     * Returns the JLabel that corresponds to the Country which name is
     * <code>country</code>.
     *
     * @param country
     * @return
     */
    public JLabel getLabelByCountry(String country) {
        return countryLabelMap.get(country);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        phaseLabel = new javax.swing.JLabel();
        buttonNextPhase = new javax.swing.JButton();
        buttonShowMission = new javax.swing.JButton();
        mapLayeredPane = new javax.swing.JLayeredPane();
        labelMap = new GraphicsJLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textAreaInfo = new javax.swing.JTextArea();
        playerLabel = new javax.swing.JLabel();
        exitButton = new javax.swing.JButton();
        showCardButton = new javax.swing.JButton();
        labelPlayer6 = new javax.swing.JLabel();
        labelPlayer5 = new javax.swing.JLabel();
        labelPlayer4 = new javax.swing.JLabel();
        labelPlayer3 = new javax.swing.JLabel();
        labelPlayer2 = new javax.swing.JLabel();
        labelPlayer1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        settingsItem = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        phaseLabel.setBackground(new java.awt.Color(225, 207, 218));
        phaseLabel.setForeground(new java.awt.Color(1, 1, 1));
        phaseLabel.setFont(new Font("Calibri", Font.BOLD, 24));

        buttonNextPhase.setText("Cambia fase");
        buttonNextPhase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonNextPhaseActionPerformed(evt);
            }
        });

        buttonShowMission.setText("La mia missione");
        buttonShowMission.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonShowMissionActionPerformed(evt);
            }
        });

        mapLayeredPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        labelMap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/risiko.png"))); // NOI18N

        mapLayeredPane.setLayer(labelMap, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout mapLayeredPaneLayout = new javax.swing.GroupLayout(mapLayeredPane);
        mapLayeredPane.setLayout(mapLayeredPaneLayout);
        mapLayeredPaneLayout.setHorizontalGroup(
            mapLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mapLayeredPaneLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(labelMap))
        );
        mapLayeredPaneLayout.setVerticalGroup(
            mapLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mapLayeredPaneLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(labelMap))
        );

        textAreaInfo.setColumns(18);
        textAreaInfo.setRows(3);
        jScrollPane1.setViewportView(textAreaInfo);
        textAreaInfo.setForeground(Color.white);
        textAreaInfo.setLineWrap(true);
        textAreaInfo.setWrapStyleWord(true);
        textAreaInfo.setOpaque(false);
        jScrollPane1.setOpaque(false);
        jScrollPane1.getViewport().setOpaque(false);
        textAreaInfo.setFont(new Font("Serif", Font.BOLD, 16));
        textAreaInfo.setEditable(false);

        playerLabel.setBackground(new java.awt.Color(225, 207, 218));
        playerLabel.setForeground(new java.awt.Color(1, 1, 1));
        phaseLabel.setFont(new Font("Serif", Font.BOLD, 24));

        exitButton.setText("Termina partita");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        showCardButton.setText("Mostra/Nascondi carte");
        showCardButton.setVisible(false);
        showCardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showCardButtonActionPerformed(evt);
            }
        });

        labelPlayer6.setText("jLabel1");

        labelPlayer5.setText("jLabel2");

        labelPlayer4.setText("jLabel3");

        labelPlayer3.setText("jLabel4");

        labelPlayer2.setText("jLabel5");

        labelPlayer1.setText("jLabel6");

        jMenu1.setText("Settings");

        settingsItem.setText("AISettings");
        settingsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsItemActionPerformed(evt);
            }
        });
        jMenu1.add(settingsItem);

        jMenuItem1.setText("abbandona");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mapLayeredPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(phaseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(playerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(buttonShowMission, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(buttonNextPhase, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(showCardButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(labelPlayer3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(labelPlayer2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(labelPlayer1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(labelPlayer6, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelPlayer5, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(labelPlayer4, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mapLayeredPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(phaseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(playerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelPlayer1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(labelPlayer2, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelPlayer3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(labelPlayer4, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(buttonShowMission, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelPlayer5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(labelPlayer6))
                            .addComponent(buttonNextPhase, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addComponent(showCardButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    //----------------------- Event controllers ------------------------------//
    /**
     *
     *
     * @param evt
     */
    private void buttonNextPhaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonNextPhaseActionPerformed
        try {
            game.nextPhase();
        } catch (PendingOperationsException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }//GEN-LAST:event_buttonNextPhaseActionPerformed

    /**
     * Shows active player's mission.
     *
     * @param evt
     */
    private void buttonShowMissionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonShowMissionActionPerformed
        JOptionPane.showMessageDialog(null, game.getActivePlayerMission());
    }//GEN-LAST:event_buttonShowMissionActionPerformed

    /**
     * Shows the JDialog that can be used to change artificial players'
     * settings.
     *
     * @param evt
     */
    private void settingsItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsItemActionPerformed
        SettingsDialog settings = new SettingsDialog(this, true, game);
        settings.setVisible(true);
    }//GEN-LAST:event_settingsItemActionPerformed

    /**
     * Ends the game.
     *
     * @param evt
     */
    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        try {
            if (JOptionPane.showConfirmDialog(this, FileManager.getInstance().getInfoFor("ASK_END", LANG)) == 0) {
                game.endGame();
            }
        } catch (FileManagerException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_exitButtonActionPerformed

    /**
     * Shows <code>cardPanel</code>
     *
     * @param evt
     */
    private void showCardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showCardButtonActionPerformed

        if (cardPanel.getY() == cardPanel.getLOW_Y()) {
            cardPanel.moveTo(cardPanel.getHIGH_Y());
        } else {
            cardPanel.moveTo(cardPanel.getLOW_Y());
        }
    }//GEN-LAST:event_showCardButtonActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        game.toArtificialPlayer();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

//----------------------------- Update ---------------------------------------//
    /**
     * Updates <code> textAreaInfo</code> right after a country has been
     * reinforced.
     *
     * @param bonusArmies
     */
    @Override
    public void updateOnReinforce(int bonusArmies) {
        try {
            String info = FileManager.getInstance().getInfoFor("REINFORCE", LANG);
            info = info.replace("?", String.valueOf(bonusArmies));
            textAreaInfo.setText(info);
        } catch (FileManagerException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Shows a fading image with the name of the current phase. Updates
     * <code>textAreaInfo</code> accordingly.
     *
     * @param player
     * @param phase
     */
    @Override
    public void updateOnPhaseChange(PlayerInfo player, String phase) {
        ((GraphicsJLabel) labelMap).resetCone();
        fadeOutLabel.setImage("src/resources/images/" + phase + ".png");
        fadeOutLabel.setVisible(true);
        this.mapLayeredPane.moveToFront(fadeOutLabel);
        fadeOutLabel.startFadeOut();
        updateLabels(player, phase);
        updateTextAreaInfo(player, phase);
        labelMapListener.resetCache();

        switch (phase) {
            case "PLAY_CARDS":
                buttonNextPhase.setVisible(true);
                buttonNextPhase.setText("Niente Tris");
                break;
            case "REINFORCE":
                buttonNextPhase.setVisible(false);
                break;
            case "FIGHT":
                buttonNextPhase.setVisible(true);
                buttonNextPhase.setText("Stop attacchi");
                break;
            case "MOVE":
                buttonNextPhase.setText("Passa il turno");
        }
        
    }

    /**
     * Updates <code>phaseLabel</code> showing the current phase and
     * <code>playerLabel</code> showing the name of the active player.
     *
     * @param player
     * @param phase
     */
    private void updateLabels(PlayerInfo player, String phase) {
        try {
            this.phaseLabel.setText(Translator.getInstance().translate("phase", LANG, false) + " " + getFormattedPhase(phase));
        } catch (TranslationException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.playerLabel.setText(player.getName());
        this.phaseLabel.setForeground(DefaultColor.valueOf(player.getColor().toUpperCase()).getColor());
        this.playerLabel.setForeground(DefaultColor.valueOf(player.getColor().toUpperCase()).getColor());
    }

    /**
     * Updates <code>textAreaInfo</code> and shows a proper message for the
     * current phase.
     *
     * @param player
     * @param phase
     */
    private void updateTextAreaInfo(PlayerInfo player, String phase) {
        try {
            String info = FileManager.getInstance().getInfoFor(phase, LANG);
            if (phase.equals("REINFORCE")) {
                info = info.replace("?", String.valueOf(player.getBonusArmies()));
            }
            textAreaInfo.setText(info);
        } catch (FileManagerException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Updates <code>textAreaInfo</code> as soon as the attacker has chosen from
     * which country to launch an attack.
     *
     * @param attackerInfo
     */
    @Override
    public void updateOnSetAttacker(CountryInfo attackerInfo) {

        ((GraphicsJLabel) labelMap).resetCone();
        labelMapListener.resetCache();

        if (attackerInfo == null) {
            textAreaInfo.setText("Clicca su un tuo territorio per sceglierlo come attaccante");
            return;
        }

        textAreaInfo.setText("Clicca su un territorio nemico confinante per attaccarlo");
        attackerDialog.setMaxArmies(attackerInfo.getMaxArmies());
    }

    /**
     * Updates <code>textAreaInfo</code> as soon as the attacker has chosen
     * which country to attack.
     *
     * @param fightingCountries
     * @param reattack
     */
    @Override
    public void updateOnSetDefender(CountryInfo[] fightingCountries, boolean reattack) {

        CountryInfo attacker = fightingCountries[0];
        CountryInfo defender = fightingCountries[1];

        ((GraphicsJLabel) labelMap).drawCone(attacker.getName(), defender.getName());
        if (fightingCountries[1].getName() == null) {
            textAreaInfo.setText("Clicca su un tuo territorio per sceglierlo come attaccante");
        }

        defenseDialog.setMaxArmies(fightingCountries[1].getMaxArmies());
        defenseDialog.setMaxArmies(fightingCountries[1].getMaxArmies());
        defenseDialog.setFightingLabels(attacker.getName(), attacker.getPlayerColor(), defender.getName(), defender.getPlayerColor());
        attackerDialog.setFightingLabels(attacker.getName(), attacker.getPlayerColor(), defender.getName(), defender.getPlayerColor());
        diceDialog.setFightingLabels(attacker.getName(), attacker.getPlayerColor(), defender.getName(), defender.getPlayerColor());

        if (reattack) {
            this.attackerDialog.setVisible(true);
        }
        repaint(textAreaInfo);
    }

    /**
     * Updates <code>textAreaInfo</code> as soon as the active player has chosen
     * from which country to move its armies.
     *
     * @param countryName
     */
    @Override
    public void updateOnSetFromCountry(String countryName) {
        this.labelMapListener.resetCache();
        if (countryName != null) {
            textAreaInfo.setText("Clicca su un tuo territorio confinante per sceglierlo come destinazione");
        } else {
            textAreaInfo.setText("Clicca su un tuo territorio da cui vuoi spostare una o più armate");
        }
    }

    /**
     * Shows the information of an attack result.
     *
     * @param ar
     */
    @Override
    public void updateOnAttackResult(AttackResultInfo ar) {
        if (!ar.areBothArtificial()) {
            showDiceDialog(ar);
        }

        if (ar.hasConquered() && !ar.isAttackerArtificial()) {
            showCongratsForConquest(ar);
        }

        if (!ar.hasConquered()) {
            defenseDialog.setMaxArmies(ar.getMaxArmiesDefender());
        }

        if (ar.hasConquered() || !ar.canAttackFromCountry()) {
            labelMapListener.resetCache();
        }
    }

    /**
     * Sets and shows <code>DiceDialog</code>.
     *
     * @param ar
     */
    private void showDiceDialog(AttackResultInfo ar) {
        diceDialog.setArtificialAttacker(ar.isAttackerArtificial());
        diceDialog.setIsConquered(ar.hasConquered());
        diceDialog.setCanAttackFromCountry(ar.canAttackFromCountry());
        diceDialog.updateDice(ar.getDice()[0], ar.getDice()[1]);
        diceDialog.showDice();
        diceDialog.setVisible(true);
    }

    /**
     * Shows a dialog with a congrats message for the conquest. This dialog can
     * be used to move armies from the attacker's country to the country that's
     * just been conquered.
     *
     * @param ar
     */
    private void showCongratsForConquest(AttackResultInfo ar) {
        String info = "Complimenti, hai conquistato " + ar.getDefenderCountryName() + ".\n";
        if (ar.getConqueredContinent() != null) {
            info += "Ora possiedi " + ar.getConqueredContinent();
        }
        
        MoveDialog moveDialog = new MoveDialog(game, ar.getAttackerCountryName(), ar.getDefenderCountryName(), info, ar.getMaxArmiesAttacker());
        moveDialog.setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
        PlayAudio.play("src/resources/sounds/conquest.wav");
        moveDialog.setVisible(true);
    }

    /**
     * Removes the dark cones and resets <code>textAreaInfo</code>.
     */
    public void resetAfterAttack() {
        repaint(textAreaInfo);
        ((GraphicsJLabel) labelMap).resetCone();
    }

    /**
     * Shows a congrats message for the victory and closes the game.
     *
     * @param winMessage
     */
    @Override
    public void updateOnVictory(String winMessage) {
        JOptionPane.showMessageDialog(null, winMessage);
        this.dispose();
        System.exit(0);
        // etc
        // bisogna bloccare i run dei giocatori artificiali
    }

    /**
     * Updates the coutries' JLabels after the initial assignment.
     *
     * @param countriesInfo
     */
    @Override
    public void updateOnCountriesAssignment(CountryInfo[] countriesInfo) {
        for (CountryInfo country : countriesInfo) {
            updateOnArmiesChange(country);
        }
    }

    /**
     * Updates the JLabel that belongs to <code>countryInfo</code> with the new
     * number of armies on that country.
     */
    @Override
    public void updateOnArmiesChange(CountryInfo countryInfo) {

        JLabel label = countryLabelMap.get(countryInfo.getName());
        label.setForeground(Color.WHITE);
        label.setText(Integer.toString(countryInfo.getArmies()));
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setIcon(new ImageIcon("src/resources/images/labelCountry/" + countryInfo.getPlayerColor() + "label1.png"));

        repaint(label);
    }

    /**
     * Shows the card panel.
     */
    @Override
    public void updateOnNextTurn(List<String> cards) {
        if (!cards.isEmpty()) {
            showCardButton.setVisible(true);
        }
        cardPanel.setCards(cards);
        cardPanel.moveTo(cardPanel.getHIGH_Y());
    }

    /**
     * Shows a congrats message and the card that has just been drawn.
     *
     *
     * @param cardName
     */
    @Override
    public void updateOnDrawnCard(String cardName, boolean isArtificialPlayer) {
        if (!isArtificialPlayer) {
            JOptionPane.showMessageDialog(null, "Complimenti,\nhai pescato questa carta.", null,
                    JOptionPane.INFORMATION_MESSAGE, new ImageIcon("src/resources/images/" + cardName + ".png"));
        }
    }

    /**
     * Hides the cards panel.
     */
    @Override
    public void updateOnPlayedTris() {
        cardPanel.moveTo(cardPanel.getLOW_Y());
        showCardButton.setVisible(false);
    }

    /**
     * Shows the dialog that asks the defender the number of armies to set in
     * defense. (If it isn't an artificial player)
     *
     * @param defenderCountryInfo
     */
    @Override
    public void updateOnDefend(CountryInfo defenderCountryInfo) {
        if (!defenderCountryInfo.hasArtificialOwner()) {
            this.defenseDialog.setVisible(true);
        }
    }

    /**
     * Shows a message for the player that has just been removed from the game.
     * (if it(?) isn't an artificial player)
     *
     * @param defenderName
     * @param artificialAttack
     */
    @Override
    public void updateOnElimination(String defenderName, boolean artificialAttack) {
        if (!artificialAttack) {
            JOptionPane.showMessageDialog(null, defenderName + " sei stato eliminato dal gioco");
        }

    }

    /**
     * Hides the gui and shows StartGameGUI().
     */
    @Override
    public void updateOnEndGame() {
        this.setVisible(false);
        StartGameGUI startGui = new StartGameGUI();
        startGui.setVisible(true);
    }

    /**
     * Returns the name of the attacker country.
     *
     * @return
     */
    public String getAttackerCountry() {
        return game.getAttackerCountryName();
    }

    /**
     * Sets <code>attackerDialog</code>'s visible property to
     * <code>visible</code>.
     *
     * @param visible
     */
    public void setAttackerDialogVisible(boolean visible) {
        attackerDialog.setVisible(visible);
    }

    /**
     * Returns the translation (in the language specified in <code>LANG</code>)
     * of the phase.
     *
     * @param phase
     * @return
     */
    private String getFormattedPhase(String phase) {
        try {
            return Translator.getInstance().translate(phase, LANG, false);
        } catch (TranslationException ex) {
            return "";
        }
    }

    /**
     * Calls component.repaint() on the Components passed as parameters of the
     * method.
     *
     * @param components
     */
    private void repaint(Component... components) {
        for (Component c : components) {
            c.repaint();
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonNextPhase;
    private javax.swing.JButton buttonShowMission;
    private javax.swing.JButton exitButton;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelMap;
    private javax.swing.JLabel labelPlayer1;
    private javax.swing.JLabel labelPlayer2;
    private javax.swing.JLabel labelPlayer3;
    private javax.swing.JLabel labelPlayer4;
    private javax.swing.JLabel labelPlayer5;
    private javax.swing.JLabel labelPlayer6;
    private javax.swing.JLayeredPane mapLayeredPane;
    private javax.swing.JLabel phaseLabel;
    private javax.swing.JLabel playerLabel;
    private javax.swing.JMenuItem settingsItem;
    private javax.swing.JButton showCardButton;
    private javax.swing.JTextArea textAreaInfo;
    // End of variables declaration//GEN-END:variables

    public void moveToBack() {
        mapLayeredPane.moveToBack(fadeOutLabel);
    }

}
