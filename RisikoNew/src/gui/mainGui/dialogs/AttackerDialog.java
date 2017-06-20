package gui.mainGui.dialogs;

import exceptions.TranslationException;
import gui.DefaultColor;
import gui.PlayAudio;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SpinnerNumberModel;
import risiko.game.GameProxy;
import services.Translator;

/**
 * JDialog in which the attacker can set the number of armies with which he
 * wants to attack.
 *
 */
public class AttackerDialog extends javax.swing.JDialog {

    private final static String LANG = "ITA";
    private GameProxy game;

    /**
     * Creates a new AttackerDialog
     *
     * @param game
     * @param parent
     * @param modal
     */
    public AttackerDialog(GameProxy game, Frame parent, boolean modal) {
        super(parent, modal);
        this.game = game;
        initComponents();
        init();
    }

    /**
     * Initialization
     */
    private void init() {
        Dimension dim = getToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getWidth() / 2, dim.height / 2 - this.getHeight() / 2);
        try {
            this.setTitle(Translator.translate("Attack", LANG, false));
        } catch (TranslationException ex) {
            this.setTitle("");
        }

        AttackerDialog attackerDialog = this;
        attackerArmies.setModel(new SpinnerNumberModel(1, 1, 1, 1));

        declare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlayAudio.play("src/resources/sounds/tank.wav");
                game.setAttackerArmies((int) attackerArmies.getValue());
                attackerDialog.setVisible(false);
                game.declareAttack();
            }
        });

    }

    /**
     * Displays the names of the attacker and defender country, which a color
     * that depends on the owner of that country.
     *
     * @param attackerCountryName
     * @param attackerColor
     * @param defenderCountryName
     * @param defenderColor
     */
    public void setFightingLabels(String attackerCountryName, String attackerColor, String defenderCountryName, String defenderColor) {
        this.attackerCountryName.setText(attackerCountryName);
        this.attackerCountryName.setForeground(DefaultColor.valueOf(attackerColor.toUpperCase()).getColor());
        this.defenderCountryName.setText(defenderCountryName);
        this.defenderCountryName.setForeground(DefaultColor.valueOf(defenderColor.toUpperCase()).getColor());
    }

    /**
     * Sets the maximun number of armies that the attacker can choose
     *
     * @param maxArmiesAttacker
     * @param maxArmiesDefender
     */
    public void setMaxArmies(int maxArmiesAttacker) {
        attackerArmies.setModel(new SpinnerNumberModel(maxArmiesAttacker, 1, maxArmiesAttacker, 1));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        defenderCountryName = new javax.swing.JLabel();
        attackerCountryName = new javax.swing.JLabel();
        attackerArmies = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        declare = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        defenderCountryName.setText("Stati UNiti Occidentali");

        attackerCountryName.setText("Stati UNiti Occidentali");

        jLabel1.setText("Armate attacco");

        declare.setText("Dichiara l'attacco");

        jLabel4.setText("VS");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(attackerCountryName)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(defenderCountryName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(attackerArmies, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(declare, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(attackerCountryName)
                    .addComponent(defenderCountryName)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(attackerArmies, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(declare, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner attackerArmies;
    private javax.swing.JLabel attackerCountryName;
    private javax.swing.JButton declare;
    private javax.swing.JLabel defenderCountryName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
}
