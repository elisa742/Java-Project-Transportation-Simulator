package edu.kit.kastel.trafficsimulation.simulation.module;

/**
 * This class describes a counter that counts the turns.
 * When the count reaches its maximum value, it restarts.
 *
 * @author uyjad
 * @version 1.0
 */
public class Counter {
    // Limit is the boundary the counter cannot reach.
    private final int limit;
    private int currentCount;

    /**
     * Constructor of a counter.
     * The parameter limit is the boundary the counter cannot reach.
     * In other words, (limit - 1) will be the maximum value the counter can reach.
     *
     * @param limit limit is the boundary the counter cannot reach
     */
    public Counter(int limit) {
        this.limit = limit;
    }

    /**
     * Increases the count.
     * If the counter hits the maximum value(which is limit -1), the counter restarts at 0.
     */
    public void increaseCount() {
        if (this.currentCount >= this.limit - 1) {
            this.currentCount = 0;
        } else {
            this.currentCount++;
        }
    }

    /**
     * Gets the value of current count.
     *
     * @return value of current count
     */
    public int getCurrentCount() {
        return this.currentCount;
    }

    /**
     * Checks whether it is the last count in this round.
     *
     * @return true if it is the last count in this round, otherwise false
     */
    public boolean isEndOfCount() {
        return this.currentCount == this.limit - 1;
    }

}
