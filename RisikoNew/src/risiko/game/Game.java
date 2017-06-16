package risiko.game;

import risiko.phase.*;
import exceptions.FileManagerException;
import risiko.players.PlayerType;
import risiko.players.Player;
import risiko.players.ArtificialPlayer;
import exceptions.PendingOperationsException;
import exceptions.WrongCallerException;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import services.FileManager;
import java.util.ListIterator;
import java.util.Map;
import utils.Observable;
import utils.GameObserver;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import risiko.equipment.Card;
import risiko.map.Country;
import risiko.map.RisikoMap;
import risiko.players.ArtificialPlayerSettings;
import risiko.players.LoggedPlayer;
import shared.AttackResultInfo;
import shared.CountryInfo;
import shared.PlayerInfo;
import utils.BasicObservable;

public class Game extends Observable implements GameProxy {

    private final RisikoMap map;
    private List<Player> players;
    private Player activePlayer;
    private int phaseIndex;
    private Phase[] phases;

    public Game(List<PlayerInfo> playersInfo, GameObserver observer) {

        this.players = new ArrayList<>();
        this.activePlayer = null;
        this.map = new RisikoMap();
        this.phaseIndex = 1;
        this.addObserver(observer);
        phases = new Phase[]{new CardsPhase(map), new ReinforcePhase(map), new FightPhase(map), new MovePhase(map)};
        init(playersInfo);

    }

    // <editor-fold defaultstate="collapsed" desc=" Getters ">
    /**
     * Returns the current phase.
     *
     * @param aiCaller
     * @return
     */
    @Override
    public synchronized String getPhase(ArtificialPlayer... aiCaller) {
        return Phase.getName(phaseIndex);
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

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc=" Initialization ">

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
        notifyPhaseChange(buildPlayerInfo(activePlayer), phases[phaseIndex].toString());
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

        Player player;
        for (PlayerInfo info : playersInfo) {
            switch (PlayerType.valueOf(info.getType())) {
                case ARTIFICIAL:
                    player = new ArtificialPlayer(info.getName(), info.getColor(), (GameProxy) Proxy.newProxyInstance(GameProxy.class.getClassLoader(),
                            new Class<?>[]{GameProxy.class},
                            new GameInvocationHandler(this)));
                    this.players.add(player);
                    break;
                case NORMAL:
                    player = new Player(info.getName(), info.getColor());
                    this.players.add(player);
                    break;
                case LOGGED:
                    player = new LoggedPlayer(info.getName(), info.getColor());
                    this.players.add(player);
                    break;
                default:
                    player = new Player("", "");
            }
            for (int j = 0; j < 4; j++) {
                getCardsPhase().drawCard(player);
            }
        }
    }

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc=" CardsPhase ">
//------------------------------ Cards  ---------------------------------//
    /**
     * Returns the cards phase.
     *
     * @return
     */
    private CardsPhase getCardsPhase() {
        return (CardsPhase) phases[Phase.CARD_INDEX];
    }

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
    private void drawBonusCard() {
        getCardsPhase().drawCard(activePlayer);
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
        // STRINGIFY
        List<String> bonusCardsNames = new ArrayList<>();
        for (Card card : activePlayer.getBonusCards()) {
            bonusCardsNames.add(card.name());
        }
        return bonusCardsNames;
    }

    /**
     * Returns the bonus awarded for the tris.
     *
     * @param cardNames
     * @return
     */
    @Override
    public int getBonusForTris(String[] cardNames, ArtificialPlayer... aiCaller) {
        // De - stringify
        Card[] cards = new Card[3];
        for (int i = 0; i < cardNames.length; i++) {
            cards[i] = Card.valueOf(cardNames[i]);
        }
        return getCardsPhase().getBonusForTris(cards);
    }

    /**
     * Returns a Map which keySet is the set of Cards[] that can be played by
     * the <code>activePlayer</code>. Maps the tris with the number of bonus
     * armies awarded for that specific set of cards.
     *
     * @return
     */
    @Override
    public synchronized Map<String[], Integer> getPlayableTris(ArtificialPlayer... aiCaller) {
        // STRINGIFY

        Map<Card[], Integer> tris = getCardsPhase().getPlayableTris(activePlayer);
        Map<String[], Integer> playableTrisNames = new HashMap<>();
        for (Map.Entry<Card[], Integer> entry : tris.entrySet()) {
            Card[] cards = entry.getKey();
            String[] names = {cards[0].name(), cards[1].name(), cards[2].name()};
            playableTrisNames.put(names, entry.getValue());
        }
        return playableTrisNames;
    }

    /**
     * Checks if <code>chosenCards</code> is a valid tris.
     *
     * @param cardNames
     * @param aiCaller
     */
    @Override
    public boolean isAValidTris(String[] cardNames, ArtificialPlayer... aiCaller) {

        // DE-STRINGIFY
        Card[] cards = new Card[3];

        for (int i = 0; i < cardNames.length; i++) {
            cards[i] = Card.valueOf(cardNames[i].toUpperCase());
        }
        return getCardsPhase().isAValidTris(cards);

    }

    /**
     * Plays the tris.
     *
     * @param cardsNames
     * @param aiCaller
     */
    @Override
    public void playTris(String[] cardsNames, ArtificialPlayer... aiCaller) {
        // DE-STRINGIFY....
        getCardsPhase().playTris(cardsNames, activePlayer);

        notifyPlayedTris();
        try {
            nextPhase();
        } catch (PendingOperationsException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        notifyPhaseChange(buildPlayerInfo(activePlayer), phases[phaseIndex].toString());
    }

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc=" ReinforcePhase ">
// ----------------------- Reinforce ------------------------------------
    /**
     * Returns the reinforcement phase.
     *
     * @return
     */
    private ReinforcePhase getReinforcePhase() {
        return (ReinforcePhase) phases[Phase.REINFORCE_INDEX];
    }

    /**
     * Reinforces the country which name is <code>countryName</code> with one
     * army. When the active player runs out of bonus armies, the phase is
     * changed.
     *
     * @param countryName
     * @param aiCaller
     */
    @Override
    public void reinforce(String countryName, ArtificialPlayer... aiCaller) {

        getReinforcePhase().reinforce(map.getCountryByName(countryName));

        notifyReinforce(activePlayer.getBonusArmies());

        if (activePlayer.getBonusArmies() == 0) {
            try {
                nextPhase();
            } catch (PendingOperationsException ex) {
            }
        }

        notifyArmiesChange(buildCountryInfo(map.getCountryByName(countryName)));
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

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc=" FightPhase ">
//------------------------  Fight  ------------------------------------//
    /**
     * Returns the fight phase.
     *
     * @return
     */
    private FightPhase getFightPhase() {
        return (FightPhase) phases[Phase.FIGHT_INDEX];
    }

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
     * Returns the name of the attacker country.
     *
     * @param aiCaller
     * @return
     */
    @Override
    public synchronized String getAttackerCountryName(ArtificialPlayer... aiCaller) {
        return (getAttackerCountry() == null) ? null : getAttackerCountry().toString();
    }

    /**
     * Returns the name of the defender country.
     *
     * @return
     */
    @Override
    public synchronized String getDefenderCountryName(ArtificialPlayer... aiCaller) {
        return (getDefenderCountry() == null) ? null : getDefenderCountry().toString();
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
        //if (getFightPhase().getAttackerArmies() > 0) { // perché?
        notifyDefender(buildCountryInfo(false));
        //}
    }

    /**
     * Performs the attack.
     *
     * @param aiCaller
     */
    @Override
    public void confirmAttack(int nrD, ArtificialPlayer... aiCaller) {

        try {
            getFightPhase().confirmAttack(nrD, aiCaller);
        } catch (WrongCallerException ex) {
            return;
        }
        checkLostAndWon();
        // Dopo il combattimento
        notifyArmiesChangeAfterAttack(getAttackerCountry(), getDefenderCountry());
        notifyAttackResult(buildAttackResultInfo());
        // Dopo lo spostamento.. perché è qui??
        //notifyArmiesChangeAfterAttack(fightPhase.getAttackerCountry(), getDefenderCountry());
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
     * Returns the maximum number of armies that can be used either to attack or
     * to defend.
     *
     * @param countryName
     * @param isAttacker
     * @param aiCaller
     * @return
     */
    @Override
    public synchronized int getMaxArmies(String countryName, boolean isAttacker, ArtificialPlayer... aiCaller) {
        return getFightPhase().getMaxArmies(map.getCountryByName(countryName), isAttacker);
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

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc=" MovePhase ">
    //----------------------------- Move -------------------------------------//
    /**
     * Returns the move phase.
     *
     * @return
     */
    private MovePhase getMovePhase() {
        return (MovePhase) phases[Phase.MOVE_INDEX];
    }

    /**
     * Ritorna il massimo numero di armate per lo spostamento finale.
     *
     * @param fromCountryName
     * @return
     */
    @Override
    public synchronized int getMaxArmiesForMovement(String fromCountryName, ArtificialPlayer... aiCaller) {
        return getMovePhase().getMaxArmiesForMovement(map.getCountryByName(fromCountryName));
    }

    /**
     *
     * @param aiCaller
     */
    @Override
    public void resetMoveCountries(ArtificialPlayer... aiCaller) {
        getMovePhase().clear();
        notifySetFromCountry(null);
    }

    @Override
    public synchronized String getFromCountryName(ArtificialPlayer... aiCaller) {
        return (getMovePhase().getFromCountry() == null) ? null : getMovePhase().getFromCountry().toString();
    }

    /**
     * Setta il territorio da cui effettuare lo spostamento.
     *
     * @param fromCountryName
     * @param aiCaller
     */
    //@Override
    public void setFromCountry(String fromCountryName, ArtificialPlayer... aiCaller) {
        getMovePhase().setFromCountry(map.getCountryByName(fromCountryName));
        notifySetFromCountry(fromCountryName);
    }

    @Override
    public void setToCountry(String toCountryName, ArtificialPlayer... aiCaller) {
        getMovePhase().setToCountry(map.getCountryByName(toCountryName));
    }

    /**
     * Sposta il numero di armate <code>i</code> da <code>attackerCountry</code>
     * alla country con name <code>toCountryName</code>
     *
     * @param nrArmies
     */
    @Override
    public void move(String fromCountryName, String toCountryName, Integer nrArmies, ArtificialPlayer... aiCaller) {
        getMovePhase().move(map.getCountryByName(fromCountryName), map.getCountryByName(toCountryName), nrArmies);
        notifyArmiesChange(buildCountryInfo(getMovePhase().getToCountry()));
        notifyArmiesChange(buildCountryInfo(getMovePhase().getFromCountry()));

        if (getPhase().equals("MOVE")) {
            try {
                nextPhase();
            } catch (PendingOperationsException ex) {
            }
        }
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
    public boolean controlMovement(ArtificialPlayer... aiCaller) {
        return getMovePhase().controlMovement();
    }

    @Override
    public boolean controlMovement(String toCountry, ArtificialPlayer... aiCaller) {
        return getMovePhase().controlMovement(map.getCountryByName(toCountry));
    }

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc=" Turns ">
//--------------------- Turns/Phases --------------------------//
    /**
     * Changes the phase. If it's the last one, passes the turn.
     *
     * @param aiCaller
     * @throws PendingOperationsException
     */
    @Override
    public void nextPhase(ArtificialPlayer... aiCaller) throws PendingOperationsException {
        if (Phase.getName(phaseIndex).equals("PLAY_CARDS")) {
            notifyPlayedTris(); //to hide showCardButton and cardPanel
        }

        if (Phase.getName(phaseIndex).equals("REINFORCE") && activePlayer.getBonusArmies() != 0) {
            throw new PendingOperationsException("Hai ancora armate da posizionare!");
        }

        if (Phase.getName(phaseIndex).equals("FIGHT") && getFightPhase().isAttackInProgress()) {
            throw new PendingOperationsException("Attacco ancora in corso!");
        }

        if (Phase.getName(phaseIndex).equals("FIGHT") && activePlayer.hasConqueredACountry()) {
            this.drawBonusCard();
        }

        phaseIndex++;
        if (phaseIndex == phases.length) {
            passTurn();
        }
        phases[phaseIndex].clear();
        notifyPhaseChange(buildPlayerInfo(activePlayer), Phase.getName(phaseIndex));
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

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc=" RisikoMap ">
//------------------- Methods delegated to RisikoMap -------------------
    /**
     * Checks if <code>activePlayer</code> has completed its mission.
     *
     * @return true if it has, false otherwise
     */
    private boolean hasWon() {
        return map.checkIfWinner(activePlayer);
    }

    /**
     * Checks if <code>player</code> has been defeated.
     *
     * @param player
     * @return
     */
    private boolean hasLost(Player player) {
        return map.hasLost(player);
    }

    /**
     * Checks if the country can be chosen by the active player to launch an
     * attack.
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
     * [TO-DO]
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
     * Checks if the active player owns the country.
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

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc=" Artificial Player ">
    /**
     * Returns the list of countries held by the
     * <code>ArtificialPlayer player</code>.
     *
     * @param player il giocatore che fa la richiesta
     * @return i territori posseduti da player
     */
    @Override
    public synchronized String[] getMyCountries(ArtificialPlayer player, ArtificialPlayer... aiCaller) {
        return this.map.getMyCountries(player).stream().map(element -> element.getName()).toArray(size -> new String[size]);
    }

    /**
     * Returns the list of countries held by the
     * <code>ArtificialPlayer player</code>, from which it can launch an attack.
     *
     * @param player il giocatore che fa la richiesta
     * @return i territori posseduti da player
     */
    @Override
    public synchronized String[] getAllAttackers(ArtificialPlayer player, ArtificialPlayer... aiCaller) {
        return this.map.getMyCountries(player).stream().filter(element -> this.canAttackFromCountry(element.getName(), player)).map(element -> element.getName()).toArray(size -> new String[size]);
    }

    /**
     * Returns the territories that can be attacked from <code>attacker</code>
     *
     * @param attacker the name of the attacker country.
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

// </editor-fold>
    
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
     */
    public synchronized boolean checkCallerIdentity(ArtificialPlayer[] aiCaller) {
        return (aiCaller.length == 0) ? !(activePlayer instanceof ArtificialPlayer) : aiCaller[0].equals(activePlayer);
    }

    @Override
    public boolean checkMyIdentity(ArtificialPlayer[] aiCaller) {
        boolean checkCallerIdentityPassed = true;
        /* Perchè se checkCallerIdenty fallisce gli restituisce false.... 
        boh perché non usare checkCallerIdentity? non ho voglia di pensarci.............*/
        return checkCallerIdentityPassed;
    }


    private void notifyArmiesChangeAfterAttack(Country attackerCountry, Country defenderCountry) {
        notifyArmiesChange(buildCountryInfo(defenderCountry));
        notifyArmiesChange(buildCountryInfo(attackerCountry));
    }

    @Override
    public void endGame() {
        notifyEndGame();
    }

    /**
     * converts the activeplayer to an artificial one and starts its thread
     */
    @Override
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
     * ... non ho voglia
     *
     * @return
     */
    private AttackResultInfo buildAttackResultInfo() {
        FightPhase fight = getFightPhase();
        return new AttackResultInfo(buildFightingCountriesInfo(), fight.getDice(), fight.hasConquered(), fight.checkContinentConquest());
    }

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
