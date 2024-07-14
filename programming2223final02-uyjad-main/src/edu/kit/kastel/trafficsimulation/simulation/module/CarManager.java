package edu.kit.kastel.trafficsimulation.simulation.module;

import edu.kit.kastel.trafficsimulation.simulation.initialization.Car;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This class describes a car manager that can execute command on cars, such as adding, moving or deleting cars.
 *
 * @author uyjad
 * @version 1.0
 */
public class CarManager {
    private static final int MINIMUM_DISTANCE_BETWEEN_CARS = 10;
    private final int lengthOfEdge;
    private final int maximumSpeed;
    private final LinkedList<Car> carList = new LinkedList<>();

    /**
     * Constructor of car manager.
     * The position of car will be initialized accordingly.
     * For instance, first car will be put at the end of edge.
     * The next car will be put backwards while the requirement of minimum distance between cars is fulfilled.
     *
     * @param cars cars to be managed
     * @param lengthOfEdge length of edge
     * @param maximumSpeed maximum speed on this edge
     */
    public CarManager(List<Car> cars, int lengthOfEdge, int maximumSpeed) {
        for (int i = 0; i < cars.size(); i++) {
            Car newCar = cars.get(i);
            newCar.setPosition(lengthOfEdge - MINIMUM_DISTANCE_BETWEEN_CARS * i);
            this.carList.add(newCar);
        }
        this.lengthOfEdge = lengthOfEdge;
        this.maximumSpeed = maximumSpeed;
    }

    /**
     * Gets car by id.
     *
     * @param carOfID id of car to be checked
     * @return car if it is found, otherwise return null
     */
    public Car getCarByID(int carOfID) {
        for (Car carToSearch : this.carList) {
            if (carToSearch.getId() == carOfID) {
                return carToSearch;
            }
        }
        return null;
    }

    /**
     * Updates the position and speed of cars. Cars will only move within this edge.
     *
     * @param isOvertakingAllowed boolean shows whether it is allowed to overtake
     */
    public void updateCarsInternally(boolean isOvertakingAllowed) {
        for (int i = 0; i < this.carList.size(); i++) {
            Collections.sort(this.carList);
            Car carToUpdate = this.carList.get(i);

            if (carToUpdate.isUpdated()) {
                continue;
            }

            carToUpdate.setAsUpdated();
            int currentPosition = carToUpdate.getPosition();

            // If the car already stands at the end, no need to move. Set the speed as 0.
            if (currentPosition == this.lengthOfEdge) {
                carToUpdate.setCurrentSpeed(0);
                continue;
            }

            int speed = Math.min(carToUpdate.getNewSpeed(), this.maximumSpeed);
            carToUpdate.setCurrentSpeed(speed);
            // If it is the first car(the one closet to end node), need to consider comparison with the length of edge.
            if (i == 0) {
                carToUpdate.setPosition(Math.min(speed + currentPosition, this.lengthOfEdge));
                continue;
            }

            int positionOfFrontCar = this.carList.get(i - 1).getPosition();
            if (isOvertakingAllowed) {
                // Assume that overtaking happens.
                // Calculate remaining distance between car and front car after deduction of required minimum distance.
                int remainingDistance = currentPosition + speed - positionOfFrontCar - MINIMUM_DISTANCE_BETWEEN_CARS;

                // If it is the second car (which means it is second closet to the end node),
                // then check if the minimum distance between car and end of edge still holds after overtaking.
                if (i == 1) {
                    if (remainingDistance >= 0
                            && this.lengthOfEdge - positionOfFrontCar >= MINIMUM_DISTANCE_BETWEEN_CARS) {
                        carToUpdate.setPosition(Math.min(currentPosition + speed, this.lengthOfEdge));
                        continue;
                    }
                } else {
                    // If it is not the second car, check if minimum distance between cars still holds after overtaking.
                    int limit = this.carList.get(i - 2).getPosition();
                    if (remainingDistance >= 0 && (limit - positionOfFrontCar >= 2 * MINIMUM_DISTANCE_BETWEEN_CARS)) {
                        carToUpdate.setPosition(Math.min(currentPosition + speed,
                                limit - MINIMUM_DISTANCE_BETWEEN_CARS));
                        continue;
                    }
                }
            }

            // If overtaking is not allowed or overtaking does not happen, the car will be updated as following.
            int allowedMovement = positionOfFrontCar - MINIMUM_DISTANCE_BETWEEN_CARS - currentPosition;
            // Check whether the car can meet the minimum distance condition. If not, do not move and set speed as 0.
            if (allowedMovement <= 0) {
                carToUpdate.setCurrentSpeed(0);
            } else {
                carToUpdate.increasePosition(Math.min(allowedMovement, speed));
            }
        }
    }

    /**
     * The purpose of this method is to check whether there is space for a new car.
     * Gets the position of the car that is closet to the start node of the edge.
     * If edge is empty, return -1.
     * If the result is less than 10 meters, it means that there is not enough space.
     *
     * @return -1 if the edge is empty, otherwise return position of the car that is closet to the start node
     */
    public int getLastCarPosition() {
        if (this.carList.isEmpty()) {
            return -1;
        }
        Collections.sort(this.carList);
        return this.carList.getLast().getPosition();
    }

    /**
     * Gets the wished distance to drive on new street of the car that is closet to the end of edge.
     *
     * @return the wished distance to drive on new street
     */
    public int getFirstCarWishedRemainingDistance() {
        Collections.sort(this.carList);
        return (this.carList.getFirst().getPosition() + calculateSpeedOfFirstCar() - this.lengthOfEdge);
    }

    /**
     * Calculates speed of first car that is closet to the end of edge.
     *
     * @return speed of first car that is closet to the end of edge
     */
    public int calculateSpeedOfFirstCar() {
        return Math.min(this.carList.getFirst().getNewSpeed(), this.maximumSpeed);
    }

    /**
     * Updates the speed of first car that is closet to the end of edge.
     */
    public void updateSpeedOfFirstCar() {
        this.carList.getFirst().setCurrentSpeed(calculateSpeedOfFirstCar());
    }

    /**
     * Adds a new car.
     *
     * @param carToAdd car to be added
     * @param wishedDistance the distance the new car wishes to drive
     */
    public void addCar(Car carToAdd, int wishedDistance) {
        if (isEmpty()) {
            carToAdd.setPosition(wishedDistance);
        } else {
            carToAdd.setPosition(Math.min(wishedDistance, getLastCarPosition() - MINIMUM_DISTANCE_BETWEEN_CARS));
        }
        this.carList.add(carToAdd);
    }

    /**
     * Gets the car that is closet to the end of edge.
     *
     * @return the car that is closet to the end of edge
     */
    public Car getFirstCar() {
        return this.carList.getFirst();
    }

    /**
     * Removes the car that is closet to the end of edge.
     */
    public void removeFirstCar() {
        this.carList.removeFirst();
    }

    /**
     * Resets status of all cars as "not updated". So cars are ready for updates next tick.
     */
    public void reset() {
        for (Car carToReset : this.carList) {
            carToReset.reset();
        }
    }

    /**
     * Checks whether all cars are updated.
     *
     * @return true if all cars are updated, otherwise return false
     */
    public boolean isAllUpdated() {
        for (Car carToCheck : this.carList) {
            if (!carToCheck.isUpdated()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether the edge is empty.
     *
     * @return true if the edge is empty, otherwise return false
     */
    public boolean isEmpty() {
        return this.carList.isEmpty();
    }

    /**
     * Checks whether the first car is at the end of edge.
     *
     * @return true if the first car is at the end of edge, otherwise return false
     */
    public boolean isCarAtTheEnd() {
        return this.carList.getFirst().getPosition() == this.lengthOfEdge;
    }

}
