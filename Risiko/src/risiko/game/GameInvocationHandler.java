package risiko.game;

import risiko.game.GameProxy;
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
     * Se viene invocato un metodo che esegue un'azione di attacco, difesa, passaggio turno
     * viene richiesto un permesso al semaforo in game in modo che non sia possibile eseguire una di queste azioni
     * durante il timeout, o in contemporanea fra di loro.
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     * @throws Exception 
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable, Exception {
        boolean isCommandMethod = method.getName().equals("setAttackerCountry")
                || method.getName().equals("setDefenderCountry")
                || method.getName().equals("setDefenderArmies")
                || method.getName().equals("setAttackerArmies")
                || method.getName().equals("declareAttack")
                || method.getName().equals("confirmAttack")
                || method.getName().equals("reinforce")
                || method.getName().equals("nextPhase");

        boolean isPermitGranted = true;

        if (isCommandMethod) {
            isPermitGranted = game.tryToAcquirePermit();
        }

        if (isPermitGranted) {
            boolean isDefenseMethod = method.getName().equals("confirmAttack") || method.getName().equals("setDefenderArmies");
            if (method.getDeclaringClass() == GameProxy.class && !isDefenseMethod && (method.getReturnType().equals(boolean.class) || method.getReturnType().equals(Void.TYPE))) {
                ArtificialPlayer[] player = (ArtificialPlayer[]) args[args.length - 1];
                if (!this.game.checkCallerIdentity(player)) {
                    //non rilasciare i permessi nel caso non siano stati assegnati
                    //i permessi non hanno nessun controllo su chi li rilasci
                    if (isCommandMethod) {
                        game.releasePermit();
                    }
                    return false;
                }
            }
            try {
                return method.invoke(this.game, args);
            } catch (InvocationTargetException e) {
                throw e.getCause();// Per esempio PendingOperationsException
            } finally {
                if (isCommandMethod) {
                    game.releasePermit();
                }
            }
        } else {
            return false;
        }
    }
}

