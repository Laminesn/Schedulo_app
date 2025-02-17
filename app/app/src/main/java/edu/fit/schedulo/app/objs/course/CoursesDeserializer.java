package edu.fit.schedulo.app.objs.course;

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
public class CoursesDeserializer {

    /**
     * Load all course instances from a JSON object into the
     * Courses singleton.
     *
     * @param root   The JSON object of course instances.
     * @param mapper The ObjectMapper to use for deserialization.
     */
    public static void loadFromJSON(ObjectNode root, ObjectMapper mapper) {

        for (Iterator<Map.Entry<String, JsonNode>> it = root.fields(); it.hasNext(); ) {
            // Get Semester object
            Map.Entry<String, JsonNode> semesterField = it.next();
            Semester semester = Semesters.getInstance().getSemester(semesterField.getKey());

            // Field value is an array
            ArrayNode courseInstancesJSON = (ArrayNode) semesterField.getValue();
            for (int i = 0; i < courseInstancesJSON.size(); i++) {

                ObjectNode courseInstanceJSON = (ObjectNode) courseInstancesJSON.get(i);

                // Read ObjectNode as CourseInstance object
                CourseInstance instance;
                try {
                    instance = mapper.treeToValue(courseInstanceJSON, CourseInstance.class);
                } catch (Exception e) {
                    System.err.println("Could not read CourseInstance[" + i + "] into " +
                            "object. Skipping.");
                    e.printStackTrace();
                    continue;
                }

                // Add to semester
                Courses.getInstance().addInstance(semester, instance);

            }

        }

    }

}
