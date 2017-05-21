package risiko;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import risiko.BonusDeck.Card;

public class Player {

    private Mission mission;
    private ArrayList<Card> bonusCards;
    private String name;
    private Color color;
    //private int contaCarte[];
    private int bonusArmies;
    private boolean alreadyDrawnCard;

    public Player(String name, Color color) {
        this.alreadyDrawnCard = false;
        this.bonusArmies = 0;
        this.mission = null;
        //this.contaCarte = new int[4];
        this.bonusCards = new ArrayList<>();
        this.color = color;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getBonusArmies() {
        return bonusArmies;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addBonusArmies(int bonusArmies) {
        this.bonusArmies += bonusArmies;
    }

    public void decrementBonusArmies(int bonusArmies) {
        this.bonusArmies -= bonusArmies;
    }

    public Color getColor() {
        return this.color;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public ArrayList<Card> getBonusCards() {
        return bonusCards;
    }

    /**
     * Gioca il tris di carte, guadagna le corrispettive bonus aramies.
     *
     * @param cards
     * @param bonusArmiesTris
     */
    public void playTris(Card[] cards, int bonusArmiesTris) {
        for (Card card : cards) {
            bonusCards.remove(card);
        }
        bonusArmies += bonusArmiesTris;
    }

    /**
     * Ritorna true se il giocatore può giocare il tris di carte
     * <code> cards</code>. <code>cards</code> deve essere un array di 3
     * elementi, o tutti dello stesso tipo, o tutti diversi.
     *
     * @param cards
     * @return
     */
    public boolean canPlayThisTris(Card[] cards) {
        if (cards[0].equals(cards[1]) && cards[1].equals(cards[2])) {  // elementi tutti uguali
            return (Collections.frequency(bonusCards, cards[0])) >= cards.length; //3
        }
        if (cards[0].equals(cards[1])) { // 2 elementi uguali + 1 jolly (in caso di 3 uguali ho già fatto return)
            return (Collections.frequency(bonusCards, cards[0])) >= 2 && bonusCards.contains(Card.WILD);
        }

        return bonusCards.containsAll(Arrays.asList(cards)); // 3 elementi diversi
    }

    /**
     * Ritorna i tris giocabili dal player.
     *
     * @param tris
     * @return
     */
    public Map<Card[], Integer> getPlayableTris(Map<Card[], Integer> tris) {
        Map<Card[], Integer> playableTris = new HashMap<>();
        for (Map.Entry<Card[], Integer> entry : tris.entrySet()) {
            Card[] coll = entry.getKey();
            if (canPlayThisTris(coll)) {
                playableTris.put(coll, entry.getValue());
            }
        }
        return playableTris;
    }

    public void addCard(Card card) {
        bonusCards.add(card);
        this.alreadyDrawnCard = true;
    }

    public boolean hasAlreadyDrawnCard() {
        return alreadyDrawnCard;
    }

    public void setJustDrawnCard(boolean alreadyDrawn) {
        this.alreadyDrawnCard = alreadyDrawn;
    }

    public Card getLastDrawnCard() {
        return bonusCards.get(bonusCards.size() - 1);
    }

    public String getMissionDescription() {
        return mission.getDescription();
    }
}
