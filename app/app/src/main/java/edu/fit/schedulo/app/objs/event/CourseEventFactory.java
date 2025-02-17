package edu.fit.schedulo.app.objs.event;

import edu.fit.schedulo.app.objs.course.Catalog;
import edu.fit.schedulo.app.objs.course.CourseDescription;
import edu.fit.schedulo.app.objs.course.CourseDescriptionID;
import edu.fit.schedulo.app.objs.course.CourseInstance;

/**
 * Provides the ability to automatically create a CourseEvent
 * object from a CourseInstance object.
 *
 * @author Joshua Sheldon
 */
public class CourseEventFactory {

    /**
     * Creates a <code>CourseEvent</code> object from the given
     * <code>CourseInstance</code> object.
     *
     * @param instance The course instance to create an event from.
     * @return A new <code>CourseEvent</code> object if the instance
     * is not null, otherwise <code>null</code>.
     */
    public static CourseEvent createCourseEvent(CourseInstance instance) {

        // Cannot create event from null instance
        if (instance == null) return null;

        // Retrieve the course description from the catalog
        CourseDescriptionID id = instance.getDescriptionID();
        CourseDescription description = Catalog.getInstance().getCourseDescriptionByID(id);

        return new CourseEvent(
                description.getTitle(),
                instance.getSchedule(),
                instance.getPlace(),
                true,
                instance.getCRN()
        );

    }

}
