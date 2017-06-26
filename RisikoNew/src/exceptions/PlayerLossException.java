package exceptions;

/**
 * This exception is thrown if after an attack with conquest the defender player
 * has lost, so if he has no more countries.
 *
 */
public class PlayerLossException extends Exception {

    private String loserPlayer;

    /**
     * Creates a new instance of <code>ConquestException</code> without detail
     * message.
     */
    public PlayerLossException() {
    }

    /**
     * Constructs an instance of <code>ConquestException</code> with the name of
     * the player that has lost.
     *
     * @param loserPlayer
     */
    public PlayerLossException(String loserPlayer) {
        this.loserPlayer = loserPlayer;
    }
    
    /**
     * Returns the name of the player that has lost
     * @return 
     */
    public String getLoserPlayer(){
        return loserPlayer;
    }

}
