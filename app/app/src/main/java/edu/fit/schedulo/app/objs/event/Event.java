package edu.fit.schedulo.app.objs.event;

import androidx.annotation.NonNull;

import edu.fit.schedulo.app.objs.datetime.EventSchedule;
import edu.fit.schedulo.app.objs.loc.Location;

/**
 * An event in a semester. Can represent a class
 * or another type of activity.
 *
 * @author Joshua Sheldon
 */
public abstract class Event {

    /* ---------- INSTANCE VARIABLES ---------- */

    /**
     * The title of the event.
     */
    private final String title;

    /**
     * The days of the week that the event occurs on,
     * and the time range on those days that
     * the event occupies.
     */
    private final EventSchedule schedule;

    /**
     * The location of the event, can be on
     * campus, off campus, virtual, or
     * <code>null</code>
     */
    private final Location location;

    /**
     * Whether this event observes holidays or not.
     * Holidays are instances of the
     * <code>AcademicCalendarDate</code> class.
     */
    private final boolean observesHoliday;

    /* ---------- CONSTRUCTORS ---------- */

    /**
     * Constructor for an event with all parameters.
     *
     * @param title           The event's title.
     * @param schedule        The event's schedule.
     * @param location        The event's location, can
     *                        be <code>null</code>.
     * @param observesHoliday Whether the event observes
     *                        holidays or not.
     */
    public Event(String title, EventSchedule schedule, Location location, boolean observesHoliday) {

        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Event title cannot be null or empty.");
        }

        if (schedule == null || schedule.getMap().isEmpty()) {
            throw new IllegalArgumentException("Event schedule cannot be null or empty.");
        }

        this.title = title;
        this.schedule = schedule;
        this.location = location;
        this.observesHoliday = observesHoliday;

    }

    /**
     * Constructor for an event without a location.
     *
     * @param title           The event's title.
     * @param schedule        The event's schedule.
     * @param observesHoliday Whether the event observes
     *                        holidays or not.
     */
    public Event(String title, EventSchedule schedule, boolean observesHoliday) {
        this(title, schedule, null, observesHoliday);
    }

    /* ---------- PUBLIC METHODS ---------- */

    /**
     * @return The title of the event.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return The schedule of the event.
     */
    public EventSchedule getSchedule() {
        return schedule;
    }

    /**
     * @return The location of the event.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @return Whether the event observes holidays or not.
     */
    public boolean getObservesHoliday() {
        return observesHoliday;
    }

    @Override
    public boolean equals(Object other) {

        if (!(other instanceof Event)) {
            return false;
        }

        Event otherEvent = (Event) other;

        boolean mostlyEqual = this.title.equals(otherEvent.title) &&
                this.schedule.equals(otherEvent.schedule) &&
                this.observesHoliday == otherEvent.observesHoliday;

        if (!mostlyEqual) {
            return false;
        }

        if (this.location == null && otherEvent.getLocation() == null) {
            return true;
        } else if (this.location == null || otherEvent.getLocation() == null) {
            return false;
        } else {
            return this.location.equals(otherEvent.getLocation());
        }

    }

    /**
     * @return A string representation of the event.
     */
    @NonNull
    @Override
    public String toString() {
        return this.title + " (" + this.schedule + ")";
    }

}
