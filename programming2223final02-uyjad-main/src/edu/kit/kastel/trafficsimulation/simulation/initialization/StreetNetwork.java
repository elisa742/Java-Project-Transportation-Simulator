package edu.kit.kastel.trafficsimulation.simulation.initialization;

import edu.kit.kastel.trafficsimulation.resource.ErrorMessage;
import edu.kit.kastel.trafficsimulation.simulation.exception.InquiryException;
import edu.kit.kastel.trafficsimulation.simulation.module.Edge;
import edu.kit.kastel.trafficsimulation.simulation.module.Node;
import edu.kit.kastel.trafficsimulation.simulation.module.NodeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class describes the street network.
 * The nodes (which also means crossings) and edges (which also means streets) can be managed and updated here.
 *
 * @author uyjad
 * @version 1.0
 */
public class StreetNetwork {
    private static final String REGEX_CAR_DETAILS = "Car %d on street %d with speed %d and position %d";
    private static final int MINIMUM_DISTANCE_BETWEEN_CARS = 10;
    private final Map<Integer, NodeData> nodeDataList;
    private final List<StreetData> streetDataList;
    private final Map<Node, List<Edge>> graph;

    /**
     * Constructor of a network.
     * Initiate nodes, edge and green light access.
     *
     * @param nodeDataList list of node data
     * @param streetDataList list of street data
     */
    public StreetNetwork(Map<Integer, NodeData> nodeDataList, List<StreetData> streetDataList) {
        this.nodeDataList = nodeDataList;
        this.streetDataList = streetDataList;
        this.graph = new HashMap<>();
        initiateNode();
        initiateEdge();
        setInitialGreenLightPermission();
    }

    /**
     * Creates nodes and add them to the graph.
     */
    public void initiateNode() {
        for (NodeData nodeData : this.nodeDataList.values()) {
            int duration = nodeData.getDurationOfGreenLight();
            if (duration != 0) {
                this.graph.put(new Node(nodeData.getId(), duration, nodeData.getNumberOfIncomingStreets()),
                        new ArrayList<>());
            } else {
                this.graph.put(new Node(nodeData.getId()), new ArrayList<>());
            }
        }
    }

    /**
     * Creates edges and adds edges to the graph.
     * Meanwhile, for each edge, the end node will also be updated.
     */
    public void initiateEdge() {
        for (int i = 0; i < this.streetDataList.size(); i++) {
            StreetData streetData = this.streetDataList.get(i);
            Edge newEdge = null;
            Node edgeStartNode = null;
            Node endNode = null;
            for (Node node : this.graph.keySet()) {
                if (node.getId() == streetData.getIdOfStartNode()) {
                    edgeStartNode = node;
                }
                if (node.getId() == streetData.getIdOfEndNode()) {
                    endNode = node;
                    newEdge = new Edge(i, streetData, endNode);
                }
                //Add edge to the graph.
                if (edgeStartNode != null & newEdge != null) {
                    this.graph.get(edgeStartNode).add(newEdge);
                    endNode.addEdge(newEdge);
                    break;
                }
            }
        }
    }

    /**
     * Sets the green light permission at the initial state.
     */
    public void setInitialGreenLightPermission() {
        for (Node node : this.graph.keySet()) {
            if (node.getType() == NodeType.INTERSECTION) {
                node.getIncomingEdges().get(0).setAsHasGreenLightAccess();
            }
        }
    }

    /**
     * Resets the status of all cars from "updated" to "not updated". So they are ready for updates next tick.
     */
    public void resetCars() {
        for (Node node : this.graph.keySet()) {
            for (Edge edge : this.graph.get(node)) {
                edge.reset();
            }
        }
    }

    /**
     * Finds edge by id.
     * First checks the list of edges connected to the id of start node.
     * Then uses the id of street to find the edge.
     *
     * @param idOfStreet id of street
     * @return edge that matches the id of street, if not found, return null
     */
    public Edge findEdgeByID(int idOfStreet) {
        int idOfStartNode = this.streetDataList.get(idOfStreet).getIdOfStartNode();
        Node newStartNode = new Node(idOfStartNode);
        for (Edge edgeToFind : this.graph.get(newStartNode)) {
            if (edgeToFind.getId() == idOfStreet) {
                return edgeToFind;
            }
        }
        return null;
    }

    /**
     * Executes simulation for one tick: updates cars on each edge and then updates nodes.
     */
    public void update() {
        for (int i = 0; i < this.streetDataList.size(); i++) {
            Edge edgeToHandle = findEdgeByID(i);
            while (!edgeToHandle.allCarsStay() && !edgeToHandle.isEmpty() && !edgeToHandle.isFullyUpdated()) {
                // We start with the first car which is closet to end of edge.
                int wishedDistanceOnNextEdge = edgeToHandle.getFirstCarWishedDistance();

                // If the wished distance to travel on next edge is negative or 0,
                // then all cars currently on this edge will not move to next edge.
                if (wishedDistanceOnNextEdge <= 0) {
                    edgeToHandle.updateInternally();
                    edgeToHandle.setAsAllCarsStay();
                } else {
                    // If the wished distance to travel on next edge is greater than 0,
                    // will attempt to update the car on next edge.
                    updateCar(edgeToHandle, wishedDistanceOnNextEdge);
                }
            }
        }

        // Change the status of car from "updated" to "not updated". So it will be ready for the next tick.
        resetCars();

        // Update the crossings(nodes) by changing the green light duration and green light access.
        for (Node node : this.graph.keySet()) {
            if (node.getType() == NodeType.INTERSECTION) {
                if (node.isEndOfDuration()) {
                    // Reset current edge, so the edge no longer has green light access.
                    int greenLightIndicator = node.getEdgeIndicator();
                    node.getIncomingEdges().get(greenLightIndicator).setAsNoGreenLightAccess();
                    // Update the green light duration, indicator and set the green light access.
                    node.updateIndicator();
                    int newGreenLightIndicator = node.getEdgeIndicator();
                    node.getIncomingEdges().get(newGreenLightIndicator).setAsHasGreenLightAccess();
                }
                node.updateDuration();
            }
        }
    }

    /**
     * Checks green light access.
     * If the edge does not have green light access, all cars on edge will not move to next edge.
     * (after updates all cars will still remain on the current edge.)
     *
     * @param edgeToHandle the edge to be checked by the green light access
     * @return true if the edge has green light access, otherwise return false
     */
    public boolean checkGreenLightAccess(Edge edgeToHandle) {
        if (edgeToHandle.getEndNode().getType() == NodeType.INTERSECTION && !edgeToHandle.hasGreenLightAccess()) {
            edgeToHandle.setAsAllCarsStay();
            edgeToHandle.updateInternally();
            return false;
        }
        return true;
    }

    /**
     * Updates the speed and position of car.
     *
     * @param edgeToHandle edge where car was positioned
     * @param wishedRemainingDistance wished distance to travel on new edge
     */
    public void updateCar(Edge edgeToHandle, int wishedRemainingDistance) {
        if (!checkGreenLightAccess(edgeToHandle)) {
            return;
        }

        // Find next edge.
        Edge nextEdge;
        Car carToHandle = edgeToHandle.getCar();
        int wishedDirection = carToHandle.getWishedDirection();
        Node endNode = edgeToHandle.getEndNode();

        if (this.graph.get(endNode).size() < wishedDirection + 1) {
            nextEdge = this.graph.get(endNode).get(0);
        } else {
            nextEdge = this.graph.get(endNode).get(wishedDirection);
        }

        // Update car depending on the situation on next edge.
        // If there is not enough space, then the car stays on old edge, otherwise the car is added to the new edge.
        int availableDistance = nextEdge.getLastCarPosition();
        if (!nextEdge.isEmpty() && availableDistance < MINIMUM_DISTANCE_BETWEEN_CARS) {
            edgeToHandle.setAsAllCarsStay();
            edgeToHandle.updateInternally();
            return;
        }

        // Car now made it to the next edge.
        carToHandle.setAsUpdated();
        carToHandle.updateWishedDirection();
        edgeToHandle.updateSpeedOfFirstCar();
        int movement;
        // Check whether the next edge is empty of cars.
        if (nextEdge.isEmpty()) {
            movement = Math.min(wishedRemainingDistance, nextEdge.getLength());
        } else {
            movement = Math.min(wishedRemainingDistance, availableDistance - MINIMUM_DISTANCE_BETWEEN_CARS);
        }
        // Check whether it is a pure turn (that means car moves from end of old edge to Position 0 of new edge).
        if (edgeToHandle.isFirstCarAtEndOfEdge() && movement == 0) {
            carToHandle.setCurrentSpeed(0);
        }
        nextEdge.addCar(new Car(carToHandle), movement);
        edgeToHandle.removeFirstCar();
    }

    /**
     * Gets the string detail of car, including id, position, speed and current edge it is positioned.
     *
     * @param idOfCar id of car to be checked
     * @return string detail of the car to be checked
     * @throws InquiryException if the id of car does not exist
     */
    public String getCarDetail(int idOfCar) throws InquiryException {
        for (int idOfEdge = 0; idOfEdge < this.streetDataList.size(); idOfEdge++) {
            Edge edgeToSearch = findEdgeByID(idOfEdge);
            if (edgeToSearch.getCarByID(idOfCar) != null) {
                Car carToSearch = edgeToSearch.getCarByID(idOfCar);
                return String.format(REGEX_CAR_DETAILS, idOfCar, idOfEdge, carToSearch.getCurrentSpeed(),
                        carToSearch.getPosition());
            }
        }
        throw new InquiryException(String.format(ErrorMessage.ID_NOT_FOUND.toString(), idOfCar));
    }

}
