package risiko.equipment;

import exceptions.TrisNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import services.FileManager;

/**
 * Class that represents the deck containing the bonus cards.
 */
public class BonusDeck {

    private List<Card> cards;
    private Map<Card[], Integer> tris;

    /**
     * Creates a new BonusDeck.
     */
    public BonusDeck() {
        cards = new ArrayList<>();
        tris = new HashMap<>();
        buildDeck();
        buildTris();
    }

    /**
     * Builds the deck. It puts a proper amount of card of each type into the
     * deck. The amount is specified as an attribute of the enumeration class
     * Card.
     */
    private void buildDeck() {

        for (Card card : Card.values()) {
            buildCards(card);
        }
        Collections.shuffle(cards);
    }

    /**
     * Creates <code>card.amount</code> of cards of the specified card type.
     *
     * @param card
     */
    private void buildCards(Card card) {
        for (int i = 0; i < card.getAmount(); i++) {
            cards.add(getCardByName(card.toString()));
        }
    }

    /**
     * Builds a map with the playable tris and the corrisponing bonus.
     *
     */
    private void buildTris() {

        List<Map<String, Object>> combinations = FileManager.getInstance().getTris();
        String[] cardsNames;
        int bonus;

        for (Map<String, Object> combo : combinations) {
            cardsNames = (String[]) combo.get("cards");
            bonus = (Integer) combo.get("bonus");
            Card[] c = {getCardByName(cardsNames[0]), getCardByName(cardsNames[1]), getCardByName(cardsNames[2])};
            tris.put(c, bonus);
        }
    }

    /**
     * Returns a card from the deck. If the deck is empty it recreates it.
     *
     * @return
     */
    public Card drawCard() {
        if (cards.isEmpty()) {
            buildDeck();
        }
        return cards.remove(cards.size() - 1);
    }

    /**
     * Taking as input an array that contains the names of the cards, it returns
     * an array with the corresponing cards.
     *
     * @param names
     * @return
     */
    public Card[] getCardsByNames(String[] names) {
        if (names == null) {
            return null;
        }
        Card[] set = new Card[names.length];
        for (int i = 0; i < names.length; i++) {
            set[i] = getCardByName(names[i]);
        }
        return set;
    }

    /**
     * Returns the card with the name <code>name</code>.
     *
     * @param name
     * @return
     */
    private Card getCardByName(String name) {
        return Card.valueOf(Card.class, name);
    }

    /**
     * Returns an HashMap with the possible combinations of cards and the
     * corresponding bonus awarded to the player in case it plays the tris.
     *
     * @return
     */
    public Map<Card[], Integer> getTris() {
        return this.tris;
    }

    /**
     * Given a tris of cards it returns the corresponding bonus; if it doesn't
     * find the tris it throws an excption.
     *
     * @param cards
     * @return
     */
    public int getBonusForTris(Card[] cards) throws TrisNotFoundException {

        for (Card[] set : tris.keySet()) {
            boolean success = true;
            for (Card card : set) {
                success = success && (Collections.frequency(Arrays.asList(set), card)) == (Collections.frequency(Arrays.asList(cards), card));
            }
            if (success) {
                return tris.get(set);
            }
        }
        throw new TrisNotFoundException();
    }

}
