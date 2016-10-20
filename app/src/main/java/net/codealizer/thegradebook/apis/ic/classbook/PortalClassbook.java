package net.codealizer.thegradebook.apis.ic.classbook;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.codealizer.thegradebook.apis.ic.calendar.Semester;
import net.codealizer.thegradebook.apis.ic.calendar.Term;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class PortalClassbook {
    public String sectionID;
    private Course course;

    ArrayList<Semester> mSemesters;


    public PortalClassbook(JsonObject classbookElement) {
        this.sectionID = classbookElement.get("sectionID").getAsString();
        JsonObject e = classbookElement.get("ClassbookDetail").getAsJsonObject()
                .get("StudentList").getAsJsonObject().getAsJsonObject("Student").getAsJsonObject("Classbook");
        JsonObject period = classbookElement.getAsJsonObject("StudentList").getAsJsonObject("Student").getAsJsonObject("GradingDetailSummary");

        course = new Course(e, period);

        mSemesters = new ArrayList<>();

        try {
            JsonArray semesters = classbookElement.getAsJsonObject("Calendar").getAsJsonObject("scheduleStructures")
                    .getAsJsonObject("ScheduleStructure").getAsJsonObject("termSchedules").getAsJsonObject("TermSchedule")
                    .getAsJsonObject("terms").getAsJsonArray("Term");

            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

            int i = 1;

            for (JsonElement s : semesters) {
                Date start = format.parse(s.getAsJsonObject().get("startDate").getAsString());
                Date end = format.parse(s.getAsJsonObject().get("endDate").getAsString());

                mSemesters.add(new Semester(start, end, i, period, e));
                i++;
            }
        } catch (NullPointerException | ParseException ex) {
            ex.printStackTrace();
            mSemesters.clear();
        }
    }
   @Override
    public boolean equals(Object obj) {
        if (obj instanceof Course) {
            return obj.equals(course);
        } else {
            return false;
        }
    }

    public ArrayList<Term> getTerms() {
        ArrayList<Term> terms = new ArrayList<>();

        for (Semester s : mSemesters) {
            terms.addAll(s.getTerms());
        }

        return terms;
    }

    public ArrayList<Term> getTerm(int semester) {
        return mSemesters.get(semester).getTerms();
    }

    public Course getCourse() {
        return course;
    }


    public ArrayList<Semester> getSemesters() {
        return mSemesters;
    }

    public void setSemesters(ArrayList<Semester> mSemesters) {
        this.mSemesters = mSemesters;
    }
}
