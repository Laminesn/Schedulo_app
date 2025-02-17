package edu.fit.schedulo.app.objs.course;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.fit.schedulo.app.objs.academic_year.AcademicYear;

/**
 * Maps from course description ID to course description objects.
 * Should store all course descriptions the app has.
 *
 * @author Lamine Djibo
 * Modified by Joshua Sheldon
 */
@JsonSerialize(using = CatalogSerializer.class)
public class Catalog {

    /* ---------- CONSTANTS ---------- */

    /**
     * The highest valid course code in FIT's catalogs.
     */
    public static final short MAX_COURSE_CODE = 6_999;

    /* ---------- INSTANCE VARIABLES ---------- */

    /**
     * Maps from course description ID to actual course description
     * object.
     */
    private final HashMap<CourseDescriptionID, CourseDescription> descriptions;

    /* ---------- CONSTRUCTOR ---------- */

    private Catalog() {
        this.descriptions = new HashMap<>();
    }

    /* ---------- SINGLETON ---------- */

    private static final Catalog instance = new Catalog();

    public static Catalog getInstance() {
        return instance;
    }

    /* ---------- PUBLIC METHODS ---------- */

    /**
     * Adds a course description to the catalog.
     *
     * @param courseDescription The course description to add.
     */
    public synchronized void addCourseDescription(CourseDescription courseDescription) {
        this.descriptions.put(courseDescription.getID(), courseDescription);
    }

    /**
     * Retrieves a course description by its ID.
     *
     * @param id The ID of the course description to retrieve.
     * @return The course description with the specified ID,
     * or <code>null</code> if none is found.
     */
    public synchronized CourseDescription getCourseDescriptionByID(CourseDescriptionID id) {
        return this.descriptions.get(id);
    }

    /**
     * Retrieves all course descriptions listed under a specific academic year.
     *
     * @param academicYear The academic year for which to retrieve course descriptions.
     * @return A list of course descriptions for the specified academic year,
     * or an empty list if none are found.
     */
    public synchronized List<CourseDescription> getCourseDescriptionsByYear(AcademicYear academicYear) {

        LinkedList<CourseDescription> descriptions = new LinkedList<>();

        for (CourseDescription description : this.descriptions.values()) {
            // Make sure the course description is from the correct year
            if (description.getCatalogYear().getStartYear() == academicYear.getStartYear()) {
                descriptions.push(description);
            }
        }

        return descriptions;

    }

    /**
     * Retrieves all academic years present in the catalog.
     *
     * @return A set of academic years stored in the catalog.
     */
    public synchronized Set<AcademicYear> getAcademicYears() {

        Set<AcademicYear> academicYears = Collections.emptySet();

        for (CourseDescription description : this.descriptions.values()) {
            academicYears.add(description.getCatalogYear());
        }

        return academicYears;

    }

    /**
     * Retrieves all course descriptions stored in the catalog.
     *
     * @return A collection of all course descriptions stored in the catalog.
     */
    public synchronized Collection<CourseDescription> getAllCourseDescriptions() {
        return Collections.unmodifiableCollection(this.descriptions.values());
    }

    /**
     * Clears all course descriptions from the catalog.
     */
    public synchronized void clearCatalog() {
        this.descriptions.clear();
    }

}
