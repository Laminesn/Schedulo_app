package edu.fit.schedulo.app.objs.datetime;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.DayOfWeek;
import java.util.LinkedList;

public class EventScheduleSerializer extends StdSerializer<EventSchedule> {

    public EventScheduleSerializer() {
        super(EventSchedule.class);
    }

    @Override
    public void serialize(EventSchedule eventSchedule, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartArray();

        LinkedList<TimeRange> addedRanges = new LinkedList<>();

        for (TimeRange range : eventSchedule.getMap().values()) {

            if (addedRanges.contains(range)) {
                continue;
            }

            jsonGenerator.writeStartObject();
            jsonGenerator.writeArrayFieldStart("days");

            for (DayOfWeek day : eventSchedule.getDays()) {
                if (eventSchedule.getTimeForDay(day).equals(range)) {
                    jsonGenerator.writeObject(day);
                }
            }

            jsonGenerator.writeEndArray();
            jsonGenerator.writeObjectField("range", range);
            jsonGenerator.writeEndObject();

            addedRanges.push(range);

        }

        jsonGenerator.writeEndArray();

    }
}
