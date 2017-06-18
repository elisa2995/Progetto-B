package risiko.map;

import java.util.ArrayList;
import java.util.List;
import risiko.players.Player;

public class Country implements Comparable<Country> {

    private final String name;
    private int armies;
    private List<Country> neighbors;
    private Player owner;

    public Country(String name) {
        this.name = name;
        this.neighbors = new ArrayList<>();
    }

    public List<Country> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<Country> neighbors) {
        this.neighbors = neighbors;
    }

    public String getName() {
        return this.name;
    }

    public void setArmies(int armies) {
        this.armies = armies;
    }

    public int getArmies() {
        return this.armies;
    }

    public void removeArmies(int armies) {
        this.armies -= armies;
    }

    public void addArmies(int armies) {
        this.armies += armies;
    }

    public boolean isConquered() {
        return (armies == 0);
    }

    @Override
    public int compareTo(Country o) {
        return this.armies - o.getArmies();
    }

    /**
     * questo metodo seve per mostrare il nome nella combobox
     *
     * @return il nome del territorio
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Ridà il massimo numero di armate per lo spinner rispetto al tipo di
     * country
     */
    public int getMaxArmies(boolean isAttacker) {
        if (isAttacker) {
            return Math.min(3, armies - 1);
        }
        return Math.min(3, armies);
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public void reinforce() {
        armies++;
        owner.decrementBonusArmies();
    }

    /**
     * Controlla che toCountry sia dell'active player e che sia un confinante
     * dell'fromCountry
     */
    // Nome da rivedere
    public boolean controlMovement(Country toCountry) {
        boolean sameOwner = owner.equals(toCountry.getOwner());
        return sameOwner && neighbors.contains(toCountry);
    }

    // Nome da rivedere. isRightAsOwner?
    public boolean controlPlayer(Player player) {
        return owner.equals(player);
    }

    /**
     * Checks if attacker and defender don't belong to the same player and
     * they're neighbors.
     */
    // Nome da rivedere. isRightAsDefender?
    public boolean controlDefender(Country defender) {
        return !defender.getOwner().equals(owner) && neighbors.contains(defender);
    }

    /**
     * Metodo chiamato nel caso in cui un giocatore abbia conquistato un
     * territorio. Setta il nuovo proprietario del territorio appena conquistato
     * (countryPlayer)
     */
    // Nome da rivedere. 
    public void updateOnConquer(Country defenderCountry) {
        owner.setConqueredACountry(true);
        defenderCountry.setOwner(owner);
    }

    /**
     * Controlla che il territorio sia dell'active player e che si legale
     * attaccare
     */
    // Nome da rivedere. canAttack?
    public boolean controlAttacker(Player player) {
        return owner.equals(player) && armies > 1;
    }

    /**
     * Controlla che il territorio sia dell'attaccante, abbia più di un armata e
     * abbia territori vicini in cui spostare le armate
     */
    // Nome da rivedere. canMove?
    public boolean controlFromCountryPlayer(Player player) {
        boolean canMove = false;
        for (Country neighbor : neighbors) {
            if (neighbor.getOwner().equals(owner)) {
                canMove = true;
            }
        }
        return controlAttacker(player) && canMove;
    }

    // Nome da rivedere. canAttackOneNeighbors?
    public boolean canAttackFromCountry() {
        boolean canAttack = false;
        for (Country c : neighbors) {
            canAttack = canAttack || (c.getOwner() != owner);
        }
        return canAttack & armies > 1;
    }

}
