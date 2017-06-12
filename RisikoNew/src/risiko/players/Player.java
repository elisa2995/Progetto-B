package risiko.players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import risiko.Card;
import risiko.Country;
import risiko.missions.Mission;

public class Player {

    private Mission mission;
    private ArrayList<Card> bonusCards;
    private final String name;
    private final String color;
    protected int bonusArmies;
    private boolean conqueredACountry;

    public Player(String name, String color) {
        this.bonusArmies = 0;
        this.mission = null;
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

    public void setBonusCards(ArrayList<Card> bonusCards) {
        this.bonusCards = bonusCards;
    }

    /**
     * Plays <code>cards</code> and therefore gains <code>bonusArmiesTris</code>
     * armies.
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
     * It tells wheter the player has enough cards to play the tris
     * <code>cards</code>.
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
     * Returns the valid combinations of cards that can be played having this
     * player's cards.
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
        return playable;
    }

    public void addCard(Card card) {
        bonusCards.add(card);
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

    public void setConqueredACountry(boolean flag) {
        conqueredACountry = flag;
    }

    public boolean hasConqueredACountry() {
        return conqueredACountry;
    }

    
    @Override
    public String toString() {
        return name;
    }
}
