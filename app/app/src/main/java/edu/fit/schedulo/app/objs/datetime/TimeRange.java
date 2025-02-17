package edu.fit.schedulo.app.objs.datetime;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a range of times by defining a start and an end time.
 * Start and end time are considered inclusive.
 *
 * @author Joshua Sheldon
 */
public class TimeRange {

    public static final DateTimeFormatter classScheduleFormatter =
            DateTimeFormatter.ofPattern("HHmm");

    /* ---------- INSTANCE VARIABLES ---------- */

    /**
     * When the range starts.
     */
    private final LocalTime startTime;

    /**
     * When the range ends.
     */
    private final LocalTime endTime;

    /* ---------- CONSTRUCTOR ---------- */

    /**
     * Creates a new <code>TimeRange</code> object.
     *
     * @param startTime The start time of the range.
     * @param endTime   The end time of the range.
     */
    @JsonCreator
    public TimeRange(@JsonProperty("startTime") LocalTime startTime,
                     @JsonProperty("endTime") LocalTime endTime) {

        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Neither start time nor end time can be null.");
        }

        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time cannot be after end time.");
        }

        this.startTime = startTime;
        this.endTime = endTime;

    }

    /* ---------- PUBLIC METHODS ---------- */

    /**
     * @return The start time of the range.
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * @return The end time of the range.
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * @param time The time to check.
     * @return Whether the given time is within this range.
     */
    public boolean contains(LocalTime time) {
        return !time.isBefore(startTime) && !time.isAfter(endTime);
    }

    /**
     * @param range The range to check.
     * @return Whether the given time range is within this range.
     */
    public boolean contains(TimeRange range) {
        return contains(range.getStartTime()) && contains(range.getEndTime());
    }

    /**
     * @param range The range to check.
     * @return Whether the given time range overlaps with this range.
     */
    public boolean overlaps(TimeRange range) {
        return contains(range.getStartTime()) || contains(range.getEndTime());
    }

    /**
     * Checks if this object is equivalent to the given object.
     * Will only return true if the other object is
     * a <code>TimeRange</code> object and has the same
     * start and end times.
     *
     * @param other The object to compare to.
     * @return Whether the two objects are equivalent.
     */
    @Override
    public boolean equals(Object other) {

        if (!(other instanceof TimeRange)) {
            return false;
        }

        TimeRange range = (TimeRange) other;
        return startTime.equals(range.getStartTime()) &&
                endTime.equals(range.getEndTime());

    }

    /**
     * Returns this time range as a string, formatted in the same
     * fashion as the FIT class schedules: <code>HHmm-HHmm</code>
     *
     * @return A string representation of this object.
     */
    public String toClassScheduleString() {
        return this.startTime.format(classScheduleFormatter) + "-" +
                this.endTime.format(classScheduleFormatter);
    }

    /**
     * Returns a String in the format of:
     * "<code>startTime</code> - <code>endTime</code>"
     *
     * @return A string representation of this object.
     */
    @NonNull
    @Override
    public String toString() {
        return startTime + " - " + endTime;
    }

}
