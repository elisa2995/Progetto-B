package risiko.equipment;

/**
 * Enumeration that represents the different types of cards, namely : infantry,
 * cavalry, artillery and wild.
 */
public enum Card {

    INFANTRY(13), CAVALRY(13), ARTILLERY(13), WILD(3);

    private final int amount; // The amount of cards of that type in the deck.

    /**
     * Creates a new Card.
     *
     * @param n the amount of cards of that type in the deck.
     */
    private Card(int n) {
        this.amount = n;
    }

    /**
     * Returns the number of cards of the type of this card that are in the
     * bonus deck.
     *
     * @return
     */
    public int getAmount() {
        return amount;
    }
}
