package risiko.equipment;

public enum Card{

    INFANTRY(13), CAVALRY(13), ARTILLERY(13), WILD(3);

    private final int amount; // Il numero di carte di quel tipo nel mazzo

    private Card(int n) {
        this.amount = n;
    }
    
    public int getAmount(){
        return amount;
    }
}
