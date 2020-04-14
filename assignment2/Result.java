/**
 * The Result class, for storing scores of two players after each round
 */
public class Result {
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