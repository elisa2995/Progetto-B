package risiko.game;

import risiko.game.Game;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import risiko.players.ArtificialPlayer;
import java.lang.reflect.InvocationTargetException;

public class GameInvocationHandler implements InvocationHandler {

    private Game game;

    public GameInvocationHandler(Game game) {
        this.game = game;
    }

    /**
     * Metodo che intercetta tutte le chiamate ai metodi boolean e void di game. 
     * Nel caso tali metodi siano dichiarati in GameProxy e non siano metodi
     * di difesa, viene controllato il caller del metodo. Se il caller è il 
     * giocatore di turno, viene invocato il metodo di game. 
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     * @throws Exception 
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable, Exception {
        boolean isDefenseMethod = method.getName().equals("confirmAttack") || method.getName().equals("setDefenderArmies");
        boolean isEndGame= method.getName().equals("endGame");
        boolean istoArtificialPlayer = method.getName().equals("toArtificialPlayer");
        if (method.getDeclaringClass() == GameProxy.class && !isDefenseMethod && !istoArtificialPlayer && !isEndGame && (method.getReturnType().equals(boolean.class) || method.getReturnType().equals(Void.TYPE))) {
            ArtificialPlayer[] player = (ArtificialPlayer[]) args[args.length - 1];
            if (!this.game.checkCallerIdentity(player)) {
                return false;
            }
        }
        try {
            return method.invoke(this.game, args);
        } catch (InvocationTargetException e) { 
            throw e.getCause();// Per esempio PendingOperationsException
        }
    }
}

