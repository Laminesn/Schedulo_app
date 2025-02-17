package edu.fit.schedulo.app.objs.course;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import edu.fit.schedulo.app.objs.semester.Semester;

/**
 * Maps from semester to CRN to course instance objects.
 * Should store all course instances the app has.
 *
 * @author Joshua Sheldon
 */
@JsonSerialize(using = CoursesSerializer.class)
public class Courses {

    /* ---------- INSTANCE VARIABLES ---------- */

    /**
     * Map from semester to CRN to course instance object.
     */
    private final Map<Semester, Map<Integer, CourseInstance>> courses;

    /* ---------- CONSTRUCTOR ---------- */

    private Courses() {
        this.courses = new HashMap<>();
    }

    /* ---------- SINGLETON ---------- */

    private static final Courses instance = new Courses();

    public static Courses getInstance() {
        return instance;
    }

    /* ---------- METHODS ---------- */

    /**
     * Adds a course object to the list of courses.
     * Must also give the semester which the course
     * is held in.
     *
     * @param semester The semester the course is held in.
     * @param instance The course instance to add.
     */
    public synchronized void addInstance(Semester semester, CourseInstance instance) {

        // Neither can be null
        if (semester == null || instance == null) {
            return;
        }

        // If the courses map doesn't already have a map for this semester,
        // then create a new one.
        this.courses.computeIfAbsent(semester, k -> new TreeMap<>())
                .put(instance.getCRN(), instance);

    }

    /**
     * Attempts to retrieve a course object from a list of
     * courses. Must provide the CRN of the course and
     * the semester it is held in.
     *
     * @param semester The semester the course is held in.
     * @param crn      The CRN of the course.
     * @return The course instance object if found,
     * otherwise <code>null</code>.
     */
    public synchronized CourseInstance getInstance(Semester semester, int crn) {

        if (semester == null) {
            return null;
        }

        Map<Integer, CourseInstance> semesterCourses = this.courses.get(semester);

        if (semesterCourses == null) {
            return null;
        }

        return semesterCourses.get(crn);
    }

    /**
     * Retrieves all course instances for a specific semester.
     *
     * @param semester The semester to retrieve courses for.
     * @return A list of course instances for the specified semester,
     * or an empty list if none are found.
     */
    public synchronized List<CourseInstance> getInstancesBySemester(Semester semester) {

        LinkedList<CourseInstance> instances = new LinkedList<>();

        if (semester == null) {
            return instances;
        }

        Map<Integer, CourseInstance> semesterCourses = this.courses.get(semester);

        if (semesterCourses == null) {
            return instances;
        }

        instances.addAll(semesterCourses.values());
        return instances;

    }

    /**
     * @return All semesters that have courses.
     */
    public synchronized Set<Semester> getSemesters() {
        return Collections.unmodifiableSet(this.courses.keySet());
    }

    /**
     * @return All course instances.
     */
    public synchronized Set<CourseInstance> getAllInstances() {

        Set<CourseInstance> instances = Collections.emptySet();

        for (Map<Integer, CourseInstance> semesterCourses : this.courses.values()) {
            instances.addAll(semesterCourses.values());
        }

        return Collections.unmodifiableSet(instances);

    }

    /**
     * Clears all course instances from the courses list.
     */
    public synchronized void clearCourses() {
        this.courses.clear();
    }

}
