package risiko;

import risiko.players.PlayerType;
import risiko.players.Player;
import risiko.players.ArtificialPlayer;
import exceptions.LastPhaseException;
import exceptions.PendingOperationsException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import utils.Observable;
import utils.GameObserver;
import java.awt.Color;
//import java.awt.Image;
import java.util.HashMap;
import java.util.Random;
import risiko.BonusDeck;
import risiko.BonusDeck.Card;
import risiko.players.ArtificialPlayerSettings;

public class Game extends Observable {

    private Country attackerCountry;
    private Country defenderCountry;
    private int attackerArmies;
    private int defenderArmies;
    private boolean attackInProgress = false;

    private RisikoMap map;
    private BonusDeck deck;
    private List<Player> players;
    private Player activePlayer;
    //private Player winner; serve??
    private Phase phase;
    private int resultsDiceAttack[];
    private int resultsDiceDefense[];

    
    public void setPlayerSettings(ArtificialPlayerSettings aps){
        for(Player p:players){
            if(p instanceof ArtificialPlayer){
                ((ArtificialPlayer) p).setSetting(aps);
            }
        }
    }
    /**
     * setta il numero di armate con il quale si vuole difenders
     *
     * @param nrD numero di armate, se il valore è -1 allore verrà settato il
     * numero di armate massimo
     * @param aiCaller
     */
    public void setDefenderArmies(int nrD, ArtificialPlayer... aiCaller) {
        if (aiCaller.length == 0) {
            if ((map.getPlayerByCountry(defenderCountry) instanceof ArtificialPlayer)) {
                return;
            }
        } else if (!aiCaller[0].equals(map.getPlayerByCountry(defenderCountry))) {
            return;
        }

        if (nrD == -1) {
            defenderArmies = map.getMaxArmies(defenderCountry, false);
        } else {
            defenderArmies = nrD;
        }
    }

    public void setAttackerArmies(int nrA, ArtificialPlayer... aiCaller) {
        if (!checkCallerIdentity(aiCaller)) {
            return;
        }

        if (nrA == -1) {
            attackerArmies = map.getMaxArmies(attackerCountry, true);
        } else {
            attackerArmies = nrA;
        }
    }

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
    public void declareAttack(ArtificialPlayer... aiCaller) {
        attackInProgress = true;
        if (!checkCallerIdentity(aiCaller)) {
            return;
        }

        Player defenderPlayer = map.getPlayerByCountry(defenderCountry);
        Player attackerPlayer = map.getPlayerByCountry(attackerCountry);
        if (attackerArmies > 0) {
            setChanged();
            if (defenderPlayer instanceof ArtificialPlayer) {
                notifyDefender(defenderPlayer.getName(), defenderCountry.getName(), attackerPlayer.getName(), attackerCountry.getName(), this.attackerArmies, true);
            } else {
                notifyDefender(defenderPlayer.getName(), defenderCountry.getName(), attackerPlayer.getName(), attackerCountry.getName(), this.attackerArmies, false);
            }
        }
    }

    /**
     * dopo che un attacco viene dichiarato viene chiamato questo metodo per
     * eseguirlo aggiungendo il numero di armate del difensore
     *
     * @param nrD
     * @param aiCaller
     */
    public void confirmAttack(ArtificialPlayer... aiCaller) {
        if (attackInProgress == false) {
            return;
        }

        if (aiCaller.length == 0) {
            if ((map.getPlayerByCountry(defenderCountry) instanceof ArtificialPlayer)) {
                return;
            }
        } else if (!aiCaller[0].equals(map.getPlayerByCountry(defenderCountry))) {
            return;
        }
//        if (!checkCallerIdentity(aiCaller)) {
//            return;
//        }
        int nrA = this.attackerArmies;
        int nrD = this.defenderArmies;

        Player defenderPlayer = map.getPlayerByCountry(defenderCountry);
        Player attackerPlayer = map.getPlayerByCountry(attackerCountry);
        int lostArmies[] = fight(attackerCountry, defenderCountry, nrA, nrD);
        boolean conquered = map.isConquered(defenderCountry);
        String attackResult = (new AttackResult(attackerPlayer,
                defenderPlayer, nrA, nrD,
                lostArmies, conquered)).toString();

        if (conquered) {
            map.updateOnConquer(attackerCountry, defenderCountry, nrA);
            notifyArmiesChangeAfterAttack(attackerCountry, defenderCountry);
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
        notifyAttackResult(attackResult, conquered, map.canAttackFromCountry(attackerCountry), map.getMaxArmies(attackerCountry, true), map.getMaxArmies(defenderCountry, false), this.getResultsDiceAttack(), this.getResultsDiceDefense());
        attackInProgress = false;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public String getActivePlayerMission() {
        return activePlayer.getMissionDescription();
    }

    public Game(Map<String, String> playersMap, Map<String, String> playersColor, GameObserver observer) throws Exception {

        this.players = new ArrayList<>();
        this.activePlayer = null;
        this.deck = new BonusDeck();
        this.map = new RisikoMap();
        this.addObserver(observer);
        init(playersMap, playersColor);

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
    private void init(Map<String, String> playersMap, Map<String, String> playersColor) throws Exception {

        buildPlayers(playersMap, playersColor);
        map.assignCountriesToPlayers(players);
        map.assignMissionToPlayers(players);
        setChanged();
        notifyCountryAssignment(getCountriesNames(), getCountriesArmies(), getCountriesColors());
        activePlayer = players.get(new Random().nextInt(players.size()));
        map.computeBonusArmies(activePlayer);
        phase = Phase.REINFORCE;
        setChanged();
        notifyPhaseChange(activePlayer.getName(), phase.name(), activePlayer.getColor());
        startArtificialPlayerThreads();
    }

    /**
     * aggiunge gli observer degli artificial player alla lista e fa partire i
     * thread
     */
    private void startArtificialPlayerThreads() {
        for (Player playerThread : this.players) {
            if (playerThread instanceof ArtificialPlayer) {
                this.addObserver((ArtificialPlayer) playerThread);
                new Thread((ArtificialPlayer) playerThread).start();
            }
        }
    }

    /**
     *
     * @param playersMap una Map nomePlayer - tipoPlayer
     * @param colors una Map nomePlayer - colorePlayer
     */
    private void buildPlayers(Map<String, String> playersTypeMap, Map<String, String> playersColor) {

        int i = 0;
        for (Map.Entry<String, String> playerType : playersTypeMap.entrySet()) {
            String color = playersColor.get(playerType.getKey());
            switch (PlayerType.valueOf(playerType.getValue())) {
                case ARTIFICIAL:
                    this.players.add(new ArtificialPlayer("GiocatoreArtificiale - " + i, color, this));
                    break;
                case NORMAL:
                case LOGGED:
                    this.players.add(new Player(playerType.getKey(), color));
                    break;
            }
            i++;

        }
    }

    //------------------------  Attacco  ------------------------------------//
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
        notifySetDefender(getAttackerCountryName(), defenderCountryName, map.getPlayerByCountry(defenderCountry).getName(), map.getMaxArmies(attackerCountry, true), map.getMaxArmies(defenderCountry, false));
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
        switch (getPhase()) {
            case FIGHT:
                notifySetAttacker(null);
                break;
            case MOVE:
                notifySetFromCountry(null);
                break;
        }
    }

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

        Player defenderPlayer = map.getPlayerByCountry(defenderCountry);
        Player attackerPlayer = map.getPlayerByCountry(attackerCountry);
        int lostArmies[] = fight(attackerCountry, defenderCountry, nrA, nrD);
        boolean conquered = map.isConquered(defenderCountry);
        String attackResult = (new AttackResult(attackerPlayer,
                defenderPlayer, nrA, nrD,
                lostArmies, conquered)).toString();

        if (conquered) {
            map.updateOnConquer(attackerCountry, defenderCountry, nrA);
            if (!activePlayer.hasAlreadyDrawnCard()) {
                drawBonusCard();
            }
            notifyArmiesChangeAfterAttack(attackerCountry, defenderCountry);
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
        notifyAttackResult(attackResult, conquered, map.canAttackFromCountry(attackerCountry), map.getMaxArmies(attackerCountry, true), map.getMaxArmies(defenderCountry, false), this.getResultsDiceAttack(), this.getResultsDiceDefense());

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
    private int[] fight(Country attackerCountry, Country defenderCountry, int nrA, int nrD) {
        int lostArmies[] = computeLostArmies(nrA, nrD);
        map.removeArmies(attackerCountry, lostArmies[0]);
        map.removeArmies(defenderCountry, lostArmies[1]);

        notifyArmiesChangeAfterAttack(attackerCountry, defenderCountry);
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
        resultsDiceAttack = rollDice(nrA);
        resultsDiceDefense = rollDice(nrD);
        int armiesLost[] = new int[2];
        int min = (nrA > nrD) ? nrD : nrA;
        for (int i = 0; i < min; i++) {
            if (resultsDiceAttack[i] > resultsDiceDefense[i]) {
                armiesLost[1]++;
            } else {
                armiesLost[0]++;
            }
        }
        return armiesLost;
    }

    public int[] getResultsDiceAttack() {
        return resultsDiceAttack;
    }

    public int[] getResultsDiceDefense() {
        return resultsDiceDefense;
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
            dices[i] = rollDie();
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
    private int rollDie() {
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
     */
    public void reinforce(String countryName, int nArmies, ArtificialPlayer... aiCaller) {

        // La seconda condizione sarà da cancellare in futuro perché in teoria sempre vera
        if (!checkCallerIdentity(aiCaller) || activePlayer.getBonusArmies() - nArmies < 0) {
            return;
        }
        Country country = map.getCountryByName(countryName);
        activePlayer.decrementBonusArmies(nArmies);
        map.addArmies(country, nArmies);

        if (activePlayer.getBonusArmies() == 0) {
            try {
                nextPhase();
            } catch (PendingOperationsException ex) {
            }
        }

        setChanged();
        notifyReinforce(countryName, activePlayer.getBonusArmies());
        setChanged();
        notifyArmiesChange(countryName, country.getArmies(), map.getPlayerColorByCountry(country));
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
        resetFightingCountries(); //Affinchè sia ripristinato il cursore del Mouse.

        if (phase == Phase.REINFORCE && activePlayer.getBonusArmies() != 0) {
            throw new PendingOperationsException("Hai ancora armate da posizionare!");
        }

        if (phase == Phase.FIGHT && attackInProgress) {
            throw new PendingOperationsException("Attacco ancora in corso!");
        }

        try {
            this.phase = phase.next();
        } catch (LastPhaseException ex) {
            passTurn();
        }
        setChanged();
        notifyPhaseChange(activePlayer.getName(), phase.name(), activePlayer.getColor());
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

        //Devo resettare a false JustDrowCardBonus così che si possa pescare con map.updateOnConquer 
        activePlayer.setAlreadyDrawnCard(false);
        if (!activePlayer.getBonusCards().isEmpty()) {
            setChanged();
            notifyNextTurn();
        }
    }

    //-------------------- Carte / spostamento finale ----------------//
    /**
     * Ritorna il nome dell'ultima carta pescata dal giocatore di turno.
     *
     * @return
     */
    public String getLastCardDrawn() {
        return activePlayer.getLastDrawnCard().name();
    }

    /**
     * Ritorna true se il giocatore di turno ha già pescato una carta.
     *
     * @return
     */
    public boolean hasAlreadyDrawnCard() {
        return activePlayer.hasAlreadyDrawnCard();
    }

    /**
     * Pesca una carta dal mazzo per l'active player.
     *
     * @param aiCaller
     */
    private void drawBonusCard(ArtificialPlayer... aiCaller) {
        if (!checkCallerIdentity(aiCaller)) {
            return;
        }
        Card card = deck.drawCard();
        activePlayer.addCard(card);

        setChanged();
        notifyDrawnCard(card.name());
    }

    /**
     * Ritorna un'arrayList contentente i nomi delle carte dell'active player.
     *
     * @return
     */
    public ArrayList<String> getCardsNames() {
        ArrayList<String> bonusCardsNames = new ArrayList<>();
        for (Card card : activePlayer.getBonusCards()) {
            bonusCardsNames.add(card.name());
        }
        return bonusCardsNames;
    }

    /**
     * Ritorna una mappa che ha come key i nomi delle carte che compongono i
     * tris giocabili dall'activePlayer, e come value le armate bonus
     * corrisponenti.
     *
     * @return
     */
    public Map<String[], Integer> getPlayableTris() {
        Map<Card[], Integer> tris = activePlayer.getPlayableTris(deck.getTris());
        Map<String[], Integer> playableTrisNames = new HashMap<>();
        for (Map.Entry<Card[], Integer> entry : tris.entrySet()) {
            Card[] cards = entry.getKey();
            String[] names = {cards[0].name(), cards[1].name(), cards[2].name()};
            playableTrisNames.put(names, entry.getValue());
        }
        return playableTrisNames;
    }

    /**
     * Ritorna true se il giocatore può giocare il tris selezionato.
     *
     * @param cards
     * @return
     */
    public boolean canPlayThisTris(Card[] cards) {
        return activePlayer.canPlayThisTris(cards);
    }

    /**
     * Gioca il tris.
     *
     * @param cardsNames
     * @param bonusArmiesTris
     * @param aiCaller
     */
    public void playTris(String[] cardsNames, int bonusArmiesTris, ArtificialPlayer... aiCaller) {
        if (!checkCallerIdentity(aiCaller) || cardsNames == null) {
            return;
        }
        activePlayer.playTris(deck.getCardsByNames(cardsNames), bonusArmiesTris);
    }

    /**
     * Ritorna il massimo numero di armate per lo spostamento finale.
     *
     * @return
     */
    public int getMaxArmiesForMovement() {
        return attackerCountry.getArmies() - 1;
    }

    /**
     * Setta il territorio da cui effettuare lo spostamento.
     *
     * @param attackerCountryName
     * @param aiCaller
     */
    public void setFromCountry(String attackerCountryName, ArtificialPlayer... aiCaller) {
        if (!checkCallerIdentity(aiCaller)) {
            return;
        }
        this.attackerCountry = map.getCountryByName(attackerCountryName);
        setChanged();
        notifySetFromCountry(attackerCountryName);
    }

    /**
     * Sposta il numero di armate <code>i</code> da <code>attackerCountry</code>
     * alla country con name <code>toCountryName</code>
     *
     * @param toCountryName
     * @param i
     */
    public void move(String toCountryName, Integer i, ArtificialPlayer... aiCaller) {
        if (!checkCallerIdentity(aiCaller)) {
            return;
        }
        Country toCountry = map.getCountryByName(toCountryName);
        map.move(attackerCountry, toCountry, i);
        setChanged();
        notifyArmiesChange(toCountryName, toCountry.getArmies(), activePlayer.getColor());
        setChanged();
        notifyArmiesChange(attackerCountry.getName(), attackerCountry.getArmies(), activePlayer.getColor());
        passTurn();
        setChanged();
        notifyPhaseChange(activePlayer.getName(), phase.name(), activePlayer.getColor());
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
     * @param countryName
     * @param aiCaller l'eventuale artificialPlayer caller del metodo.
     * @return true se la country è dell'active player, false altrimenti.
     */
    public boolean controlPlayer(String countryName, ArtificialPlayer... aiCaller) {
        return checkCallerIdentity(aiCaller) && map.controlPlayer(map.getCountryByName(countryName), activePlayer);
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
     * Controlla che l'active player possa effettuare uno spostamento da
     * <code> attackerCountry</code> al territorio con nome
     * <code>toCountryName</code>
     *
     * @param toCountryName
     * @return
     */
    public boolean controlMovement(String toCountryName) {
        return map.controlMovement(attackerCountry, map.getCountryByName(toCountryName), activePlayer);

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
        return (aiCaller.length == 0) ? !(activePlayer instanceof ArtificialPlayer) : aiCaller[0].equals(activePlayer);
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

    public String[] getCountriesNames() {

        String[] countriesName = new String[map.getCountriesList().size()];
        int i = 0;
        for (Country country : map.getCountriesList()) {
            countriesName[i] = country.getName();
            i++;
        }
        return countriesName;
    }

    public int[] getCountriesArmies() {
        int[] countriesArmies = new int[map.getCountriesList().size()];
        int i = 0;
        for (Country country : map.getCountriesList()) {
            countriesArmies[i] = country.getArmies();
            i++;
        }
        return countriesArmies;
    }

    public String[] getCountriesColors() {
        return map.getCountriesColors();

    }

    public void notifyArmiesChangeAfterAttack(Country attackerCountry, Country defenderCountry) {
        setChanged();
        notifyArmiesChange(defenderCountry.getName(), defenderCountry.getArmies(), map.getPlayerColorByCountry(defenderCountry));
        setChanged();
        notifyArmiesChange(attackerCountry.getName(), attackerCountry.getArmies(), map.getPlayerColorByCountry(attackerCountry));
    }

//    public Image getLastCardBonusDrowed(){
//        ArrayList<CardBonus> cards = activePlayer.getAllBonusCard();
//        CardBonus lastCard = cards.get(cards.size() - 1);
//        return lastCard.getImage();
//    }
//    public boolean haveJustDrowCard(){
//        return activePlayer.havejustDrowCardBonus();
//    }
    /**
     * questo metodo serve per i giocatori artificiali per determinare quali
     * sono i suoi territori
     *
     * @param player il giocatore che fa la richiesta
     * @return i territori posseduti da player
     */
    public String[] getMyCountries(ArtificialPlayer player) {
        return this.map.getMyCountries(player).stream().map(element -> element.getName()).toArray(size -> new String[size]);
    }

    /**
     * questo metodo serve per i giocatori artificiali per determinare da quali
     * territori puo attaccare
     *
     * @param player il giocatore che fa la richiesta
     * @return i territori posseduti da player
     */
    public String[] getAllAttackers(ArtificialPlayer player) {
        return this.map.getMyCountries(player).stream().filter(element -> this.canAttackFromCountry(element.getName(), player)).map(element -> element.getName()).toArray(size -> new String[size]);
    }

    /**
     * restituisce tutti i territori che possono essere attaccati dal territorio
     * attacker
     *
     * @param attacker
     * @return
     */
    public String[] getAllDefenders(String attacker) {
        String[] defenders = map.getNeighbors(map.getCountryByName(attacker)).stream().filter(element
                -> {
            if (map.controlDefender(map.getCountryByName(attacker), element, activePlayer)) {
                return true;
            } else {
                return false;
            }
        }).map(element -> element.getName()).toArray(size -> new String[size]);

        return defenders;
    }
}
