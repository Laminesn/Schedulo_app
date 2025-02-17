package edu.fit.schedulo.app.objs.mood;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zion Taylor
 * Modified by Joshua Sheldon
 */
@JsonSerialize(using = MoodReportsSerializer.class)
public class MoodReports {

    /* ---------- INSTANCE VARIABLES ---------- */

    /**
     * A map of mood reports with the key being the date of the report.
     */
    private final Map<LocalDate, MoodReport> reports;

    /* ---------- CONSTRUCTOR ---------- */

    private MoodReports() {
        this.reports = new HashMap<>();
    }

    /* ---------- SINGLETON ---------- */

    private static final MoodReports instance = new MoodReports();

    public static MoodReports getInstance() {
        return instance;
    }

    /* ---------- METHODS ---------- */

    /**
     * Adds a mood report to the list of reports.
     *
     * @param date   The date of the report.
     * @param report The report to add.
     */
    public synchronized void addReport(LocalDate date, MoodReport report) {
        this.reports.put(date, report);
    }

    /**
     * Retrieves a mood report from the list of reports from a specific date.
     *
     * @param date The date of the report to retrieve.
     * @return The report from the specified date or null if a report does not exist on specified date.
     */
    public synchronized MoodReport getReport(LocalDate date) {
        return this.reports.get(date);
    }

    /**
     * List all MoodReports
     *
     * @return The list of all mood reports.
     */
    public synchronized Map<LocalDate, MoodReport> listAllReports() {
        return new HashMap<>(reports); // Return a copy of the reports map.
    }

    /**
     * Removes a mood report from the list of reports.
     *
     * @param date The date of the report to remove.
     */
    public synchronized void removeReport(LocalDate date) {
        this.reports.remove(date);
    }

    /**
     * Checks if a mood report exists on a specific date.
     *
     * @param date The date to check for a report.
     * @return True if a report exists on the specified date, false otherwise.
     */
    public synchronized boolean hasReport(LocalDate date) {
        return this.reports.containsKey(date);
    }

}