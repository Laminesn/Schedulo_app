package edu.fit.schedulo.app.objs.course;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.fit.schedulo.app.objs.datetime.EventSchedule;
import edu.fit.schedulo.app.objs.loc.OnCampusLocation;

/**
 * An instance, or section, of a course. Has a unique
 * identifier (CRN), a description ID with information
 * about the course, and an instructor. Optionally,
 * also has a schedule and/or location.
 *
 * @author Joshua Sheldon
 */
public class CourseInstance {

    /* ---------- INSTANCE VARIABLES ---------- */

    /**
     * The course reference number.
     */
    private final int crn;

    /**
     * The course description ID, which references
     * a course description with course code, credit
     * hours, and title.
     */
    private final CourseDescriptionID descriptionID;

    /**
     * The section of the course, such as "01" or "02".
     */
    private final String section;

    /**
     * The schedule of the course, if it has one.
     */
    private final EventSchedule schedule;

    /**
     * The location of the course, if it has one.
     */
    private final OnCampusLocation place;

    /**
     * The instructor of the course.
     */
    private final String instructor;

    /* ---------- CONSTRUCTORS ---------- */

    /**
     * Creates a new <code>CourseInstance</code> object.
     *
     * @param crn           The course reference number.
     * @param descriptionID The course description ID.
     * @param section       The section of the course.
     * @param schedule      The schedule of the course.
     * @param place         The location of the course.
     * @param instructor    The instructor of the course.
     */
    @JsonCreator
    public CourseInstance(@JsonProperty("crn") int crn,
                          @JsonProperty("descriptionID") CourseDescriptionID descriptionID,
                          @JsonProperty("section") String section,
                          @JsonProperty("schedule") EventSchedule schedule,
                          @JsonProperty("place") OnCampusLocation place,
                          @JsonProperty("instructor") String instructor) {
        this.crn = crn;
        this.descriptionID = descriptionID;
        this.section = section;
        this.schedule = schedule;
        this.place = place;
        this.instructor = instructor;
    }

    /**
     * Creates a new <code>CourseInstance</code> object.
     *
     * @param crn           The course reference number.
     * @param descriptionID The course description ID.
     * @param section       The section of the course.
     * @param place         The location of the course.
     * @param instructor    The instructor of the course.
     */
    public CourseInstance(int crn, CourseDescriptionID descriptionID, String section, OnCampusLocation place, String instructor) {
        this(crn, descriptionID, section, null, place, instructor);
    }

    /**
     * Creates a new <code>CourseInstance</code> object.
     *
     * @param crn           The course reference number.
     * @param descriptionID The course description ID.
     * @param section       The section of the course.
     * @param schedule      The schedule of the course.
     * @param instructor    The instructor of the course.
     */
    public CourseInstance(int crn, CourseDescriptionID descriptionID, String section, EventSchedule schedule, String instructor) {
        this(crn, descriptionID, section, schedule, null, instructor);
    }

    /**
     * Creates a new <code>CourseInstance</code> object.
     *
     * @param crn           The course reference number.
     * @param descriptionID The course description ID.
     * @param section       The section of the course.
     * @param instructor    The instructor of the course.
     */
    public CourseInstance(int crn, CourseDescriptionID descriptionID, String section, String instructor) {
        this(crn, descriptionID, section, null, null, instructor);
    }

    /* ---------- PUBLIC METHODS ---------- */

    /**
     * @return The course reference number.
     */
    public int getCRN() {
        return this.crn;
    }

    /**
     * @return The course description ID.
     */
    public CourseDescriptionID getDescriptionID() {
        return this.descriptionID;
    }

    /**
     * @return The section of the course.
     */
    public String getSection() {
        return this.section;
    }

    /**
     * @return The schedule of the course.
     */
    public EventSchedule getSchedule() {
        return this.schedule;
    }

    /**
     * @return The location of the course.
     */
    public OnCampusLocation getPlace() {
        return this.place;
    }

    /**
     * @return The instructor of the course.
     */
    public String getInstructor() {
        return this.instructor;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.crn);
    }

    @Override
    public boolean equals(Object otherObject) {

        if(!(otherObject instanceof CourseInstance)) {
            return false;
        }

        CourseInstance otherInstance = (CourseInstance) otherObject;

        boolean mostlyEqual = this.crn == otherInstance.getCRN() &&
                              this.descriptionID.equals(otherInstance.getDescriptionID()) &&
                              this.section.equals(otherInstance.getSection()) &&
                              this.instructor.equals(otherInstance.getInstructor());

        if (!mostlyEqual) {
            return false;
        }

        boolean scheduleEqual;

        if (this.schedule == null && otherInstance.getSchedule() == null) {
            scheduleEqual = true;
        } else if (this.schedule == null || otherInstance.getSchedule() == null) {
            scheduleEqual = false;
        } else {
            scheduleEqual = this.schedule.equals(otherInstance.getSchedule());
        }

        if (!scheduleEqual) {
            return false;
        }

        boolean placeEqual;

        if (this.place == null && otherInstance.getPlace() == null) {
            placeEqual = true;
        } else if (this.place == null || otherInstance.getPlace() == null) {
            placeEqual = false;
        } else {
            placeEqual = this.place.equals(otherInstance.getPlace());
        }

        return placeEqual;

    }

    @Override
    @NonNull
    public String toString() {
        return "" + this.crn;
    }

}
