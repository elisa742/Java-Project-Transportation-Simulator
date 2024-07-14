package edu.kit.kastel.trafficsimulation.simulation.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class describes a node which is part of a street/edge.
 * (node and crossing are interchangeable in this task. Street and edge are also interchangeable.)
 * If the node type is intersection, it can update green light access of incoming streets and green light duration.
 *
 * @author uyjad
 * @version 1.0
 */
public class Node {
    private final int id;
    private final NodeType type;
    private final List<Edge> incomingEdges = new ArrayList<>();
    private GreenLight greenLight;

    /**
     * Constructor of node with green light.
     *
     * @param id id of node
     * @param duration duration of node
     * @param numberOfIncomingStreets number of incoming streets
     */
    public Node(int id, int duration, int numberOfIncomingStreets) {
        this.id = id;
        this.type = NodeType.INTERSECTION;
        this.greenLight = new GreenLight(duration, numberOfIncomingStreets);
    }

    /**
     * Constructor of node that does not have green light.
     *
     * @param id id of node
     */
    public Node(int id) {
        this.id = id;
        this.type = NodeType.CIRCLE;
    }

    /**
     * Constructor of node with a given node.
     *
     * @param nodeToCopy node to be copied
     */
    public Node(Node nodeToCopy) {
        this.id =  nodeToCopy.getId();
        this.type = nodeToCopy.getType();
        if (this.type == NodeType.INTERSECTION) {
            this.greenLight = new GreenLight(nodeToCopy.getGreenLight());
        }
    }

    /**
     * Adds a new edge to the node.
     *
     * @param edgeToAdd edge to be added
     */
    public void addEdge(Edge edgeToAdd) {
        this.incomingEdges.add(edgeToAdd);
    }

    /**
     * Updates the duration of green light.
     */
    public void updateDuration() {
        this.greenLight.updateDuration();
    }

    /**
     * Updates the street indicator.
     */
    public void updateIndicator() {
        this.greenLight.updateStreetIndicator();
    }

    /**
     * Checks whether the green light is at the end of duration.
     *
     * @return true if the green light is at the end of duration, otherwise false
     */
    public boolean isEndOfDuration() {
        return this.greenLight.isEndOfDuration();
    }

    /**
     * Gets indicator of incoming edge/street that is allowed to cross.
     *
     * @return indicator of incoming edge/street that is allowed to cross
     */
    public int getEdgeIndicator() {
        return this.greenLight.getCurrentStreetIndicator();
    }

    /**
     * Gets id of node.
     *
     * @return id of node
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets type of node.
     *
     * @return type of node
     */
    public NodeType getType() {
        return this.type;
    }

    /**
     * Gets green light of node.
     *
     * @return green light of node
     */
    public GreenLight getGreenLight() {
        return this.greenLight;
    }

    /**
     * Gets list of incoming edges connected to this node.
     *
     * @return list of incoming edges connected to this node
     */
    public List<Edge> getIncomingEdges() {
        return this.incomingEdges;
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

        Node other = (Node) obj;
        return this.id == other.getId();
    }

}
