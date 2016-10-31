package net.codealizer.thegradebook.apis.ic.calendar;

import net.codealizer.thegradebook.apis.ic.schedule.Term;
import net.codealizer.thegradebook.apis.ic.schedule.TermSchedule;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.Serializable;
import java.util.ArrayList;

public class Calendar implements Serializable {
    public String name;
    public String schoolID;
    public String calendarID;
    public String endYear;

    public ArrayList<ScheduleStructure> schedule;

    public Calendar(Element calendar) {
        schedule = new ArrayList<>();

        name = calendar.getAttribute("calendarName");
        schoolID = calendar.getAttribute("schoolID");
        calendarID = calendar.getAttribute("calendarID");
        endYear = calendar.getAttribute("endYear");

        NodeList list = calendar.getElementsByTagName("ScheduleStructure");

        for (int i = 0; i < list.getLength(); i++) {
            Element element = (Element) list.item(i);
            schedule.add(new ScheduleStructure(element));
        }
    }

    public String getName() {
        return name;
    }

    public String getSchoolID() {
        return schoolID;
    }

    public String getCalendarID() {
        return calendarID;
    }

    public String getEndYear() {
        return endYear;
    }

    public ArrayList<ScheduleStructure> getSchedule() {
        return schedule;
    }

    public ArrayList<String> getTermNames() {
        ArrayList<String> termNames = new ArrayList<>();

        for (ScheduleStructure scheduleStructure : schedule) {
            for (TermSchedule termSchedule : scheduleStructure.termSchedules) {
                for (Term t : termSchedule.getTerms()) {
                    termNames.add(t.getName());
                }
            }
        }

        return termNames;
    }

    public ArrayList<Term> getTerms() {
        ArrayList<Term> terms = new ArrayList<>();

        for (ScheduleStructure scheduleStructure : schedule) {
            for (TermSchedule termSchedule : scheduleStructure.termSchedules) {
                for (Term t : termSchedule.getTerms()) {
                    terms.add(t);
                }
            }
        }

        return terms;
    }
}
