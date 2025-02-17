package edu.fit.schedulo.app.objs.semester;

import android.util.Log;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Creates and maintains all semester objects. Ensures that
 * only one semester object exists for each semester type and year.
 *
 * @author Joshua Sheldon
 */
@JsonSerialize(using = SemestersSerializer.class)
public class Semesters {

    public static final String TAG = Semesters.class.getSimpleName();

    /* ---------- INSTANCE VARIABLES ---------- */

    /**
     * <code>TreeMap</code> from year to an array of semesters,
     * where the indices are the ordinals of the
     * <code>SemesterType</code> enum. Basically,
     * an efficient way to store <code>Semester</code>
     * objects.
     */
    private final Map<Short, Semester[]> semesters;

    /* ---------- CONSTRUCTOR ---------- */

    /**
     * Creates a new <code>Semesters</code> object.
     */
    private Semesters() {
        this.semesters = new TreeMap<>();
    }

    /* ---------- SINGLETON ---------- */

    private static final Semesters instance = new Semesters();

    public static Semesters getInstance() {
        return instance;
    }

    /* ---------- PUBLIC METHODS ---------- */

    /**
     * Get all semesters that have been inserted into the semesters map,
     * which should mean they have academic calendar dates loaded
     * from the web API.
     *
     * @return All loaded semesters.
     */
    public synchronized List<Semester> loadedSemesters() {

        LinkedList<Semester> loadedSemesters = new LinkedList<>();

        for (Semester[] sems : semesters.values()) {
            for (Semester sem : sems) {
                if (sem != null) {
                    loadedSemesters.push(sem);
                }
            }
        }

        return Collections.unmodifiableList(loadedSemesters);

    }

    /**
     * Attempt to get a <code>Semester</code> object from the given parameters.
     *
     * @param type The type of semester (fall, spring, or summer)
     * @param year The year of the semester.
     * @return A <code>Semester</code> object if the parameters are valid.
     * @throws IllegalArgumentException If the semester type is null or the year is negative.
     * @throws RuntimeException         If somehow we inserted a null array into the semesters map.
     */
    public synchronized Semester getSemester(SemesterType type, short year) {

        if (type == null) {
            throw new IllegalArgumentException("Semester type cannot be null.");
        }

        if (year < 0) {
            throw new IllegalArgumentException("Year cannot be negative.");
        }

        if (!semesters.containsKey(year)) {
            semesters.put(year, new Semester[SemesterType.values().length]);
        }

        Semester[] sems = semesters.get(year);

        if (sems == null) {
            throw new RuntimeException("Somehow, inserted a null array into the semesters map!");
        }

        if (sems[type.ordinal()] == null) {
            sems[type.ordinal()] = new Semester(type, year);
        }

        return sems[type.ordinal()];

    }

    /**
     * Attempt to retrieve a semester object with a
     * String representing the name of the semester.
     *
     * @param semesterString String like "Spring 2024", "Summer 2023", etc.
     * @return A <code>Semester</code> object if the given string is valid.
     */
    public Semester getSemester(String semesterString) {

        if (semesterString == null || semesterString.isEmpty()) {
            Log.e(TAG, "Cannot get semester with null or empty semester string.");
            return null;
        }

        String[] splitSemesterString = semesterString.split(" ");

        if (splitSemesterString.length != 2) {
            Log.e(TAG, "Semester string in invalid format: " + semesterString);
            return null;
        }

        String typeString = splitSemesterString[0].toUpperCase(Locale.ENGLISH);
        short year;

        try {
            year = Short.parseShort(splitSemesterString[1]);
        } catch (Exception ex) {
            Log.e(TAG, "Invalid year given in semester string: " + semesterString, ex);
            return null;
        }

        SemesterType type;

        try {
            type = SemesterType.valueOf(typeString);
        } catch (Exception ex) {
            Log.e(TAG, "Invalid semester type given in semester string: " + semesterString, ex);
            return null;
        }

        return getSemester(type, year);

    }

}
