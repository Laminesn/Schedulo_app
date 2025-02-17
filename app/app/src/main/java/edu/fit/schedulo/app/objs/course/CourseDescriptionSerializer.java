package edu.fit.schedulo.app.objs.course;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * Serializes the CourseDescription object as a JSON object.
 *
 * @author Joshua Sheldon
 */
public class CourseDescriptionSerializer extends StdSerializer<CourseDescription> {

    public CourseDescriptionSerializer() {
        super(CourseDescription.class);
    }

    @Override
    public void serialize(CourseDescription courseDescription, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("prefix", courseDescription.getPrefix());
        jsonGenerator.writeNumberField("code", courseDescription.getCode());
        jsonGenerator.writeStringField("creditHours", courseDescription.getCreditHours());
        jsonGenerator.writeStringField("title", courseDescription.getTitle());
        jsonGenerator.writeNumberField("catalogYear", courseDescription.getCatalogYear().getStartYear());
        jsonGenerator.writeEndObject();

    }
}
