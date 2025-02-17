package edu.fit.schedulo.app.objs.datetime;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.DayOfWeek;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Schedule for an event, where the event can occur on multiple days
 * of the week, each with a different time range.
 *
 * @author Joshua Sheldon
 */
@JsonSerialize(using = EventScheduleSerializer.class)
@JsonDeserialize(using = EventScheduleDeserializer.class)
public class EventSchedule {

    /* ---------- INSTANCE VARIABLES ---------- */

    /**
     * The underlying schedule map
     */
    private final Map<DayOfWeek, TimeRange> schedule;

    /* ---------- CONSTRUCTOR ---------- */

    /**
     * Creates a new <code>EventSchedule</code> object.
     * The schedule is a map from days of the week to time ranges.
     *
     * @param schedule The schedule.
     */
    public EventSchedule(Map<DayOfWeek, TimeRange> schedule) {

        if (schedule == null || schedule.isEmpty()) {
            throw new IllegalArgumentException("Schedule cannot be null or empty.");
        }

        this.schedule = schedule;

    }

    /**
     * Creates a new <code>EventSchedule</code> object.
     * The schedule is a map from days of the week to time ranges.
     *
     * @param daysToRanges A map from day of the week to
     *                     index of the day's time range
     *                     in the <code>timeRanges</code>
     *                     array.
     * @param timeRanges   The unique time ranges.
     */
    public EventSchedule(Map<DayOfWeek, Integer> daysToRanges, TimeRange[] timeRanges) {

        if (daysToRanges == null || timeRanges == null) {
            throw new IllegalArgumentException("Cannot use null parameters.");
        }

        // Create new schedules map
        Map<DayOfWeek, TimeRange> schedule = new HashMap<>();

        // Loop through all days and add their corresponding
        // time range to the schedule
        for (Map.Entry<DayOfWeek, Integer> entry : daysToRanges.entrySet()) {

            DayOfWeek day = entry.getKey();
            Integer rangeIndex = entry.getValue();

            if (day == null || rangeIndex == null) {
                throw new IllegalArgumentException("Cannot use null parameters.");
            }

            if (rangeIndex < 0 || rangeIndex >= timeRanges.length) {
                throw new IllegalArgumentException("Invalid range index.");
            }

            schedule.put(day, timeRanges[rangeIndex]);

        }

        if (schedule.isEmpty()) {
            throw new IllegalArgumentException("Schedule cannot be empty.");
        }

        this.schedule = schedule;

    }

    /* ---------- PUBLIC METHODS ---------- */

    /**
     * @return All days where this event takes place.
     */
    public Set<DayOfWeek> getDays() {
        return schedule.keySet();
    }

    /**
     * @return The direct underlying map of the schedule.
     */
    public Map<DayOfWeek, TimeRange> getMap() {
        return schedule;
    }

    /**
     * Returns the time range for the given day.
     *
     * @param day The day of the week.
     * @return <code>TimeRange</code>, or <code>null</code>
     * if the parameter is invalid or if the schedule doesn't
     * have a time range for the given day.
     */
    public TimeRange getTimeForDay(DayOfWeek day) {

        if (day == null) {
            return null;
        }

        return schedule.get(day);

    }

    @NonNull
    @Override
    public String toString() {

        // Sort time ranges by start time
        PriorityQueue<TimeRange> trQueue =
                new PriorityQueue<>(Comparator.comparing(TimeRange::getStartTime));

        // Map from time range to days of the week (ex. "MWF")
        Map<TimeRange, StringBuilder> trToDaysString = new HashMap<>();

        // Loop through all days, to ensure when we construct
        // the days string, we do it in order of the days
        // of the week
        for (int i = 1; i < 7; i++) {

            DayOfWeek day = DayOfWeek.of(i);
            TimeRange timeRange = this.schedule.get(day);

            if (timeRange == null) {
                // Not in schedule
                continue;
            }

            StringBuilder daysString = trToDaysString.get(timeRange);

            if (daysString != null) {

                // Append to existing string builder
                daysString.append(getDayChar(day));

            } else {

                // Create new string builder and
                // add the day character to it
                StringBuilder sb = new StringBuilder();
                sb.append(getDayChar(day));

                // Store string builder in map
                trToDaysString.put(timeRange, sb);

                // Add the time range to a priority queue,
                // because we want to order the time ranges
                // by start time
                trQueue.add(timeRange);

            }

        }

        // Build the final string
        StringBuilder finalString = new StringBuilder();

        while (!trQueue.isEmpty()) {

            TimeRange timeRange = trQueue.poll();
            StringBuilder daysString = trToDaysString.get(timeRange);

            if (timeRange == null || daysString == null) {
                // Shouldn't be possible
                continue;
            }

            // Append the time range and days string to the final string
            finalString.append(daysString);
            finalString.append(" ");
            finalString.append(timeRange.toClassScheduleString());

            // If there's more than one time range, add a comma
            if (!trQueue.isEmpty()) {
                finalString.append(", ");
            }

        }

        return finalString.toString();

    }

    /* ---------- PRIVATE METHODS ---------- */

    /**
     * Returns the character representation of the given day,
     * taken from
     * <a href="https://www.fit.edu/registrar/class-schedules/">here</a>.
     *
     * @param day The day of the week.
     * @return The character representation of the day.
     */
    private char getDayChar(DayOfWeek day) {
        switch (day) {
            case MONDAY:
                return 'M';
            case TUESDAY:
                return 'T';
            case WEDNESDAY:
                return 'W';
            case THURSDAY:
                return 'R';
            case FRIDAY:
                return 'F';
            case SATURDAY:
                return 'S';
            case SUNDAY:
                return 'U';
            default:
                return 'X';
        }
    }

}
