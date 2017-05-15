package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import risiko.CardBonus;
import risiko.Player;

/**
 * @author andrea
 */
public class CardBonusDialog extends JDialog {
    Player player;
    private final JPanel imagesPanel;
    private final JPanel buttonPanel;

    public CardBonusDialog(Player player){
        this.player        = player;
        this.imagesPanel   = new JPanel();
        this.buttonPanel   = new JPanel();

        initImagesPanel();
        initButtonPanel();
        setLayout(new BorderLayout());
        add(imagesPanel,  BorderLayout.WEST);
        add(buttonPanel,  BorderLayout.EAST);
        
        pack();
        setModalityType(DEFAULT_MODALITY_TYPE);
        setVisible(true);
    }

    //Creo un JPanel che mostra le immagini di tutte le bonusCard del player, 
    //Con questo panel non si può interagire, è a solo fini illustrativi.
    private void initImagesPanel() {
        int righe = player.getCardBonus().size()/5+1;
        imagesPanel.setLayout(new GridLayout(righe,5));
        for (CardBonus card : player.getCardBonus()) {
            imagesPanel.add(new JLabel(new ImageIcon( card.getImage())));
        }
    }   

    //Creo un JPanel nel quale ci sarà un bottone per ogni tris che le cardBonus del player gli permetteranno di giocare 
    private void initButtonPanel() {
        player.countCardsByType();  //Necessario per aggionare il conte delle varie bonusCard, e poi chiamare i vari canPlay...Tris
        buttonPanel.setLayout(new GridLayout(5,1));
        buttonPanel.add(createButtonTris("Giocherò il TRIS la prossima volta", null, null, null, 0));
        if(player.canPlay_3Fants_Tris()) 
            buttonPanel.add(createButtonTris("Play TRIS: \"Fante-Fante-Fante\", gain  6 Armies",   CardBonus.FANTE,  CardBonus.FANTE,  CardBonus.FANTE,  6));
        if(player.canPlay_3Cannon_Tris())
            buttonPanel.add(createButtonTris("Play TRIS: \"Cannon-Cannon-Cannon\", gain  4 Armies",CardBonus.CANNON,   CardBonus.CANNON, CardBonus.CANNON, 4));
        if(player.canPlay_3Knight_Tris())
            buttonPanel.add(createButtonTris("Play TRIS: \"Knight-Knight-Knight\", gain  8 Armies",CardBonus.KNIGHT,   CardBonus.KNIGHT, CardBonus.KNIGHT, 8));
        if(player.canPlay_Fants_Knight_Cannon_Tris())
            buttonPanel.add(createButtonTris("Play TRIS: \"Fante-Knight-Cannon\", gain 10 Armies", CardBonus.FANTE,    CardBonus.KNIGHT, CardBonus.CANNON, 10));
        if(player.canPlay_Jolly_2Fants_Tris())
            buttonPanel.add(createButtonTris("Play TRIS: \"Jolly-Fante-Fante\", gain 12 Armies",   CardBonus.JOLLY,  CardBonus.FANTE,  CardBonus.FANTE,  12));
        if(player.canPlay_Jolly_2Cannon_Tris())
            buttonPanel.add(createButtonTris("Play TRIS: \"Jolly-Cannon-Cannon\", gain 12 Armies", CardBonus.JOLLY,    CardBonus.CANNON, CardBonus.CANNON, 12));
        if(player.canPlay_Jolly_2Knight_Tris())
            buttonPanel.add(createButtonTris("Play TRIS: \"Jolly-Knight-Knight\", gain 12 Armies", CardBonus.JOLLY,    CardBonus.KNIGHT, CardBonus.KNIGHT, 12));
    }
      
    
    private JButton createButtonTris(final String text, final CardBonus cardBonus1,final CardBonus cardBonus2,final CardBonus cardBonus3, int bonusArmiesTris){
        JButton ButtonTris = new JButton(text);
        ButtonTris.addActionListener((ActionEvent e) -> {
            CardBonusDialog.this.player.playTris(cardBonus1, cardBonus2, cardBonus3, bonusArmiesTris);
            CardBonusDialog.this.dispose();
        });
        return ButtonTris;
    }  
}