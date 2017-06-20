package gui;

import java.awt.Color;

/**
 * Enumeration class for the colors of the armies.
 */
public enum DefaultColor {
    RED(new Color(255, 0, 0)), GREEN(new Color(0, 232, 0)), BLUE(new Color(0, 0, 255)),
    YELLOW(new Color(255, 255, 0)), PURPLE(new Color(255, 0, 255)), BLACK(new Color(0, 0, 0));

    private Color color;

    /**
     * Creates a new DefaultColot
     *
     * @param color
     */
    private DefaultColor(Color color) {
        this.color = color;
    }

    /**
     * Returns <code>java.awt.Color</code> of this DefaultColor.
     *
     * @return
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Returns the name in lower case of the color <code>color</code>, if the
     * color is one of the DefaultColor's one.
     *
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
     * Returns the name of the DefaultColor in lower case.
     *
     * @return
     */
    public String toStringLC() {
        return this.toString().toLowerCase();
    }
}
