package net.codealizer.thegradebook.apis.ic.classbook;

import com.google.gson.JsonObject;

import org.apache.commons.lang3.text.WordUtils;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Pranav on 10/9/16.
 */

public class Course implements Serializable {

    private String courseName;
    private String teacher;
    private String overallGrade;
    private String courseNumber;
    private int periodNumber;
    private boolean active;

    public Course(String courseName, String teacher, String overallGrade, String courseNumber, boolean active, int periodNumber) {
        this.courseName = courseName;
        this.teacher = teacher;
        this.overallGrade = overallGrade;
        this.courseNumber = courseNumber;
        this.active = active;
        this.periodNumber = periodNumber;
    }

    public Course(JsonObject e, JsonObject gradingDetailSummary) {
        String s = e.toString();
        courseName = e.get("courseName").getAsString();
        courseNumber = e.get("courseNumber").getAsString();
        try {
            teacher = e.get("teacherDisplay").getAsString();
        } catch (NullPointerException ex) {
            teacher = "Not available";
        }

        courseName = courseName.toLowerCase();
        courseName = (courseName);

        String keys[] = {"ac", "hn", "pe"};

        for (String key : keys) {
            if (courseName.toLowerCase().contains(key)) {
                courseName = courseName.replace(key, key.toUpperCase());
            }
        }

        overallGrade = "A";


        active = e.has("tasks");

        try {
            String period = gradingDetailSummary.getAsJsonObject("Section").getAsJsonArray("Task").get(0).getAsJsonObject().getAsJsonObject("Score").get("periodName").getAsString();
            periodNumber = Integer.parseInt(period.replaceAll("\\D+", ""));
        } catch (Exception ex) {
            periodNumber = 1000;
        }
    }


    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getOverallGrade() {
        return overallGrade;
    }

    public void setOverallGrade(String overallGrade) {
        this.overallGrade = overallGrade;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getPeriodNumber() {
        return periodNumber;
    }

    public void setPeriodNumber(int periodNumber) {
        this.periodNumber = periodNumber;
    }

    @Override
    public boolean equals(Object obj) {
        String courseNumber = ((Course) obj).courseNumber;
        if (courseNumber.equals(this.courseNumber)) {
            return true;
        }
        return false;
    }

    public static class Compare implements Comparator<Course> {
        @Override
        public int compare(Course o, Course t1) {
            Integer p1 = o.getPeriodNumber();
            Integer p2 = t1.getPeriodNumber();

            return p1.compareTo(p2);
        }
    }


}
