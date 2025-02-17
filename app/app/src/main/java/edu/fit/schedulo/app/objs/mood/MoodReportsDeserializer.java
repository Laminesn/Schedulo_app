package edu.fit.schedulo.app.objs.mood;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Joshua Sheldon
 */
public class MoodReportsDeserializer {

    /**
     * Load all MoodReport instances from a JSON object into the
     * MoodReports singleton.
     *
     * @param root   The JSON object of MoodReport instances.
     * @param mapper The ObjectMapper to use for deserialization.
     */
    public static void loadFromJSON(ObjectNode root, ObjectMapper mapper) {

        for (Iterator<Map.Entry<String, JsonNode>> it = root.fields(); it.hasNext(); ) {
            // Get LocalDate Object
            Map.Entry<String, JsonNode> reportField = it.next();
            String[] splitDate = reportField.getKey().split("-");

            LocalDate date = LocalDate.of(
                    Integer.parseInt(splitDate[0]),
                    Integer.parseInt(splitDate[1]),
                    Integer.parseInt(splitDate[2])
            );

            // Read ObjectNode as MoodReport object
            MoodReport report;
            try {
                report = (MoodReport) mapper.treeToValue(
                        (ObjectNode) reportField.getValue(),
                        MoodReport.class
                );
            } catch (Exception e) {
                System.err.println("Could not read MoodReport for " + reportField.getKey() +
                        " into object. Skipping.");
                e.printStackTrace();
                continue;
            }

            // Add to MoodReports
            MoodReports.getInstance().addReport(date, report);

        }

    }

}
