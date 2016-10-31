package net.codealizer.thegradebook.apis.ic.classbook;

import android.util.Pair;

import net.codealizer.thegradebook.apis.ic.curves.Curve;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.Serializable;
import java.util.ArrayList;


public class Classbook implements Serializable {
    private String termName;
    private String courseNumber;
    private String courseName;
    private String sectionNumber;
    private String teacherDisplay;

    public ArrayList<Curve> curves = new ArrayList<Curve>();
    public ArrayList<ClassbookTask> tasks = new ArrayList<ClassbookTask>();

    public Classbook(Element classbook) {

        termName = "";
        courseName = "";
        courseNumber = "";
        sectionNumber = "";
        teacherDisplay = "";

        if (classbook.hasAttribute("termName"))
            termName = classbook.getAttribute("termName");
        if (classbook.hasAttribute("courseNumber"))
            courseNumber = classbook.getAttribute("courseNumber");
        if (classbook.hasAttribute("courseName"))
            courseName = classbook.getAttribute("courseName");
        if (classbook.hasAttribute("sectionNumber"))
            sectionNumber = classbook.getAttribute("sectionNumber");
        if (classbook.hasAttribute("teacherDisplay"))
            teacherDisplay = classbook.getAttribute("teacherDisplay");

        //Tasks
        NodeList list = classbook.getElementsByTagName("ClassbookTask");
        for (int i = 0; i < list.getLength(); i++) {
            Element element = (Element) list.item(i);
            tasks.add(new ClassbookTask(element));
        }

        // Curves
        list = classbook.getElementsByTagName("Curve");
        for (int i = 0; i < list.getLength(); i++) {
            Element element = (Element) list.item(i);
            curves.add(new Curve(element));
        }
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(String sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public String getTeacherDisplay() {
        return teacherDisplay;
    }

    public void setTeacherDisplay(String teacherDisplay) {
        this.teacherDisplay = teacherDisplay;
    }

    public ArrayList<Curve> getCurves() {
        return curves;
    }

    public void setCurves(ArrayList<Curve> curves) {
        this.curves = curves;
    }

    public ArrayList<ClassbookTask> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<ClassbookTask> tasks) {
        this.tasks = tasks;
    }

    public ArrayList<Pair<String, ClassbookActivity>> getAllActivities() {
        ArrayList<Pair<String, ClassbookActivity>> activities = new ArrayList<>();

        for (ClassbookTask g : tasks) {
            for (ClassbookGroup group : g.groups) {
                for (ClassbookActivity activity : group.activities) {
                    activities.add(new Pair<String, ClassbookActivity>(courseName, activity));
                }
            }
        }
        return activities;
    }

    public ArrayList<ClassbookTask> getTasks(int term) {
        ArrayList<ClassbookTask> tasks = new ArrayList<>();

        for (ClassbookTask task : this.tasks) {
            if (task.termSeq == term) {
                tasks.add(task);
            }
        }

        return tasks;
    }
}
