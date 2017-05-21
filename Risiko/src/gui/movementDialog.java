package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import risiko.Game;

/**
 *
 * @author andrea
 */
public class movementDialog extends JDialog {
    private final SpinnerNumberModel movementModel;
    private JSpinner      movementArmies;
    private final JLabel  movementText;
    private final JButton execute;
    
    /**
     * Inizializzazione
     */
    public movementDialog(Game game,String countryName) {
        int tmp        = game.getMaxArmiesForMovement();
        movementText   = new JLabel("Quante armate vuoi spostare?");
        movementModel  = new SpinnerNumberModel(tmp, 1, tmp, 1);
        movementArmies = new JSpinner(movementModel);
        execute        = new JButton("Esegui");

        execute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                game.move(countryName, (Integer)movementArmies.getValue());
                movementDialog.this.dispose();
            }
        });
        setLayout(new FlowLayout());
        add(movementText);
        add(movementArmies);
        add(execute);
        
        pack();
        setModalityType(DEFAULT_MODALITY_TYPE);
        setVisible(true);
    }
}
