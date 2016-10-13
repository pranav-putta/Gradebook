package net.codealizer.thegradebook.apis.ic.classbook;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.codealizer.thegradebook.apis.ic.classbook.ClassbookActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class ClassbookGroup implements Serializable {
    public String activityID;
    public String name;
    public float weight;
    public int seq;
    public boolean notGraded = false;
    //hidePortal
    //hasValidScore
    //composite
    //calcEclude
    public int termID;
    public int taskID;
    public float percentage;
    public String formattedPercentage;
    public String letterGrade;
    public float pointsEarned;
    public float totalPointsPossible;
    public boolean ebr;

    public ArrayList<ClassbookActivity> activities = new ArrayList<ClassbookActivity>();

    public ClassbookGroup(JsonObject group) {
        activityID = group.get("activityID").getAsString();
        name = group.get("name").getAsString();
        weight = Float.parseFloat(group.get("weight").getAsString());
        seq = Integer.parseInt(group.get("seq").getAsString());
        notGraded = group.get("notGraded").getAsString().equalsIgnoreCase("true");
        termID = Integer.parseInt(group.get("termID").getAsString());
        taskID = Integer.parseInt(group.get("taskID").getAsString());
        percentage = Float.parseFloat(group.get("percentage").getAsString());
        ebr = false;
        if (group.has("formattedPercentage") && group.has("letterGrade")) {
            formattedPercentage = group.get("formattedPercentage").getAsString();
            letterGrade = group.get("letterGrade").getAsString();
            ebr = true;
        }

        pointsEarned = Float.parseFloat(group.get("pointsEarned").getAsString());
        totalPointsPossible = Float.parseFloat(group.get("totalPointsPossible").getAsString());

        JsonElement activity = group.get("activities").getAsJsonObject().get("ClassbookActivity");
        if (activity.isJsonArray()) {
            for (JsonElement e : activity.getAsJsonArray()) {
                activities.add(new ClassbookActivity(e.getAsJsonObject()));
            }
        } else {
            activities.add(new ClassbookActivity(activity.getAsJsonObject()));
        }

        letterGrade = (letterGrade == null ? "?" : letterGrade);
        formattedPercentage = (formattedPercentage == null ? "?" : formattedPercentage);
    }

    public String getInfoString() {
        String str = name + (name.length() < 16 ? "\t" : "") + "\t(" + (weight > 0 ? "Weight: " + weight + ", " : "") + "Grade: " + letterGrade + ", " + formattedPercentage + "%)";

        for (ClassbookActivity a : activities)
            str += "\n\t" + a.getInfoString().replace("\n", "\n\t");

        return str;
    }
}
