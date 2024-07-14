package edu.kit.kastel.trafficsimulation;

import edu.kit.kastel.trafficsimulation.simulation.TrafficSystemUI;

/**
 * Entry point of this application. It runs the interactive command session.
 *
 * @author uyjad
 * @version 1.0
 */
public final class Application {

    /**
     * Utility class should not have instance.
     */
    public static final String UTILITY_CLASS_INSTANTIATION = "Utility class cannot be instantiated.";

    /**
     * Private constructor to avoid object generation.
     */
    private Application() {
        throw new IllegalStateException(UTILITY_CLASS_INSTANTIATION);
    }

    /**
     * The main entry point of the application. Starts the interactive command line session.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TrafficSystemUI systemUI = new TrafficSystemUI();
        systemUI.interactive();
    }

}