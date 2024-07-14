package edu.kit.kastel.trafficsimulation.simulation.initialization;

import edu.kit.kastel.trafficsimulation.resource.ErrorMessage;
import edu.kit.kastel.trafficsimulation.simulation.exception.ParserException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class collects data of node and checks whether the attributes of node are valid.
 *
 * @author uyjad
 * @version 1.0
 */
public class NodeData {
    private static final int MAXIMUM_GREEN_LIGHT_DURATION = 10;
    private static final int MINIMUM_GREEN_LIGHT_DURATION = 3;

    //This indicates the maximum number of incoming/outgoing streets that can be connected to a node.
    private static final int MAXIMUM_NUMBER_OF_END_NODES = 4;
    private final int id;
    private final int durationOfGreenLight;
    private final List<Integer> idListOfEndNodes;
    private final List<Integer> idListOfPreviousNodes;

    /**
     * Constructor of node data if node has green light.
     *
     * @param id id of node
     * @param durationOfGreenLight duration of green light of this node
     * @throws ParserException if duration is not valid
     */
    public NodeData(int id, int durationOfGreenLight) throws ParserException {
        this.id = id;
        if (durationOfGreenLight > MAXIMUM_GREEN_LIGHT_DURATION
                || durationOfGreenLight < MINIMUM_GREEN_LIGHT_DURATION) {
            throw new ParserException(ErrorMessage.ILLEGAL_DURATION.toString());
        }
        this.durationOfGreenLight = durationOfGreenLight;
        this.idListOfEndNodes = new ArrayList<>();
        this.idListOfPreviousNodes = new ArrayList<>();
    }

    /**
     * Constructor of node data if node does not have green light.
     *
     * @param id id of node
     */
    public NodeData(int id) {
        this.id = id;
        this.durationOfGreenLight = 0;
        this.idListOfEndNodes = new ArrayList<>();
        this.idListOfPreviousNodes = new ArrayList<>();
    }

    /**
     * Gets green light duration of this node.
     *
     * @return green light duration of this node
     */
    public int getDurationOfGreenLight() {
        return this.durationOfGreenLight;
    }

    /**
     * Gets the id of this node.
     *
     * @return the id of this node
     */
    public int getId() {
        return this.id;
    }

    /**
     * Adds id of end node.
     *
     * @param idOfEndNode id of end node
     */
    public void addIdOfEndNode(int idOfEndNode) {
        this.idListOfEndNodes.add(idOfEndNode);
    }

    /**
     * Adds id of start node.
     *
     * @param idOfStartNode id of start node
     */
    public void addIdOfPreviousNode(int idOfStartNode) {
        this.idListOfPreviousNodes.add(idOfStartNode);
    }

    /**
     * Checks whether this node has at least one incoming street and one outgoing street,
     *
     * @return true if this node has at least one incoming street and one outgoing street, otherwise false
     */
    public boolean isValid() {
        return !this.idListOfPreviousNodes.isEmpty() && !this.idListOfEndNodes.isEmpty();
    }

    /**
     * Checks whether the number of incoming streets exceeds the maximum number allowed.
     *
     * @return true if Checks whether the number of incoming streets exceeds the maximum number, otherwise false
     */
    public boolean isValidForIncomingStreet() {
        return this.idListOfPreviousNodes.size() < MAXIMUM_NUMBER_OF_END_NODES;
    }

    /**
     * Checks whether the number of outgoing streets exceeds the maximum number allowed.
     *
     * @return true if Checks whether the number of outgoing streets exceeds the maximum number, otherwise false
     */
    public boolean isValidForOutgoingStreet() {
        return this.idListOfEndNodes.size() < MAXIMUM_NUMBER_OF_END_NODES;
    }

    /**
     * Gets the number of incoming streets.
     *
     * @return  the number of incoming streets
     */
    public int getNumberOfIncomingStreets() {
        return this.idListOfPreviousNodes.size();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        NodeData other = (NodeData) obj;
        return this.id == other.id;
    }
}
