
import java.util.Arrays;
import risiko.BonusDeck;


public class BonusDeckTest {

    public static void main(String[] args) throws Exception {
        BonusDeck bonus = new BonusDeck();
        
        for(int i = 1; i<100; i++){
            System.out.println(bonus.drawCard());
        }
    }

}
