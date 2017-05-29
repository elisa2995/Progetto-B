package risiko;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import risiko.players.ArtificialPlayer;

public class GameInvocationHandler implements InvocationHandler {

    private Game game;

    public GameInvocationHandler(Game game) {
        this.game = game;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() == GameProxy.class && !method.getName().equals("checkCallerIdentity")) {
            ArtificialPlayer[] player = (ArtificialPlayer[]) args[args.length - 1];
            if (!this.game.checkCallerIdentity(player)) {
                return null;
            }
        }
        return method.invoke(this.game, args);
    }
}
