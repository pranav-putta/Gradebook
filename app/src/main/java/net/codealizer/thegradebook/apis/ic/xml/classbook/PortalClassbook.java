package net.codealizer.thegradebook.apis.ic.xml.classbook;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.codealizer.thegradebook.apis.ic.xml.calendar.Calendar;
import net.codealizer.thegradebook.apis.ic.xml.calendar.ScheduleStructure;
import net.codealizer.thegradebook.apis.ic.xml.schedule.Semester;
import net.codealizer.thegradebook.apis.ic.xml.schedule.Term;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class PortalClassbook implements Serializable {
    public final String sectionID;

    private final Calendar mCalendar;
    private final CourseDetail course;
    private final GradingDetailSummary mGradingDetailSummary;

    private final ArrayList<Curve> mCurves;
    private final ArrayList<Rubric> mRubrics;
    private final ArrayList<Classbook> mClassbooks;


    public PortalClassbook(Element classbookElement) {
        //Initialize all objects
        mClassbooks = new ArrayList<>();
        mCurves = new ArrayList<>();
        mRubrics = new ArrayList<>();

        //Create sectionID object
        this.sectionID = classbookElement.getAttribute("sectionID");

        Element element;
        NodeList list;

        //Create "Classbook" Object
        element = (Element) classbookElement.getElementsByTagName("ClassbookDetail").item(0);
        list = element.getElementsByTagName("Classbook");

        for (int i = 0; i < list.getLength(); i++) {
            element = (Element) list.item(i);
            mClassbooks.add(new Classbook(element));
        }

        //Create "Rubrics" Object
        list = classbookElement.getElementsByTagName("ScoreGroup");

        for (int i = 0; i < list.getLength(); i++) {
            element = (Element) list.item(i);
            mRubrics.add(new Rubric(element));
        }

        //Create "Curves" Object
        list = classbookElement.getElementsByTagName("Curve");

        for (int i = 0; i < list.getLength(); i++) {
            element = (Element) list.item(i);

            mCurves.add(new Curve(element));
        }

        //Create "Calendar" object
        element = (Element) classbookElement.getElementsByTagName("Calendar").item(0);
        mCalendar = new Calendar(element);

        //Create "GradingDetailSummary" Object
        element = (Element) classbookElement.getElementsByTagName("GradingDetailSummary").item(0);
        mGradingDetailSummary = new GradingDetailSummary(element);

        //Create "CourseDetail" Object
        element = (Element) classbookElement.getElementsByTagName("Course").item(0);
        Element e2 = (Element) classbookElement.getElementsByTagName("Classbook").item(0);
        course = new CourseDetail(element, e2, mGradingDetailSummary);

        //
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CourseDetail) {
            return obj.equals(course);
        } else {
            return false;
        }
    }

    public CourseDetail getCourse() {
        return course;
    }

    public String getSectionID() {
        return sectionID;
    }

    public Calendar getCalendar() {
        return mCalendar;
    }

    public GradingDetailSummary getGradingDetailSummary() {
        return mGradingDetailSummary;
    }

    public ArrayList<Curve> getCurves() {
        return mCurves;
    }

    public ArrayList<Rubric> getRubrics() {
        return mRubrics;
    }

    public ArrayList<ScheduleStructure> getAllTerms() {
        return mCalendar.getSchedule();
    }

    public ArrayList<Classbook> getClassbooks() {
        return mClassbooks;
    }

    public Classbook getClassbook() {
        if (getClassbooks().size() > 0) {
            return getClassbooks().get(0);
        }

        return null;
    }

    public boolean isEBR() {
        return (!getRubrics().isEmpty());
    }

}
