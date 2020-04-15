package players;

/**
 * The ChooseTheMediumPlayer class. The player at each round chooses the medium
 * spot
 */
public class ChooseTheMediumPlayer implements Player {

    @Override
    public void reset() {
    }

    @Override
    public int move(int opponentLastMove, int xA, int xB, int xC) {
        if (xA >= xB && xA <= xC) {
            return 1;
        } else if (xB >= xA && xB <= xC) {
            return 2;
        } else {
            return 3;
        }
    }
    @Override
    public String getEmail() {
        return "n.nigmatullin@innopolis.ru";
    }
}