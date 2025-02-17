package edu.fit.schedulo.app.objs.loc;

import java.util.HashMap;
import java.util.Map;

/**
 * Keeps track of all the buildings on the Florida Tech campus
 * that can be used as locations for events. Factory for the
 * Building class.
 *
 * @author Joshua Sheldon
 */
public class Buildings {

    /* ---------- INSTANCE VARIABLES ---------- */

    /**
     * Map from building code to building object.
     */
    private final Map<String, Building> buildings;

    /* ---------- CONSTRUCTOR ---------- */

    /**
     * Creates the <code>Buildings</code> factory object.
     * Initializes the map of buildings and fills it
     * with the default buildings.
     */
    private Buildings() {
        this.buildings = new HashMap<>();
        init();
    }

    /* ---------- SINGLETON ---------- */

    private static final Buildings instance = new Buildings();

    public static Buildings getInstance() {
        return instance;
    }

    /* ---------- PUBLIC METHODS ---------- */

    /**
     * Attempts to add a new building to the list of buildings.
     * Will fail if the parameters are invalid or if a
     * building with the given code already exists.
     *
     * @param code The building's code
     * @param name The building's name
     * @return <code>true</code> if the building is successfully
     * added, <code>false</code> otherwise.
     */
    public synchronized boolean addBuilding(String code, String name) {

        Building building;

        try {
            building = new Building(code, name);
        } catch (Exception e) {
            return false;
        }

        if (buildings.containsKey(code)) {
            return false;
        }

        buildings.put(code, building);
        return true;

    }

    /**
     * Attempts to retrieve the building with the given code.
     * Will return <code>null</code> if the code is invalid or
     * if no building with the given code exists.
     *
     * @param code The code of the building to retrieve.
     * @return The building with the given code, or <code>null</code>
     * if no such building exists.
     */
    public synchronized Building getBuilding(String code) {

        if (code == null) {
            return null;
        }

        return buildings.get(code);
    }

    /* ---------- PRIVATE METHODS ---------- */

    /**
     * Add all buildings from
     * <a href="https://www.fit.edu/registrar/class-schedules/">this website</a>.
     * Could possibly be replaced with a more sophisticated
     * method such as scraping later.
     */
    private void init() {
        addBuilding("ANCH", "Evinrude Marine Operations Center (Anchorage)");
        addBuilding("118RBT", "Robert's Hall");
        addBuilding("119EVH", "Evans Hall");
        addBuilding("255RNT", "Music House");
        addBuilding("267RNT", "Music Program Studios");
        addBuilding("402QAD", "Quadrangle Classrooms");
        addBuilding("403QAD", "Quadrangle Classrooms");
        addBuilding("405QAD", "Dan Dahle Building");
        addBuilding("407QAD", "DMES Lab");
        addBuilding("410GLE", "Gleason Performing Arts Center");
        addBuilding("420CRF", "Crawford Building");
        addBuilding("424LNK", "Link Building");
        addBuilding("427FRU", "Frueauff Building");
        addBuilding("428EVL", "Evans Library Pavilion");
        addBuilding("439SHP", "Shephard Building");
        addBuilding("460SKU", "Skurla Hall (Aeronautics Building)");
        addBuilding("500OLS", "F.W. Olin Life Sciences Building");
        addBuilding("501OEC", "F.W. Olin Engineering Complex");
        addBuilding("502OPS", "F.W. Olin Physical Sciences Center");
        addBuilding("504LSA", "L3Harris Center for Science and Engineering");
        addBuilding("510CLE", "Clemente Center");
        addBuilding("512UNP", "University Plaza at Florida Tech");
        addBuilding("538LAB", "Aerospace Laboratories and Machine Shop");
        addBuilding("540MIL", "Military Sciences/Facilities Operations/Shipping Building");
        addBuilding("545AUT", "The Scott Center for Autism Treatment");
        addBuilding("547WAV", "Surf Mechanics Lab");
        addBuilding("610CTR", "All Faiths Center");
        addBuilding("710FTC", "L3Harris Commons");
        addBuilding("910CAI", "Center for Aeronautics & Innovation");
        addBuilding("927BBO", "Nathan M. Bisk College of Business / Babcock Oaks");
        addBuilding("994BLR", "Emil Buehler Center for Aviation Training");
    }

}
