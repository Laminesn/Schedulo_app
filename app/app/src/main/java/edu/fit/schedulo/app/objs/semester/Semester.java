package edu.fit.schedulo.app.objs.semester;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;

import edu.fit.schedulo.app.objs.academic_cal.AcademicCalendarDate;
import edu.fit.schedulo.app.objs.academic_cal.AcademicCalendarDateFactory;

/**
 * Represents a semester within an academic year.
 * Can store academic calendar dates for important
 * holiday and non-holiday events throughout the semester.
 * Academic calendar dates can last one or more days each.
 *
 * @author Joshua Sheldon
 */
public class Semester {

    public static final String TAG = Semester.class.getSimpleName();

    /* ---------- INSTANCE VARIABLES ---------- */

    /**
     * The type of semester (fall, spring, or summer)
     */
    private final SemesterType type;

    /**
     * The year of the semester.
     */
    private final short year;

    /**
     * A list of academic calendar dates for the semester.
     * These could be holidays, spring break, last day to
     * withdraw from a course, etc.
     */
    private final LinkedList<AcademicCalendarDate> academicCalendarDates;

    /* ---------- CONSTRUCTOR ---------- */

    /**
     * Creates a new <code>Semester</code> object.
     * Constructor should only be used by
     * <code>SemesterFactory</code>.
     *
     * @param type The type of semester (fall, spring, or summer)
     * @param year The year of the semester.
     */
    Semester(SemesterType type, short year) {

        if (type == null) {
            throw new IllegalArgumentException("Semester type cannot be null.");
        }

        if (year < 0) {
            throw new IllegalArgumentException("Year cannot be negative.");
        }

        this.type = type;
        this.year = year;
        this.academicCalendarDates = new LinkedList<>();

    }

    /* ---------- PUBLIC METHODS ---------- */

    /**
     * Adds an academic calendar date to the semester.
     * Synchronized to avoid conflict when retrieving
     * the list of academic calendar dates.
     *
     * @param date The academic calendar date to add.
     */
    public synchronized void addCalDate(AcademicCalendarDate date) {

        if (date == null) {
            System.err.println("Cannot add null AcademicCalendarDate to Semester.");
            return;
        }

        academicCalendarDates.push(date);

    }

    /**
     * Adds an academic calendar date to the semester.
     * Synchronized to avoid conflict when retrieving
     * the list of academic calendar dates.
     *
     * @param dateFromPage The date scraped from the FIT academic calendar website.
     *                     In format "Mon XX", "Mon XX-XX", or "Mon XX-Mon XX"
     * @param description  A description of the academic calendar event, like
     *                     "Last day to withdraw from a class with a final grade of W".
     */
    public synchronized void addCalDate(String dateFromPage, String description) {

        AcademicCalendarDate date = AcademicCalendarDateFactory.createDate(
                dateFromPage,
                year,
                description
        );

        if (date != null) {
            academicCalendarDates.push(date);
        } else {
            Log.e(TAG, "Failed to create academic calendar date.");
        }

    }

    /**
     * Adds a list of academic calendar dates to the semester.
     * Synchronized to avoid conflict when retrieving
     * the list of academic calendar dates.
     *
     * @param inputDates A list of academic calendar dates, where
     *                   the first value is the date, and the second
     *                   value is the description, both scraped from
     *                   the FIT website.
     */
    public synchronized void addCalDates(List<Pair<String, String>> inputDates) {

        for (Pair<String, String> datePair : inputDates) {

            AcademicCalendarDate dateObject = AcademicCalendarDateFactory.createDate(
                    datePair.first,
                    year,
                    datePair.second
            );

            if (dateObject == null) {
                Log.e(TAG, "Failed to create academic calendar date from pair: " +
                        "<\"" + datePair.first + "\", \"" + datePair.second + "\">");
                continue;
            }

            academicCalendarDates.push(dateObject);

        }

    }

    /**
     * @return The type of semester (fall, spring, or summer)
     */
    public SemesterType getType() {
        return type;
    }

    /**
     * @return The year of the semester.
     */
    public short getYear() {
        return year;
    }

    /**
     * Retrieves all academic calendar dates for the semester.
     * Synchronized to avoid conflict when adding academic
     * calendar dates.
     *
     * @return A list of academic calendar dates for this semester.
     * The list is a copy of the original list, so the original
     * list is not modified.
     */
    public synchronized List<AcademicCalendarDate> getCalDates() {
        return new LinkedList<>(academicCalendarDates);
    }

    /**
     * Checks if this object is equivalent to the given object.
     * Will only return true if the other object is a
     * <code>Semester</code> object and has the same
     * type and year. Note that academic calendar dates
     * are not checked.
     *
     * @param other The object to compare to.
     * @return Whether this object is equal to the other object.
     */
    @Override
    public synchronized boolean equals(Object other) {

        if (!(other instanceof Semester)) {
            return false;
        }

        Semester otherSemester = (Semester) other;

        return type.equals(otherSemester.getType()) &&
                year == otherSemester.getYear();

    }

    /**
     * @return The hash code of the String representation of this object.
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * Returns a String in the format of:
     * "<code>type</code> <code>year</code>"
     *
     * @return A string representation of this object.
     */
    @NonNull
    @Override
    public String toString() {
        return type + " " + year;
    }

}
