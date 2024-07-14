package edu.kit.kastel.trafficsimulation.simulation.exception;

/**
 * This exception should be thrown if during the inquiry of car information user input is invalid.
 *
 * @author uyjad
 * @version 1.0
 */
public class InquiryException extends Exception {
    private static final long serialVersionUID = 393325942172046492L;

    /**
     * Constructs a new instance of inquiry exception with error message to be printed.
     *
     * @param message error message to be printed
     */
    public InquiryException(final String message) {
        super(message);
    }

}
