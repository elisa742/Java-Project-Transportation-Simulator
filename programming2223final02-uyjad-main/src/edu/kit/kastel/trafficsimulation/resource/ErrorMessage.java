package edu.kit.kastel.trafficsimulation.resource;

/**
 * This class stores all error messages and adjusts the format of the error message.
 *
 * @author uyjad
 * @version 1.0
 */
public enum ErrorMessage {

    /**
     * If the start node and end node of a street is same, the error message will be printed.
     */
    INVALID_STREET_WITH_SAME_NODES("the start node and end node should be different."),

    /**
     * If string input does not match the regex and cannot be parsed into a node, the error message will be printed.
     */
    INVALID_NODE_DATA("cannot parse it into a node."),

    /**
     * If string input does not match the regex and cannot be parsed into a street, the error message will be printed.
     */
    INVALID_STREET_DATA("cannot parse it into a street"),

    /**
     * If the street id is not valid, the error message will be printed.
     */
    ILLEGAL_STREET_ID("this street id is not valid."),

    /**
     * If the street is full of cars, the error message will be printed.
     * Expects two format arguments: the street id and quota of cars on that street.
     */
    STREET_IS_FULL("Street %d cannot have more than %d cars."),

    /**
     * If the accelerator is not within range, the error message will be printed.
     */
    ILLEGAL_ACCELERATOR("the accelerator is not valid."),

    /**
     * Node must have at least one incoming street and one outgoing street, otherwise the error message will be printed.
     */
    NO_STREET_CONNECTED("the node must have at least one incoming street and one outgoing street."),

    /**
     * If the duration is not within the range, the error message will be printed.
     */
    ILLEGAL_DURATION("the duration is not valid."),

    /**
     * If the speed is not within the range, the error message will be printed.
     */
    ILLEGAL_SPEED("the speed is not valid."),

    /**
     * If the node is connected to four incoming streets already, the error message will be printed.
     */
    ILLEGAL_CONNECTION_TO_INCOMING_STREET("the node is connected to four incoming streets already."),

    /**
     * If the node is connected to four outgoing streets already, the error message will be printed.
     */
    ILLEGAL_CONNECTION_TO_OUTGOING_STREET("the node is connected to four outgoing streets already."),

    /**
     * If node is not found, the error message will be printed.
     */
    NODE_NOT_FOUND("node does not exist."),

    /**
     * If the id already exists, the error message will be printed.
     */
    ILLEGAL_NEW_ID("id already exists."),

    /**
     * If the input length is not within the expected range, the error message will be printed.
     */
    ILLEGAL_LENGTH("length is not valid."),

    /**
     * If the string input does not match the regex of a car, the error message will be printed.
     */
    ILLEGAL_CAR_FORMAT("the input of this car is not valid."),

    /**
     * If this id of car does not exist, the error message will be printed.
     * Expects one format argument: the invalid id of car.
     */
    ID_NOT_FOUND("There is no car with the identifier %d."),

    /**
     * If input is not valid, the error message will be printed.
     */
    INPUT_NOT_VALID("input ist not valid."),

    /**
     * If the street network is not fully set up, the error message will be printed.
     */
    INCOMPLETE_SET_UP("Street network is yet to be loaded."),

    /**
     * If a string could not be parsed to an integer, the error message will be printed.
     * Expects one format argument: the illegal string.
     */
    ILLEGAL_INTEGER("cannot parse %s into an integer.");

    private static final String PREFIX = "Error: ";
    private final String message;

    /**
     * Constructs an error message.
     *
     * @param message the content of the message
     */
    ErrorMessage(String message) {
        this.message = message;
    }

    /**
     * Adjusts the format of the error message with the user given input.
     *
     * @param args user given input that is needed for the error message
     * @return the format adjusted error message
     */
    public String format(Object... args) {
        return PREFIX + String.format(this.message, args);
    }

    @Override
    public String toString() {
        return PREFIX + this.message;
    }

}
