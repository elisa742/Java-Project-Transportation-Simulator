package edu.kit.kastel.trafficsimulation.simulation.module;

/**
 * This class describes green light which counts both the duration of light and indicator of allowed incoming street.
 *
 * @author uyjad
 * @version 1.0
 */
public class GreenLight {

    //This counter counts the time lapsed in each round of green light duration.
    private final Counter durationCounter;
    private final Counter indicatorCounter;
    private final int numberOfIncomingStreets;

    /**
     * Constructor of green light with duration and number of incoming streets.
     *
     * @param duration duration of green light
     * @param numberOfIncomingStreets number of incoming streets
     */
    public GreenLight(int duration, int numberOfIncomingStreets) {
        this.numberOfIncomingStreets = numberOfIncomingStreets;
        this.durationCounter = new Counter(duration);
        this.indicatorCounter = new Counter(numberOfIncomingStreets);
    }

    /**
     * Constructor of green light with existing green light.
     *
     * @param greenLight green light to be copied
     */
    public GreenLight(GreenLight greenLight) {
        this.durationCounter = greenLight.getDurationCounter();
        this.numberOfIncomingStreets = greenLight.getNumberOfIncomingStreets();
        this.indicatorCounter = greenLight.getStreetIndicatorCounter();
    }

    /**
     * Updates the duration by increasing the count of time lapsed.
     */
    public void updateDuration() {
        this.durationCounter.increaseCount();
    }

    /**
     * Gets the street indicator counter.
     *
     * @return the street indicator counter
     */
    public Counter getStreetIndicatorCounter() {
        return this.indicatorCounter;
    }

    /**
     * Gets duration counter.
     *
     * @return duration counter
     */
    public Counter getDurationCounter() {
        return this.durationCounter;
    }

    /**
     * Updates the street indicator by increasing the count.
     */
    public void updateStreetIndicator() {
        this.indicatorCounter.increaseCount();
    }

    /**
     * Checks whether it is end of duration now.
     *
     * @return true if it is end of duration now, otherwise false
     */
    public boolean isEndOfDuration() {
        return this.durationCounter.isEndOfCount();
    }

    /**
     * Gets the current street indicator.
     *
     * @return the current street indicator
     */
    public int getCurrentStreetIndicator() {
        return this.indicatorCounter.getCurrentCount();
    }

    /**
     * Gets the number of incoming streets.
     *
     * @return the number of incoming streets
     */
    public int getNumberOfIncomingStreets() {
        return this.numberOfIncomingStreets;
    }

}
