package edu.fit.schedulo.app.objs.event;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

import edu.fit.schedulo.app.objs.semester.Semester;

public class StudentCalendarSerializer extends StdSerializer<StudentCalendar> {

    public StudentCalendarSerializer() {
        super(StudentCalendar.class);
    }

    @Override
    public void serialize(StudentCalendar studentCalendar, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();

        // Add a key-value pair for each semester
        for (Semester semester : studentCalendar.getSemesters()) {
            jsonGenerator.writeArrayFieldStart(semester.toString());
            for (Event event : studentCalendar.getEvents(semester)) {
                jsonGenerator.writeObject(event);
            }
            jsonGenerator.writeEndArray(); // end semester value array
        }

        jsonGenerator.writeEndObject(); // end root obj

    }
}
