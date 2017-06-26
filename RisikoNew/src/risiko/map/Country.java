package risiko.map;

import java.util.ArrayList;
import java.util.List;
import risiko.players.Player;

/**
 * Class that represents a country. The country is represented by its name, the
 * number of armies that are currently occupying it, the player that owns those
 * armies and a list of its neighbors.
 *
 */
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

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Returns the maximum number of armies available.
     *
     * @param isAttacker
     * @return
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

    /**
     * Adds an army on this country and decrement its owner's BonusArmies.
     */
    public void reinforce() {
        armies++;
        owner.decrementBonusArmies();
    }

    /**
     * Checks if toCountry belongs to this country's owner and if this country
     * and <code>toCountry</code> are neighbors.
     *
     * @param toCountry
     * @return
     */
    public boolean canMoveTo(Country toCountry) {
        boolean sameOwner = owner.equals(toCountry.getOwner());
        return sameOwner && neighbors.contains(toCountry);
    }

    /**
     * Checks if <code>player</code> is the owner of this country.
     *
     * @param player
     * @return
     */
    public boolean isMyOwner(Player player) {
        return owner.equals(player);
    }

    /**
     * Checks if <code>defender</code> and this country belong to different
     * players and if they're neighbors. Used to check if this country can
     * attack <code>defender</code>.
     *
     * @param defender
     * @return
     */
    public boolean controlDefender(Country defender) {
        return !defender.getOwner().equals(owner) && neighbors.contains(defender);
    }

    /**
     * Called if <code>defenderCountry</code> has just been conquered by this
     * country : changes the owner of the conquered country to the owner of this
     * country.
     *
     * @param defenderCountry
     */
    public void conquer(Country defenderCountry) {
        owner.setConqueredACountry(true);
        defenderCountry.setOwner(owner);
    }

    /**
     * Checks if the country belongs to <code>player</code> and it's occupied by
     * more than 1 army.
     *
     * @param player
     * @return
     */
    public boolean controlAttacker(Player player) {
        return owner.equals(player) && armies > 1;
    }

    /**
     * Checks if the country belongs to <code>player</code>, if it's occupied by
     * more than 1 army and if those armies can be moved to some of its
     * neighbors.
     *
     * @param player
     * @return
     */
    public boolean canMove(Player player) {
        boolean canMove = false;
        for (Country neighbor : neighbors) {
            if (neighbor.getOwner().equals(owner)) {
                canMove = true;
            }
        }
        return controlAttacker(player) && canMove;
    }

    /**
     * Check if the country has at least one valid neighbor to attack and is
     * occupied by more than one army.
     *
     * @return
     */
    public boolean canAttack() {
        boolean canAttack = false;
        for (Country c : neighbors) {
            canAttack = canAttack || (c.getOwner() != owner);
        }
        return canAttack & armies > 1;
    }

}
