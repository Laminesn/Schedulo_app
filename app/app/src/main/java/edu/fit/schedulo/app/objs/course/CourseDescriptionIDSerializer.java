package edu.fit.schedulo.app.objs.course;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class CourseDescriptionIDSerializer extends StdSerializer<CourseDescriptionID> {

    public CourseDescriptionIDSerializer() {
        super(CourseDescriptionID.class);
    }

    @Override
    public void serialize(CourseDescriptionID courseDescriptionID, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("startYear", courseDescriptionID.getStartYear());
        jsonGenerator.writeStringField("prefix", courseDescriptionID.getPrefixAsString());
        jsonGenerator.writeNumberField("code", courseDescriptionID.getCode());
        jsonGenerator.writeEndObject();

    }
}
