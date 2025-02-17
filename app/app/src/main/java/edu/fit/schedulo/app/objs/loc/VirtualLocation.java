package edu.fit.schedulo.app.objs.loc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A virtual event location, for online events
 * or meetings.
 *
 * @author Joshua Sheldon
 */
public class VirtualLocation implements Location {

    /* ---------- INSTANCE VARIABLES ---------- */

    /**
     * The link to the virtual location.
     */
    private final String link;

    /* ---------- CONSTRUCTOR ---------- */

    /**
     * Creates a new <code>VirtualLocation</code> object.
     *
     * @param link The link to the virtual location.
     */
    @JsonCreator
    public VirtualLocation(@JsonProperty("link") String link) {
        if (link == null || link.isEmpty()) {
            throw new IllegalArgumentException("Link cannot be null or empty!");
        }

        this.link = link;
    }

    /* ---------- PUBLIC METHODS ---------- */

    /**
     * @return The link to the virtual location.
     */
    public String getLink() {
        return link;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof VirtualLocation)) {
            return false;
        }

        VirtualLocation otherLoc = (VirtualLocation) other;

        return link.equals(otherLoc.link);
    }

    @Override
    public int hashCode() {
        return link.hashCode();
    }

    @Override
    public String toString() {
        return link;
    }

}
