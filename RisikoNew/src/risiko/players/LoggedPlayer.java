package risiko.players;

public class LoggedPlayer extends Player{
    
    String username;
    public LoggedPlayer(String name, String color) {
        super(name, color);
        this.username=name;
    }
}
