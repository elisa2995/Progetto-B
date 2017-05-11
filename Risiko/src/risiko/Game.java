package risiko;

import exceptions.LastPhaseException;
import exceptions.PendingOperationsException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import gui.Observable;
import gui.GameObserver;
import java.util.Random;

public class Game extends Observable {

    private Country attackerCountry;
    private Country defenderCountry;
    private RisikoMap map;
    private List<Player> players;
    private Player activePlayer;
    //private Player winner; serve??
    private Phase phase;

    public Game(int humanPlayers, int artificialPlayers, GameObserver observer) throws Exception {
        this.players = new ArrayList<>();
        this.activePlayer = null;
        //this.winner = null;
        this.map = new RisikoMap();
        this.addObserver(observer);
        init(humanPlayers, artificialPlayers);

    }

    public Phase getPhase() {
        return this.phase;
    }

    /**
     * Inizializza il gioco. Ovvero chiama il metodo della mappa per
     * l'assegnazione iniziale dei territori ai giocatori -
     * assignCountriesToPlayers() - , setta un giocatore a caso come
     * activePlayer
     *
     * @author Federico
     * @throws rilancia l'eccezione che potrebbe lanciare la mappa nel caso in
     * cui l'url del file dei territori fosse sbagliato.
     */
    private void init(int humanPlayers, int artificialPlayers) throws Exception {

        buildPlayers(humanPlayers, artificialPlayers);
        map.assignCountriesToPlayers(players);
        activePlayer = players.get(new Random().nextInt(players.size()));
        map.computeBonusArmies(activePlayer);
        phase = Phase.REINFORCE;

        setChanged();
        notifyPhaseChange(activePlayer.getName(), phase.name());
    }

    /**
     * Costruisce nrPlayer giocatori (nome di default "Giocatore-i"), e li
     * aggiunge alla lista {@code List<Player> this.players}.
     *
     * @param nrPlayers
     */
    private void buildPlayers(int humanPlayers, int artificialPlayers) {
        for (int i = 0; i < humanPlayers; i++) {
            this.players.add(new Player("Giocatore-" + i));
        }
        for (int i = 0; i < artificialPlayers; i++) {
            this.players.add(new Player("FintoGiocatoreArtificiale - " + i));
            //this.players.add(new ArtificialPlayer("GiocatoreArtificiale - " + i));

        }
    }

    //------------------------  Attacco  ------------------------------------//
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
    public void attack(int nrA, int nrD, ArtificialPlayer... aiCaller) {

        if (!checkCallerIdentity(aiCaller)) {
            return;
        }

        Player defenderPlayer = map.getPlayerFromCountry(defenderCountry);
        Player attackerPlayer = map.getPlayerFromCountry(attackerCountry);
        int lostArmies[] = fight(attackerCountry, defenderCountry, nrA, nrD);
        boolean conquered = map.isConquered(defenderCountry);

        String attackResult = (new AttackResult(attackerPlayer,
                defenderPlayer, nrA, nrD,
                lostArmies, conquered)).toString();

        if (conquered) {
            map.updateOnConquer(attackerCountry, defenderCountry, nrA);
            if (hasLost(defenderPlayer)) {
                players.remove(defenderPlayer);
            }
            if (hasWon()) {
                setChanged();
                notifyVictory(activePlayer.getName());
                return;
            }
        }

        setChanged();
        notifyAttackResult(attackResult, conquered, map.canAttackFromCountry(attackerCountry), map.getMaxArmies(attackerCountry, true), map.getMaxArmies(defenderCountry, false));

    }

    /**
     * Simula lo scontro tra due eserciti, eliminado le armate perse da ogni
     * Country.
     *
     * @param countries
     * @param nrA
     * @param nrD
     * @return armiesLost necessario per istanziare attackResult nel metodo
     * attack
     * @author Andrea
     */
    private int[] fight(Country countryAttacker, Country countryDefender, int nrA, int nrD) {
        int lostArmies[] = computeLostArmies(nrA, nrD);
        map.removeArmies(countryAttacker, lostArmies[0]);
        map.removeArmies(countryDefender, lostArmies[1]);
        return lostArmies;
    }

    /**
     * Genera il numero di armate perse per giocatore durante uno scontro.
     *
     * @return array da 2 elementi, il primo valore è il numero di armate perse
     * dall'attaccante, il secondo il numero di armate perse dal difensore.
     * @author Andrea
     */
    private int[] computeLostArmies(int nrA, int nrD) {
        int resultsDiceAttack[] = rollDice(nrA);
        int resultsDiceDefens[] = rollDice(nrD);
        int armiesLost[] = new int[2];
        int min = (nrA > nrD) ? nrD : nrA;
        for (int i = 0; i < min; i++) {
            if (resultsDiceAttack[i] > resultsDiceDefens[i]) {
                armiesLost[1]++;
            } else {
                armiesLost[0]++;
            }
        }
        return armiesLost;
    }

    /**
     * Lancia una serie di dadi e restituisce i loro valori in ordine
     * decrescente.
     *
     * @param nrDice numero di dadi da tirare
     * @return un array[nrDadi]con i risultati del lancio in ordine decrescente
     * @author Andrea
     */
    private int[] rollDice(int nrDice) {
        int dices[] = new int[nrDice];
        int tmp;
        for (int i = 0; i < nrDice; i++) {
            dices[i] = rollDice();
        }
        Arrays.sort(dices);
        if (nrDice > 1) {
            tmp = dices[0];
            dices[0] = dices[nrDice - 1];
            dices[nrDice - 1] = tmp;
        }
        return dices;
    }

    /**
     * Lancia il dado e ritorna il suo risultato.
     *
     * @return un numero random da 1 a 6
     * @author Andrea
     */
    private int rollDice() {
        return (int) (Math.random() * 6) + 1;
    }

    // ----------------------- Rinforzo ------------------------------------
    /**
     * Controlla e aggiunge le armate al territorio. Queste vengono prese dal
     * campo bonusArmies del giocatore fino ad esaurimento.
     *
     * @param countryName
     * @param nArmies numero di armate da aggiungere
     * @param aiCaller l'eventuale giocatore artificiale che chiama il metodo.
     * @return
     */
    public void reinforce(String countryName, int nArmies, ArtificialPlayer... aiCaller) {

        // La seconda condizione sarà da cancellare in futuro perché in teoria sempre vera
        if (!checkCallerIdentity(aiCaller) || activePlayer.getBonusArmies() - nArmies < 0) {
            return;
        }

        activePlayer.decrementBonusArmies(nArmies);
        map.addArmies(map.getCountryByName(countryName), nArmies);

        setChanged();
        notifyReinforce(countryName, activePlayer.getBonusArmies());

    }

    /**
     * Controlla se il giocatore può rinforzare del numero di armate
     * selezionato. (Previo controllo sul caller del metodo). (prima ci facevamo
     * passare CountryName, perché??)
     *
     * @param nArmies
     * @param aiCaller l'eventuale giocatore artificiale che chiama il metodo
     * @return
     */
    public boolean canReinforce(int nArmies, ArtificialPlayer... aiCaller) {
        return checkCallerIdentity(aiCaller) && activePlayer.getBonusArmies() - nArmies >= 0;
    }

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
    public void nextPhase(ArtificialPlayer... aiCaller) throws PendingOperationsException {

        if (!checkCallerIdentity(aiCaller)) {
            return;
        }

        if (phase == Phase.REINFORCE && activePlayer.getBonusArmies() != 0) {
            throw new PendingOperationsException("Hai ancora armate da posizionare!");
        }

        try {
            this.phase = phase.next();
        } catch (LastPhaseException ex) {
            passTurn();
        }
        setChanged();
        notifyPhaseChange(activePlayer.getName(), phase.name());
    }

    /**
     * Passa il turno al giocatore successivo. Ovvero 1 - Setta come active
     * player il successivo nel giro 2 - Setta come fase la prima del turno 3 -
     * Assegna all'active player le armate bonus
     *
     * @author Carolina
     */
    private void passTurn() {
        nextTurn();
        this.phase = Phase.values()[0];
        map.computeBonusArmies(activePlayer);
    }

    /**
     * Setta come activePlayer il successivo nel giro.
     *
     * @author Federico
     */
    private void nextTurn() {
        ListIterator<Player> iter = players.listIterator(players.indexOf(activePlayer) + 1);

        if (iter.hasNext()) {
            activePlayer = iter.next();
        } else {
            activePlayer = players.get(0);
        }
    }

    //  M E T O D I   R I P R E S I   D A   M A P
    /**
     * Controlla se l'active player ha vinto.
     *
     * @return true se il giocatore ha vinto, false altrimenti
     * @author Carolina
     */
    private boolean hasWon() {
        return map.checkIfWinner(activePlayer);
    }

    /**
     * Chiede a map se il <code>player</code> ha perso.
     *
     * @param player
     * @return
     */
    private boolean hasLost(Player player) {
        return map.hasLost(player);
    }

    /**
     * Controlla che country sia dell'activePlayer e che si legale attaccare.
     * (Previo controllo sul caller del metodo).
     *
     * @param countryName
     * @param aiCaller l'eventuale ArtificialPlayer che chiama il metodo
     * @return true se l'attacco è legale, false altrimenti.
     */
    public boolean controlAttacker(String countryName, ArtificialPlayer... aiCaller) {
        return checkCallerIdentity(aiCaller) && map.controlAttacker(map.getCountryByName(countryName), activePlayer);
    }

    /**
     * Controlla che country sia dell'activePlayer. (Previo controllo sul caller
     * del metodo). // mmh
     *
     * @param country
     * @param aiCaller l'eventuale artificialPlayer caller del metodo.
     * @return true se la country è dell'active player, false altrimenti.
     */
    public boolean controlPlayer(Country country, ArtificialPlayer... aiCaller) {
        return checkCallerIdentity(aiCaller) && map.controlPlayer(country, activePlayer);
    }

    /**
     * Controlla che il territorio non sia dell'active player e che sia un
     * confinante dell'attacker.
     *
     * @param defenderCountryName
     * @param aiCaller l'eventuale artificialPlayer caller del metodo.
     * @return
     */
    public boolean controlDefender(String defenderCountryName, ArtificialPlayer... aiCaller) {
        return checkCallerIdentity(aiCaller) && map.controlDefender(attackerCountry, map.getCountryByName(defenderCountryName), activePlayer);
    }

    /**
     * Setta l'attacker.
     *
     * @param attackerCountryName
     * @param aiCaller l'eventuale artificialPlayer caller del metodo.
     */
    public void setAttackerCountry(String attackerCountryName, ArtificialPlayer... aiCaller) {
        if (!checkCallerIdentity(aiCaller)) {
            return;
        }
        this.attackerCountry = map.getCountryByName(attackerCountryName);
        setChanged();
        notifySetAttacker(attackerCountryName);
    }

    /**
     * Setta il defender.
     *
     * @param defenderCountryName
     * @param aiCaller l'eventuale artificialPlayer caller del metodo.
     */
    public void setDefenderCountry(String defenderCountryName, ArtificialPlayer... aiCaller) {
        if (!checkCallerIdentity(aiCaller)) {
            return;
        }
        this.defenderCountry = map.getCountryByName(defenderCountryName);
        setChanged();
        notifySetDefender(getAttackerCountryName(), defenderCountryName, map.getPlayerFromCountry(defenderCountry).getName(), map.getMaxArmies(attackerCountry, true), map.getMaxArmies(defenderCountry, false));
    }

    /**
     * Resetta le countries dell'attacco. (Previo controllo sul caller del
     * metodo).
     *
     * @param aiCaller
     */
    public void resetFightingCountries(ArtificialPlayer... aiCaller) {
        if (!checkCallerIdentity(aiCaller)) {
            return;
        }
        this.defenderCountry = null;
        this.attackerCountry = null;
        setChanged();
        notifySetAttacker(null);
    }

    //  M E T O D I   P E R   D A R E   I N F O
    /**
     * Ritorna l'array di countries. Utile per l'artificial player??
     *
     * @return
     */
    public Country[] getCountryList() {
        return (Country[]) map.getCountriesList().toArray();
    }

    /**
     * Ritorna la Map<Country,Player>. Utile per l'artificial player??
     *
     * @return
     */
    public Map<Country, Player> getCountryPlayer() {
        return map.getCountryPlayer();
    }

    /**
     * Controlla se la Country ha armate sufficienti per attaccare (>=2).
     *
     * @param attackerCountryName
     * @param aiCaller
     * @return
     */
    public boolean canAttackFromCountry(String attackerCountryName, ArtificialPlayer... aiCaller) {
        return checkCallerIdentity(aiCaller) && map.canAttackFromCountry(map.getCountryByName(attackerCountryName));
    }

    /**
     * Ritorna il nome della Country che sta attaccando.
     *
     * @return
     */
    public String getAttackerCountryName() {
        return (attackerCountry == null) ? null : attackerCountry.getName();
    }

    /**
     * Ritorna il nome della Country in difesa.
     *
     * @return
     */
    public String getDefenderCountryName() {
        return (defenderCountry == null) ? null : defenderCountry.getName();
    }

    /**
     * Controlla che chi chiama il metodo sia il giocatore di turno. Se aiCaller
     * è vuoto, il metodo è stato chiamato dalla GUI cioè da un humanPlayer
     * (<code>!(instanceof ArtificialPlayer)</code>). In caso contrario , il
     * metodo è stato chiamato da un giocatore artificiale, che quindi deve
     * coincidere con l'activePlayer.
     *
     * @param callerAI l'eventuale <code>ArtificialPlayer</code> caller del
     * metodo.
     * @return
     * @author Carolina
     */
    private boolean checkCallerIdentity(ArtificialPlayer[] aiCaller) {
        return (aiCaller.length == 0) ? !(activePlayer instanceof ArtificialPlayer): aiCaller[0].equals(activePlayer) ;
    }

    /**
     * Dice se game ha i parametri settati per fare un combattimento.
     *
     * @return true se sono stati settati tutti i parametri, false altrimenti.
     * @author Carolina
     */
    public boolean isReadyToFight() {
        return phase.equals(Phase.FIGHT) && attackerCountry != null && defenderCountry != null;
    }

}
