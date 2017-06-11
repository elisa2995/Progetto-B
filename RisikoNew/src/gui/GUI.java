package gui;

import controllers.LabelMapListener;
import utils.GameObserver;
import exceptions.PendingOperationsException;
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
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import risiko.game.Game;
import risiko.game.GameInvocationHandler;
import risiko.game.GameProxy;
import services.FileManager;
import shared.AttackResultInfo;
import shared.CountryInfo;
import shared.PlayerInfo;

/**
 * @author andrea
 */
public class GUI extends JFrame implements GameObserver {

    //private Game game;
    private GameProxy game;
    private Map<Color, String> colorCountryNameMap;
    private final Map<String, JLabel> countryLabelMap;
    private DefenseDialog defenseArmies;
    private CardBonusDialog cardBonusDialog;
    private AttackerDialog attackerDialog;
    private DiceDialog diceDialog;
    private LabelMapListener labelMapListener;
    private final int PREFERRED_WIDTH = 400;
    private final int PREFERRED_HEIGHT = 192;

    public GUI(Map<String, String> players, Map<String, String> playersColor) throws Exception {
        initBackground();
        initComponents();
        labelMap.setIcon(new javax.swing.ImageIcon(ImageIO.read(new File("images/risiko.png"))));
        countryLabelMap = new HashMap<>();
        initColorCountryNameMap();
        init(players, playersColor);
    }

    private void initBackground() {
        BufferedImage backgroundImage;
        try {
            backgroundImage = ImageIO.read(new File("images/background.jpg"));
            this.setContentPane(new BackgroundPane(backgroundImage));
        } catch (IOException ex) {
            System.out.println("loginBackground.png not found");
        }
    }

    /**
     * Inizializza la gui e il game.
     *
     * @throws IOException
     * @throws Exception
     */
    private void init(Map<String, String> players, Map<String, String> playersColor) throws IOException, Exception {
        // Labels
        initLabels();
        ((GraphicsJLabel) labelMap).setCountryLabel(countryLabelMap);
        mapLayeredPane.setComponentZOrder(labelMap, mapLayeredPane.getComponentCount() - 1);
        textAreaInfo.setText("Clicca su un tuo territorio per rinforzarlo con 1 armata");

        playerLabel.setFont(new Font("Calibri", Font.BOLD, 24));
        phaseLabel.setFont(new Font("Calibri", Font.BOLD, 24));

        // Mouse Listeners
        labelMapListener = new LabelMapListener(labelMap, colorCountryNameMap, this);
        labelMap.addMouseListener(labelMapListener);
        labelMap.addMouseMotionListener(labelMapListener);

        // Game
        game = (GameProxy) Proxy.newProxyInstance(GameProxy.class.getClassLoader(),
                new Class<?>[]{GameProxy.class},
                new GameInvocationHandler(new Game(players, playersColor, this)));

        labelMapListener.setGame(game);

        // Dialogs
        defenseArmies = new DefenseDialog(game, this, true);
        attackerDialog = new AttackerDialog(game, this, true);
        attackerDialog.setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
        diceDialog = new DiceDialog(game, this, true);
        cardBonusDialog = new CardBonusDialog(game);

        // Setting
        Dimension dim = getToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getWidth() / 2, dim.height / 2 - this.getHeight() / 2);
    }

    /**
     * Inizializza i labels
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
     * Crea un label e lo aggiunge a <code>countryLabelMap</code>.
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
     * Ritorna la jlabel corrispondente al territorio di nome
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
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        settingsItem = new javax.swing.JMenuItem();

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

        jMenu1.setText("Settings");

        settingsItem.setText("AISettings");
        settingsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsItemActionPerformed(evt);
            }
        });
        jMenu1.add(settingsItem);

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
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(buttonShowMission, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(buttonNextPhase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(playerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(phaseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(playerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonShowMission, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonNextPhase, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(mapLayeredPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Passa alla fase successiva del gioco
     *
     * @param evt
     */
    private void buttonNextPhaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonNextPhaseActionPerformed
        //System.out.println(javax.swing.SwingUtilities.isEventDispatchThread());
        //((GraphicsJLabel) labelMap).resetCone();

        try {
            game.nextPhase();
        } catch (PendingOperationsException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        //repaint();
    }//GEN-LAST:event_buttonNextPhaseActionPerformed

    /**
     * Mostra la missione del giocatore di turno
     *
     * @param evt
     */
    private void buttonShowMissionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonShowMissionActionPerformed
        JOptionPane.showMessageDialog(null, game.getActivePlayerMission());
    }//GEN-LAST:event_buttonShowMissionActionPerformed

    /**
     * Mostra la Dialog per la gestione dei settings del player artificiale
     */
    private void settingsItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsItemActionPerformed
        SettingsDialog settings = new SettingsDialog(this, true, game);
        settings.setVisible(true);
    }//GEN-LAST:event_settingsItemActionPerformed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        if (JOptionPane.showConfirmDialog(this, "Sei sicuro di voler abbandonare la partita?") == 0) {
            game.endGame();
        }

    }//GEN-LAST:event_exitButtonActionPerformed

    /**
     * Creazione di una map<Color,String> a partire da un file di testo
     * contenente un numero a piacere di linee, dove ogni linea contiene un
     * [token] avente la forma: [token] -> [Country] = [RGB] [Country] ->
     * qualsiasi sequenza di caratteri ASCII diversi dal carattere = e
     * interruzioni di linea [RGB] -> [R],[G],[B] [R] -> un numerCountryo intero
     * da 0 a 255 [G] -> un numero intero da 0 a 255 [B] -> un numero intero da
     * 0 a 255.
     */
    private void initColorCountryNameMap() throws FileNotFoundException {

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
     * Aggiorna <code> textAreaInfo</code> e <code>labelAdvice</code> dopo che
     * la country <code>countryName</code> è stata rinforzata.
     *
     * @param bonusArmies
     */
    @Override
    public void updateOnReinforce(int bonusArmies) {
        textAreaInfo.setText("Clicca su un tuo territorio per rinforzarlo con un'armata.\nHai " + bonusArmies + " armate bonus.");
    }

    /**
     * Aggiorna <code>textAreaInfo</code> e <code>labelAdvice</code> quando
     * cambia la fase del gioco.
     *
     * @param player
     * @param phase
     */
    @Override
    public void updateOnPhaseChange(PlayerInfo player, String phase) {
        ((GraphicsJLabel) labelMap).resetCone();

        updateLabels(player, phase);
        updateTextAreaInfo(player, phase);

        labelMapListener.resetCache();

    }

    private void updateLabels(PlayerInfo player, String phase) {
        this.phaseLabel.setText("FASE DI " + getFormattedPhase(phase));
        this.playerLabel.setText(player.getName());
        this.phaseLabel.setForeground(DefaultColor.valueOf(player.getColor().toUpperCase()).getColor());
        this.playerLabel.setForeground(DefaultColor.valueOf(player.getColor().toUpperCase()).getColor());
    }

    private void updateTextAreaInfo(PlayerInfo player, String phase) {

        this.textAreaInfo.setText("");
        switch (phase) {
            case "REINFORCE":
                textAreaInfo.setText("Clicca su un tuo territorio per rinforzarlo con un'armata.\nHai " + player.getBonusArmies() + " armate bonus.");
                break;
            case "FIGHT":
                textAreaInfo.setText("Clicca su un tuo territorio per sceglierlo come attaccante");
                break;
            case "MOVE":
                textAreaInfo.setText("Scegli un tuo territorio da cui spostare una o più armate");
                break;
        }
    }

    /**
     * Aggiorna <code>textAreaInfo</code> e <code>labelAdvice</code> quando è
     * stato scelto il territorio da cui attaccare.
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
        attackerDialog.setAttackerCountry(attackerInfo.getName(), attackerInfo.getPlayerColor());

    }

    /**
     * Aggiorna  <code>textAreaInfo</code> e <code>labelAdvice</code> quando è
     * stato scelto il territorio da attaccare. Aggiorna anche i valori di
     * massimo numero di armate dell'attaccante/difensore per preparare il
     * jspinner dell'AttackDialog.
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

        //ERRORE reattack è true ma maxArmiesDefender è 0.
        defenseArmies.setMaxArmies(fightingCountries[1].getMaxArmies());

        if (reattack) {
            this.attackerDialog.setVisible(true);
        }
        repaint(textAreaInfo);
    }

    /**
     * Aggiorna <code>textAreaInfo</code> e <code>labelAdvice</code> quando è
     * stato scelto il territorio da cui attaccare.
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
     * Shows the information of an attackResult.
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
            defenseArmies.setMaxArmies(ar.getMaxArmiesDefender());
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
        diceDialog.setAttackerCountryName(ar.getAttackerCountryName());
        diceDialog.setArtificialAttacker(ar.isAttackerArtificial());
        diceDialog.setIsConquered(ar.hasConquered());
        diceDialog.setCanAttackFromCountry(ar.canAttackFromCountry());
        diceDialog.setDefenderCountryName(ar.getDefenderCountryName());
        diceDialog.updateDice(ar.getDice()[0], ar.getDice()[1]);
        diceDialog.showDice();
        diceDialog.setVisible(true);
    }

    /**
     * Shows a dialog with a congrats message for the conquest. This dialog can
     * be used to move armies from tbe attacker's country to the country that's
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
        PlayAudio.play("sounds/conquest.wav");
        moveDialog.setVisible(true);
    }

    /**
     * Cancella i coni e resetta le stringhe visualizzate
     */
    public void resetAfterAttack() {
        repaint(textAreaInfo, textAreaInfo);
        ((GraphicsJLabel) labelMap).resetCone();
    }

    /**
     * Mostra un messaggio di vittoria e chiude il gioco
     *
     * @param winner
     */
    @Override
    public void updateOnVictory(String winner) {
        JOptionPane.showMessageDialog(null, "Complimenti " + winner + " hai vinto!");
        this.dispose();
        System.exit(0);
        // etc
        // bisogna bloccare i run dei giocatori artificiali
    }

    /**
     * Updates the coutries' labels after the initial assignment.
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
     * Aggiorna l'etichetta del territorio <code>country</code> quando cambia il
     * numero di armate.
     *
     * @param country
     * @param armies
     * @param color
     */
    @Override
    public void updateOnArmiesChange(CountryInfo countryInfo) {
        
        JLabel label = countryLabelMap.get(countryInfo.getName());
        label.setForeground(Color.WHITE);
        label.setText(Integer.toString(countryInfo.getArmies()));
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setIcon(new ImageIcon("files/images/labelCountry/" + countryInfo.getPlayerColor() + "label1.png"));

        repaint(label);
    }

    /**
     * Mostra la finestra di gestione delle carte
     */
    @Override
    public void updateOnNextTurn() {
        cardBonusDialog.initImagesPanel();
        cardBonusDialog.initButtonPanel();
        cardBonusDialog.setVisible(true);
    }

    /**
     * Setta la carta vinta in <code>diceDialog</code>
     *
     * @param cardName
     */
    @Override
    public void updateOnDrawnCard(String cardName, boolean isArtificialPlayer) {
        if (!isArtificialPlayer) {
            JOptionPane.showMessageDialog(null, "Complimenti,\nhai pescato questa carta.", null,
                    JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/" + cardName + ".png"));
        }
    }

    /**
     * Chiama Component.repaint() sui components passati come parametro del
     * metodo.
     *
     * @param components
     */
    private void repaint(Component... components) {
        for (Component c : components) {
            c.repaint();
        }
    }

    /**
     * Se il giocatore è reale viene richiamata una dialog che chiede al
     * difensore con quante armate difendersi per completare l'attacco
     *
     * @param defenderCountryInfo
     */
    @Override
    public void updateOnDefend(CountryInfo defenderCountryInfo) {
        if (!defenderCountryInfo.hasArtificialOwner()) {
            defenseArmies.setDefenderCountryName(defenderCountryInfo.getName());
            this.defenseArmies.setVisible(true);
        }
    }

    public void setVisibleAttackerDialog(boolean visible) {
        attackerDialog.setVisible(visible);
    }

    public String getAttackerCountry() {
        return game.getAttackerCountryName();
    }

    @Override
    public void updateOnElimination(String defenderName, boolean artificialAttack) {
        if (!artificialAttack) {
            JOptionPane.showMessageDialog(null, defenderName + " sei stato eliminato dal gioco");
        }

    }

    @Override
    public void updateOnEndGame() {
        this.setVisible(false);
        StartGameGUI startGui = new StartGameGUI();
        startGui.setVisible(true);
    }

    private String getFormattedPhase(String phase) {
        switch (phase) {
            case "REINFORCE":
                return "RINFORZO";
            case "FIGHT":
                return "COMBATTIMENTO";
            case "MOVE":
                return "SPOSTAMENTO";
            default:
                return "";
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonNextPhase;
    private javax.swing.JButton buttonShowMission;
    private javax.swing.JButton exitButton;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelMap;
    private javax.swing.JLayeredPane mapLayeredPane;
    private javax.swing.JLabel phaseLabel;
    private javax.swing.JLabel playerLabel;
    private javax.swing.JMenuItem settingsItem;
    private javax.swing.JTextArea textAreaInfo;
    // End of variables declaration//GEN-END:variables

}
