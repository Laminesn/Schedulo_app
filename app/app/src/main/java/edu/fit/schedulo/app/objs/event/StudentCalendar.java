package edu.fit.schedulo.app.objs.event;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import edu.fit.schedulo.app.objs.semester.Semester;

/**
 * Manages a student's calendar events on a per-semester basis.
 *
 * @author Lamine Djibo
 * Modified by Joshua Sheldon
 */
@JsonSerialize(using = StudentCalendarSerializer.class)
public class StudentCalendar {

    /* ---------- INSTANCE VARIABLES ---------- */

    /**
     * Map from Semester to a list of Event objects for that semester.
     */
    private final Map<Semester, List<Event>> eventsMap;

    /* ---------- CONSTRUCTOR ---------- */

    /**
     * Private constructor to prevent external instantiation.
     */
    private StudentCalendar() {
        this.eventsMap = new HashMap<>();
    }

    /* ---------- SINGLETON ---------- */

    private static final StudentCalendar instance = new StudentCalendar();

    public static StudentCalendar getInstance() {
        return instance;
    }

    /* ---------- PUBLIC METHODS ---------- */

    /**
     * Adds an event to the calendar for a specific semester.
     *
     * @param semester The semester to add the event to.
     * @param event    The event to add.
     */
    public synchronized void addEvent(Semester semester, Event event) {
        List<Event> eventsForSemester = this.eventsMap.getOrDefault(semester, new LinkedList<>());
        eventsForSemester.add(event);
        this.eventsMap.put(semester, eventsForSemester);
    }

    /**
     * Retrieves all events for a given semester.
     *
     * @param semester The semester to retrieve events for.
     * @return A list of events for the specified semester.
     */
    public synchronized List<Event> getEvents(Semester semester) {
        return this.eventsMap.getOrDefault(semester, new LinkedList<>());
    }

    /**
     * @return A set containing all semesters the calendar
     * has events for.
     */
    public synchronized Set<Semester> getSemesters() {
        return Collections.unmodifiableSet(this.eventsMap.keySet());
    }

    /**
     * Clears all events from the calendar.
     */
    public synchronized void clearCalendar() {
        this.eventsMap.clear();
    }

}
