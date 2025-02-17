package edu.fit.schedulo.app.objs.course;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * Serializes the Catalog object as a JSON array.
 *
 * @author Joshua Sheldon
 */
public class CatalogSerializer extends StdSerializer<Catalog> {

    public CatalogSerializer() {
        super(Catalog.class);
    }

    @Override
    public void serialize(Catalog catalog, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartArray();

        for (CourseDescription description : catalog.getAllCourseDescriptions()) {
            jsonGenerator.writeObject(description);
        }

        jsonGenerator.writeEndArray();

    }

}
