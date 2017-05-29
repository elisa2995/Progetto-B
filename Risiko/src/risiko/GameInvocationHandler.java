package risiko;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import risiko.players.ArtificialPlayer;
import exceptions.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

public class GameInvocationHandler implements InvocationHandler {

    private Game game;

    public GameInvocationHandler(Game game) {
        this.game = game;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable, Exception {
        if (method.getDeclaringClass() == GameProxy.class && !method.getName().equals("confirmAttack") && !method.getName().equals("setDefenderArmies") && (method.getReturnType().equals(boolean.class) || method.getReturnType().equals(Void.TYPE))) {
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

