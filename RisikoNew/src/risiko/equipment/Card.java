package risiko.equipment;

/**
 * Enumeration that represents the different types of cards
 */
public enum Card {

    INFANTRY(13), CAVALRY(13), ARTILLERY(13), WILD(3);

    private final int amount; // Il numero di carte di quel tipo nel mazzo

    /**
     * Creates a new Card, passing the number of cards of that type that are in
     * the bonus deck.
     *
     * @param n
     */
    private Card(int n) {
        this.amount = n;
    }

    /**
     * Returns the number of cards of the type of this card that are in
     * the bonus deck.
     * @return 
     */
    public int getAmount() {
        return amount;
    }
}
