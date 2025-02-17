package edu.fit.schedulo.app.scheduloAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import edu.fit.schedulo.app.objs.academic_year.AcademicYear;
import edu.fit.schedulo.app.objs.academic_year.AcademicYears;
import edu.fit.schedulo.app.objs.course.CourseDescription;
import edu.fit.schedulo.app.objs.course.CourseInstance;
import edu.fit.schedulo.app.objs.datetime.EventSchedule;
import edu.fit.schedulo.app.objs.datetime.TimeRange;
import edu.fit.schedulo.app.objs.loc.OnCampusLocation;
import edu.fit.schedulo.app.objs.semester.Semester;
import edu.fit.schedulo.app.objs.semester.Semesters;

/**
 * @author Zion Taylor
 * Modified by Joshua Sheldon
 */
public class WebScraper {

    /**
     * Attempts to scrape the class schedule page on FIT's website
     * and compile the data into JSON objects.
     *
     * @return An array of JSON arrays, where the first array
     * is course descriptions and the second array is
     * course instances. If an error occurs, <code>null</code>
     */
    public static ArrayNode[] scrapeCourseSchedule() {

        String url = "https://apps.fit.edu/schedule/main-campus/fall";

        // Create object mapper
        ObjectMapper objMapper = new ObjectMapper();
        objMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

        try {

            // Fetch the page
            Document doc = Jsoup.connect(url).get();

            // Get semester name from header, and semester object from name
            Elements h2 = doc.select("h2");
            String semesterName = h2.text().split(":")[1].trim().toUpperCase(Locale.ENGLISH);
            Semester semester = Semesters.getInstance().getSemester(semesterName);
            AcademicYear year = AcademicYears.getInstance().getYearFromSemester(semester);

            // Get the table
            Elements rows = doc.select("table#course-table > tbody > tr");

            // Create array nodes for course descriptions and instances
            ArrayNode courseDescriptions = objMapper.createArrayNode();
            ArrayNode courseInstances = objMapper.createArrayNode();

            // Keep track of what course descriptions have already
            // been added to avoid duplicates
            HashSet<String> addedCourseIDs = new HashSet<>();

            // Iterate through the rows
            for (Element row : rows) {

                //Get the columns
                Elements cols = row.select("td");

                // Get variables from scraped data
                int crn = Integer.parseInt(cols.get(0).text().trim());
                String courseID = cols.get(1).text().trim();
                String section = cols.get(2).text().trim();
                String creditHours = cols.get(3).text().trim();
                String title = cols.get(4).text().trim();

                String[] days = cols.get(6).text().trim().split(" ");

                // Fix for single-element empty string arrays (blank days)
                if (days.length == 1 && days[0].isEmpty()) {
                    days = new String[0];
                }

                String[] time = cols.get(7).text().trim().split(" ");

                // Fix for single-element empty string arrays (blank times)
                if (time.length == 1 && time[0].isEmpty()) {
                    time = new String[0];
                }

                String[] rawPlace = cols.get(8).text().trim().split(" ");

                // Construct real place array where each place
                // contains one space
                String[] place;

                if (rawPlace.length > 1) {
                    place = new String[rawPlace.length / 2];
                    for (int i = 0; i < rawPlace.length; i += 2) {
                        int placeIndex = 0;

                        if (i != 0) {
                            placeIndex = i / 2;
                        }

                        place[placeIndex] = rawPlace[i] + " " + rawPlace[i + 1];
                    }
                } else {
                    place = new String[0];
                }

                String instructor = cols.get(9).text();

                // Create course description
                CourseDescription description = new CourseDescription(
                        courseID,
                        creditHours,
                        title,
                        year
                );

                // Attempt to create event schedule
                EventSchedule schedule = null;

                if (days.length > 0) {

                    Map<DayOfWeek, TimeRange> scheduleMap = new TreeMap<>();

                    // Loop through each distinct days-time range pairing
                    for (int i = 0; i < days.length; i++) {

                        // Parse the time range on the schedule into
                        // an actual object
                        TimeRange range = parseTimeRangeFromClassSchedule(time[i]);

                        // Loop through each of the days associated
                        // with this time range and add to map
                        for (char letter : days[i].toCharArray()) {
                            DayOfWeek day = parseDotWFromLetter(letter);
                            scheduleMap.put(day, range);
                        }

                    }

                    // Create event schedule object from map
                    schedule = new EventSchedule(scheduleMap);

                }

                // Attempt to create on-campus location
                OnCampusLocation loc = null;

                if (place.length > 0) {
                    loc = new OnCampusLocation(place[0]);
                }

                // Create course instance
                CourseInstance instance = new CourseInstance(
                        crn,
                        description.getID(),
                        section,
                        schedule,
                        loc,
                        instructor
                );

                // Add to course descriptions and instances

                // Don't add course description if we've already
                // added it
                if (!addedCourseIDs.contains(courseID)) {
                    courseDescriptions.add(objMapper.valueToTree(description));
                    addedCourseIDs.add(courseID);
                }

                courseInstances.add(objMapper.valueToTree(instance));

            }

            return new ArrayNode[]{courseDescriptions, courseInstances};

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * Parse letter for day of the week from the class schedule.
     *
     * @param letter The letter representing the day of the week.
     * @return The <code>DayOfWeek</code> object if the letter is valid,
     * otherwise <code>null</code>.
     */
    public static DayOfWeek parseDotWFromLetter(char letter) {
        switch(letter) {
            case 'M':
                return DayOfWeek.MONDAY;
            case 'T':
                return DayOfWeek.TUESDAY;
            case 'W':
                return DayOfWeek.WEDNESDAY;
            case 'R':
                return DayOfWeek.THURSDAY;
            case 'F':
                return DayOfWeek.FRIDAY;
            case 'S':
                return DayOfWeek.SATURDAY;
            case 'U':
                return DayOfWeek.SUNDAY;
            default:
                return null;
        }
    }

    /**
     * Attempts to parse a time range from the class schedule
     * (i.e. 0950-1105) into a <code>TimeRange</code> object.
     *
     * @param timeRange The time range string from the class schedule.
     * @return A <code>TimeRange</code> object if the time range is valid.
     */
    public static TimeRange parseTimeRangeFromClassSchedule(String timeRange) {

        String[] splitRange = timeRange.split("-");
        String startTime = splitRange[0];
        String endTime = splitRange[1];

        LocalTime start = LocalTime.of(
                Integer.parseInt(startTime.substring(0, 2)),
                Integer.parseInt(startTime.substring(2))
        );

        LocalTime end = LocalTime.of(
                Integer.parseInt(endTime.substring(0, 2)),
                Integer.parseInt(endTime.substring(2))
        );

        return new TimeRange(start, end);

    }

}