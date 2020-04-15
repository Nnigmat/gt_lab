package players;

import java.util.Random;

/**
 * The CopycatPlayer class. The player each round make move, that opponent made
 * in the previous round. In the first round it chooses spot randomly
 */
public class CopycatPlayer implements Player {

    @Override
    public void reset() {
    }

    @Override
    public int move(int opponentLastMove, int xA, int xB, int xC) {
        // The first move is random
        if (opponentLastMove == 0) {
            Random r = new Random();
            return r.ints(1, 4).findFirst().getAsInt();
        }

        // Return the previous move of the opponent
        return opponentLastMove;
    }
    @Override
    public String getEmail() {
        return "n.nigmatullin@innopolis.ru";
    }
}