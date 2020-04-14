import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import players.ChooseTheBestPlayer;
import players.ChooseTheMediumPlayer;
import players.ChooseTheWorstPlayer;
import players.CopycatPlayer;
import players.Player;
import players.RandomPlayer;
import players.SemiBestRandomPlayer;

public class Testing {
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
