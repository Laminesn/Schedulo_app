package edu.fit.schedulo.app.objs.academic_year;

import android.util.Log;

import androidx.annotation.NonNull;

import edu.fit.schedulo.app.objs.semester.Semester;
import edu.fit.schedulo.app.objs.semester.SemesterType;
import edu.fit.schedulo.app.objs.semester.Semesters;

/**
 * Represents an academic year, such as 2023-2024,
 * where 2023 is the start year and 2024 is the end year.
 * The fall semester is from the start year, and the
 * spring and summer semesters are from the end year.
 *
 * @author Joshua Sheldon
 */
public class AcademicYear {

    public static final String TAG = AcademicYear.class.getSimpleName();

    /**
     * The year that FIT was founded.
     */
    public static final short FIRST_ACADEMIC_YEAR = 1958;

    /* ---------- INSTANCE VARIABLES ---------- */

    /**
     * The start year of the academic year.
     * If the academic year is 2023-2024,
     * this would be 2023.
     */
    private final short startYear;

    /* ---------- CONSTRUCTORS ---------- */

    /**
     * Creates a new academic year object. With package-private
     * visibility, should ensure that academic year objects
     * are only made by <code>AcademicYears</code>.
     *
     * @param startYear The start year of the academic year.
     *                  If the academic year is 2023-2024,
     *                  this would be 2023.
     */
    AcademicYear(short startYear) {

        if (startYear < FIRST_ACADEMIC_YEAR) {
            throw new IllegalArgumentException("FIT did not exist in " + startYear + "!");
        }

        if (startYear == Short.MAX_VALUE) {
            throw new IllegalArgumentException("Cannot create academic year with start year of " + Short.MAX_VALUE + "!");
        }

        this.startYear = startYear;

    }

    /* ---------- PUBLIC METHODS ---------- */

    /**
     * Gets the start year, i.e., if the year is
     * 2023-2024, then return 2023.
     *
     * @return The start year of the academic year.
     */
    public short getStartYear() {
        return startYear;
    }

    /**
     * Gets the end year, i.e., if the year is
     * 2023-2024, then return 2024.
     *
     * @return The end year of the academic year.
     */
    public short getEndYear() {
        return (short) (startYear + 1);
    }

    /**
     * Gets the semester of the given type within this academic year.
     *
     * @param type The type of semester to get.
     * @return The Semester object, or <code>null</code>
     * if type is <code>null</code>
     */
    public Semester getSemester(SemesterType type) {

        if (type == null) {
            Log.e(TAG, "Cannot get semester from academic year with null type.");
            return null;
        }

        if (type == SemesterType.FALL) {
            return Semesters.getInstance().getSemester(type, getStartYear());
        } else {
            return Semesters.getInstance().getSemester(type, getEndYear());
        }

    }

    @Override
    public boolean equals(Object other) {

        if (!(other instanceof AcademicYear)) {
            return false;
        }

        AcademicYear otherYear = (AcademicYear) other;

        return this.getStartYear() == otherYear.getStartYear();

    }

    @NonNull
    @Override
    public String toString() {
        return getStartYear() + "-" + getEndYear();
    }

}
