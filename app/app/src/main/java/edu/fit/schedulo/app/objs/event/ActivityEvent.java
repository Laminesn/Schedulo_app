package edu.fit.schedulo.app.objs.event;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.fit.schedulo.app.objs.Category;
import edu.fit.schedulo.app.objs.datetime.EventSchedule;
import edu.fit.schedulo.app.objs.loc.Location;

/**
 * An event in a semester that is not a class.
 * This could be a job, club, social event, etc.
 *
 * @author Joshua Sheldon
 */
public class ActivityEvent extends Event {

    /* ---------- INSTANCE VARIABLES ---------- */

    /**
     * The category of the activity.
     */
    private final Category category;

    /**
     * Creates a new <code>ActivityEvent</code> object.
     *
     * @param title           The title of the event.
     * @param schedule        The schedule of the event.
     * @param location        The location of the event.
     * @param observesHoliday Whether the event observes holidays.
     * @param category        The category of the event.
     */
    @JsonCreator
    public ActivityEvent(@JsonProperty("title") String title,
                         @JsonProperty("schedule") EventSchedule schedule,
                         @JsonProperty("location") Location location,
                         @JsonProperty("observesHoliday") boolean observesHoliday,
                         @JsonProperty("category") Category category) {
        super(title, schedule, location, observesHoliday);
        this.category = category;
    }

    /**
     * Creates a new <code>ActivityEvent</code> object.
     *
     * @param title           The title of the event.
     * @param schedule        The schedule of the event.
     * @param observesHoliday Whether the event observes holidays.
     * @param category        The category of the event.
     */
    public ActivityEvent(String title, EventSchedule schedule, boolean observesHoliday, Category category) {
        this(title, schedule, null, observesHoliday, category);
    }

    /* ---------- PUBLIC METHODS ---------- */

    /**
     * @return The category of the event.
     */
    public Category getCategory() {
        return this.category;
    }

    @Override
    public boolean equals(Object otherObject) {

        if(!(otherObject instanceof ActivityEvent)) {
            return false;
        }

        Event thisEvent = this;
        Event otherEvent = (Event) otherObject;

        return thisEvent.equals(otherEvent) &&
                this.category.equals(((ActivityEvent) otherObject).getCategory());

    }

    @Override
    @NonNull
    public String toString() {
        return super.toString();
    }

}
