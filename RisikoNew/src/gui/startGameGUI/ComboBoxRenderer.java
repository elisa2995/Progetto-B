package gui.startGameGUI;

import java.awt.Component;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import static javax.swing.SwingConstants.CENTER;

/**
 * Componente che può essere usato per fare il rendering delle icone nelle celle
 * dei JList.
 */
class ComboBoxRenderer extends JLabel
        implements ListCellRenderer {

    private ImageIcon[] icons;
    private String[] names;

    public ComboBoxRenderer(ImageIcon[] icons, String[] names) {
        super();
        this.icons = icons;
        this.names = names;
        setBorder(null);
        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
    }

    /**
     * Trova l'immagine che corrisponde al valore selezionato, e ritorna la
     * JLabel pronta per mostrare l'icona.
     */
    @Override
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        // Getta l'indice selezionato
        int selectedIndex = Arrays.asList(names).indexOf((String) value);

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        //Setta l'icona
        ImageIcon icon = icons[selectedIndex];
        setIcon(icon);
        return this;
    }
}
