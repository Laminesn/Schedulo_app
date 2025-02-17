package edu.fit.schedulo.app.objs.loc;

import androidx.annotation.NonNull;

/**
 * Used to represent buildings on the Florida Tech campus.
 * Would've liked to use an enum, but the building codes
 * contain numbers.
 *
 * @author Joshua Sheldon
 */
public class Building {

    /* ---------- INSTANCE VARIABLES ---------- */

    /**
     * The code of the building, used in the class
     * schedule, ex. ANCH, 118RBT, 119EVH
     */
    private final String code;

    /**
     * The proper name of the building, such as
     * "Evans Hall", "Crawford Building", etc.
     */
    private final String name;

    /* ---------- CONSTRUCTOR ---------- */

    /**
     * Creates a new building object.
     *
     * @param code The code of the building.
     * @param name The name of the building.
     */
    Building(String code, String name) {

        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("Building code cannot be null or empty.");
        }

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Building name cannot be null or empty.");
        }

        this.code = code;
        this.name = name;

    }

    /* ---------- PUBLIC METHODS ---------- */

    /**
     * @return The code of the building.
     */
    public String getCode() {
        return code;
    }

    /**
     * @return The name of the building.
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if this object is equivalent to the given object.
     * Will only return true if the other object is a
     * <code>Building</code> object and has the same code.
     *
     * @param other The object to compare to.
     * @return Whether this object is equal to the other object.
     */
    @Override
    public boolean equals(Object other) {

        if (!(other instanceof Building)) {
            return false;
        }

        Building otherBuilding = (Building) other;

        return this.code.equals(otherBuilding.getCode());

    }

    /**
     * Generates a hash code based on the code of the building.
     *
     * @return This object's hash code.
     */
    @Override
    public int hashCode() {
        return this.code.hashCode();
    }

    /**
     * Returns a String in the format of:
     * "<code>name</code> (<code>code</code>)"
     *
     * @return A string representation of the object.
     */
    @NonNull
    @Override
    public String toString() {
        return this.name + " (" + this.code + ")";
    }

}
