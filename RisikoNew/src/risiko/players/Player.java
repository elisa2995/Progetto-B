package risiko.players;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import risiko.Card;
import risiko.Country;
import risiko.missions.Mission;

public class Player {

    private Mission mission;
    private ArrayList<Card> bonusCards;
    private String name;
    //private Color color;
    private String color;
    //private int contaCarte[];
    protected int bonusArmies;
    private boolean alreadyDrawnCard;

    public Player(String name, String color) {
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

    public void decrementBonusArmies() {
        this.bonusArmies--;
    }

    public String getColor() {
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
     * <code> cards</code>. <code>cards</code> è un array di 3 carte.
     *
     * @param cards
     * @return
     */
    public boolean canPlayThisTris(Card[] cards) {

        boolean success = true;
        for (Card card : cards) {
            success = success && (Collections.frequency(bonusCards, card)) >= (Collections.frequency(Arrays.asList(cards), card));
        }
        return success;
    }

    /**
     * Ritorna i tris giocabili dal player.
     *
     * @param tris
     * @return
     */
    public Map<Card[], Integer> getPlayableTris(Map<Card[], Integer> tris) {
        Map<Card[], Integer> playable = new HashMap<>(tris);
        for (Card[] cards : tris.keySet()) {
            if (!(canPlayThisTris(cards))) {
                playable.remove(cards);
            }
        }

        /*Predicate <Card[]> cantPlay = cards -> !canPlayThisTris(cards);
            tris.keySet().removeIf(cantPlay);*/
        return playable;
    }

    public void addCard(Card card) {
        bonusCards.add(card);
        this.alreadyDrawnCard = true;
    }

    public boolean hasAlreadyDrawnCard() {
        return alreadyDrawnCard;
    }

    public void setAlreadyDrawnCard(boolean alreadyDrawn) {
        this.alreadyDrawnCard = alreadyDrawn;
    }

    public Card getLastDrawnCard() {
        return bonusCards.get(bonusCards.size() - 1);
    }

    public String getMissionDescription() {
        return mission.getDescription();
    }

    public boolean checkIfWinner(List<Country> myCountries) {
        return mission.isCompleted(myCountries);
    }
}
