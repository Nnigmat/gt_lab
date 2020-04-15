import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

interface Player {
    void reset();

    int move(int opponentLastMove, int xA, int xB, int xC);

    String getEmail();
}

/**
 * The ChooseTheBestPlayer class. The player at the each round chooses the best
 * spot
 */
class ChooseTheBestPlayer implements Player {

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
        return "n.nigmatullin@innopolis.ru";
    }

}

/**
 * The ChooseTheMediumPlayer class. The player at each round chooses the medium
 * spot
 */
class ChooseTheMediumPlayer implements Player {

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

/**
 * The ChooseTheWorstPlayer class. The player at the each round chooses the
 * worst spot
 */
class ChooseTheWorstPlayer implements Player {

    @Override
    public void reset() {
    }

    @Override
    public int move(int opponentLastMove, int xA, int xB, int xC) {
        if (xA <= xB && xA <= xC) {
            return 1;
        } else if (xB <= xA && xB <= xC) {
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

/**
 * The CopycatPlayer class. The player each round make move, that opponent made
 * in the previous round. In the first round it chooses spot randomly
 */
class CopycatPlayer implements Player {

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

/**
 * The RandomPlayer class. Each round the player returns random spot
 */
class RandomPlayer implements Player {
    @Override
    public void reset() {
    }

    @Override
    public int move(int opponentLastMove, int xA, int xB, int xC) {
        Random r = new Random();
        return r.ints(1, 4).findFirst().getAsInt();
    }

    @Override
    public String getEmail() {
        return "n.nigmatullin@innopolis.ru";
    }

}

/**
 * The SemiBestRandomPlayer class. The player first nPrevious rounds collect the
 * information about player and chooses the best spot during that time. If the
 * opponent too chooses the best spot change to random selection.
 */
class SemiBestRandomPlayer implements Player {
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

/**
 * The Result class, for storing scores of two players after each round
 */
class Result {
    private double val1, val2 = 0;

    /**
     * Constructor
     * 
     * @param val1
     * @param val2
     */
    public Result(double val1, double val2) {
        this.val1 = val1;
        this.val2 = val2;
    }

    /**
     * Get score of the first player
     * 
     * @return
     */
    public double firstPlayerScore() {
        return this.val1;
    }

    /**
     * Get score of the second player
     * 
     * @return
     */
    public double secondPlayerScore() {
        return this.val2;
    }
}

/**
 * The Field class consists of 3 spots. Initial values are 1. Each round it
 * accepts moves in method play and based on this increase or decrease values of
 * spots and return payoffs of each player.
 */
class Field {
    private int[] field = { 1, 1, 1 };

    /**
     * Calculate sigmoid by given x. Formula: 10 * e^x / (1 + e^x)
     * 
     * @param x
     * @return
     */
    private double sigmoid(int x) {
        double e_pow_x = Math.pow(Math.E, x);
        return 10 * e_pow_x / (1 + e_pow_x);
    }

    /**
     * Decrease value of a field. The value should be greater or equeal to 0
     * 
     * @param index
     */
    private void decreaseFieldValue(int index) {
        this.field[index] = this.field[index] >= 1 ? this.field[index] - 1 : 0;
    }

    /**
     * Increase values of each field except one
     * 
     * @param index
     */
    private void increaseFieldValuesExcept(int index) {
        for (int i = 0; i < field.length; i++) {
            if (i != index) {
                this.field[i] += 1;
            }
        }
    }

    /**
     * Increase values of each field except two
     * 
     * @param index1
     * @param index2
     */
    private void increaseFieldValuesExcept(int index1, int index2) {
        for (int i = 0; i < field.length; i++) {
            if (i != index1 || i != index2) {
                this.field[i] += 1;
            }
        }
    }

    /**
     * Play one round of a game
     * 
     * @param player1Move
     * @param player2Move
     * @return
     */
    public Result play(int player1Move, int player2Move) {
        // If both players chose the same spot decrease its value, increase others one
        // and return 0s to each player
        if (player1Move == player2Move) {
            decreaseFieldValue(player1Move - 1);
            increaseFieldValuesExcept(player1Move - 1);

            return new Result(0, 0);
        }

        // Otherwise calculate score of each player
        double player1Score = sigmoid(this.field[player1Move - 1]) - sigmoid(0);
        double player2Score = sigmoid(this.field[player2Move - 1]) - sigmoid(0);

        // Decrease value of chosen spots in field
        // Increase value of spot that haven't been chosen
        decreaseFieldValue(player1Move - 1);
        decreaseFieldValue(player2Move - 1);
        increaseFieldValuesExcept(player1Move - 1, player2Move - 1);

        // Return each player score
        return new Result(player1Score, player2Score);
    }

    /**
     * Reset field. Make it equals to 1 1 1
     */
    public void reset() {
        this.field = new int[] { 1, 1, 1 };
    }

    /**
     * Get value of A spot
     * 
     * @return
     */
    public int xA() {
        return this.field[0];
    }

    /**
     * Get value of B spot
     * 
     * @return
     */
    public int xB() {
        return this.field[1];
    }

    /**
     * Get value of C spot
     * 
     * @return
     */
    public int xC() {
        return this.field[2];
    }
}

/**
 * Main Testing Class
 */
public class NikitaNigmatullinTesting {
    private static Field field = new Field();
    private static String[] labels = { "Random Player", "Choose best Player", "Choose medium Player",
            "Choose worst Player", "Copycat Player", "SemiBestRandom Player" };
    private static int nRounds = 10;
    private static int nOfGames = 5;

    /**
     * Display scores into terminal, show the title before
     * 
     * @param scores
     * @param title
     */
    private static void showScores(double[] scores, String title) {
        System.out.println(title);
        for (int i = 0; i < title.length(); i++) {
            System.out.print("-");
        }
        System.out.println();
        showScores(scores);
    }

    /**
     * Display scores into terminal
     * 
     * @param scores
     */
    private static void showScores(double[] scores) {
        for (int i = 0; i < labels.length; i++) {
            System.out.printf("%s: %f %n", labels[i], scores[i]);
        }
        System.out.println();
    }

    /**
     * Game between two players
     * 
     * @param first
     * @param second
     * @return
     */
    private static Result play(Player first, Player second) {
        // Reset field
        field.reset();

        // Reset players
        first.reset();
        second.reset();

        // Init of last moves of players
        int player1LastMove = 0, player2LastMove = 0;

        // Scores of players
        double[] scores = new double[2];

        // Play nRounds
        for (int n = 0; n < nRounds; n++) {
            // Get current value of A, B and C in Field
            int xA = field.xA(), xB = field.xB(), xC = field.xC();

            // Players make their moves based on current values of field
            // and other's player last move
            int player1Move = first.move(player1LastMove, xA, xB, xC);
            int player2Move = second.move(player2LastMove, xA, xB, xC);

            // Memorize last move of each player
            player1LastMove = player1Move;
            player2LastMove = player2Move;

            // Play the round based on players moves
            Result result = field.play(player1Move, player2Move);

            // Store the score of each player
            scores[0] += result.firstPlayerScore();
            scores[1] += result.secondPlayerScore();
        }

        return new Result(scores[0], scores[1]);
    }

    /**
     * Games between all players in `players` list
     * 
     * @param players
     * @return
     */
    private static double[] playBetweenAll(ArrayList<Player> players) {
        double[] scores = new double[players.size()];

        // Begin of the games. Each one play with another ones
        for (int i = 0; i < players.size(); i++) {
            for (int j = i + 1; j < players.size(); j++) {
                double[] tmpScores = new double[2];
                for (int n = 0; n < nOfGames; n++) {
                    Result result = play(players.get(i), players.get(j));

                    // Update tmpScores
                    tmpScores[0] += result.firstPlayerScore();
                    tmpScores[1] += result.firstPlayerScore();
                }

                // Update scores
                scores[i] += tmpScores[0] / nOfGames;
                scores[j] += tmpScores[1] / nOfGames;
            }
        }

        return scores;
    }

    /**
     * Create players with different amounts and play with each outher
     * 
     * @param playerTypes
     * @param denses
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private static double[] playMultipleDensesPlayers(ArrayList<Player> playerTypes, int[] denses)
            throws InstantiationException, IllegalAccessException {
        ArrayList<Player> densePlayers = new ArrayList<Player>();

        // Create array of players
        for (int i = 0; i < denses.length; i++) {
            for (int j = 0; j < denses[i]; j++) {
                densePlayers.add(playerTypes.get(i).getClass().newInstance());
            }
        }

        // Play games between players
        double[] scores = playBetweenAll(densePlayers);
        double[] res = new double[playerTypes.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = 0;
        }

        // Calculate the average score per type
        int index = 0;
        for (int i = 0; i < denses.length; i++) {
            double tempScore = 0;
            for (int j = 0; j < denses[i]; j++) {
                tempScore += scores[index];
                index++;
            }
            res[i] = tempScore / denses[i];
        }

        return res;
    }

    private static int[] createDistribution(int total, int classes) {
        int[] result = new int[classes];
        ArrayList<Integer> classesArr = new ArrayList<>();
        for (int i = 0; i < classes; i++) {
            classesArr.add(i);
        }

        Random r = new Random();
        for (int i = 0; i < classes; i++) {
            int index = r.nextInt(classesArr.size());
            int classNum = classesArr.get(index);
            classesArr.remove(index);

            result[classNum] = r.ints(0, total + 1).findFirst().getAsInt();
            total -= result[classNum];
        }

        return result;
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        ArrayList<Player> playerTypes = new ArrayList<Player>(
                Arrays.asList(new RandomPlayer(), new ChooseTheBestPlayer(), new ChooseTheMediumPlayer(),
                        new ChooseTheWorstPlayer(), new CopycatPlayer(), new SemiBestRandomPlayer()));

        showScores(playBetweenAll(playerTypes), "1 player of each type");

        showScores(playMultipleDensesPlayers(playerTypes, new int[] { 75, 15, 15, 15, 15, 15 }),
                "75 Random players, 15 of other types");

        showScores(playMultipleDensesPlayers(playerTypes, new int[] { 15, 75, 15, 15, 15, 15 }),
                "75 Choose best players, 15 of other types");

        showScores(playMultipleDensesPlayers(playerTypes, new int[] { 15, 15, 75, 15, 15, 15 }),
                "75 Choose medium players, 15 of other types");

        showScores(playMultipleDensesPlayers(playerTypes, new int[] { 15, 15, 15, 75, 15, 15 }),
                "75 Choose worst players, 15 of other types");

        showScores(playMultipleDensesPlayers(playerTypes, new int[] { 15, 15, 15, 15, 75, 15 }),
                "75 Copycat players, 15 of other types");

        showScores(playMultipleDensesPlayers(playerTypes, new int[] { 15, 15, 15, 15, 15, 75 }),
                "75 SemiBestRandom players, 15 of other types");

        double[] averageScores = new double[playerTypes.size()];
        for (int i = 0; i < averageScores.length; i++) {
            averageScores[i] = 0;
        }

        for (int i = 0; i < 100000; i++) {
            int[] dense = createDistribution(150, playerTypes.size());
            double[] scores = playMultipleDensesPlayers(playerTypes, dense);

            for (int j = 0; j < scores.length; j++) {
                if (!Double.isNaN(scores[j]))
                    averageScores[j] += scores[j] / 1000;
            }
        }
        showScores(averageScores, "100000 Randomly distributed number of players");
    }
}
