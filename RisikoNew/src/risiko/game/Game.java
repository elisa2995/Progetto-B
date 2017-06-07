package risiko.game;

import exceptions.FileManagerException;
import risiko.players.PlayerType;
import risiko.players.Player;
import risiko.players.ArtificialPlayer;
import exceptions.LastPhaseException;
import exceptions.PendingOperationsException;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import services.FileManager;
import java.util.ListIterator;
import java.util.Map;
import utils.Observable;
import utils.GameObserver;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import risiko.BonusDeck;
import risiko.Card;
import risiko.Country;
import risiko.Phase;
import risiko.RisikoMap;
import risiko.players.ArtificialPlayerSettings;
import risiko.players.LoggedPlayer;
import utils.BasicObservable;

public class Game extends Observable implements GameProxy {

    private Country attackerCountry;
    private Country defenderCountry;
    private int attackerArmies;
    private int defenderArmies;
    private boolean attackInProgress = false;
    private RisikoMap map;
    private BonusDeck deck;
    private List<Player> players;
    private Player activePlayer;
    private Phase phase;
    private int resultsDiceAttack[];
    private int resultsDiceDefense[];
    private boolean reattack;
    private GameProxy proxy;

    public Game(Map<String, String> playersMap, Map<String, String> playersColor, GameObserver observer) throws Exception {

        this.players = new ArrayList<>();
        this.activePlayer = null;
        this.deck = new BonusDeck();
        this.map = new RisikoMap();
        this.addObserver(observer);
        initInvocationHandler();
        init(playersMap, playersColor);

    }

    /**
     * Ridà la fase di gioco corrente
     *
     * @return
     */
    @Override
    public synchronized Phase getPhase(ArtificialPlayer... aiCaller) {
        return this.phase;
    }

    /**
     * Ridà il nome della fase di gioco corrente.
     *
     * @param aiCaller
     * @return
     */
    public synchronized String getPhaseName(ArtificialPlayer... aiCaller) {
        return this.phase.toString();
    }

    /**
     * Ridà il giocatore di turno
     *
     * @return
     */
    @Override
    public synchronized Player getActivePlayer(ArtificialPlayer... aiCaller) {

        return activePlayer;
    }

    /**
     * Ridà la missione del giocatore di turno
     *
     * @return
     */
    @Override
    public synchronized String getActivePlayerMission(ArtificialPlayer... aiCaller) {

        return activePlayer.getMissionDescription();
    }

    private void initInvocationHandler() {

        this.proxy = (GameProxy) Proxy.newProxyInstance(GameProxy.class.getClassLoader(),
                new Class<?>[]{GameProxy.class},
                new GameInvocationHandler(this));
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
        notifyCountryAssignment(getCountriesNames(), getCountriesArmies(), getCountriesColors());
        activePlayer = players.get(new Random().nextInt(players.size()));
        map.computeBonusArmies(activePlayer);
        phase = Phase.REINFORCE;
        reattack = false;
        notifyPhaseChange(activePlayer.getName(), phase.name(), activePlayer.getColor(), activePlayer.getBonusArmies());
        startArtificialPlayerThreads();
    }

    /**
     * Aggiunge i giocatori artificiali come observers e inizializza i loro
     * threads
     */
    private void startArtificialPlayerThreads() {
        for (Player playerThread : this.players) {
            if (playerThread instanceof ArtificialPlayer) {
                this.addObserver((ArtificialPlayer) playerThread);
                new Thread((ArtificialPlayer) playerThread).start();
            }
        }
    }

    @Override
    public void setPlayerSettings(ArtificialPlayerSettings aps, ArtificialPlayer... aiCaller) {
        for (Player p : players) {
            if (p instanceof ArtificialPlayer) {
                ((ArtificialPlayer) p).setSetting(aps);
            }
        }
    }

    /**
     * Costruisce i <code>players</code>
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
                    this.players.add(new ArtificialPlayer(playerType.getKey(), color, (GameProxy) Proxy.newProxyInstance(GameProxy.class.getClassLoader(),
                            new Class<?>[]{GameProxy.class},
                            new GameInvocationHandler(this))));
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
    @Override
    public void setAttackerCountry(String attackerCountryName, ArtificialPlayer... aiCaller) {
        this.attackerCountry = map.getCountryByName(attackerCountryName);
        notifySetAttacker(attackerCountryName, map.getMaxArmies(attackerCountry, true),attackerCountry.getName(),map.getPlayerColorByCountry(attackerCountry));
    }

    /**
     * Setta il defender.
     *
     * @param defenderCountryName
     * @param aiCaller l'eventuale artificialPlayer caller del metodo.
     */
    @Override
    public void setDefenderCountry(String defenderCountryName, ArtificialPlayer... aiCaller) {
        this.defenderCountry = map.getCountryByName(defenderCountryName);

        ((BasicObservable) this).notifySetDefender(getAttackerCountryName(), defenderCountryName, map.getPlayerByCountry(defenderCountry).getName(), map.getMaxArmies(attackerCountry, true), map.getMaxArmies(defenderCountry, false), reattack);
    }

    @Override
    public void setReattack(boolean reattack, ArtificialPlayer... aiCaller) {
        this.reattack = reattack;

    }

    /**
     * Resetta le countries dell'attacco. (Previo controllo sul caller del
     * metodo).
     *
     * @param aiCaller
     */
    @Override
    public void resetFightingCountries(ArtificialPlayer... aiCaller) {
        this.defenderCountry = null;
        this.attackerCountry = null;
        switch (getPhase()) {
            case FIGHT:
                notifySetAttacker(null, 0, null, null);
                break;
            case MOVE:
                notifySetFromCountry(null);
                break;
        }
    }

    /**
     * Simula lo scontro tra due eserciti, eliminado le armate perse da ogni
     * Country.
     *
     * @param countries
     * @param nrA
     * @param nrD attack
     * @author Andrea
     */
    private void fight(Country attackerCountry, Country defenderCountry, int nrA, int nrD) {
        int lostArmies[] = computeLostArmies(nrA, nrD);
        map.removeArmies(attackerCountry, lostArmies[0]);
        map.removeArmies(defenderCountry, lostArmies[1]);

        notifyArmiesChangeAfterAttack(attackerCountry, defenderCountry);
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

    @Override
    public synchronized int[] getResultsDiceAttack(ArtificialPlayer... aiCaller) {
        return resultsDiceAttack;
    }

    @Override
    public synchronized int[] getResultsDiceDefense(ArtificialPlayer... aiCaller) {
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

    /**
     * setta il numero di armate con il quale si vuole difenders
     *
     * @param nrD numero di armate, se il valore è -1 allore verrà settato il
     * numero di armate massimo
     * @param aiCaller
     */
    @Override
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

    @Override
    public void setAttackerArmies(int nrA, ArtificialPlayer... aiCaller) {
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
     * @param aiCaller
     */
    @Override
    public void declareAttack(ArtificialPlayer... aiCaller) {
        attackInProgress = true;
        Player defenderPlayer = map.getPlayerByCountry(defenderCountry);
        Player attackerPlayer = map.getPlayerByCountry(attackerCountry);
        if (attackerArmies > 0) {
            if (defenderPlayer instanceof ArtificialPlayer) {
                notifyDefender(defenderPlayer.getName(), defenderCountry.getName(), attackerPlayer.getName(), attackerCountry.getName(), this.attackerArmies, true);
            } else {
                notifyDefender(defenderPlayer.getName(), defenderCountry.getName(), attackerPlayer.getName(), attackerCountry.getName(), this.attackerArmies, false);
            }
        }
        //this.resetFightingCountries();        
    }

    /**
     * Esegue l'attacco
     *
     * @param aiCaller
     */
    @Override
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

        int nrA = this.attackerArmies;
        int nrD = this.defenderArmies;

        Player defenderPlayer = map.getPlayerByCountry(defenderCountry);
        Player attackerPlayer = map.getPlayerByCountry(attackerCountry);
        fight(attackerCountry, defenderCountry, nrA, nrD);
        boolean conquered = map.isConquered(defenderCountry);

        boolean hasAlreadyDrawnCard = activePlayer.hasAlreadyDrawnCard();

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
                recordGainedPoints();
                return;
            }

        }
        boolean[] artificialAttack = new boolean[2];
        artificialAttack[0] = defenderPlayer instanceof ArtificialPlayer && attackerPlayer instanceof ArtificialPlayer;
        artificialAttack[1] = attackerPlayer instanceof ArtificialPlayer;

        notifyAttackResult(conquered, map.canAttackFromCountry(attackerCountry), map.getMaxArmies(attackerCountry, true), map.getMaxArmies(defenderCountry, false), this.getResultsDiceAttack(), this.getResultsDiceDefense(), artificialAttack, hasAlreadyDrawnCard);
        attackInProgress = false;

    }

    /**
     * Registra i punti guadagnati dal giocatore che ha vinto il gioco e
     * notifica la vittoria.
     */
    private void recordGainedPoints() {

        String winMessage = "Complimenti " + activePlayer.getName() + " hai vinto!\n";
        if (activePlayer instanceof LoggedPlayer) {
            String username = ((LoggedPlayer) activePlayer).getUsername();
            FileManager.getInstance().recordGainedPoints(username, activePlayer.getMission().getPoints());
            try {
                winMessage += "Punti : " + FileManager.getInstance().getPlayerPoints(username);
            } catch (FileManagerException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        notifyVictory(winMessage);

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
    @Override
    public void reinforce(String countryName, ArtificialPlayer... aiCaller) {

        Country country = map.getCountryByName(countryName);
        activePlayer.decrementBonusArmies();
        map.addArmies(country, 1);

        notifyReinforce(countryName, activePlayer.getBonusArmies());

        if (activePlayer.getBonusArmies() == 0) {
            try {
                nextPhase();
            } catch (PendingOperationsException ex) {
            }
        }

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
    @Override
    public boolean canReinforce(ArtificialPlayer... aiCaller) {
        return activePlayer.getBonusArmies() > 0;
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
    @Override
    public void nextPhase(ArtificialPlayer... aiCaller) throws PendingOperationsException {
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
        notifyPhaseChange(activePlayer.getName(), phase.name(), activePlayer.getColor(), activePlayer.getBonusArmies());
    }

    /**
     * Passa il turno al giocatore successivo. Ovvero 1 - Setta come active
     * player il successivo nel giro 2 - Setta come fase la prima del turno 3 -
     * Assegna all'active player le armate bonus
     *
     * @author Carolina
     */
    private void passTurn() {
        ListIterator<Player> iter = players.listIterator(players.indexOf(activePlayer) + 1);

        if (iter.hasNext()) {
            activePlayer = iter.next();
        } else {
            activePlayer = players.get(0);
        }

        //Devo resettare a false JustDrowCardBonus così che si possa pescare con map.updateOnConquer 
        activePlayer.setAlreadyDrawnCard(false);
        if (!activePlayer.getBonusCards().isEmpty() && !(activePlayer instanceof ArtificialPlayer)) {
            notifyNextTurn();
        }
        this.phase = Phase.values()[0];
        map.computeBonusArmies(activePlayer);
        /*ListIterator<Player> iter = players.listIterator(players.indexOf(activePlayer) + 1);
        Player newActivePlayer;
        if (iter.hasNext()) {
            newActivePlayer = iter.next();
        } else {
            newActivePlayer = players.get(0);
        }

        //Devo resettare a false JustDrowCardBonus così che si possa pescare con map.updateOnConquer 
        newActivePlayer.setAlreadyDrawnCard(false);
        if (!newActivePlayer.getBonusCards().isEmpty() && !(newActivePlayer instanceof ArtificialPlayer)) {
            notifyNextTurn();
        }
        activePlayer=newActivePlayer;
        phase = Phase.values()[0];
        map.computeBonusArmies(activePlayer);*/
    }

    /**
     * Setta come activePlayer il successivo nel giro.
     *
     * @author Federico
     */
    //-------------------- Carte / spostamento finale ----------------//
    /**
     * Ritorna il nome dell'ultima carta pescata dal giocatore di turno.
     *
     * @return
     */
    @Override
    public synchronized String getLastCardDrawn(ArtificialPlayer... aiCaller) {
        return activePlayer.getLastDrawnCard().name();
    }

    /**
     * Ritorna true se il giocatore di turno ha già pescato una carta.
     *
     * @return
     */
    @Override
    public boolean hasAlreadyDrawnCard(ArtificialPlayer... aiCaller) {
        return activePlayer.hasAlreadyDrawnCard();
    }

    /**
     * Pesca una carta dal mazzo per l'active player.
     *
     * @param aiCaller
     */
    private void drawBonusCard(ArtificialPlayer... aiCaller) {
        Card card = deck.drawCard();
        activePlayer.addCard(card);

        notifyDrawnCard(card.name());
    }

    /**
     * Ritorna un arrayList contentente i nomi delle carte dell'active player.
     *
     * @param aiCaller
     * @return
     */
    @Override
    public synchronized List<String> getCardsNames(ArtificialPlayer... aiCaller) {
        List<String> bonusCardsNames = new ArrayList<>();
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
    @Override
    public synchronized Map<String[], Integer> getPlayableTris(ArtificialPlayer... aiCaller) {
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
     * @param aiCaller
     * @return
     */
    @Override
    public boolean canPlayThisTris(Card[] cards, ArtificialPlayer... aiCaller) {
        return activePlayer.canPlayThisTris(cards);
    }

    /**
     * Gioca il tris.
     *
     * @param cardsNames
     * @param bonusArmiesTris
     * @param aiCaller
     */
    @Override
    public void playTris(String[] cardsNames, int bonusArmiesTris, ArtificialPlayer... aiCaller) {
        if (cardsNames == null) {
            return;
        }
        activePlayer.playTris(deck.getCardsByNames(cardsNames), bonusArmiesTris);
    }

    /**
     * Ritorna il massimo numero di armate per lo spostamento finale.
     *
     * @return
     */
    @Override
    public synchronized int getMaxArmiesForMovement(ArtificialPlayer... aiCaller) {
        return attackerCountry.getArmies() - 1;
    }

    /**
     * Setta il territorio da cui effettuare lo spostamento.
     *
     * @param attackerCountryName
     * @param aiCaller
     */
    @Override
    public void setFromCountry(String attackerCountryName, ArtificialPlayer... aiCaller) {
        this.attackerCountry = map.getCountryByName(attackerCountryName);
        notifySetFromCountry(attackerCountryName);
    }

    /**
     * Sposta il numero di armate <code>i</code> da <code>attackerCountry</code>
     * alla country con name <code>toCountryName</code>
     *
     * @param toCountryName
     * @param i
     */
    @Override
    public void move(String toCountryName, Integer i, ArtificialPlayer... aiCaller) {
        Country toCountry = map.getCountryByName(toCountryName);
        map.move(attackerCountry, toCountry, i);
        notifyArmiesChange(toCountryName, toCountry.getArmies(), activePlayer.getColor());
        notifyArmiesChange(attackerCountry.getName(), attackerCountry.getArmies(), activePlayer.getColor());
        passTurn();
        notifyPhaseChange(activePlayer.getName(), phase.name(), activePlayer.getColor(), activePlayer.getBonusArmies());
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
    @Override
    public boolean controlAttacker(String countryName, ArtificialPlayer... aiCaller) {
        return map.controlAttacker(map.getCountryByName(countryName), activePlayer);
    }

    /**
     * Controlla che il territorio sia dell'attaccante, abbia più di un armata e
     * abbia territori vicini in cui spostare le armate
     *
     * @param countryName
     * @param aiCaller
     * @return
     */
    @Override
    public boolean controlFromCountryPlayer(String countryName, ArtificialPlayer... aiCaller) {
        return map.controlFromCountryPlayer(map.getCountryByName(countryName), activePlayer);
    }

    /**
     * Controlla che country sia dell'activePlayer. (Previo controllo sul caller
     * del metodo). // mmh
     *
     * @param countryName
     * @param aiCaller l'eventuale artificialPlayer caller del metodo.
     * @return true se la country è dell'active player, false altrimenti.
     */
    @Override
    public boolean controlPlayer(String countryName, ArtificialPlayer... aiCaller) {
        return map.controlPlayer(map.getCountryByName(countryName), activePlayer);
    }

    /**
     * Controlla che il territorio non sia dell'active player e che sia un
     * confinante dell'attacker.
     *
     * @param defenderCountryName
     * @param aiCaller l'eventuale artificialPlayer caller del metodo.
     * @return
     */
    @Override
    public boolean controlDefender(String defenderCountryName, ArtificialPlayer... aiCaller) {
        return map.controlDefender(attackerCountry, map.getCountryByName(defenderCountryName), activePlayer);
    }

    /**
     * Controlla che l'active player possa effettuare uno spostamento da
     * <code> attackerCountry</code> al territorio con nome
     * <code>toCountryName</code>
     *
     * @param toCountryName
     * @return
     */
    @Override
    public boolean controlMovement(String toCountryName, ArtificialPlayer... aiCaller) {
        return map.controlMovement(attackerCountry, map.getCountryByName(toCountryName), activePlayer);

    }

    //  M E T O D I   P E R   D A R E   I N F O
    /**
     * Controlla se la Country ha armate sufficienti per attaccare (>=2).
     *
     * @param attackerCountryName
     * @param aiCaller
     * @return
     */
    @Override
    public boolean canAttackFromCountry(String attackerCountryName, ArtificialPlayer... aiCaller) {
        return map.canAttackFromCountry(map.getCountryByName(attackerCountryName));
    }

    /**
     * Ritorna il nome della Country che sta attaccando.
     *
     * @return
     */
    @Override
    public synchronized String getAttackerCountryName(ArtificialPlayer... aiCaller) {
        return (attackerCountry == null) ? null : attackerCountry.getName();
    }

    /**
     * Ritorna il nome della Country in difesa.
     *
     * @return
     */
    @Override
    public synchronized String getDefenderCountryName(ArtificialPlayer... aiCaller) {
        return (defenderCountry == null) ? null : defenderCountry.getName();
    }

    /**
     * Controlla che chi chiama il metodo sia il giocatore di turno. Se aiCaller
     * è vuoto, il metodo è stato chiamato dalla GUI cioè da un humanPlayer
     * (<code>!(instanceof ArtificialPlayer)</code>). In caso contrario , il
     * metodo è stato chiamato da un giocatore artificiale, che quindi deve
     * coincidere con l'activePlayer.
     *
     * @param aiCaller l'eventuale <code>ArtificialPlayer</code> caller del
     * metodo.
     * @return
     * @author Carolina
     */
    public synchronized boolean checkCallerIdentity(ArtificialPlayer[] aiCaller) {
        return (aiCaller.length == 0) ? !(activePlayer instanceof ArtificialPlayer) : aiCaller[0].equals(activePlayer);
    }

    /**
     * Dice se game ha i parametri settati per fare un combattimento.
     *
     * @return true se sono stati settati tutti i parametri, false altrimenti.
     * @author Carolina
     */
    @Override
    public boolean isReadyToFight(ArtificialPlayer... aiCaller) {
        return phase.equals(Phase.FIGHT) && attackerCountry != null && defenderCountry != null;
    }

    @Override
    public synchronized String[] getCountriesNames(ArtificialPlayer... aiCaller) {

        String[] countriesName = new String[map.getCountriesList().size()];
        int i = 0;
        for (Country country : map.getCountriesList()) {
            countriesName[i] = country.getName();
            i++;
        }
        return countriesName;
    }

    @Override
    public synchronized int[] getCountriesArmies(ArtificialPlayer... aiCaller) {
        int[] countriesArmies = new int[map.getCountriesList().size()];
        int i = 0;
        for (Country country : map.getCountriesList()) {
            countriesArmies[i] = country.getArmies();
            i++;
        }
        return countriesArmies;
    }

    @Override
    public synchronized String[] getCountriesColors(ArtificialPlayer... aiCaller) {
        return map.getCountriesColors();

    }

    /**
     * questo metodo serve per i giocatori artificiali per determinare quali
     * sono i suoi territori
     *
     * @param player il giocatore che fa la richiesta
     * @return i territori posseduti da player
     */
    @Override
    public synchronized String[] getMyCountries(ArtificialPlayer player, ArtificialPlayer... aiCaller) {
        return this.map.getMyCountries(player).stream().map(element -> element.getName()).toArray(size -> new String[size]);
    }

    /**
     * questo metodo serve per i giocatori artificiali per determinare da quali
     * territori puo attaccare
     *
     * @param player il giocatore che fa la richiesta
     * @return i territori posseduti da player
     */
    @Override
    public synchronized String[] getAllAttackers(ArtificialPlayer player, ArtificialPlayer... aiCaller) {
        return this.map.getMyCountries(player).stream().filter(element -> this.canAttackFromCountry(element.getName(), player)).map(element -> element.getName()).toArray(size -> new String[size]);
    }

    /**
     * restituisce tutti i territori che possono essere attaccati dal territorio
     * attacker
     *
     * @param attacker
     * @return
     */
    @Override
    public synchronized String[] getAllDefenders(String attacker, ArtificialPlayer... aiCaller) {
        String[] defenders = map.getNeighbors(map.getCountryByName(attacker)).stream().filter(element
                -> {
            return map.controlDefender(map.getCountryByName(attacker), element, activePlayer);
        }).map(element -> element.getName()).toArray(size -> new String[size]);

        return defenders;
    }

    public void setPlayerSettings(ArtificialPlayerSettings aps) {
        for (Player p : players) {
            if (p instanceof ArtificialPlayer) {
                ((ArtificialPlayer) p).setSetting(aps);
            }
        }
    }

    public synchronized int getMaxArmies(String countryName, boolean isAttacker, ArtificialPlayer... aiCaller) {
        return map.getMaxArmies(map.getCountryByName(countryName), isAttacker);
    }

    private void notifyArmiesChangeAfterAttack(Country attackerCountry, Country defenderCountry) {
        notifyArmiesChange(defenderCountry.getName(), defenderCountry.getArmies(), map.getPlayerColorByCountry(defenderCountry));
        notifyArmiesChange(attackerCountry.getName(), attackerCountry.getArmies(), map.getPlayerColorByCountry(attackerCountry));
    }

    /**
     *
     * @param cardNames
     * @param aiCaller
     * @return
     */
    public boolean canPlayThisTris(String[] cardNames, ArtificialPlayer[] aiCaller) {

        List<Card[]> playableTris = new ArrayList<>();
        Card[] cards = new Card[3];

        for (int i = 0; i < cardNames.length; i++) {
            cards[i] = Card.valueOf(cardNames[i].toUpperCase());
        }

        playableTris.addAll((Set) deck.getTris().keySet());

        for (Card[] cardArray : playableTris) {
            List<Card> cardList = Arrays.asList(cardArray);
            boolean success = true;
            for (Card card : cardList) {
                success = success && (Collections.frequency(cardList, card)) == (Collections.frequency(Arrays.asList(cards), card));
            }
            if (success) {
                return success;
            }

        }
        return false;

    }
    
    public void endGame(){
        notifyEndGame();        
    }

}
