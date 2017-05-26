package risiko;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BonusDeck {

    private List<Card> cards;
    private Map<Card[], Integer> tris;
    
    public BonusDeck() {
        cards = new ArrayList<>();
        tris = new HashMap<>();
        buildDeck();
        buildTris();
    }

    /**
     * Crea il mazzo di carte.
     */
    private void buildDeck() {

        for (Card card : Card.values()) {
            buildCards(card);
        }
        Collections.shuffle(cards);
    }

    /**
     * Crea le carte di un determinato tipo (fante/cavalleria/cannone/jolly).
     *
     * @param card
     */
    private void buildCards(Card card) {
        for (int i = 0; i < card.getAmount(); i++) {
            cards.add(getCardByName(card.toString()));
        }
    }
    
    /**
     * Costruisce una mappa con i tris giocabili e i corrispettivi bonus.
     */
    private void buildTris(){
    try (BufferedReader br = new BufferedReader(new FileReader("files/bonusTris.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] cardsNames = line.split("\t")[0].split(",");
                int bonus = Integer.parseInt(line.split("\t")[1]);
                Card[] c = {getCardByName(cardsNames[0]),getCardByName(cardsNames[1]),getCardByName(cardsNames[2])};
                tris.put(c, bonus);
            }
        } catch (Exception ex) {
            System.out.println("Error in buildTris");
        }
    }

    /**
     * Restituisce una carta del mazzo. Se il mazzo è finito, lo ricrea.
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
     * Dato un array contentente i nomi delle carte, ritorna l'array di carte
     * con i nomi corrisponenti.
     *
     * @param names
     * @return
     */
    public Card[] getCardsByNames(String[] names) {
        Card[] set = new Card[names.length];
        for (int i = 0; i < names.length; i++) {
            set[i] = getCardByName(names[i]);
        }
        return set;
    }
    
    /**
     * Ritorna la carta con nome <code>name</code>.
     * @param name
     * @return 
     */
    private Card getCardByName(String name){
        return Card.valueOf(Card.class, name);
    }
    
    /**
     * Ritorna una mappa con le combinazioni di carte giocabili e i rispettivi
     * bonus.
     * @return 
     */
    public Map<Card[], Integer> getTris(){
        return this.tris;
    }

    
}
