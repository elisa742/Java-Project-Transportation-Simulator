package edu.kit.kastel.trafficsimulation.simulation.initialization;

import edu.kit.kastel.trafficsimulation.resource.ErrorMessage;
import edu.kit.kastel.trafficsimulation.simulation.exception.ParserException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class collects data about street and checks whether the attributes of street are valid.
 *
 * @author uyjad
 * @version 1.0
 */
public class StreetData {
    private static final int MINIMUM_DISTANCE_BETWEEN_CARS = 10;
    private static final int MINIMUM_LENGTH = 10;
    private static final int MAXIMUM_LENGTH = 10000;
    private static final int MINIMUM_SPEED_ALLOWED = 5;
    private static final int MAXIMUM_SPEED_ALLOWED = 40;
    private final List<Car> cars;
    private final int idOfStartNode;
    private final int idOfEndNode;
    private final int idOfStreetType;
    private final int length;
    private final int maximumSpeed;
    private final int quotaOfCars;

    /**
     * Constructor of street data.
     * Calculates the quota of cars(in other word, maximum number of cars allowed on this street).
     *
     * @param idOfStartNode id of start node
     * @param idOfEndNode id of end node
     * @param idOfStreetType id of street type
     * @param length length of street
     * @param maximumSpeed maximum speed that is allowed
     * @throws ParserException if length or maximum speed is not valid
     */
    public StreetData(int idOfStartNode, int idOfEndNode, int idOfStreetType, int length, int maximumSpeed)
            throws ParserException {
        this.idOfStartNode = idOfStartNode;
        this.idOfEndNode = idOfEndNode;
        this.idOfStreetType = idOfStreetType;

        if (length < MINIMUM_LENGTH || length > MAXIMUM_LENGTH) {
            throw new ParserException(ErrorMessage.ILLEGAL_LENGTH.toString());
        }
        this.length = length;

        if (maximumSpeed > MAXIMUM_SPEED_ALLOWED || maximumSpeed < MINIMUM_SPEED_ALLOWED) {
            throw new ParserException(ErrorMessage.ILLEGAL_SPEED.toString());
        }
        this.maximumSpeed = maximumSpeed;

        this.quotaOfCars = length / MINIMUM_DISTANCE_BETWEEN_CARS + 1;
        this.cars = new ArrayList<>();
    }

    /**
     * Adds car to the list.
     *
     * @param carToAdd car to be added
     */
    public void addCars(Car carToAdd) {
        this.cars.add(carToAdd);
    }

    /**
     * Checks whether the list of cars is full.
     *
     * @return true if the list of cars is full, otherwise false
     */
    public boolean isFullOfCars() {
        return this.cars.size() == this.quotaOfCars;
    }

    /**
     * Gets id of street type.
     *
     * @return id of street type
     */
    public int getIdOfStreetType() {
        return this.idOfStreetType;
    }

    /**
     * Gets cars.
     *
     * @return a list of copied cars
     */
    public List<Car> getCars() {
        List<Car> newCars = new ArrayList<>();
        for (Car carToAdd : this.cars) {
            newCars.add(carToAdd);
        }
        return newCars;
    }

    /**
     * Gets length of street.
     *
     * @return length of street
     */
    public int getLength() {
        return this.length;
    }

    /**
     * Gets id of end node.
     *
     * @return id of end node
     */
    public int getIdOfEndNode() {
        return this.idOfEndNode;
    }

    /**
     * Gets id of start node.
     *
     * @return id of start node
     */
    public int getIdOfStartNode() {
        return this.idOfStartNode;
    }

    /**
     * Gets maximum speed the street allows.
     *
     * @return maximum speed the street allows
     */
    public int getMaximumSpeed() {
        return this.maximumSpeed;
    }

    /**
     * Gets the quota of cars on this street.
     *
     * @return quota of cars on this street
     */
    public int getQuotaOfCars() {
        return this.quotaOfCars;
    }

}
