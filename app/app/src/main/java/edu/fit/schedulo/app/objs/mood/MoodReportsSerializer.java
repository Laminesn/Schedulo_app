package edu.fit.schedulo.app.objs.mood;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

public class MoodReportsSerializer extends StdSerializer<MoodReports> {

    public MoodReportsSerializer() {
        super(MoodReports.class);
    }

    @Override
    public void serialize(MoodReports moodReports, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();

        for (Map.Entry<LocalDate, MoodReport> entry : moodReports.listAllReports().entrySet()) {

            jsonGenerator.writeFieldName(entry.getKey().toString());
            jsonGenerator.writeObject(entry.getValue());

        }

        jsonGenerator.writeEndObject();

    }

}
