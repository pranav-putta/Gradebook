package net.codealizer.thegradebook.apis.ic.classbook;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;


public class ClassbookManager implements Serializable {
    ArrayList<PortalClassbook> portalclassbooks = new ArrayList<PortalClassbook>();
    private String json;

    private int clubs;
    private int classes;

    public ClassbookManager(JsonObject classbook, String json) {
        JsonArray e = classbook.getAsJsonArray("PortalClassbook");

        String s = e.toString();

        this.json = json;
        for (int i = 0; i < e.size(); i++) {
            JsonObject object = e.get(i).getAsJsonObject();
            portalclassbooks.add(new PortalClassbook(object));
        }
    }

    public String getInfoString() {
        String str = "";
        for (PortalClassbook p : portalclassbooks)
            str += "\n";
        return str;
    }

    public ArrayList<Course> getStudentCourses() {
        ArrayList<Course> courses = new ArrayList<>();

        clubs = 0;
        classes = 0;

        for (PortalClassbook c : portalclassbooks) {
            courses.add(c.getCourse());

            if (c.getCourse().getPeriodNumber() == 1000) {
                clubs++;
            } else {
                classes++;
            }
        }

        Collections.sort(courses, new Course.Compare());

        return courses;
    }

    public PortalClassbook getClassbookForCourse(Course c) {
        for (PortalClassbook p : portalclassbooks) {
            if (p.getCourse().equals(c)) {
                return p;
            }
        }
        return null;
    }

    public String getJson() {
        return json;
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
