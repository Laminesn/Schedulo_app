package edu.fit.schedulo.app.objs.course;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.fit.schedulo.app.objs.academic_year.AcademicYears;

/**
 * @author Joshua Sheldon
 */
public class CatalogDeserializer {

    /**
     * Load all course descriptions from a JSON array into the Catalog singleton.
     *
     * @param root The JSON array of course descriptions.
     */
    public static void loadFromJSON(ArrayNode root) {

        for (int i = 0; i < root.size(); i++) {

            ObjectNode descriptionJSON = (ObjectNode) root.get(i);

            String prefix = descriptionJSON.get("prefix").asText();
            short code = (short) descriptionJSON.get("code").asInt();
            String creditHours = descriptionJSON.get("creditHours").asText();
            String title = descriptionJSON.get("title").asText();
            short catalogYear = (short) descriptionJSON.get("catalogYear").asInt();

            Catalog.getInstance().addCourseDescription(new CourseDescription(
                    prefix,
                    code,
                    creditHours,
                    title,
                    AcademicYears.getInstance().getAcademicYear(catalogYear)
            ));

        }

    }

}
