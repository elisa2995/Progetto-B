package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Arrays;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import risiko.Game;
import utils.PlayAudio;

public class AttackDialog extends JDialog {

    private Game game;
    private JPanel dialogPanel;
    private JLabel attackText;
    private JLabel defenseText;
    private SpinnerNumberModel attackerModel;
    private SpinnerNumberModel defenserModel;
    private JSpinner attackerArmies;
    private JSpinner defenderArmies;
    private JButton execute;
    private JLabel dice1R;
    private JLabel dice2R;
    private JLabel dice3R;
    private JLabel dice1B;
    private JLabel dice2B;
    private JLabel dice3B;
    private JLabel emptyLabel;
    private boolean isConquered;
    private boolean canAttackFromCountry;
    private int maxArmiesAttacker;
    private int[] attackerDice;
    private int[] defenderDice;
    private String defenderCountryName;
    private int maxArmiesDefender;

    public AttackDialog(Game game) {
        this.game = game;
        init();
    }

    /**
     * Inizializzazione
     */
    private void init() {

        JDialog inputArmies = this;
        dialogPanel = new JPanel(new GridLayout(0, 3));
        attackText = new JLabel(" n armate attaccco");
        defenseText = new JLabel(" n armate difesa");
        attackerModel = new SpinnerNumberModel(1, 1, 1, 1);
        defenserModel = new SpinnerNumberModel(1, 1, 1, 1);
        attackerArmies = new JSpinner(attackerModel);
        defenderArmies = new JSpinner(defenserModel);
        execute = new JButton("Esegui");
        dice1R = new JLabel();
        dice2R = new JLabel();
        dice3R = new JLabel();
        dice1B = new JLabel();
        dice2B = new JLabel();
        dice3B = new JLabel();
        emptyLabel = new JLabel();
        JLabel[] diceR = new JLabel[]{dice1R, dice2R, dice3R};
        JLabel[] diceB = new JLabel[]{dice1B, dice2B, dice3B};
        attackerDice = new int[]{};
        defenderDice = new int[]{};

        execute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {

                String imagePath = "files/images/dice/";
                PlayAudio.play("sounds/tank.wav");

                game.attack((int) attackerArmies.getValue(), (int) defenderArmies.getValue());
                for (int i = 0; i < diceR.length; i++) {
                    if (i < attackerDice.length) {
                        ImageIcon icon = new ImageIcon(imagePath + attackerDice[i] + "R.png");
                        diceR[i].setIcon(icon);
                    } else {
                        ImageIcon icon = null;
                        diceR[i].setIcon(icon);
                    }
                }
                for (int i = 0; i < diceB.length; i++) {
                    if (i < defenderDice.length) {
                        ImageIcon icon = new ImageIcon(imagePath + defenderDice[i] + "B.png");
                        diceB[i].setIcon(icon);
                    } else {
                        ImageIcon icon = null;
                        diceB[i].setIcon(icon);
                    }
                }

                if (isConquered) {
                    PlayAudio.play("sounds/conquest.wav");
                    JOptionPane.showMessageDialog(null, "Complimenti, hai conquistato " + getDefenderCountryName());
                    inputArmies.setVisible(false);
                    for (int i = 0; i < diceR.length; i++) {
                        diceR[i].setIcon(null);
                        diceB[i].setIcon(null);
                    }
                    return;
                }
                if (!canAttackFromCountry) {
                    JOptionPane.showMessageDialog(null, "Non è più possibile effettuare attacchi da questo territorio.");
                    inputArmies.setVisible(false);
                    for (int i = 0; i < diceR.length; i++) {
                        diceR[i].setIcon(null);
                        diceB[i].setIcon(null);
                    }
                    return;
                }
                // Seconda volta in cui attacco 
                attackerArmies.setModel(new SpinnerNumberModel(maxArmiesAttacker, 1, maxArmiesAttacker, 1));
                defenderArmies.setModel(new SpinnerNumberModel(maxArmiesDefender, 1, maxArmiesDefender, 1));
            }

        });

        dialogPanel.add(attackText);
        dialogPanel.add(new JLabel());
        dialogPanel.add(defenseText);
        dialogPanel.add(attackerArmies);
        dialogPanel.add(new JLabel());
        dialogPanel.add(defenderArmies);
        dialogPanel.add(new JLabel());
        dialogPanel.add(execute);
        dialogPanel.add(new JLabel());
        dialogPanel.add(dice1R);
        dialogPanel.add(new JLabel("hola"));
        dialogPanel.add(dice1B);
        dialogPanel.add(dice2R);
        dialogPanel.add(new JLabel("hola"));
        dialogPanel.add(dice2B);
        dialogPanel.add(dice3R);
        dialogPanel.add(new JLabel("hola"));
        dialogPanel.add(dice3B);
        inputArmies.add(dialogPanel);
        inputArmies.setModal(true);
        inputArmies.setSize(600, 300);
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
}
