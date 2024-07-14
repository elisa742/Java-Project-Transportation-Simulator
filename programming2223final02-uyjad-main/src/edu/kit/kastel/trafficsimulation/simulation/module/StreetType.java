package edu.kit.kastel.trafficsimulation.simulation.module;

/**
 * This class describes the street types.
 *
 * @author uyjad
 * @version 1.0
 */
public enum StreetType {

    /**
     * Lane that overtaking is not allowed.
     */
    SIMPLE_LANE(1),

    /**
     * Lane that overtaking is allowed.
     */
    PASSING_LANE(2);

    private final int idOfType;

    /**
     * Constructor of street type.
     *
     * @param idOfType id of street type
     */
    StreetType(int idOfType) {
        this.idOfType = idOfType;
    }

    /**
     * Gets street type from input string.
     *
     * @param idOfType id of type
     * @return street type that matches this id
     */
    public static StreetType getStreetTypeFromString(int idOfType) {
        for (StreetType type : StreetType.values()) {
            if (type.getIdOfType() == idOfType) {
                return type;
            }
        }
        return null;
    }

    /**
     * Gets the id of the type.
     *
     * @return the id of the type.
     */
    public int getIdOfType() {
        return this.idOfType;
    }

}
