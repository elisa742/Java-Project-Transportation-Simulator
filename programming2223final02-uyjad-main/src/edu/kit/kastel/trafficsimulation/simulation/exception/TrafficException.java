package edu.kit.kastel.trafficsimulation.simulation.exception;

/**
 * This exception should be thrown if a user input is invalid.
 *
 * @author uyjad
 * @version 1.0
 */
public class TrafficException extends Exception {
    private static final long serialVersionUID = -3691824971265013170L;

    /**
     * Constructs a new instance of traffic exception with error message to be printed.
     *
     * @param message error message to be printed
     */
    public TrafficException(final String message) {
        super(message);
    }

}
