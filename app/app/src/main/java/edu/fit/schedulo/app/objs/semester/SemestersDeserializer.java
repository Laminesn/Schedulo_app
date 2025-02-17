package edu.fit.schedulo.app.objs.semester;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.fit.schedulo.app.objs.academic_cal.AcademicCalendarDate;

/**
 * @author Joshua Sheldon
 */
public class SemestersDeserializer {

    /**
     * Load all semesters and their academic calendar dates from a JSON
     * array into the Semesters singleton.
     *
     * @param root   The JSON array of semesters.
     * @param mapper The ObjectMapper to use for deserialization.
     */
    public static void loadFromJSON(ArrayNode root, ObjectMapper mapper) {

        for (int i = 0; i < root.size(); i++) {

            ObjectNode semesterJSON = (ObjectNode) root.get(i);

            // Get Semester object
            String semesterType = semesterJSON.get("type").asText();
            short year = (short) semesterJSON.get("year").asInt();
            Semester semester = Semesters.getInstance().getSemester(semesterType + " " + year);

            // Add all academic calendar dates to semester
            ArrayNode calDates = (ArrayNode) semesterJSON.get("academicCalendarDates");

            for (int j = 0; j < calDates.size(); j++) {

                ObjectNode calDate = (ObjectNode) calDates.get(j);

                // Read ObjectNode as AcademicCalendarDate object
                AcademicCalendarDate acd;
                try {
                    acd = mapper.treeToValue(calDate, AcademicCalendarDate.class);
                } catch (Exception e) {
                    System.err.println("Could not read AcademicCalendarDate[" + j + "] into " +
                            "object. Skipping.");
                    e.printStackTrace();
                    continue;
                }

                // Add to semester
                semester.addCalDate(acd);

            }

        }

    }

}
