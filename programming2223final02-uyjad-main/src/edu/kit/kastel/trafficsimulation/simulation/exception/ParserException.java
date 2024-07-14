package edu.kit.kastel.trafficsimulation.simulation.exception;

/**
 * This exception should be thrown if a user input is invalid for parsing.
 *
 * @author uyjad
 * @version 1.0
 */
public class ParserException extends Exception {
    private static final long serialVersionUID = -7432250397320455618L;

    /**
     * Constructs a new instance of parser exception with error message to be printed.
     *
     * @param message error message to be printed
     */
    public ParserException(final String message) {
        super(message);
    }

}
