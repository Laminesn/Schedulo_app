package edu.fit.schedulo.app.objs.academic_cal;

import edu.fit.schedulo.app.objs.datetime.DateRange;

/**
 * Creates new instances of the <code>AcademicCalendarDate</code> class.
 *
 * @author Joshua Sheldon
 */
public class AcademicCalendarDateFactory {

    /**
     * Attempt to create a new <code>AcademicCalendarDate</code>
     * object from the given parameters.
     *
     * @param dateFromPage The date as it appears on the academic
     *                     calendar page.
     * @param year         The year of the semester.
     * @param description  A description of the academic calendar event.
     * @return A new <code>AcademicCalendarDate</code> object if
     * the parameters are valid, otherwise <code>null</code>.
     */
    public static AcademicCalendarDate createDate(String dateFromPage, int year, String description) {

        DateRange dateRange = AcademicCalendarUtils.parseRangeFromAcademicCal(dateFromPage, year);

        // Couldn't parse date range
        if (dateRange == null) {
            return null;
        }

        boolean holiday = AcademicCalendarUtils.isHoliday(description);
        return new AcademicCalendarDate(dateRange, description, holiday);

    }

}
