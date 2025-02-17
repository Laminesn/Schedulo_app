package edu.fit.schedulo.app.objs.academic_cal;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.fit.schedulo.app.objs.datetime.DateRange;

/**
 * A class representing a date or date range of interest on the academic calendar.
 * This could be a holiday, the last day to drop courses with a 'W' grade,
 * spring break, etc. Has a flag to indicate if it is a holiday,
 * i.e. whether there are no classes on that date.
 *
 * @author Joshua Sheldon
 */
public class AcademicCalendarDate {

    /* ---------- INSTANCE VARIABLES ---------- */

    /**
     * The date(s) of the academic calendar event.
     */
    private final DateRange dateRange;

    /**
     * A description of the academic calendar event.
     */
    private final String description;

    /**
     * Whether the academic calendar event is a holiday,
     * i.e. whether there are no classes on that date.
     */
    private final boolean holiday;

    /* ---------- CONSTRUCTOR ---------- */

    /**
     * Creates a new <code>AcademicCalendarDate</code> object.
     *
     * @param dateRange   The date(s) of the academic calendar event.
     * @param description A description of the academic calendar event.
     * @param holiday     Whether the academic calendar event is a holiday.
     */
    @JsonCreator
    AcademicCalendarDate(@JsonProperty("dateRange") DateRange dateRange,
                         @JsonProperty("description") String description,
                         @JsonProperty("holiday") boolean holiday) {

        if (dateRange == null) {
            throw new IllegalArgumentException("Date(s) cannot be null.");
        }

        if (description == null) {
            description = "";
        }

        this.dateRange = dateRange;
        this.description = description;
        this.holiday = holiday;

    }

    /* ---------- PUBLIC METHODS ---------- */

    /**
     * @return The date(s) of the academic calendar event.
     */
    public DateRange getDateRange() {
        return dateRange;
    }

    /**
     * @return A description of the academic calendar event.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return Whether the academic calendar event is a holiday.
     */
    public boolean isHoliday() {
        return holiday;
    }

    /**
     * @return Whether this date has a description.
     */
    public boolean hasDescription() {
        return !description.isEmpty();
    }

    /**
     * Checks if this object is equivalent to the given object.
     * Will only return true if the other object is a
     * <code>AcademicCalendarDate</code> object and has the same
     * date range, description, and holiday status.
     *
     * @param other The object to compare to.
     * @return Whether this object is equal to the other object.
     */
    public boolean equals(Object other) {

        if (!(other instanceof AcademicCalendarDate)) {
            return false;
        }

        AcademicCalendarDate otherDate = (AcademicCalendarDate) other;

        return dateRange.equals(otherDate.getDateRange()) &&
                description.equals(otherDate.getDescription()) &&
                holiday == otherDate.isHoliday();

    }

    /**
     * Returns a String in the format of:
     * "<code>dateRange</code>[ + ( + <code>description</code> + )]"
     * Where the content within the brackets is only included
     * if this object has a description.
     *
     * @return A string representation of the academic calendar event.
     */
    @NonNull
    @Override
    public String toString() {

        StringBuilder returnVal = new StringBuilder();
        returnVal.append(AcademicCalendarUtils.formatRangeLikeAcademicCal(dateRange));

        if (hasDescription()) {
            returnVal.append(" | ");
            returnVal.append(description);
        }

        return returnVal.toString();

    }

}
