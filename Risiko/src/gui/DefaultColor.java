package gui;

import java.awt.Color;

public enum DefaultColor {
    RED(new Color(255, 0, 0)), GREEN(new Color(0, 232, 0)), BLUE(new Color(0, 0, 255)),
    YELLOW(new Color(255, 255, 0)), PURPLE(new Color(255, 0, 255)), BLACK(new Color(0, 0, 0));
    
    private Color color;

    private DefaultColor(Color color) {
        this.color = color;
    }
    
    /**
     * Ritorna il <code>java.awt.Color</code> di questo DefaultColor.
     * @return 
     */
    public Color getColor() {
        return this.color;
    }
    
    /**
     * Restutyusce il nome del colore <code>color</code> in lowerCase, se questo
     * fa parte dei defaultColors.
     * @param color un java.awt.Color
     * @return 
     */
    public static String getColorName(Color color) {
        for (DefaultColor c : DefaultColor.values()) {
            if (c.getColor().equals(color)) {
                return c.toStringLC();
            }
        }
        return "This is not a default color";
    }
    
    /**
     * Restituisce il nome in lower case.
     * @return 
     */
    public String toStringLC() {
        return this.toString().toLowerCase();
    }
}
