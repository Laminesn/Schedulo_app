package edu.fit.schedulo.app.objs.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Iterator;
import java.util.Map;

import edu.fit.schedulo.app.objs.semester.Semester;
import edu.fit.schedulo.app.objs.semester.Semesters;

/**
 * @author Joshua Sheldon
 */
public class StudentCalendarDeserializer {

    /**
     * Load all event instances from a JSON object into the
     * StudentCalendar singleton.
     *
     * @param root   The JSON object of event instances.
     * @param mapper The ObjectMapper to use for deserialization.
     */
    public static void loadFromJSON(ObjectNode root, ObjectMapper mapper) {

        for (Iterator<Map.Entry<String, JsonNode>> it = root.fields(); it.hasNext(); ) {
            // Get Semester object
            Map.Entry<String, JsonNode> semesterField = it.next();
            Semester semester = Semesters.getInstance().getSemester(semesterField.getKey());

            // Field value is an array
            ArrayNode eventsJSON = (ArrayNode) semesterField.getValue();
            for (int i = 0; i < eventsJSON.size(); i++) {

                ObjectNode eventJSON = (ObjectNode) eventsJSON.get(i);

                // Read ObjectNode as Event object
                Event event;
                try {
                    event = (Event) mapper.treeToValue(
                            eventJSON,
                            getEventType(eventJSON)
                    );
                } catch (Exception e) {
                    System.err.println("Could not read Event[" + i + "] into " +
                            "object. Skipping.");
                    e.printStackTrace();
                    continue;
                }

                // Add to semester
                StudentCalendar.getInstance().addEvent(semester, event);

            }

        }

    }

    /**
     * Get the class of the event represented by the given
     * JSON object by checking the fields of the object.
     *
     * @param eventJSON The JSON object representing the event.
     * @return The class of the event represented by the JSON object,
     * or <code>null</code> if the event type could not be determined.
     */
    private static Class<?> getEventType(ObjectNode eventJSON) {

        if (eventJSON.has("crn")) {
            return CourseEvent.class;
        }

        if (eventJSON.has("category")) {
            return ActivityEvent.class;
        }

        return null;

    }

}
