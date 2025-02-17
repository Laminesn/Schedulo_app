package edu.fit.schedulo.app.objs.loc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a location on the Florida Tech campus.
 *
 * @author Joshua Sheldon
 */
public class OnCampusLocation implements Location {

    /* ---------- INSTANCE VARIABLES ---------- */

    /**
     * The code of the building where the room
     * is located.
     */
    private final String bldgCode;

    /**
     * The number of the room within the building.
     */
    private final short roomNumber;

    /* ---------- CONSTRUCTOR ---------- */

    /**
     * Creates a new <code>OnCampusLocation</code> object.
     *
     * @param bldgCode   The code of the building where the room is located.
     * @param roomNumber The number of the room within the building.
     */
    @JsonCreator
    public OnCampusLocation(@JsonProperty("bldgCode") String bldgCode,
                            @JsonProperty("roomNumber") short roomNumber) {
        if (bldgCode == null || bldgCode.isEmpty()) {
            throw new IllegalArgumentException("Building code cannot be null or empty!");
        }

        if (roomNumber < 0) {
            throw new IllegalArgumentException("Room number cannot be negative.");
        }

        this.bldgCode = bldgCode;
        this.roomNumber = roomNumber;
    }

    /**
     * Attempts to parse OnCampusLocation from a String in the
     * format of "410GLE 107".
     *
     * @param classScheduleString The string to parse.
     */
    public OnCampusLocation(String classScheduleString) {

        String[] split = classScheduleString.split(" ");

        if (split.length != 2) {
            throw new IllegalArgumentException("Invalid OnCampusLocation string: " + classScheduleString);
        }

        short roomNumber;

        try {
            roomNumber = Short.parseShort(split[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid room number: " + split[1]);
        }

        this.bldgCode = split[0];
        this.roomNumber = roomNumber;

    }

    /* ---------- PUBLIC METHODS ---------- */

    /**
     * @return The code of the building where the room is located.
     */
    public String getBldgCode() {
        return bldgCode;
    }

    /**
     * @return The number of the room within the building.
     */
    public short getRoomNumber() {
        return roomNumber;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof OnCampusLocation)) {
            return false;
        }

        OnCampusLocation otherLocation = (OnCampusLocation) other;

        return this.bldgCode.equals(otherLocation.getBldgCode()) &&
                this.roomNumber == otherLocation.getRoomNumber();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return bldgCode + " " + roomNumber;
    }

}
