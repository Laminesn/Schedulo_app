package edu.fit.schedulo.app.objs.semester;

import androidx.annotation.NonNull;

/**
 * The three possible types of semester, representing
 * the entire academic year.
 *
 * @author Joshua Sheldon
 */
public enum SemesterType {

    FALL,
    SPRING,
    SUMMER;

    @NonNull
    @Override
    public String toString() {
        switch (this) {
            case FALL:
                return "Fall";
            case SPRING:
                return "Spring";
            case SUMMER:
                return "Summer";
            default:
                return "";
        }
    }

}
