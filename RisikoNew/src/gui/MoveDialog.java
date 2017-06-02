/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import static java.awt.Dialog.DEFAULT_MODALITY_TYPE;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import risiko.game.GameProxy;

/**
 *
 * @author Elisa
 */
public class MoveDialog extends JDialog {

    private final SpinnerNumberModel movementModel;
    private JSpinner movementArmies;
    private final JLabel movementText;
    private final JButton execute;

    /**
     * Inizializzazione
     *
     * @param game
     * @param countryName
     */
    public MoveDialog(GameProxy game, String countryName) {
        int tmp = game.getMaxArmiesForMovement();
        movementText = new JLabel("Quante armate vuoi spostare?");
        movementModel = new SpinnerNumberModel(tmp, 1, tmp, 1);
        movementArmies = new JSpinner(movementModel);
        execute = new JButton("Esegui");
        init(game, countryName);
    }

    private void init(GameProxy game, String countryName) {
        MoveDialog moveDialog = this;

        Dimension dim = getToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getWidth() / 2, dim.height / 2 - this.getHeight() / 2);

        execute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                game.move(countryName, (Integer) movementArmies.getValue());
                moveDialog.dispose();
            }
        });
        this.setLayout(new FlowLayout());
        this.add(movementText);
        this.add(movementArmies);
        this.add(execute);

        this.pack();
        this.setModalityType(DEFAULT_MODALITY_TYPE);
    }
}
