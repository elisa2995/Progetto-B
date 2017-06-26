package risiko.game;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import risiko.players.ArtificialPlayer;
import java.lang.reflect.InvocationTargetException;

/**
 * Class used to handle the invocations to the methods declared by GameProxy.
 * The calls to those methods are filtered, only the valid ones are forwarded to
 * game. Each call to a method is executed in a different thread.
 */
public class GameInvocationHandler implements InvocationHandler {

    private Game game;

    public GameInvocationHandler(Game game) {
        this.game = game;
    }

    /**
     * Method that incercepts and filters the call to any method of game. If the
     * method called returns a boolean or is a void method, the call is
     * forwarded to game only if the caller is the active player. An exception
     * is made for those methods that can be called by the defender, and for
     * generic purpose methods like the ones called in case a human player wants
     * to quit the game or change the speed of the artificial players. These
     * methods are forwarded to game in any case (the controls on these methods
     * are implemented in game).
     *
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        boolean isDefenseMethod = method.getName().equals("confirmAttack") || method.getName().equals("setDefenderArmies");
        boolean isEndGame = method.getName().equals("endGame");
        boolean istoArtificialPlayer = method.getName().equals("toArtificialPlayer") || method.getName().equals("setPlayerSettings");
        if (method.getDeclaringClass() == GameProxy.class && !isDefenseMethod && !istoArtificialPlayer && !isEndGame && (method.getReturnType().equals(boolean.class) || method.getReturnType().equals(Void.TYPE))) {
            ArtificialPlayer[] player = (ArtificialPlayer[]) args[args.length - 1];
            if (!this.game.checkCallerIdentity(player)) {
                return false;
            }
        }
        try {
            return method.invoke(this.game, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
