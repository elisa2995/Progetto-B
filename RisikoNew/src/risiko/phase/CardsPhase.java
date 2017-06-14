/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package risiko.phase;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import risiko.equipment.BonusDeck;
import risiko.equipment.Card;
import risiko.map.RisikoMap;
import risiko.players.Player;

public class CardsPhase extends Phase {

    private final BonusDeck deck;

    public CardsPhase(RisikoMap map) {
        super(map);
        this.index = CARD_INDEX;
        deck = new BonusDeck();
    }

    /**
     * Plays the set of cards in <code>cards</code>.
     *
     * @param cards
     * @param activePlayer
     */
    public void playTris(String[] cards, Player activePlayer) {
        if (cards == null) {
            return;
        }
        // ... de stringify
        activePlayer.playTris(deck.getCardsByNames(cards), deck.getBonusForTris(deck.getCardsByNames(cards)));
    }

    /**
     * Draws a card from the deck.
     *
     * @param player
     */
    public void drawCard(Player player) {
        player.drawCard(deck);
    }

    /**
     * Returns the bonus awarded for the set of cards <code> cards </code>.
     *
     * @param cards
     * @return
     */
    public int getBonusForTris(Card[] cards) {
        return deck.getBonusForTris(cards);
    }

    /**
     * Returns a Map which keySet is the set of Cards[] that can be played by
     *  <code>player</code>. Maps the tris with the number of bonus
     * armies awarded for that specific set of cards.
     *
     * @return
     */
    public Map<Card[], Integer> getPlayableTris(Player player) {
        return player.getPlayableTris(deck.getTris());
    }

    /**
     * Checks if <code>chosenCards</code> is a valid tris.
     *
     * @param chosenCards
     * @return
     */
    public boolean isAValidTris(Card[] chosenCards) {
        for (Card[] validCardArray : deck.getTris().keySet()) {
            boolean success = true;
            for (Card card : validCardArray) {
                /**
                 * The tris is valid if and only each cards appears the same
                 * number of times in <code>chosenCards</code> as it does in
                 * <code>validCardArray</code>.
                 */
                success = success && (Collections.frequency(Arrays.asList(chosenCards), card)) == (Collections.frequency(Arrays.asList(validCardArray), card));
            }
            if (success) {
                return success;
            }

        }
        return false;
    }

    @Override
    public String toString() {
        return "PLAY_CARDS";
    }

}
