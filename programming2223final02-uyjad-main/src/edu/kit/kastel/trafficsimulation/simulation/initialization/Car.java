package edu.kit.kastel.trafficsimulation.simulation.initialization;

import edu.kit.kastel.trafficsimulation.resource.ErrorMessage;
import edu.kit.kastel.trafficsimulation.simulation.exception.ParserException;
import edu.kit.kastel.trafficsimulation.simulation.module.Counter;

/**
 * This class describes a car. It can provide information of the car and change behavior of the car.
 * For instance, update the speed or change the wished direction.
 *
 * @author uyjad
 * @version 1.0
 */
public class Car implements Comparable<Car> {

    // This is the maximum number of outgoing streets can be connected to a crossing.
    private static final int MAXIMUM_NUMBER_OF_STREETS = 4;
    private static final int MINIMUM_WISHED_SPEED = 20;
    private static final int MAXIMUM_WISHED_SPEED = 40;
    private static final int MINIMUM_ACCELERATOR = 1;
    private static final int MAXIMUM_ACCELERATOR = 10;
    private final int id;
    private final int wishedSpeed;
    private final int accelerator;
    private int currentSpeed;
    private final Counter directionCounter;
    private int position;
    private boolean isUpdated;

    /**
     * Constructor of a car.
     *
     * @param idOfCar id of car
     * @param wishedSpeed wished speed of car
     * @param accelerator accelerator of car
     * @throws ParserException if the speed or accelerator is not valid
     */
    public Car(int idOfCar, int wishedSpeed, int accelerator) throws ParserException {
        this.id = idOfCar;
        if (wishedSpeed > MAXIMUM_WISHED_SPEED || wishedSpeed < MINIMUM_WISHED_SPEED) {
            throw new ParserException(ErrorMessage.ILLEGAL_SPEED.toString());
        }
        this.wishedSpeed = wishedSpeed;
        if (accelerator > MAXIMUM_ACCELERATOR || accelerator < MINIMUM_ACCELERATOR) {
            throw new ParserException(ErrorMessage.ILLEGAL_ACCELERATOR.toString());
        }
        this.accelerator = accelerator;
        this.directionCounter = new Counter(MAXIMUM_NUMBER_OF_STREETS);
        this.isUpdated = false;
    }

    /**
     * Constructor of a car with another car.
     *
     * @param carToCopy car to be copied
     */
    public Car(Car carToCopy) {
        this.id = carToCopy.getId();
        this.currentSpeed = carToCopy.getCurrentSpeed();
        this.wishedSpeed = carToCopy.getWishedSpeed();
        this.accelerator = carToCopy.getAccelerator();
        this.directionCounter = carToCopy.getDirectionCounter();
        this.isUpdated = carToCopy.isUpdated();
        this.position = carToCopy.getPosition();
    }

    /**
     * Gets the wished speed of the car.
     *
     * @return the wished speed of the car
     */
    public int getWishedSpeed() {
        return this.wishedSpeed;
    }

    /**
     * Gets the direction counter.
     *
     * @return the direction counter
     */
    public Counter getDirectionCounter() {
        return this.directionCounter;
    }

    /**
     * Checks whether this car is updated.
     *
     * @return true if this car is updated, otherwise false
     */
    public boolean isUpdated() {
        return this.isUpdated;
    }

    /**
     * Gets accelerator of car.
     *
     * @return accelerator of car
     */
    public int getAccelerator() {
        return this.accelerator;
    }

    /**
     * Sets the car as updated.
     */
    public void setAsUpdated() {
        this.isUpdated = true;
    }

    /**
     * Resets the car as "not updated".
     */
    public void reset() {
        this.isUpdated = false;
    }

    /**
     * Gets new speed of car.
     * New speed is the minimum of calculated speed(which is sum of current speed and accelerator) and wished speed.
     *
     * @return new speed of car
     */
    public int getNewSpeed() {
        return Math.min(this.currentSpeed + this.accelerator, this.wishedSpeed);
    }

    /**
     * Gets current speed of car.
     *
     * @return current speed of car
     */
    public int getCurrentSpeed() {
        return this.currentSpeed;
    }

    /**
     * Updates the wished direction of car by increasing the count of direction counter.
     */
    public void updateWishedDirection() {
        this.directionCounter.increaseCount();
    }

    /**
     * Sets the current speed with the given speed.
     *
     * @param newSpeed the new speed to be set as the latest speed
     */
    public void setCurrentSpeed(int newSpeed) {
        this.currentSpeed = newSpeed;
    }

    /**
     * Gets the wished direction of car.
     *
     * @return wished direction of car
     */
    public int getWishedDirection() {
        return this.directionCounter.getCurrentCount();
    }

    /**
     * Increases the position of car by given increment.
     *
     * @param increment amount to add to current position
     */
    public void increasePosition(int increment) {
        this.position += increment;
    }

    /**
     * Gets the position of car.
     *
     * @return the position of car
     */
    public int getPosition() {
        return this.position;
    }

    /**
     * Gets id of car.
     *
     * @return id of car
     */
    public int getId() {
        return this.id;
    }

    /**
     * Sets the position of car with given position.
     *
     * @param newPosition new position
     */
    public void setPosition(int newPosition) {
        this.position = newPosition;
    }

    @Override
    public int compareTo(Car anotherCar) {
        return anotherCar.getPosition() - this.position;
    }

}