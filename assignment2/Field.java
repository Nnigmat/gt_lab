/**
 * The Field class consists of 3 spots. Initial values are 1. Each round it
 * accepts moves in method play and based on this increase or decrease values of
 * spots and return payoffs of each player.
 */
public class Field {
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