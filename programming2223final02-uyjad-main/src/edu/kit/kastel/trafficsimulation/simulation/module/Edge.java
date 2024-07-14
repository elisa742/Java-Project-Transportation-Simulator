package edu.kit.kastel.trafficsimulation.simulation.module;

import edu.kit.kastel.trafficsimulation.simulation.initialization.Car;
import edu.kit.kastel.trafficsimulation.simulation.initialization.StreetData;

/**
 * This class describes an edge that represents the street. So it shares some attributes of street.
 * (Edge and street are mentioned interchangeably in this programming task)
 * But the edge here has only one end node as attribute. Start node is not included.
 * The edge can add, move and remove cars. Also, it can retrieve information of the cars.
 *
 * @author uyjad
 * @version 1.0
 */
public class Edge {
    private final Node endNode;
    private final StreetType type;
    private final int length;
    private final CarManager carManager;
    private boolean hasGreenLightAccess;
    private boolean allCarStay;
    private final int id;

    /**
     * Constructor of an edge.
     *
     * @param id id of edge
     * @param streetData pre street that edge is built upon
     * @param endNode end node of the edge
     */
    public Edge(int id, StreetData streetData, Node endNode) {
        this.id = id;
        this.type = StreetType.getStreetTypeFromString(streetData.getIdOfStreetType());
        this.length = streetData.getLength();
        this.endNode = endNode;
        this.carManager = new CarManager(streetData.getCars(), streetData.getLength(), streetData.getMaximumSpeed());
        this.allCarStay = false;
        this.hasGreenLightAccess = false;
    }

    /**
     * Adds car to the edge.
     *
     * @param car car to add
     * @param wishedDistance car's wished distance to travel
     */
    public void addCar(Car car, int wishedDistance) {
        this.carManager.addCar(car, wishedDistance);
    }

    /**
     * Gets the car by id.
     *
     * @param idOfCar id of the car to be searched
     * @return the car that matches the id
     */
    public Car getCarByID(int idOfCar) {
        return this.carManager.getCarByID(idOfCar);
    }

    /**
     * Gets the wished distance to travel on next street.
     * Formula for calculation:
     * wished distance on next street = car's wished travel distance - the length of the current street.
     *
     * @return the wished distance to travel on next street
     */
    public int getFirstCarWishedDistance() {
        return this.carManager.getFirstCarWishedRemainingDistance();
    }

    /**
     * Updates the speed of first car on this edge.
     */
    public void updateSpeedOfFirstCar() {
        this.carManager.updateSpeedOfFirstCar();
    }

    /**
     * Gets the id of edge.
     *
     * @return id of edge
     */
    public int getId() {
        return this.id;
    }

    /**
     * Removes the first car.
     */
    public void removeFirstCar() {
        this.carManager.removeFirstCar();
    }

    /**
     * Updates the movement and speed of car on this edge.
     * If on this edge car can overtake other cars, then car manager will allow overtaking during updates.
     */
    public void updateInternally() {
        this.carManager.updateCarsInternally(isOvertakingAllowed());
    }

    /**
     * Gets the end node of this edge.
     *
     * @return end node of this edge
     */
    public Node getEndNode() {
        return this.endNode;
    }

    /**
     * Gets length of this edge.
     *
     * @return length of this edge
     */
    public int getLength() {
        return this.length;
    }

    /**
     * Gets the first car (the one that travels the farthest on this edge).
     *
     * @return the first car on this edge.
     */
    public Car getCar() {
        return this.carManager.getFirstCar();
    }

    /**
     * The purpose of this method is to check whether there is space for a new car.
     * Gets the position of the car that is closet to the start node of the edge.
     * If edge is empty, return -1.
     * If the result is less than 10 meters, it means that there is not enough space.
     *
     * @return the position of the last car. If edge is empty, return -1.
     */
    public int getLastCarPosition() {
        return this.carManager.getLastCarPosition();
    }

    /**
     * Resets status of all cars as "not updated", so cars are ready for next update.
     * Resets the status of "all car stay", so it is possible for car to move to next edge during next update.
     */
    public void reset() {
        this.carManager.reset();
        this.allCarStay = false;
    }

    /**
     * Checks whether all cars are updated.
     *
     * @return true if all cars are updated, otherwise return false
     */
    public boolean isFullyUpdated() {
        return this.carManager.isAllUpdated();
    }

    /**
     * Checks whether the edge is empty.
     *
     * @return true if the edge is empty, otherwise return false
     */
    public boolean isEmpty() {
        return this.carManager.isEmpty();
    }

    /**
     * Checks whether all cars will stay on this edge.
     * The purpose of this method is :
     * If boolean is true, then during updates we can skip checking whether cars are capable of heading next edge.
     *
     * @return true if all cars will stay on this edge, otherwise false
     */
    public boolean allCarsStay() {
        return this.allCarStay;
    }

    /**
     * Sets the status of edge as all cars will remain on this edge.
     */
    public void setAsAllCarsStay() {
        this.allCarStay = true;
    }

    /**
     * Sets edge as "green light access granted".
     */
    public void setAsHasGreenLightAccess() {
        this.hasGreenLightAccess = true;
    }

    /**
     * Checks whether this edge has green light access.
     *
     * @return true if this edge has green light access, otherwise false
     */
    public boolean hasGreenLightAccess() {
        return this.hasGreenLightAccess;
    }

    /**
     * Sets as "green light access not granted".
     */
    public void setAsNoGreenLightAccess() {
        this.hasGreenLightAccess = false;
    }

    /**
     * Checks whether the first car(the car closet to end node) is at the end of edge.
     *
     * @return true if the first car(the car closet to end node) is at the end of edge, otherwise false
     */
    public boolean isFirstCarAtEndOfEdge() {
        return this.carManager.isCarAtTheEnd();
    }

    /**
     * Checks whether overtaking is allowed on this edge.
     *
     * @return true if overtaking is allowed on this edge, otherwise false
     */
    public boolean isOvertakingAllowed() {
        return this.type == StreetType.PASSING_LANE;
    }

}
