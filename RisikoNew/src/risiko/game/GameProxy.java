package risiko.game;

import exceptions.PendingOperationsException;
import java.util.List;
import java.util.Map;
import risiko.players.ArtificialPlayer;
import risiko.players.ArtificialPlayerSettings;

public interface GameProxy {

    public String getPhase(ArtificialPlayer... aiCaller);

    public String getActivePlayerMission(ArtificialPlayer... aiCaller);

    public void setPlayerSettings(ArtificialPlayerSettings aps, ArtificialPlayer... aiCaller);

    //------------------------  Fight  ------------------------------------//
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

    public void setReattack(boolean reattack, ArtificialPlayer... aiCaller);

    /**
     * Resets the countries selected for the fight. 
     *
     * @param aiCaller
     */
    public void resetFightingCountries(ArtificialPlayer... aiCaller);

    /**
     * Resets the countries selected for the movement. 
     *
     * @param aiCaller
     */
    public void resetMoveCountries(ArtificialPlayer... aiCaller);

    /**
     * Sets the number of armies with which the player wants to attack.
     *
     * @param nrA 
     * @param aiCaller
     */
    public void setAttackerArmies(int nrA, ArtificialPlayer... aiCaller);

    /**
     * Declars an attack.
     *
     * @param aiCaller
     */
    public void declareAttack(ArtificialPlayer... aiCaller);

    /**
     * Executes a fight between attacker and defender.
     *
     * @param nrD
     * @param aiCaller
     */
    public void confirmAttack(int nrD, ArtificialPlayer... aiCaller);

    // ----------------------- Reinforce ------------------------------------
    /**
     * Adds an army to the country.
     *
     * @param countryName
     * @param aiCaller l'eventuale giocatore artificiale che chiama il metodo.
     */
    public void reinforce(String countryName, ArtificialPlayer... aiCaller);

    /**
     * Check if the player can reinforce.
     *
     * @param aiCaller l'eventuale giocatore artificiale che chiama il metodo
     * @return
     */
    public boolean canReinforce(ArtificialPlayer... aiCaller);

    //--------------------- Gestione fasi / turni --------------------------//
    /**
     * Change the phase
     *
     * @param aiCaller 
     * @throws PendingOperationsException 
     */
    public void nextPhase(ArtificialPlayer... aiCaller) throws PendingOperationsException;

    //-------------------- Cards/Movement ----------------//
    /**
     * Returns the name of the last card drawn.
     *
     * @return
     */
    public String getLastCardDrawn(ArtificialPlayer... aiCaller);

    /**
     * Returns an ArrayList containing the names of <code>activePlayer</code>'s
     * cards.
     * @return
     */
    public List<String> getCardsNames(ArtificialPlayer... aiCaller);

    /**
     * Returns a Map which keySet is the set of Cards[] that can be played by
     * the <code>activePlayer</code>. Maps the tris with the number of bonus
     * armies awarded for that specific set of cards.
     *
     * @return
     */
    public Map<String[], Integer> getPlayableTris(ArtificialPlayer... aiCaller);

    /**
     * Play the tris.
     *
     * @param cardsNames
     * @param bonusArmiesTris
     * @param aiCaller
     */
    public void playTris(String[] cardsNames, ArtificialPlayer... aiCaller);

    /**
     * Ritorna il massimo numero di armate per lo spostamento finale.
     *
     * @param aiCaller
     * @return
     */
    public int getMaxArmiesForMovement(String fromCountryName, ArtificialPlayer... aiCaller);

    /**
     * Setta il territorio da cui effettuare lo spostamento.
     *
     * @param attackerCountryName
     * @param aiCaller
     */
    public void setFromCountry(String attackerCountryName, ArtificialPlayer... aiCaller);

    /**
     * Sposta il numero di armate <code>i</code> da <code>attackerCountry</code>
     * alla country con name <code>toCountryName</code>
     *
     * @param fromCountry
     * @param toCountryName
     * @param i
     * @param aiCaller
     */
    public void move(String fromCountry,String toCountryName, Integer i, ArtificialPlayer... aiCaller);

    /**
     * Controlla che country sia dell'activePlayer e che si legale attaccare.
     * (Previo controllo sul caller del metodo).
     *
     * @param countryName
     * @param aiCaller l'eventuale ArtificialPlayer che chiama il metodo
     * @return true se l'attacco è legale, false altrimenti.
     */
    public boolean controlAttacker(String countryName, ArtificialPlayer... aiCaller);

    /**
     * Controlla che il territorio sia dell'attaccante, abbia più di un armata e
     * abbia territori vicini in cui spostare le armate
     *
     * @param countryName
     * @param aiCaller
     * @return true se è possibile compiere uno spostamento da quel territorio
     */
    public boolean canMoveFromHere(String countryName, ArtificialPlayer... aiCaller);

    /**
     * Controlla che country sia dell'activePlayer. (Previo controllo sul caller
     * del metodo). // mmh
     *
     * @param countryName
     * @param aiCaller l'eventuale artificialPlayer caller del metodo.
     * @return true se la country è dell'active player, false altrimenti.
     */
    public boolean isCountryOwner(String countryName, ArtificialPlayer... aiCaller);

    /**
     * Controlla che il territorio non sia dell'active player e che sia un
     * confinante dell'attacker.
     *
     * @param defenderCountryName
     * @param aiCaller l'eventuale artificialPlayer caller del metodo.
     * @return
     */
    public boolean controlDefender(String defenderCountryName, ArtificialPlayer... aiCaller);

    /**
     * Controlla che l'active player possa effettuare uno spostamento da
     * <code> attackerCountry</code> al territorio con nome
     * <code>toCountryName</code>
     *
     * @param toCountryName
     * @return
     */
    public boolean controlMovement(ArtificialPlayer... aiCaller);

    public boolean controlMovement(String toCountry, ArtificialPlayer... aiCaller);

    //  M E T O D I   P E R   D A R E   I N F O
    /**
     * Controlla se la Country ha armate sufficienti per attaccare (>=2).
     *
     * @param attackerCountryName
     * @param aiCaller
     * @return
     */
    public boolean canAttackFromCountry(String attackerCountryName, ArtificialPlayer... aiCaller);

    /**
     * Ritorna il nome della Country che sta attaccando.
     *
     * @param aiCaller
     * @return
     */
    public String getAttackerCountryName(ArtificialPlayer... aiCaller);

    /**
     * Ritorna il nome della Country in difesa.
     *
     * @return
     */
    public String getDefenderCountryName(ArtificialPlayer... aiCaller);

    /**
     * Dice se game ha i parametri settati per fare un combattimento.
     *
     * @return true se sono stati settati tutti i parametri, false altrimenti.
     * @author Carolina
     */
    public boolean isReadyToFight(ArtificialPlayer... aiCaller);

    /**
     * questo metodo serve per i giocatori artificiali per determinare quali
     * sono i suoi territori
     *
     * @param player il giocatore che fa la richiesta
     * @return i territori posseduti da player
     */
    public List<String> getMyCountries(ArtificialPlayer player, ArtificialPlayer... aiCaller);

    /**
     * questo metodo serve per i giocatori artificiali per determinare quali
     * sono i  territori confinanti
     *
     * @param player il giocatore che fa la richiesta
     * @return i territori posseduti da player
     */
    public List<String> getNeighbors(ArtificialPlayer player, String country, ArtificialPlayer... aiCaller);
    /**
     * questo metodo serve per i giocatori artificiali per determinare da quali
     * territori puo attaccare
     *
     * @param player il giocatore che fa la richiesta
     * @param aiCaller
     * @return i territori posseduti da player
     */
    public String[] getAllAttackers(ArtificialPlayer player, ArtificialPlayer... aiCaller);

    /**
     * restituisce tutti i territori che possono essere attaccati dal territorio
     * attacker
     *
     * @param attacker
     * @return
     */
    public String[] getAllDefenders(String attacker, ArtificialPlayer... aiCaller);

    public int getMaxArmies(String countryName, boolean isAttacker, ArtificialPlayer... aiCaller);

    public boolean isAValidTris(String[] cardNames, ArtificialPlayer... aiCaller);

    public int getBonusForTris(String[] cardNames, ArtificialPlayer... aiCaller);

    public void endGame();

    public void toArtificialPlayer();

    public String getFromCountryName(ArtificialPlayer... aiCaller);

    public void setToCountry(String country, ArtificialPlayer... aiCaller);

    public boolean checkMyIdentity(ArtificialPlayer... aiCalle);

}
