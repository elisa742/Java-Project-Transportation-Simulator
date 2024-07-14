package edu.kit.kastel.trafficsimulation.simulation.initialization;

import edu.kit.kastel.trafficsimulation.resource.ErrorMessage;
import edu.kit.kastel.trafficsimulation.simulation.exception.ParserException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class parses the street, crossing and car information and checks whether there is a valid street network.
 *
 * @author uyjad
 * @version 1.0
 */
public class Parser {
    private static final String KEY_ID_OF_CAR = "idOfCar";
    private static final String KEY_ID_OF_STREET = "idOfStreet";
    private static final String KEY_WISHED_SPEED = "wishedSpeed";
    private static final String KEY_ACCELERATOR = "accelerator";
    private static final Pattern REGEX_CAR = Pattern.compile("(?<" + KEY_ID_OF_CAR + ">\\d+),(?<"
            + KEY_ID_OF_STREET + ">\\d+),(?<" + KEY_WISHED_SPEED + ">\\d+),(?<" + KEY_ACCELERATOR + ">\\d+)");
    private static final String KEY_ID = "id";
    private static final String KEY_DURATION = "duration";
    private static final Pattern REGEX_NODE = Pattern.compile("(?<" + KEY_ID + ">\\d+):(?<" + KEY_DURATION
            + ">\\d+)t");
    private static final String KEY_START_NODE = "startNode";
    private static final String KEY_END_NODE = "endNode";
    private static final String KEY_LENGTH = "length";
    private static final String KEY_TYPE = "type";
    private static final String KEY_MAXIMUM_SPEED = "maximumSpeed";
    private static final Pattern REGEX_STREET = Pattern.compile("(?<" + KEY_START_NODE + ">\\d+)-->(?<"
            + KEY_END_NODE + ">\\d+):(?<" + KEY_LENGTH + ">\\d+)m,(?<" + KEY_TYPE + ">1|2)x,(?<" + KEY_MAXIMUM_SPEED
            + ">\\d+)max");
    private final List<String> stringsOfCrossings;
    private final List<String> stringsOfStreets;
    private final List<String> stringsOfCars;
    private final Map<Integer, NodeData> nodeDataList;
    private final List<StreetData> streetDataList;
    private final List<Integer> idListOfCars;

    /**
     * Constructor of a parser.
     *
     * @param streets string list of street
     * @param crossings string list of nodes
     * @param cars string list of cars
     */
    public Parser(List<String> streets, List<String> crossings, List<String> cars) {
        this.stringsOfStreets = cloneList(streets);
        this.stringsOfCrossings = cloneList(crossings);
        this.stringsOfCars = cloneList(cars);
        this.nodeDataList = new TreeMap<>();
        this.streetDataList = new ArrayList<>();
        this.idListOfCars = new ArrayList<>();
    }

    /**
     * Parses the strings and checks whether nodes, cars, streets are valid.
     *
     * @throws ParserException if string from the file is not valid,
     * or the nodes, cards, streets derived from the string has conflicts with each other
     */
    public void setUp() throws ParserException {
        createPreNodes();
        createStreets();
        checkNodeWithoutStreet();
        createCars();
    }

    /**
     * Creates a new street network with valid node data and street data.
     *
     * @return new street network with valid node data and street data
     */
    public StreetNetwork createStreetNetwork() {
        return new StreetNetwork(this.nodeDataList, this.streetDataList);
    }

    /**
     * Creates street data.
     *
     * @throws ParserException if the string from file is not valid, node does not exist, node is fully utilized,
     * or the length and/or maximum speed of the street is not valid
     */
    public void createStreets() throws ParserException {
        for (String line : this.stringsOfStreets) {
            Matcher streetMatcher = REGEX_STREET.matcher(line);
            if (!streetMatcher.matches()) {
                throw new ParserException(ErrorMessage.INVALID_STREET_DATA.toString());
            }
            int idOfStartNode = parseInteger(streetMatcher.group(KEY_START_NODE));
            int idOfEndNode = parseInteger(streetMatcher.group(KEY_END_NODE));
            int length = parseInteger(streetMatcher.group(KEY_LENGTH));
            int idOfType = parseInteger(streetMatcher.group(KEY_TYPE));
            int maximumSpeed = parseInteger(streetMatcher.group(KEY_MAXIMUM_SPEED));

            if (idOfStartNode == idOfEndNode) {
                throw new ParserException(ErrorMessage.INVALID_STREET_WITH_SAME_NODES.toString());
            }
            // Check whether node exists in node data list.
            if (!this.nodeDataList.containsKey(idOfStartNode) || !this.nodeDataList.containsKey(idOfEndNode)) {
                throw new ParserException(ErrorMessage.NODE_NOT_FOUND.toString());
            }
            // Check if node is still valid for connection with another street.
            if (!this.nodeDataList.get(idOfEndNode).isValidForIncomingStreet()) {
                throw new ParserException(ErrorMessage.ILLEGAL_CONNECTION_TO_INCOMING_STREET.toString());
            }
            if (!this.nodeDataList.get(idOfStartNode).isValidForOutgoingStreet()) {
                throw new ParserException(ErrorMessage.ILLEGAL_CONNECTION_TO_OUTGOING_STREET.toString());
            }

            StreetData streetData = new StreetData(idOfStartNode, idOfEndNode, idOfType, length, maximumSpeed);
            this.streetDataList.add(streetData);
            this.nodeDataList.get(idOfStartNode).addIdOfEndNode(idOfEndNode);
            this.nodeDataList.get(idOfEndNode).addIdOfPreviousNode(idOfStartNode);
        }
    }

    /**
     * Creates node data.
     *
     * @throws ParserException if the string from file is not valid, the node already exists, or duration is not valid
     */
    public void createPreNodes() throws ParserException {
        for (String line : this.stringsOfCrossings) {
            Matcher crossingMatcher = REGEX_NODE.matcher(line);
            if (!crossingMatcher.matches()) {
                throw new ParserException(ErrorMessage.INVALID_NODE_DATA.toString());
            }
            int id = parseInteger(crossingMatcher.group(KEY_ID));
            if (this.nodeDataList.containsKey(id)) {
                throw new ParserException(ErrorMessage.ILLEGAL_NEW_ID.toString());
            }
            int duration = parseInteger(crossingMatcher.group(KEY_DURATION));
            NodeData newNodeData;
            if (duration == 0) {
                newNodeData = new NodeData(id);
            } else {
                newNodeData = new NodeData(id, duration);
            }
            this.nodeDataList.put(id, newNodeData);
        }
    }

    /**
     * Checks whether there is a node that is not connected to any incoming street and/or any outgoing street.
     *
     * @throws ParserException if a node is not connected to any incoming street and/or any outgoing street
     */
    public void checkNodeWithoutStreet() throws ParserException {
        for (NodeData nodeData : this.nodeDataList.values()) {
            if (!nodeData.isValid()) {
                throw new ParserException(ErrorMessage.NO_STREET_CONNECTED.toString());
            }
        }
    }

    /**
     * Creates car. If successful, the car will be added to the street where it belongs.
     *
     * @throws ParserException if the string from the file is invalid, the street does not exist, or the street is full
     */
    public void createCars() throws ParserException {
        for (String line : this.stringsOfCars) {
            Matcher carMatcher = REGEX_CAR.matcher(line);
            if (!carMatcher.matches()) {
                throw new ParserException(ErrorMessage.ILLEGAL_CAR_FORMAT.toString());
            }
            int idOfCar = parseInteger(carMatcher.group(KEY_ID_OF_CAR));
            // Check whether id of car already exists.
            if (this.idListOfCars.contains(idOfCar)) {
                throw new ParserException(ErrorMessage.ILLEGAL_NEW_ID.toString());
            }

            int idOfStreet = parseInteger(carMatcher.group(KEY_ID_OF_STREET));
            // Check whether id of street is valid.
            if (idOfStreet > this.streetDataList.size() - 1) {
                throw new ParserException(ErrorMessage.ILLEGAL_STREET_ID.toString());
            }
            // Check whether the street is full of cars.
            if (this.streetDataList.get(idOfStreet).isFullOfCars()) {
                throw new ParserException(String.format(ErrorMessage.STREET_IS_FULL.toString(), idOfStreet,
                        this.streetDataList.get(idOfStreet).getQuotaOfCars()));
            }

            int wishedSpeed = parseInteger(carMatcher.group(KEY_WISHED_SPEED));
            int accelerator = parseInteger(carMatcher.group(KEY_ACCELERATOR));
            Car carToAdd = new Car(idOfCar, wishedSpeed, accelerator);
            this.streetDataList.get(idOfStreet).addCars(carToAdd);
            this.idListOfCars.add(idOfCar);
        }
    }

    /**
     * Clones the string list.
     *
     * @param inputList list to be cloned
     * @return the cloned string list
     */
    public List<String> cloneList(List<String> inputList) {
        List<String> copyList = new ArrayList<>();
        for (String line : inputList) {
            copyList.add(line);
        }
        return copyList;
    }

    /**
     * Parses the string input into integer.
     *
     * @param inputToParse string input to parse
     * @return integer parsed from the input if parsing is successful, otherwise throw exception
     * @throws ParserException if the string input cannot be parsed into an integer
     */
    public int parseInteger(String inputToParse) throws ParserException {
        int result;
        try {
            result = Integer.parseInt(inputToParse);
        } catch (NumberFormatException e) {
            throw new ParserException(ErrorMessage.ILLEGAL_INTEGER.format(inputToParse));
        }
        return result;
    }

}
