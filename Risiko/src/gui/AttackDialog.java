package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import risiko.Game;

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
    private boolean isConquered;
    private boolean canAttackFromCountry;
    private int maxArmiesAttacker;

    public void setMaxArmiesAttacker(int maxArmiesAttacker) {
        this.maxArmiesAttacker = maxArmiesAttacker;
    }

    public void setMaxArmiesDefender(int maxArmiesDefender) {
        this.maxArmiesDefender = maxArmiesDefender;
    }
    private int maxArmiesDefender;

    public AttackDialog(Game game) {
        this.game = game;
        init();
    }

    private void init() {

        JDialog inputArmies = this;
        dialogPanel = new JPanel(new GridLayout(0, 2));
        attackText = new JLabel(" n armate attaccco");
        defenseText = new JLabel(" n armate difesa");
        attackerModel = new SpinnerNumberModel(1, 1, 1, 1);
        defenserModel = new SpinnerNumberModel(1, 1, 1, 1);
        attackerArmies = new JSpinner(attackerModel);
        defenderArmies = new JSpinner(defenserModel);
        execute = new JButton("Esegui");

        execute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                game.attack((int) attackerArmies.getValue(), (int) defenderArmies.getValue());
                if (isConquered) {
                    inputArmies.setVisible(false);
                    return;
                }
                if (!canAttackFromCountry) {
                    JOptionPane.showMessageDialog(null, "Non è più possibile effettuare attacchi da questo territorio.");
                    inputArmies.setVisible(false);
                    return;
                }
                // Seconda volta in cui attacco 
                attackerArmies.setModel(new SpinnerNumberModel(maxArmiesAttacker, 1, maxArmiesAttacker, 1));
                defenderArmies.setModel(new SpinnerNumberModel(maxArmiesDefender, 1, maxArmiesDefender, 1));
            }
        });

        dialogPanel.add(attackText);
        dialogPanel.add(defenseText);
        dialogPanel.add(attackerArmies);
        dialogPanel.add(defenderArmies);
        dialogPanel.add(execute);
        inputArmies.add(dialogPanel);
        inputArmies.setModal(true);
        inputArmies.setSize(600, 300);
    }

    public void setMaxArmies(int maxArmiesAttacker, int maxArmiesDefender) {
        
        attackerArmies.setModel(new SpinnerNumberModel(maxArmiesAttacker, 1, maxArmiesAttacker, 1));
        defenderArmies.setModel(new SpinnerNumberModel(maxArmiesDefender, 1, maxArmiesDefender, 1));   
    }

    public void setIsConquered(boolean isConquered) {
        this.isConquered = isConquered;
    }

    public void setCanAttackFromCountry(boolean canAttackFromCountry) {
        this.canAttackFromCountry = canAttackFromCountry;
    }
}
// NEW