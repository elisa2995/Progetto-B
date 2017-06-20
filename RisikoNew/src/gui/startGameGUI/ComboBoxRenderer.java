package gui.startGameGUI;

import java.awt.Component;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import static javax.swing.SwingConstants.CENTER;

/**
 * JLabel that is used to show the color icons in a list of colors.
 *
 */
class ComboBoxRenderer extends JLabel
        implements ListCellRenderer {

    private ImageIcon[] icons;
    private String[] names;

    /**
     * Creates a new ComboBoxRenderer
     *
     * @param icons
     * @param names
     */
    public ComboBoxRenderer(ImageIcon[] icons, String[] names) {
        super();
        this.icons = icons;
        this.names = names;
        init();
    }

    /**
     * Initialization
     */
    private void init() {
        setBorder(null);
        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
    }

    /**
     * Finds the image that corresponds to the selected value and returns a
     * JLabel that shows the icon
     *
     */
    @Override
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        int selectedIndex = Arrays.asList(names).indexOf((String) value);

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        ImageIcon icon = icons[selectedIndex];
        setIcon(icon);
        return this;
    }
}
