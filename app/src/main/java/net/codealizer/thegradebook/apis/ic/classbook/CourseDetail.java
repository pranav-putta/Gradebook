package net.codealizer.thegradebook.apis.ic.classbook;

import org.w3c.dom.Element;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Pranav on 10/9/16.
 */

public class CourseDetail implements Serializable {

    private String name;
    private boolean active;
    private int terms;
    private int periods;
    private CreditType creditType;
    private boolean full;
    private int periodNumber;
    private String courseNumber;
    private String teacherName;

    public CourseDetail(Element e, Element e1, GradingDetailSummary summary) {
        name = e.getAttribute("name");
        terms = Integer.parseInt(e.getAttribute("terms"));
        periods = Integer.parseInt(e.getAttribute("periods"));
        full = Boolean.parseBoolean(e.getAttribute("full"));

        if (e.hasAttribute("honorsCourse") && e.getAttribute("honorsCourse").equalsIgnoreCase("true")) {
            creditType = CreditType.HONORS;
        } else if (e.hasAttribute("acceleratedCourse") && e.getAttribute("acceleratedCourse").equalsIgnoreCase("true")) {
            creditType = CreditType.ACCELERATED;
        } else {
            creditType = CreditType.CORE;
        }

        this.periodNumber = summary.getPeriodNumber();
        this.courseNumber = e.getAttribute("number");
        this.teacherName = e1.getAttribute("teacherDisplay");

        /**this.name = WordUtils.capitalize(name.toLowerCase());

         String keys[] = {"ap", "pe", "hn", "ac", "Ap", "Pe", "Hn", "Ac"};

         for (String key : keys) {
         this.name = this.name.replaceAll(key, key.toUpperCase());
         }**/

        active = e1.getElementsByTagName("ClassbookTask").getLength() > 1;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public int getTerms() {
        return terms;
    }

    public int getPeriods() {
        return periods;
    }

    public CreditType getCreditType() {
        return creditType;
    }

    public boolean isFull() {
        return full;
    }

    public int getPeriodNumber() {
        return periodNumber;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setPeriodNumber(int periodNumber) {
        this.periodNumber = periodNumber;
    }

    @Override
    public boolean equals(Object obj) {
        String courseNumber = ((CourseDetail) obj).courseNumber;
        if (courseNumber.equals(this.courseNumber)) {
            return true;
        }
        return false;
    }

    public static class Compare implements Comparator<CourseDetail> {
        @Override
        public int compare(CourseDetail o, CourseDetail t1) {
            Integer p1 = o.getPeriodNumber();
            Integer p2 = t1.getPeriodNumber();

            int compare = p1.compareTo(p2);

            if (compare == 0) {
                return o.getCourseNumber().compareTo(t1.getCourseNumber());
            } else {
                return compare;
            }
        }
    }


}
