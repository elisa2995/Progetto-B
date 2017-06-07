/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Dimension;
import java.awt.Frame;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import risiko.game.Game;
import risiko.game.GameProxy;

/**
 *
 * @author Elisa
 */
public class DiceDialog extends javax.swing.JDialog {

    private GameProxy game;
    private int[] attackerDice;
    private int[] defenderDice;
    private JLabel[] diceR;
    private JLabel[] diceB;
    private JLabel[] cones;
    private String defenderCountryName;
    private boolean canAttackFromCountry;
    private boolean isConquered;
    private String attackerCountryName;
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
     * Inizializza le componenti
     */
    private void init() {
        diceR = new JLabel[]{dice1R, dice2R, dice3R};
        diceB = new JLabel[]{dice1B, dice2B, dice3B};
        cones = new JLabel[]{cone1, cone2, cone3};
        attackerDice = new int[]{};
        defenderDice = new int[]{};
        Dimension dim = getToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getWidth() / 2, dim.height / 2 - this.getHeight() / 2);
    }

    /**
     * Setta i territori che prendono parte nel combattimento e il risultato dei
     * dadi
     */
    public void showDice() {
        String imagePath = "files/images/dice/";
        ImageIcon icon;

        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        resetLabels();

        defenderLabel.setText(getDefenderCountryName());
        attackerLabel.setText(getAttackerCountryName());

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
     * Resetta le icone dei dadi e i coni
     */
    private void resetLabels() {
        for (int i = 0; i < diceR.length; i++) {
            diceR[i].setIcon(null);
            diceB[i].setIcon(null);
            cones[i].setIcon(null);
        }

    }


    /**
     * Setta se il territorio difensore è stato conquistato
     *
     * @param isConquered
     */
    public void setIsConquered(boolean isConquered) {
        this.isConquered = isConquered;
    }

    /**
     * Setta se l'attacante può continuare ad attaccare dal territorio dal quale
     * ha già attaccato
     *
     * @param canAttackFromCountry
     */
    public void setCanAttackFromCountry(boolean canAttackFromCountry) {
        this.canAttackFromCountry = canAttackFromCountry;
    }

    /**
     * Setta il risultato del lancio dei dadi
     *
     * @param attackerDice
     * @param defenderDice
     */
    public void updateDice(int[] attackerDice, int[] defenderDice) {
        this.attackerDice = attackerDice;
        this.defenderDice = defenderDice;
    }

    /**
     * Setta il nome del territorio difensore
     *
     * @param defenderCountryName
     */
    public void setDefenderCountryName(String defenderCountryName) {
        this.defenderCountryName = defenderCountryName;
    }

    /**
     * Ridà il nome del territorio difensore
     *
     * @return
     */
    private String getDefenderCountryName() {
        return defenderCountryName;
    }

    /**
     * Ridà il nome dello stato attaccante
     *
     * @return
     */
    private String getAttackerCountryName() {
        return attackerCountryName;
    }

    /**
     * Setta il nome dello stato attaccante
     *
     * @param attackerCountryName
     */
    public void setAttackerCountryName(String attackerCountryName) {
        this.attackerCountryName = attackerCountryName;
    }

    /**
     * Setta se l'attaccante è un giocatore artificiale
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

        attackerLabel = new javax.swing.JLabel();
        defenderLabel = new javax.swing.JLabel();
        cone3 = new javax.swing.JLabel();
        cone2 = new javax.swing.JLabel();
        dice1B = new javax.swing.JLabel();
        dice3B = new javax.swing.JLabel();
        dice2B = new javax.swing.JLabel();
        dice1R = new javax.swing.JLabel();
        dice3R = new javax.swing.JLabel();
        dice2R = new javax.swing.JLabel();
        cone1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        exit = new javax.swing.JButton();
        reattack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Attaccante:");

        jLabel2.setText("Difensore:");

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
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(attackerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(dice1R, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(dice3R, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(dice2R, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(cone2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                                    .addComponent(cone1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cone3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(defenderLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(dice3B, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(dice2B, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(dice1B, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(exit, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 293, Short.MAX_VALUE)
                        .addComponent(reattack, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(48, 48, 48))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(attackerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(defenderLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                        .addGap(15, 15, 15)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exit, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reattack, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Chiude il dialog e resetta i territori coinvolti nel combattimento
     *
     * @param evt
     */
    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        game.resetFightingCountries();
        gui.resetAfterAttack();
        this.setVisible(false);
    }//GEN-LAST:event_exitActionPerformed

    /**
     * Fa partire un nuovo attacco con gli stessi territori coinvolti
     * nell'ultimo attacco
     *
     * @param evt
     */
    private void reattackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reattackActionPerformed
        this.setVisible(false);
        game.setAttackerCountry(attackerCountryName);
        game.setReattack(true);
        game.setDefenderCountry(defenderCountryName);
        game.setReattack(false);
    }//GEN-LAST:event_reattackActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel attackerLabel;
    private javax.swing.JLabel cone1;
    private javax.swing.JLabel cone2;
    private javax.swing.JLabel cone3;
    private javax.swing.JLabel defenderLabel;
    private javax.swing.JLabel dice1B;
    private javax.swing.JLabel dice1R;
    private javax.swing.JLabel dice2B;
    private javax.swing.JLabel dice2R;
    private javax.swing.JLabel dice3B;
    private javax.swing.JLabel dice3R;
    private javax.swing.JButton exit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton reattack;
    // End of variables declaration//GEN-END:variables

}
