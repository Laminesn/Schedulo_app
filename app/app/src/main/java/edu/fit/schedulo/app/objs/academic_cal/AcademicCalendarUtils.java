package edu.fit.schedulo.app.objs.academic_cal;

import android.util.Log;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import edu.fit.schedulo.app.objs.datetime.DateRange;

/**
 * Includes utilities for parsing data from FIT's academic calendars
 * and representing data in a similar format to FIT's academic calendars.
 *
 * @author Joshua Sheldon
 */
public class AcademicCalendarUtils {

    public static final String TAG = AcademicCalendarUtils.class.getSimpleName();

    /* ---------- FORMATTERS ---------- */

    /**
     * Formats a date/time as just the day, without
     * any leading zeroes.
     */
    private static final DateTimeFormatter dayFormatter =
            DateTimeFormatter.ofPattern("d", Locale.ENGLISH);

    /**
     * Formats a date/time in the following format:<br>
     * - 3 letter month abbreviation (ex. "Jan", "Feb", "Mar", etc.)<br>
     * - Day, without any leading zeroes.<br>
     * - Year
     */
    private static final DateTimeFormatter fullDateFormatter =
            DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);

    /**
     * Formats a date/time in the following format:<br>
     * - 3 letter month abbreviation (ex. "Jan", "Feb", "Mar", etc.)<br>
     * - Day, without any leading zeroes.
     */
    private static final DateTimeFormatter monthDayFormatter =
            DateTimeFormatter.ofPattern("MMM d", Locale.ENGLISH);

    /* ---------- METHODS ---------- */

    /**
     * Mimics the formatting of date ranges on the academic calendar.
     *
     * @param dateRange The date range to format.
     * @return A string representation of the date range in
     * one of the following formats: "Mon XX", "Mon XX-XX",
     * or "Mon XX-Mon XX"
     */
    public static String formatRangeLikeAcademicCal(DateRange dateRange) {

        if (dateRange.getStartDate().getYear() != dateRange.getEndDate().getYear()) {
            // Don't support this case
            return dateRange.toString();
        }

        if (dateRange.getStartDate().equals(dateRange.getEndDate())) {
            // Format: "Mon XX"
            return dateRange.getStartDate().format(monthDayFormatter);
        } else if (dateRange.getStartDate().getMonth().equals(dateRange.getEndDate().getMonth())) {
            // Format: "Mon XX-XX"
            String startDate = dateRange.getStartDate().format(monthDayFormatter);
            String endDate = dateRange.getEndDate().format(dayFormatter);
            return startDate + "-" + endDate;
        } else {
            // Format: "Mon XX-Mon XX"
            String startDate = dateRange.getStartDate().format(monthDayFormatter);
            String endDate = dateRange.getEndDate().format(monthDayFormatter);
            return startDate + "-" + endDate;
        }

    }

    /**
     * Determine whether an academic calendar date
     * with the given description is a holiday.
     *
     * @param description The description of the academic calendar date.
     * @return <code>true</code> if the date is a holiday, otherwise <code>false</code>.
     */
    public static boolean isHoliday(String description) {

        if (description == null || description.isEmpty()) {
            return false;
        }

        return description.toLowerCase().contains("(no classes)");

    }

    /**
     * Parses a <code>LocalDate</code> object from the
     * given parameters.
     *
     * @param month The abbreviation of the month,
     *              i.e. ("Jan", "Feb", "Mar", etc.)
     * @param day   The day of the month.
     * @param year  The year.
     * @return A <code>LocalDate</code> object if the
     * parameters are valid, otherwise <code>null</code>.
     */
    public static LocalDate parseDateFromAcademicCal(String month, String day, int year) {

        try {
            return LocalDate.parse(month + " " + day + " " + year, fullDateFormatter);
        } catch (DateTimeParseException ex) {
            Log.e(TAG, "Date could not be parsed. Date: " + month + " " + day + " " + year, ex);
            return null;
        }

    }

    /**
     * Given an academic calendar date from the FIT website and a
     * year, attempts to parse the date text into a <code>DateRange</code>
     * object representing one day, a span of days within a month, or a
     * span of days that spans two months.
     *
     * @param dateText The text of the academic calendar date from the FIT website.
     * @param year     The year of the semester of the academic calendar.
     * @return A <code>DateRange</code> object if the parameters are valid,
     * otherwise <code>null</code>.
     */
    public static DateRange parseRangeFromAcademicCal(String dateText, int year) {

        if (dateText == null || dateText.isEmpty()) {
            Log.e(TAG, "Date must be provided.");
            return null;
        }

        if (year < 0) {
            Log.e(TAG, "Year cannot be negative.");
            return null;
        }

        dateText = dateText.trim();
        String[] splitDate = dateText.split(" ");

        if (!(splitDate.length == 2 || splitDate.length == 3)) {
            Log.e(TAG, "Date must be in one of the following formats: \"Month XX\", " +
                    "\"Month XX-XX\", \"Month XX-Month XX\"");
            return null;
        }

        DateRange dateRange;

        try {
            if (splitDate.length == 2) {
                if (!dateText.contains("-")) {
                    // Format: Month XX
                    LocalDate dateObj = parseDateFromAcademicCal(splitDate[0], splitDate[1], year);
                    dateRange = new DateRange(dateObj, dateObj);
                } else {
                    // Format: Month XX-XX
                    String[] days = splitDate[1].split("-");
                    LocalDate startDate = parseDateFromAcademicCal(splitDate[0], days[0], year);
                    LocalDate endDate = parseDateFromAcademicCal(splitDate[0], days[1], year);
                    dateRange = new DateRange(startDate, endDate);
                }
            } else {
                // Format: Month XX-Month XX
                String[] dates = dateText.split("-");

                String[] dateOneSplit = dates[0].split(" ");
                String[] dateTwoSplit = dates[1].split(" ");

                LocalDate startDate = parseDateFromAcademicCal(dateOneSplit[0], dateOneSplit[1], year);
                LocalDate endDate = parseDateFromAcademicCal(dateTwoSplit[0], dateTwoSplit[1], year);

                dateRange = new DateRange(startDate, endDate);
            }
        } catch (Exception ex) {
            Log.e(TAG, "Date could not be parsed. Date: " + dateText, ex);
            return null;
        }

        return dateRange;

    }

}
