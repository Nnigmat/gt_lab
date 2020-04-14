package players;

import java.util.Random;

/**
 * The RandomPlayer class. Each round the player returns random spot
 */
public class RandomPlayer implements Player {
    @Override
    public void reset() {
    }

    @Override
    public int move(int opponentLastMove, int xA, int xB, int xC) {
        Random r = new Random();
        return r.ints(1, 4).findFirst().getAsInt();
    }
}