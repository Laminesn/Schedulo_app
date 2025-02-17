package edu.fit.schedulo.app.objs.course;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

import edu.fit.schedulo.app.objs.semester.Semester;

/**
 * Serializes the Courses object as a JSON array.
 *
 * @author Joshua Sheldon
 */
public class CoursesSerializer extends StdSerializer<Courses> {

    public CoursesSerializer() {
        super(Courses.class);
    }

    @Override
    public void serialize(Courses courses, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();

        for (Semester semester : courses.getSemesters()) {

            jsonGenerator.writeFieldName(semester.toString());
            jsonGenerator.writeStartArray();

            for (CourseInstance courseInstance : courses.getInstancesBySemester(semester)) {
                jsonGenerator.writeObject(courseInstance);
            }

            jsonGenerator.writeEndArray();

        }

        jsonGenerator.writeEndObject();

    }
}
