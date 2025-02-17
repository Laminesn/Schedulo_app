package edu.fit.schedulo.app.scheduloAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.fit.schedulo.app.objs.course.CourseInstance;
import java.io.File;
import java.io.IOException;

public class JsonParser {
    public static void parse() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File coursesFile = new File("courses.json");
        CourseInstance[] courses = mapper.readValue(coursesFile, CourseInstance[].class);

        for (CourseInstance course : courses) {
            System.out.println(course);
        }

    }
}
