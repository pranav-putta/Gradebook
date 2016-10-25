package net.codealizer.thegradebook.apis.ic.xml.classbook;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;


public class ClassbookManager implements Serializable {
    public ArrayList<PortalClassbook> portalclassbooks = new ArrayList<PortalClassbook>();
    private String xml;

    private int clubs;
    private int classes;

    public ClassbookManager(Element element, String xml) {
        this.xml = xml;

        NodeList classbooks = element.getElementsByTagName("PortalClassbook");

        for (int i = 0; i < classbooks.getLength(); i++) {
            Element portalClassbook = (Element) classbooks.item(i);

            portalclassbooks.add(new PortalClassbook(portalClassbook));
        }
    }

    public String getInfoString() {
        String str = "";
        for (PortalClassbook p : portalclassbooks)
            str += "\n";
        return str;
    }

    public ArrayList<CourseDetail> getStudentCourses() {
        ArrayList<CourseDetail> courses = new ArrayList<>();

        clubs = 0;
        classes = 0;

        for (PortalClassbook c : portalclassbooks) {
            courses.add(c.getCourse());

            if (c.getCourse().isActive()) {
                classes++;
            } else {
                c.getCourse().setPeriodNumber(999);
                clubs++;
            }
        }

        Collections.sort(courses, new CourseDetail.Compare());

        return courses;
    }

    public PortalClassbook getClassbookForCourse(CourseDetail c) {
        for (PortalClassbook p : portalclassbooks) {
            if (p.getCourse().equals(c)) {
                return p;
            }
        }
        return null;
    }


    public String getXML() {
        return xml;
    }

    public int getClubs() {
        return clubs;
    }

    public void setClubs(int clubs) {
        this.clubs = clubs;
    }

    public int getClasses() {
        return classes;
    }

    public void setClasses(int classes) {
        this.classes = classes;
    }
}
