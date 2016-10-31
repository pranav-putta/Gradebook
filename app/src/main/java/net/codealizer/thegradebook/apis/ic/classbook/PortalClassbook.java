package net.codealizer.thegradebook.apis.ic.classbook;

import net.codealizer.thegradebook.apis.ic.calendar.Calendar;
import net.codealizer.thegradebook.apis.ic.calendar.ScheduleStructure;
import net.codealizer.thegradebook.apis.ic.curves.Curve;
import net.codealizer.thegradebook.apis.ic.curves.Rubric;
import net.codealizer.thegradebook.data.SessionManager;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;


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

    public static PortalClassbook find(String sectionID) {
        for (PortalClassbook classbook : SessionManager.mCoreManager.gradebookManager.portalclassbooks) {
            if (classbook.sectionID.equals(sectionID)) {
                return classbook;
            }
        }

        return null;
    }

    public static class Compare implements Comparator<PortalClassbook> {
        @Override
        public int compare(PortalClassbook o, PortalClassbook t1) {
            Integer p1 = o.getCourse().getPeriodNumber();
            Integer p2 = t1.getCourse().getPeriodNumber();

            int compare = p1.compareTo(p2);

            if (compare == 0) {
                return o.getCourse().getCourseNumber().compareTo(t1.getCourse().getCourseNumber());
            } else {
                return compare;
            }
        }
    }

    public boolean isEBR() {
        return (!getRubrics().isEmpty());
    }

}
