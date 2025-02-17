package edu.fit.schedulo.app.objs.loc;

import android.location.Address;

// TODO REPRESENT ADDRESS WITH DIFFERENT DATA TYPE, CAN'T BE ANDROID

/**
 * Represents an off-campus location.
 * Wrapper for an <code>Address</code> object
 * that implements the Location interface.
 *
 * @author Joshua Sheldon
 */
public class OffCampusLocation implements Location {

    /* ---------- INSTANCE VARIABLES ---------- */

    /**
     * The address of the off-campus location.
     */
    private final Address address;

    /* ---------- CONSTRUCTOR ---------- */

    /**
     * Creates a new <code>OffCampusLocation</code> object.
     *
     * @param address The address of the off-campus location.
     */
    public OffCampusLocation(Address address) {

        if (address == null) {
            throw new IllegalArgumentException("Address cannot be null.");
        }

        this.address = address;

    }

    /* ---------- PUBLIC METHODS ---------- */

    /**
     * @return The address of the off-campus location.
     */
    public Address getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object other) {

        if (!(other instanceof OffCampusLocation)) {
            return false;
        }

        OffCampusLocation otherLoc = (OffCampusLocation) other;

        return address.equals(otherLoc.address);

    }

    @Override
    public int hashCode() {
        return address.hashCode();
    }

    @Override
    public String toString() {
        return address.toString();
    }

}
