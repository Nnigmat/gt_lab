package players;

public interface Player {
    void reset();

    int move(int opponentLastMove, int xA, int xB, int xC);

    String getEmail();
}