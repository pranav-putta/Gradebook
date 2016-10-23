package net.codealizer.thegradebook.apis.ic.calendar;

import com.google.gson.JsonObject;

import java.util.ArrayList;

public class Calendar {
    public String name;
    public String schoolID;
    public String calendarID;
    public String endYear;

    public ScheduleStructure schedule;

    public Calendar(JsonObject calendar) {
        name = calendar.get("calendarName").getAsString();
        schoolID = calendar.get("schoolID").getAsString();
        calendarID = calendar.get("calendarID").getAsString();
        endYear = calendar.get("endYear").getAsString();
        schedule = new ScheduleStructure(calendar.getAsJsonObject("ScheduleStructure"));
    }

    public String getInfoString() {
        String returnString = "Information for Calendar \'" + name + "\':\nSchool ID: " + schoolID + "\nCalendar ID: " + calendarID + "\nEnding Year: " + endYear + "\n\n===Schedules===";

        returnString += "\n" + schedule.getInfoString();

        return returnString;
    }
}
