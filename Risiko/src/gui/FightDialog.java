/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import risiko.Game;

/**
 *
 * @author Elisa
 */
public class FightDialog extends javax.swing.JDialog {

    private Game game;
    private boolean isConquered;
    private boolean canAttackFromCountry;
    private int maxArmiesAttacker;
    private int[] attackerDice;
    private int[] defenderDice;
    private String defenderCountryName;
    private int maxArmiesDefender;
    private String drawnCard;

    public FightDialog(Game game, java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.game = game;
        initComponents();
        init();
    }

    /**
     * Inizializzazione
     */
    private void init() {

        FightDialog inputArmies = this;
        attackerArmies.setModel(new SpinnerNumberModel(1, 1, 1, 1));
        defenderArmies.setModel(new SpinnerNumberModel(1, 1, 1, 1));

        JLabel[] diceR = new JLabel[]{dice1R, dice2R, dice3R};
        JLabel[] diceB = new JLabel[]{dice1B, dice2B, dice3B};
        JLabel[] cones = new JLabel[]{cone1, cone2, cone3};
        attackerDice = new int[]{};
        defenderDice = new int[]{};

        execute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                resetLabels();
                String imagePath = "files/images/dice/";
                PlayAudio.play("sounds/tank.wav");

                //game.attack() potrebbe impostare a true il valore ritornante da game.haveJustDrowCard()
                //quindi occorre salvare il valore ritornante da game.haveJustDrowCard() prima di invocare game.attack() 
                //useremo la variabile haveJustDrowCard nel caso isConquered fosse true.
                boolean hasAlreadyDrawnCard = game.hasAlreadyDrawnCard();

                game.attack((int) attackerArmies.getValue(), (int) defenderArmies.getValue());
                ImageIcon icon;
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
                    icon = (defenderDice[i] >= attackerDice[i]) ? new ImageIcon(imagePath + "DefenderCone.png") : new ImageIcon(imagePath + "AttackerCone.png");
                    cones[i].setIcon(icon);
                }

                if (isConquered) {
                    PlayAudio.play("sounds/conquest.wav");

                    //Informa il player della conquista del nuovo territorio, 
                    //nel caso fosse la prima conquista del turno informa anche il player della cardBonus pescata
                    if (hasAlreadyDrawnCard) {
                        JOptionPane.showMessageDialog(null, "Complimenti, hai conquistato " + getDefenderCountryName());
                    } else {
                        JOptionPane.showMessageDialog(null, "Complimenti,\nhai conquistato " + getDefenderCountryName()
                                + ",\ne pescato questa carta.", "Conquered",
                                JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/" + drawnCard + ".png"));
                    }
                    inputArmies.setVisible(false);
                    resetLabels();
                    return;
                }
                if (!canAttackFromCountry) {
                    JOptionPane.showMessageDialog(null, "Non è più possibile effettuare attacchi da questo territorio.");
                    inputArmies.setVisible(false);
                    resetLabels();
                    return;
                }
                // Seconda volta in cui attacco 
                attackerArmies.setModel(new SpinnerNumberModel(maxArmiesAttacker, 1, maxArmiesAttacker, 1));
                defenderArmies.setModel(new SpinnerNumberModel(maxArmiesDefender, 1, maxArmiesDefender, 1));
            }

            private void resetLabels() {
                for (int i = 0; i < diceR.length; i++) {
                    diceR[i].setIcon(null);
                    diceB[i].setIcon(null);
                    cones[i].setIcon(null);
                }

            }

        });

    }

    /**
     * Setta il massimo numero di armate che attaccante e difesore possono
     * scegliere
     *
     * @param maxArmiesAttacker
     * @param maxArmiesDefender
     */
    public void setMaxArmies(int maxArmiesAttacker, int maxArmiesDefender) {
        attackerArmies.setModel(new SpinnerNumberModel(maxArmiesAttacker, 1, maxArmiesAttacker, 1));
        defenderArmies.setModel(new SpinnerNumberModel(maxArmiesDefender, 1, maxArmiesDefender, 1));
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
     * Setta il massimo numero di armate che può scegliere l'attaccante
     *
     * @param maxArmiesAttacker
     */
    public void setMaxArmiesAttacker(int maxArmiesAttacker) {
        this.maxArmiesAttacker = maxArmiesAttacker;
    }

    /**
     * Setta il massimo numero di armate che può scegliere il difensore
     *
     * @param maxArmiesDefender
     */
    public void setMaxArmiesDefender(int maxArmiesDefender) {
        this.maxArmiesDefender = maxArmiesDefender;
    }

    /**
     * Setta la carta appena pescata.
     *
     * @param card
     */
    public void setDrawnCard(String card) {
        this.drawnCard = card;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        attackerArmies = new javax.swing.JSpinner();
        defenderArmies = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        execute = new javax.swing.JButton();
        dice1R = new javax.swing.JLabel();
        dice3R = new javax.swing.JLabel();
        dice2R = new javax.swing.JLabel();
        cone1 = new javax.swing.JLabel();
        cone3 = new javax.swing.JLabel();
        cone2 = new javax.swing.JLabel();
        dice1B = new javax.swing.JLabel();
        dice3B = new javax.swing.JLabel();
        dice2B = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Armate attacco");

        jLabel2.setText("Armate difesa");

        execute.setText("Esegui attacco");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(dice1R, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dice3R, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                            .addComponent(dice2R, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cone2, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                            .addComponent(cone1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cone3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dice3B, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(dice2B, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                                .addComponent(dice1B, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(attackerArmies, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(defenderArmies, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(25, 25, 25))))
            .addGroup(layout.createSequentialGroup()
                .addGap(188, 188, 188)
                .addComponent(execute, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 206, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(attackerArmies, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(defenderArmies, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(execute, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dice1R, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dice1B, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dice2R, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dice2B, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
                        .addGap(18, 18, 18))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(cone1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(87, 87, 87)
                        .addComponent(cone2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(dice3R, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dice3B, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(cone3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(42, 42, 42))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner attackerArmies;
    private javax.swing.JLabel cone1;
    private javax.swing.JLabel cone2;
    private javax.swing.JLabel cone3;
    private javax.swing.JSpinner defenderArmies;
    private javax.swing.JLabel dice1B;
    private javax.swing.JLabel dice1R;
    private javax.swing.JLabel dice2B;
    private javax.swing.JLabel dice2R;
    private javax.swing.JLabel dice3B;
    private javax.swing.JLabel dice3R;
    private javax.swing.JButton execute;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
