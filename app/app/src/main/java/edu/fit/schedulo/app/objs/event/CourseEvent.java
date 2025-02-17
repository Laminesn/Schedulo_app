package edu.fit.schedulo.app.objs.event;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.fit.schedulo.app.objs.datetime.EventSchedule;
import edu.fit.schedulo.app.objs.loc.Location;

/**
 * An event that represents a class in a semester.
 *
 * @author Joshua Sheldon
 */
public class CourseEvent extends Event {

    /* ---------- INSTANCE VARIABLES ---------- */

    /**
     * The CRN of the course that this event
     * represents.
     */
    private final int crn;

    /* ---------- CONSTRUCTORS ---------- */

    /**
     * Creates a new <code>CourseEvent</code> object.
     *
     * @param title           The title of the course.
     * @param schedule        The schedule of the course.
     * @param location        The location of the course.
     * @param observesHoliday Whether the course observes holidays.
     * @param crn             The CRN of the course.
     */
    @JsonCreator
    CourseEvent(@JsonProperty("title") String title,
                @JsonProperty("schedule") EventSchedule schedule,
                @JsonProperty("location") Location location,
                @JsonProperty("observesHoliday") boolean observesHoliday,
                @JsonProperty("crn") int crn) {
        super(title, schedule, location, observesHoliday);

        if (crn < 0) {
            throw new IllegalArgumentException("CRN cannot be negative.");
        }

        this.crn = crn;
    }

    /**
     * Creates a new <code>CourseEvent</code> object.
     * Doesn't have a location, as some courses do
     * not have a listed location.
     *
     * @param title           The title of the course.
     * @param schedule        The schedule of the course.
     * @param observesHoliday Whether the course observes holidays.
     * @param crn             The CRN of the course.
     */
    CourseEvent(String title, EventSchedule schedule, boolean observesHoliday, int crn) {
        this(title, schedule, null, observesHoliday, crn);
    }

    /* ---------- PUBLIC METHODS ---------- */

    /**
     * @return The CRN of the course.
     */
    public int getCRN() {
        return this.crn;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof CourseEvent)) {
            return false;
        }

        Event thisEvent = this;
        Event otherEvent = (Event) obj;

        return thisEvent.equals(otherEvent) && this.crn == ((CourseEvent) obj).getCRN();

    }

    @Override
    @NonNull
    public String toString() {
        return super.toString();
    }

}
