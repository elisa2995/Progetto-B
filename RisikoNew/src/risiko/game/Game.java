package risiko.game;

import org.reflections.Reflections;
import exceptions.FileManagerException;
import risiko.players.PlayerType;
import risiko.players.Player;
import risiko.players.ArtificialPlayer;
import exceptions.PendingOperationsException;
import java.lang.reflect.InvocationTargetException;
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
import risiko.equipment.BonusDeck;
import risiko.equipment.Card;
import risiko.map.Country;
import risiko.map.RisikoMap;
import risiko.players.ArtificialPlayerSettings;
import risiko.players.LoggedPlayer;
import risk.phase.*;
import shared.AttackResultInfo;
import shared.CountryInfo;
import shared.PlayerInfo;
import utils.BasicObservable;

public class Game extends Observable implements GameProxy {

    private final RisikoMap map;
    private final BonusDeck deck;
    private List<Player> players;
    private Player activePlayer;
    private PhaseEnum phase;
    private int phaseIndex;
    private Phase[] phases;

    public Game(List<PlayerInfo> playersInfo, GameObserver observer) {

        this.players = new ArrayList<>();
        this.activePlayer = null;
        this.deck = new BonusDeck();
        this.map = new RisikoMap();
        this.addObserver(observer);
        initPhases();
        init(playersInfo);

    }

    /**
     * Returns the current phase.
     *
     * @param aiCaller
     * @return
     */
    @Override
    public synchronized PhaseEnum getPhase(ArtificialPlayer... aiCaller) {
        return this.phase;
    }

    /**
     * Returns the name of the cuurent game phase.
     *
     * @param aiCaller
     * @return
     */
    @Override
    public synchronized String getPhaseName(ArtificialPlayer... aiCaller) {
        return this.phase.toString();
    }

    /**
     * Returns the active player.
     *
     * @return
     */
    @Override
    public synchronized Player getActivePlayer(ArtificialPlayer... aiCaller) {
        return activePlayer;
    }

    /**
     * Returns the active player's mission.
     *
     * @return
     */
    @Override
    public synchronized String getActivePlayerMission(ArtificialPlayer... aiCaller) {
        return activePlayer.getMissionDescription();
    }

    /**
     * Creates a sorted array of phases, with one element for each class
     * that extends the class <code>Phase</code>,
     */
    private void initPhases() {
        Set<Class<? extends Phase>> subTypes = new Reflections().getSubTypesOf(Phase.class);

        phases = new Phase[subTypes.size()];
        int i = 0;
        for (Class<? extends Phase> cls : subTypes) {
            try {
                phases[i] = cls.getDeclaredConstructor(RisikoMap.class).newInstance(map);
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
            i++;
        }
        Arrays.sort(phases);
    }

    /**
     * Initializes the game. Calls the methods to initialize the map and sets a
     * random player as active player.
     */
    private void init(List<PlayerInfo> playersInfo) {

        buildPlayers(playersInfo);
        map.initGame(players);
        notifyCountriesAssignment(buildAllCountryInfo());
        activePlayer = players.get(new Random().nextInt(players.size()));
        map.computeBonusArmies(activePlayer);
        phaseIndex = 1;
        notifyPhaseChange(buildPlayerInfo(activePlayer), phase.toString());
        startArtificialPlayersThreads();
    }

    /**
     * Adds the artificial players as observers and starts their threads.
     */
    private void startArtificialPlayersThreads() {
        for (Player playerThread : this.players) {
            if (playerThread instanceof ArtificialPlayer) {
                this.addObserver((ArtificialPlayer) playerThread);
                new Thread((ArtificialPlayer) playerThread).start();
            }
        }
    }

    /**
     * Sets artificial players' settings.
     *
     * @param aps
     * @param aiCaller
     */
    @Override
    public void setPlayerSettings(ArtificialPlayerSettings aps, ArtificialPlayer... aiCaller) {
        for (Player p : players) {
            if (p instanceof ArtificialPlayer) {
                ((ArtificialPlayer) p).setSetting(aps);
            }
        }
    }

    /**
     * Builds the list of players.
     *
     * @param playersInfo
     */
    private void buildPlayers(List<PlayerInfo> playersInfo) {

        for (PlayerInfo info : playersInfo) {
            switch (PlayerType.valueOf(info.getType())) {
                case ARTIFICIAL:
                    this.players.add(new ArtificialPlayer(info.getName(), info.getColor(), (GameProxy) Proxy.newProxyInstance(GameProxy.class.getClassLoader(),
                            new Class<?>[]{GameProxy.class},
                            new GameInvocationHandler(this))));
                    break;
                case NORMAL:
                    Player player = new Player(info.getName(), info.getColor());
                    for (int j = 0; j < 4; j++) {
                        player.addCard(deck.drawCard());
                    }
                    this.players.add(player);
                    break;
                case LOGGED:
                    this.players.add(new LoggedPlayer(info.getName(), info.getColor()));
                    break;
            }

        }
    }

    private CardPhase getCardPhase() {
        return (CardPhase) phases[0];
    }

    private ReinforcePhase getReinforcePhase() {
        return (ReinforcePhase) phases[1];
    }

    private FightPhase getFightPhase() {
        return (FightPhase) phases[2];
    }

    private MovePhase getMovePhase() {
        return (MovePhase) phases[3];
    }

    //------------------------  Attack  ------------------------------------//
    /**
     * Sets the attacker.
     *
     * @param attackerCountryName
     * @param aiCaller l'eventuale artificialPlayer caller del metodo.
     */
    @Override
    public void setAttackerCountry(String attackerCountryName, ArtificialPlayer... aiCaller) {
        getFightPhase().setAttackerCountry(attackerCountryName);
        notifySetAttacker(buildCountryInfo(true));
    }

    /**
     * Sets the defender.
     *
     * @param defenderCountryName
     * @param aiCaller l'eventuale artificialPlayer caller del metodo.
     */
    @Override
    public void setDefenderCountry(String defenderCountryName, ArtificialPlayer... aiCaller) {
        getFightPhase().setDefenderCountry(defenderCountryName);
        ((BasicObservable) this).notifySetDefender(buildFightingCountriesInfo(), getFightPhase().reattack());
    }

    /**
     * Sets the attribute <code>reattack</code>.
     *
     * @param reattack
     * @param aiCaller
     */
    @Override
    public void setReattack(boolean reattack, ArtificialPlayer... aiCaller) {
        getFightPhase().setReattack(reattack);
    }

    /**
     * Resets the fighting countries.
     *
     * @param aiCaller
     */
    @Override
    public void resetFightingCountries(ArtificialPlayer... aiCaller) {
        getFightPhase().resetFightingCountries();
        notifySetAttacker(null);
    }

    /**
     * Sets the number of armies for the defense.
     *
     * @param nrD
     * @param aiCaller
     */
    @Override
    public void setDefenderArmies(int nrD, ArtificialPlayer... aiCaller) {
        getFightPhase().setDefenderArmies(nrD);
    }

    /**
     * Sets the number of armies for the attack.
     *
     * @param nrA
     * @param aiCaller
     */
    @Override
    public void setAttackerArmies(int nrA, ArtificialPlayer... aiCaller) {
        getFightPhase().setAttackerArmies(nrA);
    }

    /**
     * Declares an attack from <code>attackerCountry</code> to
     * <code>defenderCountry</code>. Notifies the defender so that it can choose
     * the number of armies for the defense.
     *
     * @param aiCaller
     */
    @Override
    public void declareAttack(ArtificialPlayer... aiCaller) {
        getFightPhase().declareAttack();
        if (getFightPhase().getDefenderArmies() > 0) {
            notifyDefender(buildCountryInfo(false));
        }
    }

    /**
     * Performs the attack.
     *
     * @param aiCaller
     */
    @Override
    public void confirmAttack(ArtificialPlayer... aiCaller) {
        FightPhase fightPhase = getFightPhase();
        fightPhase.confirmAttack(aiCaller);
        checkLostAndWon();
        // Dopo il combattimento
        notifyArmiesChangeAfterAttack(fightPhase.getAttackerCountry(), getDefenderCountry());
        notifyAttackResult(getAttackResultInfo());
        // Dopo lo spostamento
        notifyArmiesChangeAfterAttack(fightPhase.getAttackerCountry(), getDefenderCountry());
    }

    private AttackResultInfo getAttackResultInfo() {
        FightPhase fight = getFightPhase();
        return new AttackResultInfo(buildFightingCountriesInfo(), fight.getDice(), fight.hasConquered(), fight.checkContinentConquest());
    }

    /**
     * Checks if <code>activePlayer</code> has won or the defender has lost and
     * acts accordingly.
     */
    private void checkLostAndWon() {
        if (hasWon()) {
            recordGainedPoints();
        }
        if (hasLost(map.getPlayerByCountry(getDefenderCountry()))) {
            players.remove(map.getPlayerByCountry(getDefenderCountry()));
        }
    }

    /**
     * Returns the attacker country.
     *
     * @return
     */
    private Country getDefenderCountry() {
        return getFightPhase().getDefenderCountry();
    }

    /**
     * Returns the defender country.
     *
     * @return
     */
    private Country getAttackerCountry() {
        return getFightPhase().getAttackerCountry();
    }

    /**
     * Returns true <code>FightPhase</code> has all the parameters set to start
     * a battle.
     *
     * @param aiCaller
     * @return
     */
    @Override
    public boolean isReadyToFight(ArtificialPlayer... aiCaller) {
        return getFightPhase().isReadyToFight(phaseIndex);
    }

    /**
     * Returns true if <code>defenderCountryName</code> is a valid defender.
     *
     * @param defenderCountryName
     * @param aiCaller l'eventuale artificialPlayer caller del metodo.
     * @return
     */
    @Override
    public boolean controlDefender(String defenderCountryName, ArtificialPlayer... aiCaller) {
        return getFightPhase().controlDefender(defenderCountryName);
    }

    /**
     * Records the points gained by the player that has completed its mission
     * and notifies the victory.
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

    // ----------------------- Reinforce ------------------------------------
    /**
     * Reinforces the country whose name is <code>countryName</code> with one
     * army. When the active player runs out of bonus armies, the phase is
     * changed.
     *
     * @param countryName
     * @param aiCaller
     */
    @Override
    public void reinforce(String countryName, ArtificialPlayer... aiCaller) {

        Country country = map.getCountryByName(countryName);
        activePlayer.decrementBonusArmies();
        map.addArmies(country, 1);

        notifyReinforce(activePlayer.getBonusArmies());

        if (activePlayer.getBonusArmies() == 0) {
            try {
                nextPhase();
            } catch (PendingOperationsException ex) {
            }
        }

        notifyArmiesChange(buildCountryInfo(country));
    }

    /**
     * Checks wheter the active player has at least 1 bonus army.
     *
     * @param aiCaller l'eventuale giocatore artificiale che chiama il metodo
     * @return
     */
    @Override
    public boolean canReinforce(ArtificialPlayer... aiCaller) {
        return activePlayer.getBonusArmies() > 0;
    }

    //--------------------- Gestione fasi / turni --------------------------//
    /**
     * Changes the phase. If it's the last one, passes the turn.
     *
     * @param aiCaller
     * @throws PendingOperationsException
     */
    @Override
    public void nextPhase(ArtificialPlayer... aiCaller) throws PendingOperationsException {

        resetFightingCountries(); //Affinchè sia ripristinato il cursore del Mouse.
        if (phases[phaseIndex].toString().equals("PLAY_CARDS")) {
            notifyPlayedTris(); //to hide showCardButton and cardPanel
        }

        if (phases[phaseIndex].toString().equals("REINFORCE") && activePlayer.getBonusArmies() != 0) {
            throw new PendingOperationsException("Hai ancora armate da posizionare!");
        }

        if (phases[phaseIndex].toString().equals("FIGHT") && getFightPhase().isAttackInProgress()) {
            throw new PendingOperationsException("Attacco ancora in corso!");
        }

        if (phases[phaseIndex].toString().equals("FIGHT") && activePlayer.hasConqueredACountry()) {
            this.drawBonusCard(aiCaller);
        }

        phaseIndex++;
        if (phaseIndex == 4) {
            passTurn();
        }
        notifyPhaseChange(buildPlayerInfo(activePlayer), phase.name());
    }

    /**
     * Passes the turn.
     *
     */
    private void passTurn() {
        ListIterator<Player> iter = players.listIterator(players.indexOf(activePlayer) + 1);

        if (iter.hasNext()) {
            activePlayer = iter.next();
        } else {
            activePlayer = players.get(0);
        }

        activePlayer.setConqueredACountry(false);
        if (!getCardsNames().isEmpty() && !(activePlayer instanceof ArtificialPlayer)) {
            notifyNextTurn(getCardsNames());
        }

        phaseIndex = (getCardsNames().isEmpty()) ? 1 : 0;

        map.computeBonusArmies(activePlayer);

    }

    //------------------------------ Cards  ---------------------------------//
    /**
     * Returns the name of the last card drawn by <code>activePlayer</code>.
     *
     * @param aiCaller
     * @return
     */
    @Override
    public synchronized String getLastCardDrawn(ArtificialPlayer... aiCaller) {
        return activePlayer.getLastDrawnCard().name();
    }

    /**
     * Draws a card from the deck and gives it to <code>activePlayer</code>
     *
     * @param aiCaller
     */
    private void drawBonusCard(ArtificialPlayer... aiCaller) {
        activePlayer.addCard(deck.drawCard());

        notifyDrawnCard(getLastCardDrawn(), activePlayer instanceof ArtificialPlayer);
    }

    /**
     * Returns an ArrayList containing the names of <code>activePlayer</code>'s
     * cards.
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
     * Returns the value of the tris.
     *
     * @param cardNames
     * @return
     */
    public int getBonusForTris(String[] cardNames, ArtificialPlayer... aiCaller) {

        Card[] cards = new Card[3];
        for (int i = 0; i < cardNames.length; i++) {
            cards[i] = Card.valueOf(cardNames[i]);
        }

        return deck.getBonusForTris(cards);
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
    public void playTris(String[] cardsNames, ArtificialPlayer... aiCaller) {
        if (cardsNames == null) {
            return;
        }
        activePlayer.playTris(deck.getCardsByNames(cardsNames), getBonusForTris(cardsNames));
        notifyPlayedTris();
        try {
            nextPhase();
        } catch (PendingOperationsException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        notifyPhaseChange(buildPlayerInfo(activePlayer), phase.name());
    }

    //----------------------------- Move -------------------------------------//
    /**
     * Ritorna il massimo numero di armate per lo spostamento finale.
     *
     * @param fromCountryName
     * @return
     */
    @Override
    public synchronized int getMaxArmiesForMovement(String fromCountryName, ArtificialPlayer... aiCaller) {
        Country fromCountry = map.getCountryByName(fromCountryName);
        return fromCountry.getArmies() - 1;
    }

    /**
     *
     * @param aiCaller
     */
    @Override
    public void resetMoveCountries(ArtificialPlayer... aiCaller) {
        getMovePhase().resetMoveCountries();
        notifySetFromCountry(null);
    }

    @Override
    public synchronized String getFromCountryName(ArtificialPlayer... aiCaller) {
        return getMovePhase().getFromCountryName();
    }

    /**
     * Setta il territorio da cui effettuare lo spostamento.
     *
     * @param attackerCountryName
     * @param aiCaller
     */
    @Override
    public void setFromCountry(String fromCountryName, ArtificialPlayer... aiCaller) {
        getMovePhase().setFromCountry(fromCountryName);
        notifySetFromCountry(fromCountryName);
    }

    /**
     * Sposta il numero di armate <code>i</code> da <code>attackerCountry</code>
     * alla country con name <code>toCountryName</code>
     *
     * @param fromCountryName
     * @param toCountryName
     * @param i
     */
    @Override
    public void move(String fromCountryName, String toCountryName, Integer i, ArtificialPlayer... aiCaller) {
        Country toCountry = map.getCountryByName(toCountryName);
        Country fromCountry = map.getCountryByName(fromCountryName);
        map.move(fromCountry, toCountry, i);
        notifyArmiesChange(buildCountryInfo(toCountry));
        notifyArmiesChange(buildCountryInfo(fromCountry));

        if (phase == PhaseEnum.MOVE) {
            try {
                nextPhase();
            } catch (PendingOperationsException ex) {
            }
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
     * Controlla che l'active player possa effettuare uno spostamento da
     * <code> attackerCountry</code> al territorio con nome
     * <code>toCountryName</code>
     *
     * @param toCountryName
     * @return
     */
    @Override
    public boolean controlMovement(String toCountryName, ArtificialPlayer... aiCaller) {
        return getMovePhase().controlMovement(map.getCountryByName(toCountryName));
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
        return (getAttackerCountry() == null) ? null : getAttackerCountry().toString();
    }

    /**
     * Ritorna il nome della Country in difesa.
     *
     * @return
     */
    @Override
    public synchronized String getDefenderCountryName(ArtificialPlayer... aiCaller) {
        return (getDefenderCountry() == null) ? null : getDefenderCountry().toString();
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
            return map.controlDefender(map.getCountryByName(attacker), element);
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
        notifyArmiesChange(buildCountryInfo(defenderCountry));
        notifyArmiesChange(buildCountryInfo(attackerCountry));
    }

    /**
     * Controlla se il tris in cardNames è un tris giocabile.
     *
     * @param cardNames
     * @param aiCaller
     * @return
     */
    @Override
    public boolean canPlayThisTris(String[] cardNames, ArtificialPlayer... aiCaller) {

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

    @Override
    public void endGame() {
        notifyEndGame();
    }

    /**
     * converts the activeplayer to an artificial one and starts its thread
     */
    public void toArtificialPlayer() {
        if (!(activePlayer instanceof ArtificialPlayer)) {
            ArtificialPlayer player = new ArtificialPlayer(activePlayer.getName(), activePlayer.getColor(), (GameProxy) Proxy.newProxyInstance(GameProxy.class.getClassLoader(),
                    new Class<?>[]{GameProxy.class},
                    new GameInvocationHandler(this)));
            player.setMission(activePlayer.getMission());
            player.setBonusCards(activePlayer.getBonusCards());
            player.addBonusArmies(activePlayer.getBonusArmies());
            player.setConqueredACountry(activePlayer.hasConqueredACountry());

            //activePlayer = player;
            int position = -1;
            for (Player entry : players) {
                if (entry.getName().equals(activePlayer.getName())) {
                    position = players.indexOf(entry);
                }
            }
            map.changeOwner(players.get(position), player);
            players.remove(position);
            players.add(position, player);
            activePlayer = players.get(position);
            this.addObserver((ArtificialPlayer) players.get(position));
            new Thread((ArtificialPlayer) player).start();
        }
    }

    //-------------------------------- Build info -----------------------------//
    /**
     * Builds an array of 2 elements of <code>CountryInfo</code>. The element at
     * index 0 represent the attacker, the one at index 1 the defender.
     *
     * @return
     */
    private CountryInfo[] buildFightingCountriesInfo() {
        CountryInfo attackerCountryInfo = buildCountryInfo(true);
        attackerCountryInfo.canAttackFromHere(map.canAttackFromCountry(getFightPhase().getAttackerCountry()));
        CountryInfo defenderCountryInfo = buildCountryInfo(false);
        return new CountryInfo[]{attackerCountryInfo, defenderCountryInfo};
    }

    /**
     * Builds an object <code>CountryInfo</code> which contains the info about
     * the attacker/defender.
     *
     * @param isAttacker
     * @return
     */
    private CountryInfo buildCountryInfo(boolean isAttacker) {
        Country country = (isAttacker) ? getFightPhase().getAttackerCountry() : getFightPhase().getDefenderCountry();
        Player player = map.getPlayerByCountry(country);
        return new CountryInfo(country.toString(), map.getMaxArmies(country, isAttacker), buildPlayerInfo(player));
    }

    /**
     * Builds an object <code>CountryInfo</code> from an object of type
     * <code>Country</code>.
     *
     * @param country
     * @return
     */
    private CountryInfo buildCountryInfo(Country country) {
        return new CountryInfo(buildPlayerInfo(map.getPlayerByCountry(country)), country.toString(), country.getArmies());
    }

    /**
     * Builds an object <code>PlayerInfo</code> which containts the info about a
     * certain player.
     *
     * @param player
     * @return
     */
    private PlayerInfo buildPlayerInfo(Player player) {
        return new PlayerInfo(player.toString(), player.getColor(), player.getBonusArmies(), player instanceof ArtificialPlayer);
    }

    /**
     * Builds an array of CountryInfo, containing the info about every country
     * on the map.
     *
     * @return
     */
    private CountryInfo[] buildAllCountryInfo() {
        Country country;
        List<Country> countries = map.getCountriesList();
        CountryInfo[] countriesInfo = new CountryInfo[countries.size()];
        for (int i = 0; i < countriesInfo.length; i++) {
            country = countries.get(i);
            countriesInfo[i] = new CountryInfo(buildPlayerInfo(map.getPlayerByCountry(country)), country.getName(), country.getArmies());
        }
        return countriesInfo;
    }
}
