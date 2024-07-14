package edu.kit.kastel.trafficsimulation.simulation;

import edu.kit.kastel.trafficsimulation.simulation.exception.InquiryException;
import edu.kit.kastel.trafficsimulation.simulation.initialization.StreetNetwork;

/**
 * This class describes the traffic system where commands are executed.
 *
 * @author uyjad
 * @version 1.0
 */
public class TrafficSystem {
    private StreetNetwork streetNetwork;

    /**
     * Sets the street network.
     *
     * @param streetNetwork graph where the simulation is executed upon.
     */
    public void setNetwork(StreetNetwork streetNetwork) {
        this.streetNetwork = streetNetwork;
    }

    /**
     * Executes simulation according to given ticks.
     *
     * @param ticks how many times simulation is executed.
     */
    public void simulate(int ticks) {
        for (int i = 0; i < ticks; i++) {
            this.streetNetwork.update();
        }
    }

    /**
     * Gets string detail of a car, including id, position, speed and current edge it is positioned.
     *
     * @param idOfCar id of car to be searched
     * @throws InquiryException if id of car is not valid
     */
    public void getCarDetails(int idOfCar) throws InquiryException {
        System.out.println(this.streetNetwork.getCarDetail(idOfCar));
    }

}
