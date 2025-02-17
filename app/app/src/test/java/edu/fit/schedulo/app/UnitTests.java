package edu.fit.schedulo.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;




import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import edu.fit.schedulo.app.objs.Category;
import edu.fit.schedulo.app.objs.academic_cal.AcademicCalendarDate;
import edu.fit.schedulo.app.objs.academic_cal.AcademicCalendarDateFactory;
import edu.fit.schedulo.app.objs.academic_year.AcademicYears;
import edu.fit.schedulo.app.objs.course.Catalog;
import edu.fit.schedulo.app.objs.course.CourseDescription;
import edu.fit.schedulo.app.objs.course.CourseDescriptionID;
import edu.fit.schedulo.app.objs.course.CourseInstance;
import edu.fit.schedulo.app.objs.course.Courses;
import edu.fit.schedulo.app.objs.datetime.DateRange;
import edu.fit.schedulo.app.objs.datetime.EventSchedule;
import edu.fit.schedulo.app.objs.datetime.TimeRange;
import edu.fit.schedulo.app.objs.event.ActivityEvent;
import edu.fit.schedulo.app.objs.event.CourseEvent;
import edu.fit.schedulo.app.objs.event.CourseEventFactory;
import edu.fit.schedulo.app.objs.event.StudentCalendar;
import edu.fit.schedulo.app.objs.loc.Building;
import edu.fit.schedulo.app.objs.loc.Buildings;
import edu.fit.schedulo.app.objs.loc.OnCampusLocation;
import edu.fit.schedulo.app.objs.mood.MoodReport;
import edu.fit.schedulo.app.objs.mood.MoodReports;
import edu.fit.schedulo.app.objs.semester.Semester;
import edu.fit.schedulo.app.objs.semester.SemesterType;
import edu.fit.schedulo.app.objs.semester.Semesters;
import edu.fit.schedulo.app.scheduloAPI.WebScraper;
import edu.fit.schedulo.app.UI.Main;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTests {

    public static final String PASSWORD = "password";

    @BeforeClass
    public static void setUpClass() {

        // Create description and add to catalog
        CourseDescription desc = new CourseDescription(
                "CSE",
                (short) 1002,
                "4",
                "Fundamentals of Software Development 2",
                AcademicYears.getInstance().getAcademicYear((short) 2024)
        );

        Catalog.getInstance().addCourseDescription(desc);

        // Represent schedule through code objects and get EventSchedule object
        TimeRange[] timeRanges = new TimeRange[2];
        timeRanges[0] = new TimeRange(LocalTime.of(9, 0), LocalTime.of(9, 50));
        timeRanges[1] = new TimeRange(LocalTime.of(12, 30), LocalTime.of(13, 45));

        Map<DayOfWeek, Integer> daysToRanges = new TreeMap<>();
        daysToRanges.put(DayOfWeek.MONDAY, 0);
        daysToRanges.put(DayOfWeek.TUESDAY, 1);
        daysToRanges.put(DayOfWeek.WEDNESDAY, 0);
        daysToRanges.put(DayOfWeek.THURSDAY, 1);
        daysToRanges.put(DayOfWeek.FRIDAY, 0);

        EventSchedule schedule = new EventSchedule(daysToRanges, timeRanges);

        // Create OnCampusLocation object
        OnCampusLocation loc = new OnCampusLocation(
                "500OLS",
                (short) 129
        );

        // Create Fall 2024 semester
        Semester semester = Semesters.getInstance().getSemester(SemesterType.FALL, (short) 2024);
        semester.addCalDate("Oct 14-15", "Fall break (No classes)");

        // Create course instance object
        CourseInstance instance = new CourseInstance(
                80471,
                desc.getID(),
                "01",
                schedule,
                loc,
                "TBA"
        );

        Courses.getInstance().addInstance(semester, instance);

        // Create Mood Report for the first day of fall break
        MoodReport report = new MoodReport((short) 10, Category.PERSONAL, "Life is good!");
        MoodReports.getInstance().addReport(LocalDate.of(2024, 10, 14), report);

        // Create course event from instance object
        CourseEvent courseEvent = CourseEventFactory.createCourseEvent(instance);

        // Add to calendar
        StudentCalendar.getInstance().addEvent(semester, courseEvent);

        // Create ActivityEvent with null location

        Map<DayOfWeek, TimeRange> breatheScheduleMap = new HashMap<>();
        breatheScheduleMap.put(DayOfWeek.MONDAY, new TimeRange(
                LocalTime.of(10, 0),
                LocalTime.of(10, 5)
        ));
        EventSchedule breatheSchedule = new EventSchedule(breatheScheduleMap);

        ActivityEvent activityEvent = new ActivityEvent(
                "Breathe",
                breatheSchedule,
                false,
                Category.HEALTH
        );

        StudentCalendar.getInstance().addEvent(semester, activityEvent);

    }

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void dateRange_contains_singleDay() {

        LocalDate mlk = LocalDate.of(2024, 1, 15);
        LocalDate newYears = LocalDate.of(2024, 1, 1);
        LocalDate julyFourth = LocalDate.of(2024, 7, 4);

        DateRange mlkRange = new DateRange(
                LocalDate.of(2024, 1, 15),
                LocalDate.of(2024, 1, 15)
        );

        assertTrue(mlkRange.contains(mlk));
        assertFalse(mlkRange.contains(newYears));
        assertFalse(mlkRange.contains(julyFourth));

    }

    @Test
    public void dateRange_contains_multipleDays() {

        LocalDate inSpringBreak = LocalDate.of(2024, 3, 27);
        LocalDate newYears = LocalDate.of(2024, 1, 1);
        LocalDate julyFourth = LocalDate.of(2024, 7, 4);

        DateRange springBreak = new DateRange(
                LocalDate.of(2024, 3, 25),
                LocalDate.of(2024, 3, 29)
        );

        assertTrue(springBreak.contains(inSpringBreak));
        assertFalse(springBreak.contains(newYears));
        assertFalse(springBreak.contains(julyFourth));

    }

    /**
     * Date Format: Month XX
     */
    @Test
    public void academicCalendarDateFactory_create_dateFormatOne_testOne() {

        String dateFromPage = "Apr 24";
        int year = 2024;
        String description = "Last day of classes";
        boolean holiday = false;

        AcademicCalendarDate date = AcademicCalendarDateFactory.createDate(
                dateFromPage,
                year,
                description
        );

        LocalDate dateObj = LocalDate.of(2024, 4, 24);

        assertEquals(date.getDateRange().getStartDate(), dateObj);
        assertEquals(date.getDateRange().getEndDate(), dateObj);
        assertEquals(date.getDescription(), description);
        assertEquals(date.isHoliday(), holiday);

        System.out.println(date);

    }

    /**
     * Date Format: Month XX
     */
    @Test
    public void academicCalendarDateFactory_create_dateFormatOne_testTwo() {

        String dateFromPage = "Jan 8";
        int year = 2024;
        String description = "Classes begin (Monday)";
        boolean holiday = false;

        AcademicCalendarDate date = AcademicCalendarDateFactory.createDate(
                dateFromPage,
                year,
                description
        );

        LocalDate dateObj = LocalDate.of(2024, 1, 8);

        assertEquals(date.getDateRange().getStartDate(), dateObj);
        assertEquals(date.getDateRange().getEndDate(), dateObj);
        assertEquals(date.getDescription(), description);
        assertEquals(date.isHoliday(), holiday);

        System.out.println(date);

    }

    /**
     * Date Format: Month XX-XX
     */
    @Test
    public void academicCalendarDateFactory_create_dateFormatTwo() {

        String dateFromPage = "Apr 25-26";
        int year = 2024;
        String description = "Study days (No classes)";
        boolean holiday = true;

        AcademicCalendarDate date = AcademicCalendarDateFactory.createDate(
                dateFromPage,
                year,
                description
        );

        LocalDate startDate = LocalDate.of(2024, 4, 25);
        LocalDate endDate = LocalDate.of(2024, 4, 26);

        assertEquals(date.getDateRange().getStartDate(), startDate);
        assertEquals(date.getDateRange().getEndDate(), endDate);
        assertEquals(date.getDescription(), description);
        assertEquals(date.isHoliday(), holiday);

        System.out.println(date);

    }

    /**
     * Date Format: Month XX-Month XX
     */
    @Test
    public void academicCalendarDateFactory_create_dateFormatThree() {

        String dateFromPage = "Apr 29-May 3";
        int year = 2024;
        String description = "Final exams";
        boolean holiday = false;

        AcademicCalendarDate date = AcademicCalendarDateFactory.createDate(
                dateFromPage,
                year,
                description
        );

        LocalDate startDate = LocalDate.of(2024, 4, 29);
        LocalDate endDate = LocalDate.of(2024, 5, 3);

        assertEquals(date.getDateRange().getStartDate(), startDate);
        assertEquals(date.getDateRange().getEndDate(), endDate);
        assertEquals(date.getDescription(), description);
        assertEquals(date.isHoliday(), holiday);

        System.out.println(date);

    }

    @Test
    public void semesterFactory_getSemesterByString() {

        String semesterName = "Fall 2023";
        Semester semester = Semesters.getInstance().getSemester(semesterName);

        assertNotNull(semester);
        assertEquals(semester.getType(), SemesterType.FALL);
        assertEquals(semester.getYear(), 2023);

    }

    @Test
    public void courseDescriptionID_prefixUnpackWorks() {
        CourseDescriptionID id = new CourseDescriptionID((short) 2023, "CSE", (short) 1002);
        assertEquals("CSE", id.getPrefixAsString());
    }

    @Test
    public void buildings_getEvansHall() {
        Buildings buildings = Buildings.getInstance();
        assertNotNull(buildings);
        Building evansHall = buildings.getBuilding("119EVH");
        assertNotNull(evansHall);
        System.out.println(evansHall);
    }

    @Test
    public void eventSchedule_toString() {

        Map<DayOfWeek, TimeRange> schedule = new HashMap<>();
        schedule.put(DayOfWeek.FRIDAY, new TimeRange(
                LocalTime.of(11, 0), LocalTime.of(13, 45)));

        TimeRange rangeTwo = new TimeRange(
                LocalTime.of(14, 0), LocalTime.of(14, 50));
        schedule.put(DayOfWeek.MONDAY, rangeTwo);
        schedule.put(DayOfWeek.WEDNESDAY, rangeTwo);

        EventSchedule eventSchedule = new EventSchedule(schedule);
        System.out.println(eventSchedule);

    }

    @Test
    public void printSerializedStorage() {
        StudentStorage storage = new StudentStorage();
        ObjectNode storageJSON = storage.createStorageJSON();
        assertNotNull(storageJSON);

        try {
            System.out.println(new ObjectMapper().writeValueAsString(storageJSON));
        } catch (Exception e) {
            System.err.println("Could not print storage JSON.");
            e.printStackTrace();
        }
    }

    @Test
    public void writeStorage() {
        StudentStorage storage = new StudentStorage();
        assertTrue(storage.writeStorageToDisk(PASSWORD));
    }

    @Test
    public void readStorage() {
        StudentStorage storage = new StudentStorage();
        assertFalse(storage.readStorageFromDisk("-" + PASSWORD));
        assertTrue(storage.readStorageFromDisk(PASSWORD));
    }

    @Test
    public void scrapeCourseSchedule() {

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode[] scrapeResults = WebScraper.scrapeCourseSchedule();
        assertNotNull(scrapeResults);

        ObjectNode root = mapper.createObjectNode();
        root.set("descriptions", scrapeResults[0]);
        root.set("instances", scrapeResults[1]);

        try {
            System.out.println(mapper.writeValueAsString(root));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}