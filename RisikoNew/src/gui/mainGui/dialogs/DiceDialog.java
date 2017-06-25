package gui.mainGui.dialogs;

import gui.DefaultColor;
import gui.mainGui.GUI;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import risiko.game.GameProxy;

/**
 * JDialog that shows the dice that have just been rolled.
 */
public class DiceDialog extends javax.swing.JDialog {

    private GameProxy game;
    private int[] attackerDice;
    private int[] defenderDice;
    private JLabel[] diceR;
    private JLabel[] diceB;
    private JLabel[] cones;
    private boolean canAttackFromCountry;
    private boolean isConquered;
    private boolean artificialAttacker;
    private final GUI gui;

    /**
     * Creates new form DiceDialog
     *
     * @param game
     * @param parent
     * @param modal
     */
    public DiceDialog(GameProxy game, GUI parent, boolean modal) {
        super(parent, modal);
        this.game = game;
        this.gui = parent;
        initComponents();
        init();
    }

    /**
     * Initialization.
     */
    private void init() {
        diceR = new JLabel[]{dice1R, dice2R, dice3R};
        diceB = new JLabel[]{dice1B, dice2B, dice3B};
        cones = new JLabel[]{cone1, cone2, cone3};
        attackerDice = new int[]{};
        defenderDice = new int[]{};
        Dimension dim = getToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getWidth() / 2, dim.height / 2 - this.getHeight() / 2);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeDialog();
            }

        });
    }

    /**
     * Shows the 2 sets of dice. For each couple of dice it also shows a
     * triangle with its base towards the winner.
     */
    public void showDice() {
        String imagePath = "src/resources/images/dice/";
        ImageIcon icon;

        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        resetLabels();

        for (int i = 0; i < diceR.length; i++) {
            icon = (i < attackerDice.length) ? new ImageIcon(imagePath + attackerDice[i] + "R.png") : null;
            diceR[i].setIcon(icon);
        }
        for (int i = 0; i < diceB.length; i++) {
            icon = (i < defenderDice.length) ? new ImageIcon(imagePath + defenderDice[i] + "B.png") : null;
            diceB[i].setIcon(icon);
        }

        int pairs = Math.min(attackerDice.length, defenderDice.length);
        for (int i = 0; i < pairs; i++) {
            icon = (defenderDice[i] >= attackerDice[i]) ? new ImageIcon(imagePath + "DefenderConeB.png") : new ImageIcon(imagePath + "AttackerConeR.png");
            cones[i].setIcon(icon);
        }

        boolean visible = !(isConquered || !canAttackFromCountry) && !artificialAttacker;

        reattack.setVisible(visible);
    }

    /**
     * Resets the labels of the dice.
     */
    private void resetLabels() {
        for (int i = 0; i < diceR.length; i++) {
            diceR[i].setIcon(null);
            diceB[i].setIcon(null);
            cones[i].setIcon(null);
        }

    }

    /**
     * Sets if the defender country has been conquered.
     *
     * @param isConquered
     */
    public void setIsConquered(boolean isConquered) {
        this.isConquered = isConquered;
    }

    /**
     * Sets if the attacker can keep attacking from the same country of the
     * previous attack.
     *
     *
     * @param canAttackFromCountry
     */
    public void setCanAttackFromCountry(boolean canAttackFromCountry) {
        this.canAttackFromCountry = canAttackFromCountry;
    }

    /**
     * Sets the values of the dice that have just been rolled.
     *
     * @param attackerDice
     * @param defenderDice
     */
    public void updateDice(int[] attackerDice, int[] defenderDice) {
        this.attackerDice = attackerDice;
        this.defenderDice = defenderDice;
    }

    /**
     * Shows the attacker and defender country names in their owner's color.
     *
     * @param attackerCountryName
     * @param attackerColor
     * @param defenderCountryName
     * @param defenderColor
     */
    public void setFightingLabels(String attackerCountryName, String attackerColor, String defenderCountryName, String defenderColor) {
        this.attackerCountryLabel.setText(attackerCountryName);
        this.attackerCountryLabel.setForeground(DefaultColor.valueOf(attackerColor.toUpperCase()).getColor());
        this.defenderCountryLabel.setText(defenderCountryName);
        this.defenderCountryLabel.setForeground(DefaultColor.valueOf(defenderColor.toUpperCase()).getColor());
    }

    /**
     * Sets if the attacker is an artificial player.
     *
     * @param artificialAttacker
     */
    public void setArtificialAttacker(boolean artificialAttacker) {
        this.artificialAttacker = artificialAttacker;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        defenderCountryLabel = new javax.swing.JLabel();
        cone2 = new javax.swing.JLabel();
        attackerCountryLabel = new javax.swing.JLabel();
        dice1B = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        dice3B = new javax.swing.JLabel();
        dice2B = new javax.swing.JLabel();
        dice1R = new javax.swing.JLabel();
        dice3R = new javax.swing.JLabel();
        dice2R = new javax.swing.JLabel();
        cone1 = new javax.swing.JLabel();
        exit = new javax.swing.JButton();
        reattack = new javax.swing.JButton();
        cone3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        defenderCountryLabel.setText("Stati UNiti Occidentali");

        attackerCountryLabel.setText("Stati UNiti Occidentali");

        jLabel4.setText("VS");

        exit.setText("Ritorna al gioco");
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });

        reattack.setText("Attacca ancora");
        reattack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reattackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(dice1R, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dice3R, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dice2R, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cone2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                            .addComponent(cone1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cone3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dice3B, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(dice2B, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(dice1B, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(attackerCountryLabel)
                        .addGap(73, 73, 73)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE)
                        .addComponent(defenderCountryLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(exit, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(reattack, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(attackerCountryLabel)
                    .addComponent(defenderCountryLabel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dice1R, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addComponent(cone1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dice2R, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(cone2, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dice3R, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(cone3, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(dice1B, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(dice2B, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dice3B, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exit, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reattack, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Exits the dialog.
     *
     * @param evt
     */
    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        closeDialog();
    }//GEN-LAST:event_exitActionPerformed

    /**
     * Starts a new attack between the countries of the previous attack.
     *
     * @param evt
     */
    private void reattackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reattackActionPerformed
        this.setVisible(false);
        game.setAttackerCountry(attackerCountryLabel.getText());
        game.setReattack(true);
        game.setDefenderCountry(defenderCountryLabel.getText());
        game.setReattack(false);
    }//GEN-LAST:event_reattackActionPerformed

    /**
     * Closes the dialog.
     */
    private void closeDialog() {
        game.resetFightingCountries();
        gui.resetAfterAttack();
        this.setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel attackerCountryLabel;
    private javax.swing.JLabel cone1;
    private javax.swing.JLabel cone2;
    private javax.swing.JLabel cone3;
    private javax.swing.JLabel defenderCountryLabel;
    private javax.swing.JLabel dice1B;
    private javax.swing.JLabel dice1R;
    private javax.swing.JLabel dice2B;
    private javax.swing.JLabel dice2R;
    private javax.swing.JLabel dice3B;
    private javax.swing.JLabel dice3R;
    private javax.swing.JButton exit;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JButton reattack;
    // End of variables declaration//GEN-END:variables

}
