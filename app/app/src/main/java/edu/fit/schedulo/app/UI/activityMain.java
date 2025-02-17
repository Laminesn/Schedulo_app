package edu.fit.schedulo.app.UI;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import edu.fit.schedulo.app.R;
import edu.fit.schedulo.app.scheduloAPI.WebScraper;
import edu.fit.schedulo.app.objs.course.CourseInstance;

import androidx.fragment.app.FragmentActivity;


public class activityMain extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_main);

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayNode[] courseData = WebScraper.scrapeCourseSchedule();
                    ObjectMapper mapper = new ObjectMapper();
                    CourseInstance[] courses = mapper.readValue(courseData[1].toString(), CourseInstance[].class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activityMain.this, "Error: fetching courses" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }


            }

        }).start();*/
    }
}