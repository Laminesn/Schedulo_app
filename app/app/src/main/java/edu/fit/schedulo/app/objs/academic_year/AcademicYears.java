package edu.fit.schedulo.app.objs.academic_year;

import android.util.Log;

import java.util.Map;
import java.util.TreeMap;

import edu.fit.schedulo.app.objs.semester.Semester;
import edu.fit.schedulo.app.objs.semester.SemesterType;

/**
 * Factory class for AcademicYear objects.
 *
 * @author Joshua Sheldon
 */
public class AcademicYears {

    public static final String TAG = AcademicYears.class.getSimpleName();

    /* ---------- INSTANCE VARIABLES ---------- */

    /**
     * Map from the start year of the academic year to
     * the <code>AcademicYear</code> object.
     */
    private final Map<Short, AcademicYear> academicYears;

    /* ---------- CONSTRUCTOR ---------- */

    /**
     * Creates a new <code>AcademicYears</code> object.
     */
    private AcademicYears() {
        this.academicYears = new TreeMap<>();
    }

    /* ---------- SINGLETON ---------- */

    private static final AcademicYears instance = new AcademicYears();

    public static AcademicYears getInstance() {
        return instance;
    }

    /* ---------- PUBLIC METHODS ---------- */

    /**
     * Attempts to get the academic year object with the
     * start year given (ex. if the start year is 2023,
     * academic year 2023-2024 will be returned).
     *
     * @param startYear The start year of the academic year.
     * @return The academic year object, unless the
     * parameter is less than
     * <code>AcademicYear.FIRST_ACADEMIC_YEAR</code> or
     * equal to <code>Short.MAX_VALUE</code>, in which
     * case <code>null</code> is returned.
     */
    public synchronized AcademicYear getAcademicYear(short startYear) {

        if (!this.academicYears.containsKey(startYear)) {
            this.academicYears.put(startYear, new AcademicYear(startYear));
        }

        return this.academicYears.get(startYear);

    }

    /**
     * Gets academic year object from a string like
     * "2023-2024".
     *
     * @param academicYearText The academic year text.
     * @return The academic year object, or <code>null</code>
     * if the text is not in the correct format.
     */
    public AcademicYear getAcademicYear(String academicYearText) {

        if (academicYearText.length() != 9) {
            Log.e(TAG, "Invalid academic year text: \"" + academicYearText + "\"");
            return null;
        }

        String startYear = academicYearText.substring(0, 4);

        short startYearShort;

        try {
            startYearShort = Short.parseShort(startYear);
        } catch (Exception ex) {
            Log.e(TAG, "Invalid start year for academic year: \"" + startYear + "\"");
            return null;
        }

        return this.getAcademicYear(startYearShort);

    }

    /**
     * Get the academic year the given semester is within.
     *
     * @param semester The semester to get the academic year from.
     * @return The academic year the semester is within.
     */
    public AcademicYear getYearFromSemester(Semester semester) {

        int startYear = semester.getYear();

        if (semester.getType() == SemesterType.SPRING || semester.getType() == SemesterType.SUMMER) {
            startYear--;
        }

        return this.getAcademicYear((short) startYear);

    }

}
