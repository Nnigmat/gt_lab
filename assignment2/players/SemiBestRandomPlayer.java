package players;

import java.util.ArrayList;
import java.util.Random;

/**
 * The SemiBestRandomPlayer class. The player first nPrevious rounds collect the
 * information about player and chooses the best spot during that time. If the
 * opponent too chooses the best spot change to random selection.
 */
public class SemiBestRandomPlayer implements Player {
    private enum Type {
        PREPARE, BEST, RANDOM;
    }

    private int round = 0;
    private int nPrevious = 3;
    private ArrayList<Integer> opponentLastMoves = new ArrayList<Integer>();
    private ArrayList<Integer> ourLastMoves = new ArrayList<Integer>();

    private Type type = Type.PREPARE;

    /**
     * Return index of the biggest element
     * 
     * @param xA
     * @param xB
     * @param xC
     * @return
     */
    private int max(int xA, int xB, int xC) {
        if (xA >= xB && xA >= xC) {
            return 1;
        } else if (xB >= xA && xB >= xC) {
            return 2;
        } else {
            return 3;
        }
    }

    /**
     * Reset the player's state
     */
    @Override
    public void reset() {
        this.round = 0;
        this.opponentLastMoves = new ArrayList<Integer>();

        this.ourLastMoves = new ArrayList<Integer>();
        this.ourLastMoves.add(0);
        this.type = Type.PREPARE;
    }

    @Override
    public int move(int opponentLastMove, int xA, int xB, int xC) {
        switch (this.type) {
            case PREPARE:
                // If played rounds less than 3, choose the best field cell
                this.opponentLastMoves.add(opponentLastMove);
                this.round += 1;

                int move = max(xA, xB, xC);

                if (round > this.nPrevious) {
                    if (this.ourLastMoves.equals(this.opponentLastMoves)) {
                        this.type = Type.RANDOM;
                    } else {
                        this.type = Type.BEST;
                    }
                }

                this.ourLastMoves.add(move);
                return move;
            case BEST:
                return max(xA, xB, xC);
            case RANDOM:
                Random r = new Random();
                return r.ints(1, 4).findFirst().getAsInt();
            default:
                return max(xA, xB, xC);
        }
    }

    @Override
    public String getEmail() {
        return "n.nigmatullin@innopolis.ru";
    }
}