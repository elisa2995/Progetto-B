package risiko.players;

/**
 * A logged player.
 */
public class LoggedPlayer extends Player {

    String username;
    int points;

    public LoggedPlayer(String name, String color) {
        super(name, color);
        this.username = name;
        points = 0;
    }

    public String getUsername() {
        return this.username;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return this.points;
    }
}
