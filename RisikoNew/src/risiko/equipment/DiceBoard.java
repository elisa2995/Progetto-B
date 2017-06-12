package risiko.equipment;

import java.util.Arrays;
import java.util.Random;

public class DiceBoard {

    private int[] attackerDice, defenderDice;

    public void rollAllDice(int nrA, int nrD) {
        attackerDice = rollDice(nrA);
        defenderDice = rollDice(nrD);
    }

    /**
     * Rolls <code>nrDice</code> dice and returns their value in descending
     * order.
     *
     * @param nrDice
     */
    private int[] rollDice(int nr) {
        int dice[] = new int[nr];
        int tmp;
        for (int i = 0; i < nr; i++) {
            dice[i] = rollDie();
        }
        Arrays.sort(dice);
        if (nr > 1) {
            tmp = dice[0];
            dice[0] = dice[nr - 1];
            dice[nr - 1] = tmp;
        }
        return dice;
    }

    /**
     * Rolls a die and returns its result.
     *
     * @return a random number between 1 and 6 (inclusive).
     */
    private int rollDie() {
        return 1 + new Random().nextInt(6);
    }

    /**
     * Returns both the set of attackDice and defenseDice.
     *
     * @return
     */
    public int[][] getDice() {
        return new int[][]{attackerDice, defenderDice};
    }

    public int[] getAttackerDice() {
        return attackerDice;
    }

    public int[] getDefenderDice() {
        return defenderDice;
    }
}
