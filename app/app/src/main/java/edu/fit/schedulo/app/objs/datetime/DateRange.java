package edu.fit.schedulo.app.objs.datetime;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

/**
 * Represents a range of dates by defining a start and an end date.
 * Start and end date are considered inclusive, so if the start
 * date is January 1 and the end date is January 3, the dates in
 * the range are [January 1, January 2, January 3].
 *
 * @author Joshua Sheldon
 */
public class DateRange {

    /* ---------- INSTANCE VARIABLES ---------- */

    /**
     * When the range starts.
     */
    private final LocalDate startDate;

    /**
     * When the range ends.
     */
    private final LocalDate endDate;

    /* ---------- CONSTRUCTOR ---------- */

    /**
     * Creates a new <code>DateRange</code> object.
     *
     * @param startDate The start date of the range.
     * @param endDate   The end date of the range.
     */
    @JsonCreator
    public DateRange(@JsonProperty("startDate") LocalDate startDate,
                     @JsonProperty("endDate") LocalDate endDate) {

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Neither start date nor end date can be null.");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        this.startDate = startDate;
        this.endDate = endDate;

    }

    /* ---------- PUBLIC METHODS ---------- */

    /**
     * @return The start date of the range.
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * @return The end date of the range.
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * @param date The date to check.
     * @return Whether the given date is within this range.
     */
    public boolean contains(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    /**
     * @param range The range to check.
     * @return Whether the given date range is within this range.
     */
    public boolean contains(DateRange range) {
        return contains(range.getStartDate()) && contains(range.getEndDate());
    }

    /**
     * @param range The range to check.
     * @return Whether the given date range overlaps with this range.
     */
    public boolean overlaps(DateRange range) {
        return contains(range.getStartDate()) || contains(range.getEndDate());
    }

    /**
     * Checks if this object is equivalent to the given object.
     * Will only return true if the other object is
     * a <code>DateRange</code> object and has the same
     * start and end dates.
     *
     * @param other The object to compare to.
     * @return Whether the two objects are equivalent.
     */
    @Override
    public boolean equals(Object other) {

        if (!(other instanceof DateRange)) {
            return false;
        }

        DateRange range = (DateRange) other;
        return startDate.equals(range.getStartDate()) && endDate.equals(range.getEndDate());

    }

    /**
     * Returns a String in the format of:
     * "<code>startDate</code> - <code>endDate</code>"
     *
     * @return A string representation of this object.
     */
    @NonNull
    @Override
    public String toString() {
        return startDate + " - " + endDate;
    }

}
