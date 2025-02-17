package edu.fit.schedulo.app.objs.semester;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

import edu.fit.schedulo.app.objs.academic_cal.AcademicCalendarDate;

/**
 * Serializes the Semesters object as a JSON array.
 *
 * @author Joshua Sheldon
 */
public class SemestersSerializer extends StdSerializer<Semesters> {

    public SemestersSerializer() {
        super(Semesters.class);
    }

    @Override
    public void serialize(Semesters semesters, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartArray();

        for (Semester semester : semesters.loadedSemesters()) {

            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("type", semester.getType().toString());
            jsonGenerator.writeNumberField("year", semester.getYear());

            jsonGenerator.writeArrayFieldStart("academicCalendarDates");

            for (AcademicCalendarDate academicCalendarDate : semester.getCalDates()) {
                jsonGenerator.writeObject(academicCalendarDate);
            }

            jsonGenerator.writeEndArray();
            jsonGenerator.writeEndObject();

        }

        jsonGenerator.writeEndArray();

    }



}
