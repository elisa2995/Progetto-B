package risiko.game;

import exceptions.PendingOperationsException;
import java.util.List;
import java.util.Map;
import risiko.players.ArtificialPlayer;
import risiko.players.ArtificialPlayerSettings;

/**
 * Interface that declares all the methods of Game that can be called by the
 * View (or by an artificial player). The last parameter of each methods is a
 * variable number of ArtificialPlayers: in case the method is called from the
 * view, no ArtificialPlayer shall be passed to the method (the "caller" is
 * human), on the other hand, if the method is called by an artificial player,
 * it has to pass itself as last parameter of the method. This parameter is used
 * to grant the permit to perform some actions only to the active player.
 */
public interface GameProxy {

    /**
     * Returns the current phase.
     *
     * @param aiCaller
     * @return
     */
    public String getPhase(ArtificialPlayer... aiCaller);

    /**
     * Returns the active player's mission description.
     *
     * @return
     */
    public String getActivePlayerMission(ArtificialPlayer... aiCaller);

    /**
     * Sets artificial players' settings.
     *
     * @param aps
     * @param aiCaller
     */
    public void setPlayerSettings(ArtificialPlayerSettings aps, ArtificialPlayer... aiCaller);

    //------------------------ CardsPhase ---------------------------------//
    /**
     * Returns the name of the last card drawn by the active player.
     *
     * @return
     */
    public String getLastCardDrawn(ArtificialPlayer... aiCaller);

    /**
     * Returns an ArrayList containing the names of <code>activePlayer</code>'s
     * cards.
     *
     * @return
     */
    public List<String> getCardsNames(ArtificialPlayer... aiCaller);

    /**
     * Returns the bonus awarded for the tris.
     *
     * @param cardNames
     * @param aiCaller
     * @return
     */
    public int getBonusForTris(String[] cardNames, ArtificialPlayer... aiCaller);

    /**
     * Returns a Map which keySet is the set of Cards[] that can be played by
     * the <code>activePlayer</code>. Maps the tris with the number of bonus
     * armies awarded for that specific set of cards.
     *
     * @param aiCaller
     * @return
     */
    public Map<String[], Integer> getPlayableTris(ArtificialPlayer... aiCaller);

    /**
     * Checks if <code>chosenCards</code> is a valid tris.
     *
     * @param cardNames
     * @param aiCaller
     * @return
     */
    public boolean isAValidTris(String[] cardNames, ArtificialPlayer... aiCaller);

    /**
     * Plays the tris.
     *
     * @param cardsNames
     * @param aiCaller
     */
    public void playTris(String[] cardsNames, ArtificialPlayer... aiCaller);

    //---------------------  ReinforcePhase  -----------------------------//
    /**
     * Adds an army to the country which name is <code>countryName</code>.
     *
     * @param countryName
     * @param aiCaller
     */
    public void reinforce(String countryName, ArtificialPlayer... aiCaller);

    /**
     * Checks if the player can reinforce.
     *
     * @param aiCaller
     * @return
     */
    public boolean canReinforce(ArtificialPlayer... aiCaller);

    //---------------------- FightPhase -----------------------------------//
    /**
     * Sets the attacker.
     *
     * @param attackerCountryName
     * @param aiCaller
     */
    public void setAttackerCountry(String attackerCountryName, ArtificialPlayer... aiCaller);

    /**
     * Sets the defender.
     *
     * @param defenderCountryName
     * @param aiCaller
     */
    public void setDefenderCountry(String defenderCountryName, ArtificialPlayer... aiCaller);

    /**
     * Sets the attribute <code>reattack</code> to the fightPhase.
     *
     * @param reattack
     * @param aiCaller
     */
    public void setReattack(boolean reattack, ArtificialPlayer... aiCaller);

    /**
     * Resets the fighting countries.
     *
     * @param aiCaller
     */
    public void resetFightingCountries(ArtificialPlayer... aiCaller);

    /**
     * Sets the number of armies for the attack.
     *
     * @param nrA
     * @param aiCaller
     */
    public void setAttackerArmies(int nrA, ArtificialPlayer... aiCaller);

    /**
     * Returns the name of the attacker country.
     *
     * @param aiCaller
     * @return
     */
    public String getAttackerCountryName(ArtificialPlayer... aiCaller);

    /**
     * Returns the name of the defender country.
     *
     * @return
     */
    public String getDefenderCountryName(ArtificialPlayer... aiCaller);

    /**
     * Declares an attack from <code>attackerCountry</code> to
     * <code>defenderCountry</code>. Notifies the defender so that it can choose
     * the number of armies for the defense.
     *
     * @param aiCaller
     */
    public void declareAttack(ArtificialPlayer... aiCaller);

    /**
     * Performs the attack.
     *
     * @param aiCaller
     */
    public void confirmAttack(int nrD, ArtificialPlayer... aiCaller);

    /**
     * Returns true <code>FightPhase</code> has all the parameters set to start
     * a battle.
     *
     * @param aiCaller
     * @return
     */
    public boolean isReadyToFight(ArtificialPlayer... aiCaller);

    /**
     * Returns true if <code>defenderCountryName</code> is a valid defender.
     *
     * @param defenderCountryName
     * @param aiCaller
     * @return
     */
    public boolean controlDefender(String defenderCountryName, ArtificialPlayer... aiCaller);

    /**
     * Returns the maximum number of armies that can be used either to attack or
     * to defend.
     *
     * @param countryName
     * @param isAttacker
     * @param aiCaller
     * @return
     */
    public int getMaxArmies(String countryName, boolean isAttacker, ArtificialPlayer... aiCaller);

    // ------------------------ MovePhase -----------------------------------//
    /**
     * Returns the maximum number of armies that can be moved from the country
     * which name is <code>fromCountryName</code>.
     *
     * @param fromCountryName
     * @return
     */
    public int getMaxArmiesForMovement(String fromCountryName, ArtificialPlayer... aiCaller);

    /**
     * Resets the countries selected for the movement.
     *
     * @param aiCaller
     */
    public void resetMoveCountries(ArtificialPlayer... aiCaller);

    /**
     * Returns the name of the country from which the player wants to move or
     * <code>null</code> if it hasn't been chosen yet.
     *
     * @param aiCaller
     * @return
     */
    public String getFromCountryName(ArtificialPlayer... aiCaller);

    /**
     * Sets the country from which the player will move its armies.
     *
     * @param fromCountryName
     * @param aiCaller
     */
    public void setFromCountry(String fromCountryName, ArtificialPlayer... aiCaller);

    /**
     * Sets the country to which the player will move its armies.
     *
     * @param toCountryName
     * @param aiCaller
     */
    public void setToCountry(String toCountryName, ArtificialPlayer... aiCaller);

    /**
     * Moves <code>nrArmies</code> armies from the country which name is
     * <code>fromCountryName</code> to the one which name is
     * <code>toCountryName</code>. If this is the final movement, changes the
     * phase.
     *
     * @param fromCountry
     * @param toCountryName
     * @param nrArmies
     * @param aiCaller
     */
    public void move(String fromCountry, String toCountryName, Integer nrArmies, ArtificialPlayer... aiCaller);

    /**
     * Checks if the active Player can move its armies across the previously
     * selected countries.
     *
     * @param aiCaller
     * @return
     */
    public boolean controlMovement(ArtificialPlayer... aiCaller);

    /**
     * Checks if the active Player can move its armies from the previously
     * selected country to the country which name is <code>toCountryName</code>.
     *
     * @param toCountryName
     * @param aiCaller
     * @return
     */
    public boolean controlMovement(String toCountryName, ArtificialPlayer... aiCaller);

    //----------------------------- Turns -----------------------------------//
    /**
     * Changes the phase. If it's the last one, passes the turn.
     *
     * @param aiCaller
     * @throws PendingOperationsException
     */
    public void nextPhase(ArtificialPlayer... aiCaller) throws PendingOperationsException;

    //-------------------- Methods delegated to RisikoMap ----------------//
    /**
     * Checks if the country can be chosen by the active player to launch an
     * attack.
     *
     * @param countryName
     * @param aiCaller
     * @return
     */
    public boolean controlAttacker(String countryName, ArtificialPlayer... aiCaller);

    /**
     * Checks if the active player can move from the country which name is
     * <code>countryName</code>.
     *
     * @param countryName
     * @param aiCaller
     * @return
     */
    public boolean canMoveFromHere(String countryName, ArtificialPlayer... aiCaller);

    /**
     * Checks if the active player owns the country.
     *
     * @param countryName
     * @param aiCaller
     * @return
     */
    public boolean isCountryOwner(String countryName, ArtificialPlayer... aiCaller);

    /**
     * Check if the country has enough armies to launch an attack.
     *
     * @param attackerCountryName
     * @param aiCaller
     * @return
     */
    public boolean canAttackFromCountry(String attackerCountryName, ArtificialPlayer... aiCaller);

    //------------- Methods called by artificial players ------------------//
    /**
     * Returns the list of countries held by the
     * <code>ArtificialPlayer player</code>.
     *
     * @param player il giocatore che fa la richiesta
     * @return i territori posseduti da player
     */
    public List<String> getMyCountries(ArtificialPlayer player, ArtificialPlayer... aiCaller);

    /**
     * Returns the list of neighbors for the ArtificialPlayer's country.
     *
     * @param player il giocatore che fa la richiesta
     * @return i territori posseduti da player
     */
    public List<String> getNeighbors(ArtificialPlayer player, String country, ArtificialPlayer... aiCaller);

    /**
     * Returns the list of countries held by the
     * <code>ArtificialPlayer player</code>, from which it can launch an attack.
     *
     * @param player
     * @return
     */
    public String[] getAllAttackers(ArtificialPlayer player, ArtificialPlayer... aiCaller);

    /**
     * Returns the territories that can be attacked from <code>attacker</code>
     *
     * @param attacker the name of the attacker country.
     * @return
     */
    public String[] getAllDefenders(String attacker, ArtificialPlayer... aiCaller);

    //------------------------ General purpose methods ---------------------//
    /**
     * Returns true if the caller is the active player.
     *
     * @param aiCaller
     * @return
     */
    public boolean checkMyIdentity(ArtificialPlayer... aiCaller);

    /**
     * Ends the game.
     */
    public void endGame();

    /**
     * Turns the activePlayer into an artificial one and starts its thread.
     */
    public void toArtificialPlayer();

}
