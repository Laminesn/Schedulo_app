package edu.fit.schedulo.app.objs.course;

import android.util.Log;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import edu.fit.schedulo.app.objs.academic_year.AcademicYear;

/**
 * Represents the description of a course, including its prefix,
 * code, the number of credit hours its worth, its title,
 * and the catalog that it came from. These descriptions have IDs
 * which are referenced in <code>CourseInstance</code> objects
 * so that you can easily refer to a course's description
 * from its instance.
 *
 * @author Joshua Sheldon
 */
@JsonSerialize(using = CourseDescriptionSerializer.class)
public class CourseDescription {

    public static final String TAG = CourseDescription.class.getSimpleName();

    /* ---------- INSTANCE VARIABLES ---------- */

    /**
     * The ID of the course description.
     * Will only be set if the course description
     * ID is requested.
     */
    private CourseDescriptionID id;

    /**
     * The prefix of the course, such as "CSE" or "MTH"
     */
    private final String prefix;

    /**
     * The code of the course, such as 1002 or 2201
     */
    private final short code;

    /**
     * The number of credit hours the course is worth.
     * String to support courses with variable credit
     * hours.
     */
    private final String creditHours;

    /**
     * The full or abbreviated title of the course
     * (ex. "Diff Equat/Linear Algebra" or
     * "Differential Equations/Linear Algebra")
     */
    private final String title;

    /**
     * The academic year of the catalog in which
     * this course description was published.
     */
    private final AcademicYear catalogYear;

    /* ---------- CONSTRUCTORS ---------- */

    /**
     * Attempts to create a new course description. Will
     * fail if any of the parameters or null or if any of the
     * strings are empty.
     *
     * @param prefix      A course prefix, such as "CSE" or "MTH"
     * @param code        A course code, such as 1002 or 2201
     * @param creditHours The number of credit hours the course is worth
     *                    (can be single number or range, ex. "3" or "3-6")
     * @param title       The full or abbreviated title of the course
     *                    (ex. "Diff Equat/Linear Algebra" or
     *                    "Differential Equations/Linear Algebra")
     * @param catalogYear The academic year of the catalog in which
     *                    this course description was published.
     */
    public CourseDescription(String prefix, Short code, String creditHours, String title, AcademicYear catalogYear) {

        if (prefix == null || code == null || creditHours == null || title == null || catalogYear == null) {
            throw new IllegalArgumentException("Cannot create course description with null parameters!");
        }

        prefix = prefix.trim().toUpperCase();

        if (!CourseDescription.isCoursePrefixValid(prefix)) {
            throw new IllegalArgumentException("Invalid course prefix: \"" + prefix + "\"");
        }

        if (code < 0 || code > Catalog.MAX_COURSE_CODE) {
            throw new IllegalArgumentException("Invalid course code: \"" + code + "\"");
        }

        if (title.isEmpty()) {
            throw new IllegalArgumentException("Course title cannot be empty.");
        }

        this.id = null;
        this.prefix = prefix;
        this.code = code;
        this.creditHours = creditHours;
        this.title = title;
        this.catalogYear = catalogYear;

    }

    /**
     * Attempts to create a new course description.
     *
     * @param courseID    A combined course prefix and code, ex. "CSE 1002",
     *                    "CSE1002", "MTH 2201", "MTH2201", etc.
     * @param creditHours The number of credit hours the course is worth.
     *                    (can be single number or range, ex. "3" or "3-6")
     * @param title       The full or abbreviated title of the course, such as
     *                    "Diff Equat/Linear Algebra" or
     *                    "Differential Equations/Linear Algebra"
     * @param catalogYear The academic year of the catalog in which
     *                    this course description was published.
     */
    public CourseDescription(String courseID, String creditHours, String title, AcademicYear catalogYear) {
        this(
                CourseDescription.coursePrefixFromID(courseID),
                CourseDescription.courseCodeFromID(courseID),
                creditHours,
                title,
                catalogYear
        );
    }

    /* ---------- PUBLIC METHODS ---------- */

    /**
     * @return The course prefix, such as "CSE" or "MTH"
     */
    public String getPrefix() {
        return this.prefix;
    }

    /**
     * @return The academic year of the catalog in which
     * this course description was published.
     */
    public AcademicYear getCatalogYear() {
        return this.catalogYear;
    }

    /**
     * @return The course code, such as 1002 or 2201
     */
    public short getCode() {
        return this.code;
    }

    /**
     * @return The number of credit hours the course is worth,
     * can be single number or range, ex. "3" or "3-6"
     */
    public String getCreditHours() {
        return this.creditHours;
    }

    /**
     * Returns a course description ID object for this course.
     */
    public CourseDescriptionID getID() {

        if (this.id == null) {
            this.id = new CourseDescriptionID(this);
        }

        return this.id;

    }

    /**
     * @return The full or abbreviated title of the course
     */
    public String getTitle() {
        return this.title;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof CourseDescription)) {
            return false;
        }

        CourseDescription otherCourse = (CourseDescription) other;

        return this.prefix.equals(otherCourse.getPrefix()) &&
                this.code == otherCourse.getCode() &&
                this.creditHours.equals(otherCourse.getCreditHours()) &&
                this.title.equals(otherCourse.getTitle()) &&
                this.catalogYear.equals(otherCourse.getCatalogYear());
    }

    @NonNull
    @Override
    public String toString() {
        return this.title + " (" + this.prefix + " " + this.code + ")";
    }

    /* ---------- STATIC METHODS ---------- */

    /**
     * Pulls the course prefix (i.e. "CSE", "MTH") from a
     * course ID (course prefix + code)
     *
     * @param courseID A course ID in the format "CSE 1002" or "MTH2201"
     * @return The course prefix, or <code>null</code> if the course ID is invalid
     */
    public static String coursePrefixFromID(String courseID) {

        if (courseID.length() < 7) {
            Log.e(TAG, "Invalid course ID while trying to create course description: \"" + courseID + "\"");
            return null;
        }

        return courseID.substring(0, 3);

    }

    /**
     * Pulls the course code (i.e. 1002, 2201) from a
     * course ID (course prefix + code)
     *
     * @param courseID A course ID in the format "CSE 1002" or "MTH2201"
     * @return The course code, or <code>null</code> if the course ID is invalid
     */
    public static Short courseCodeFromID(String courseID) {

        String codeString;

        if (courseID.length() == 7) {
            codeString = courseID.substring(3);
        } else if (courseID.length() == 8) {
            codeString = courseID.substring(4);
        } else {
            Log.e(TAG, "Invalid course ID while trying to create course description: \"" + courseID + "\"");
            return null;
        }

        short code;

        try {
            code = Short.parseShort(codeString);
        } catch (Exception ex) {
            Log.e(TAG, "Invalid course code: \"" + codeString + "\"");
            return null;
        }

        return code;

    }

    /**
     * Verifies the given course prefix is valid.
     * It is assumed the parameter is already
     * trimmed and converted to uppercase.
     *
     * @param coursePrefix The course prefix to validate.
     * @return <code>true</code> if the course prefix is valid,
     * <code>false</code> otherwise.
     */
    public static boolean isCoursePrefixValid(String coursePrefix) {

        if (coursePrefix == null || coursePrefix.length() != 3) {
            return false;
        }

        // Ensure each character of the course prefix is
        // an uppercase letter of the English alphabet
        for (char c : coursePrefix.toCharArray()) {
            if (c < 'A' || c > 'Z') {
                return false;
            }
        }

        return true;

    }

}
