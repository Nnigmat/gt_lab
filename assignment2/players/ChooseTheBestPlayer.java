package players;

/**
 * The ChooseTheBestPlayer class. The player at the each round chooses the best
 * spot
 */
public class ChooseTheBestPlayer implements Player {

    @Override
    public void reset() {
    }

    @Override
    public int move(int opponentLastMove, int xA, int xB, int xC) {
        if (xA >= xB && xA >= xC) {
            return 1;
        } else if (xB >= xA && xB >= xC) {
            return 2;
        } else {
            return 3;
        }
    }

    @Override
    public String getEmail() {
        // TODO Auto-generated method stub
        return "n.nigmatullin@innopolis.ru";
    }

}