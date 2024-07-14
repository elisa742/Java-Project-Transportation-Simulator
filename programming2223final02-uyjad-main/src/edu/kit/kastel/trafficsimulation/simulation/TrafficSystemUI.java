package edu.kit.kastel.trafficsimulation.simulation;

import edu.kit.kastel.trafficsimulation.simulation.exception.InquiryException;
import edu.kit.kastel.trafficsimulation.simulation.exception.ParserException;
import edu.kit.kastel.trafficsimulation.simulation.exception.TrafficException;
import edu.kit.kastel.trafficsimulation.io.SimulationFileLoader;
import edu.kit.kastel.trafficsimulation.resource.ErrorMessage;
import edu.kit.kastel.trafficsimulation.simulation.initialization.Parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class describes a session for interactive command execution.
 *
 * @author uyjad
 * @version 1.0
 */
public class TrafficSystemUI {
    private static final String MESSAGE_READY = "READY";
    private static final String KEY_ID_OF_CAR = "idOfCar";
    private static final String KEY_PATH = "path";
    private static final String KEY_TICKS = "ticks";
    private static final String REGEX_COMMAND_QUIT = "quit";
    private static final Pattern REGEX_COMMAND_LOAD = Pattern.compile("load (?<" + KEY_PATH + ">\\S+)");
    private static final Pattern REGEX_COMMAND_SIMULATE = Pattern.compile("simulate (?<" + KEY_TICKS + ">\\d+)");
    private static final Pattern REGEX_COMMAND_POSITION = Pattern.compile("position (?<" + KEY_ID_OF_CAR
            + ">\\d+)");
    private final TrafficSystem system;
    private final Scanner scanner = new Scanner(System.in);
    private boolean isRunning;
    private boolean isInitialized;

    /**
     * Constructor of traffic system user interface.
     */
    public TrafficSystemUI() {
        this.isRunning = true;
        this.isInitialized = false;
        this.system = new TrafficSystem();
    }

    /**
     * Starts the interaction and executes commands.
     */
    public void interactive() {
        while (this.isRunning) {
            String input = scanner.nextLine();
            if (input.equals(REGEX_COMMAND_QUIT)) {
                stop();
                return;
            }
            try {
                parseInput(input);
            } catch (TrafficException e) {
                System.out.println(e.getMessage());
            }
        }
        this.scanner.close();
    }

    /**
     * Parses the input into command and execute it.
     *
     * @param input input given by user
     * @throws TrafficException if the input is not valid or cannot execute the command
     */
    public void parseInput(String input) throws TrafficException {
        Matcher loadMatcher = REGEX_COMMAND_LOAD.matcher(input);
        Matcher simulateMatcher = REGEX_COMMAND_SIMULATE.matcher(input);
        Matcher positionMatcher = REGEX_COMMAND_POSITION.matcher(input);

        // Execute the load command.
        if (loadMatcher.matches()) {
            String path = loadMatcher.group(KEY_PATH);
            parseLoadCommand(path);
            setAsInitialized();
            System.out.println(MESSAGE_READY);
            return;
        }

        if (!simulateMatcher.matches() && !positionMatcher.matches()) {
            throw new TrafficException(ErrorMessage.INPUT_NOT_VALID.toString());
        }
        if (!this.isInitialized) {
            throw new TrafficException(ErrorMessage.INCOMPLETE_SET_UP.toString());
        }

        // Execute the simulate command.
        if (simulateMatcher.matches()) {
            int ticks = parseInteger(simulateMatcher.group(KEY_TICKS));
            this.system.simulate(ticks);
            System.out.println(MESSAGE_READY);
            return;
        }

        // Execute the position command.
        if (positionMatcher.matches()) {
            int id = parseInteger(positionMatcher.group(KEY_ID_OF_CAR));
            try {
                this.system.getCarDetails(id);
            } catch (InquiryException e) {
                throw new TrafficException(e.getMessage());
            }
        }
    }

    /**
     * Parses the load command.
     *
     * @param path path for loading
     * @throws TrafficException if the path is not valid or the content derived is not valid
     */
    public void parseLoadCommand(String path) throws TrafficException {
        SimulationFileLoader loader;
        try {
            loader = new SimulationFileLoader(path);
        } catch (IOException e) {
            throw new TrafficException(e.getMessage());
        }

        List<String> crossings;
        List<String> streets;
        List<String> cars;
        try {
            crossings = loader.loadCrossings();
            streets = loader.loadStreets();
            cars = loader.loadCars();
        } catch (IOException e) {
            throw new TrafficException(e.getMessage());
        }

        List<String> clonedCrossings = cloneList(crossings);
        List<String> clonedStreets = cloneList(streets);
        List<String> clonedCars = cloneList(cars);

        Parser parser = new Parser(clonedStreets, clonedCrossings, clonedCars);
        try {
            parser.setUp();
        } catch (ParserException e) {
            throw new TrafficException(e.getMessage());
        }
        this.system.setNetwork(parser.createStreetNetwork());
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
     * Parses the string input into an integer.
     *
     * @param inputToParse string input to parse
     * @return integer parsed from the input if parsing is successful, otherwise throw exception
     * @throws TrafficException if the string input cannot be parsed into an integer
     */
    public int parseInteger(String inputToParse) throws TrafficException {
        int result;
        try {
            result = Integer.parseInt(inputToParse);
        } catch (NumberFormatException e) {
            throw new TrafficException(ErrorMessage.ILLEGAL_INTEGER.format(inputToParse));
        }
        return result;
    }

    /**
     * Sets the status as initialized when the loading is finished successfully and street network is ready.
     */
    public void setAsInitialized() {
        this.isInitialized = true;
    }

    /**
     * Sets the status as "not running", which will stop the interactive session.
     */
    public void stop() {
        this.isRunning = false;
    }

}
