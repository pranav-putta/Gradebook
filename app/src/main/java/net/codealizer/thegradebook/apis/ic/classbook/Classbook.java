package net.codealizer.thegradebook.apis.ic.classbook;

import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;


public class Classbook implements Serializable {
    public String termName;
    public String courseNumber;
    public String courseName;
    public String sectionNumber;
    public String teacherDisplay;

    public ArrayList<Curve> curves = new ArrayList<Curve>();
    public ArrayList<ClassbookTask> tasks = new ArrayList<ClassbookTask>();

    public Classbook(JsonObject classbook) {
        termName = classbook.get("termName").getAsString();
        courseNumber = classbook.get("courseNumber").getAsString();
        courseName = classbook.get("courseName").getAsString();
        sectionNumber = classbook.get("sectionNumber").getAsString();
        teacherDisplay = classbook.get("teacherDisplay").getAsString();

        for (int i = 0; i < classbook.get("tasks").getAsJsonObject().get("ClassbookTask").getAsJsonArray().size(); i++)
            tasks.add(new ClassbookTask(classbook.get("tasks").getAsJsonObject().get("ClassbookTask").getAsJsonArray().get(i).getAsJsonObject()));
        for (int i = 0; i < classbook.get("curves").getAsJsonObject().get("Curve").getAsJsonArray().size(); i++)
            curves.add(new Curve(classbook.get("curves").getAsJsonObject().get("Curve").getAsJsonArray().get(i).getAsJsonObject()));
    }

    public String getInfoString() {
        String str = "\nTasks for " + courseName + ", with teacher " + teacherDisplay + " and class ID " + courseNumber + ", " + termName;
        for (ClassbookTask t : tasks)
            str += "\n" + t.getInfoString();
        return str;
    }
}
