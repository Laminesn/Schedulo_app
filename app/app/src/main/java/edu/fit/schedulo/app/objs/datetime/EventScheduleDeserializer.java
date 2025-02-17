package edu.fit.schedulo.app.objs.datetime;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

/**
 * Deserializer for EventSchedule objects.
 *
 * @author Joshua Sheldon
 */
public class EventScheduleDeserializer extends StdDeserializer<EventSchedule> {

    public EventScheduleDeserializer() {
        super(EventSchedule.class);
    }

    @Override
    public EventSchedule deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {

        Map<DayOfWeek, TimeRange> schedule = new HashMap<>();
        ArrayNode root = jsonParser.getCodec().readTree(jsonParser);

        for (int i = 0; i < root.size(); i++) {

            // Read time range from entry
            ObjectNode scheduleEntry = (ObjectNode) root.get(i);
            TimeRange range = jsonParser.getCodec().treeToValue(
                    scheduleEntry.get("range"),
                    TimeRange.class
            );

            // Iterate through all days of the week in the entry
            // and add to schedule map
            ArrayNode days = (ArrayNode) scheduleEntry.get("days");

            for (int j = 0; j < days.size(); j++) {
                DayOfWeek day = DayOfWeek.valueOf(days.get(j).asText());
                schedule.put(day, range);
            }

        }

        return new EventSchedule(schedule);

    }

}
