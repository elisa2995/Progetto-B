package risiko.game;

import exceptions.PendingOperationsException;
import java.util.List;
import java.util.Map;
import risiko.equipment.Card;
import risiko.players.ArtificialPlayer;
import risiko.players.ArtificialPlayerSettings;

public interface GameProxy {

    public String getPhase(ArtificialPlayer... aiCaller);

    public String getActivePlayerMission(ArtificialPlayer... aiCaller);

    public void setPlayerSettings(ArtificialPlayerSettings aps, ArtificialPlayer... aiCaller);

    //------------------------  Attacco  ------------------------------------//
    /**
     * Setta l'attacker.
     *
     * @param attackerCountryName
     * @param aiCaller l'eventuale artificialPlayer caller del metodo.
     */
    public void setAttackerCountry(String attackerCountryName, ArtificialPlayer... aiCaller);

    /**
     * Setta il defender.
     *
     * @param defenderCountryName
     * @param aiCaller l'eventuale artificialPlayer caller del metodo.
     */
    public void setDefenderCountry(String defenderCountryName, ArtificialPlayer... aiCaller);

    public void setReattack(boolean reattack, ArtificialPlayer... aiCaller);

    /**
     * Resetta le countries dell'attacco. (Previo controllo sul caller del
     * metodo).
     *
     * @param aiCaller
     */
    public void resetFightingCountries(ArtificialPlayer... aiCaller);
    
    public void resetMoveCountries(ArtificialPlayer... aiCaller);

    /**
     * Simula l'attacco tra {@code this.attackerCountry} e
     * {@code this.defenderCountry}, con rispettivamente nrA e nrD armate.
     * (Previo controllo sul caller del metodo). Gestisce anche la conquista del
     * territorio, chiamando i metodi appositi. genera un nuovo oggetto
     * {@code AttackResult}.
     *
     * @param nrA
     * @param nrD
     * @param aiCaller l'eventuale giocatore artificiale che chiama il metodo.
     */
    //public void attack(int nrA, int nrD, ArtificialPlayer... aiCaller);

    
    /**
     * setta il numero di armate con il quale si vuole difenders
     *
     * @param nrD numero di armate, se il valore è -1 allore verrà settato il
     * numero di armate massimo
     * @param aiCaller
     */
    public void setDefenderArmies(int nrD, ArtificialPlayer... aiCaller);

    public void setAttackerArmies(int nrA, ArtificialPlayer... aiCaller);

    /**
     * dichiara un attacco che parte da attacker al territorio defender con nrA
     * numero di armate l'attacco non viene portato a termine finchè il
     * difensore non ha scelto con quante armate difendersi
     *
     * @param attacker territorio attaccante
     * @param defender territorio difensore
     * @param nrA numero di armate in attacco
     * @param aiCaller
     */
    public void declareAttack(ArtificialPlayer... aiCaller);

    /**
     * dopo che un attacco viene dichiarato viene chiamato questo metodo per
     * eseguirlo aggiungendo il numero di armate del difensore
     *
     * @param nrD
     * @param aiCaller
     */
    public void confirmAttack(ArtificialPlayer... aiCaller);

    // ----------------------- Rinforzo ------------------------------------
    /**
     * Controlla e aggiunge le armate al territorio. Queste vengono prese dal
     * campo bonusArmies del giocatore fino ad esaurimento.
     *
     * @param countryName
     * @param nArmies numero di armate da aggiungere
     * @param aiCaller l'eventuale giocatore artificiale che chiama il metodo.
     */
    public void reinforce(String countryName, ArtificialPlayer... aiCaller);

    /**
     * Controlla se il giocatore può rinforzare del numero di armate
     * selezionato. (Previo controllo sul caller del metodo). (prima ci facevamo
     * passare CountryName, perché??)
     *
     * @param nArmies
     * @param aiCaller l'eventuale giocatore artificiale che chiama il metodo
     * @return
     */
    public boolean canReinforce(ArtificialPlayer... aiCaller);

    //--------------------- Gestione fasi / turni --------------------------//
    /**
     * Cambia la fase. (Previo controllo sul caller del metodo) - 1 Controlla
     * che non ci siano operazioni in sospeso relative alla corrente fase del
     * gioco: > REINFORCE : activePlayer non deve avere bonus armies
     *
     * - 2 SE è l'ultima fase chiama passTurn()
     *
     * @param aiCaller l'eventuale giocatore artificiale che chiama il metodo.
     * @throws PendingOperationsException se non è possibile passare alla fase
     * successiva perché ci sono operazioni in sospeso.
     * @author Carolina
     */
    public void nextPhase(ArtificialPlayer... aiCaller) throws PendingOperationsException;

    //-------------------- Carte / spostamento finale ----------------//
    /**
     * Ritorna il nome dell'ultima carta pescata dal giocatore di turno.
     *
     * @return
     */
    public String getLastCardDrawn(ArtificialPlayer... aiCaller);

    /**
     * Ritorna un'arrayList contentente i nomi delle carte dell'active player.
     *
     * @return
     */
    public List<String> getCardsNames(ArtificialPlayer... aiCaller);

    /**
     * Ritorna una mappa che ha come key i nomi delle carte che compongono i
     * tris giocabili dall'activePlayer, e come value le armate bonus
     * corrisponenti.
     *
     * @return
     */
    public Map<String[], Integer> getPlayableTris(ArtificialPlayer... aiCaller);

    /**
     * Ritorna true se il giocatore può giocare il tris selezionato.
     *
     * @param cards
     * @return
     */
    //public boolean canPlayThisTris(Card[] cards, ArtificialPlayer... aiCaller);

    /**
     * Gioca il tris.
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
    public void move(/*String fromCountry,String toCountryName, */Integer i, ArtificialPlayer... aiCaller);
    
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
    public boolean controlFromCountryPlayer(String countryName, ArtificialPlayer... aiCaller);

    /**
     * Controlla che country sia dell'activePlayer. (Previo controllo sul caller
     * del metodo). // mmh
     *
     * @param countryName
     * @param aiCaller l'eventuale artificialPlayer caller del metodo.
     * @return true se la country è dell'active player, false altrimenti.
     */
    public boolean controlPlayer(String countryName, ArtificialPlayer... aiCaller);

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
    public boolean controlMovement(String toCountryName, ArtificialPlayer... aiCaller);

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

    public String[] getCountriesNames(ArtificialPlayer... aiCaller);

    public int[] getCountriesArmies(ArtificialPlayer... aiCaller);

    public String[] getCountriesColors(ArtificialPlayer... aiCaller);

    /**
     * questo metodo serve per i giocatori artificiali per determinare quali
     * sono i suoi territori
     *
     * @param player il giocatore che fa la richiesta
     * @return i territori posseduti da player
     */
    public String[] getMyCountries(ArtificialPlayer player, ArtificialPlayer... aiCaller);

    /**
     * questo metodo serve per i giocatori artificiali per determinare da quali
     * territori puo attaccare
     *
     * @param player il giocatore che fa la richiesta
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
