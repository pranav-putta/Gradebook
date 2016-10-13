package net.codealizer.thegradebook.apis.ic.classbook;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.codealizer.thegradebook.ui.gradebook.cards.BasicClassbookActivity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ClassbookActivity implements Serializable {
    public String activityID;
    public String name;
    public String abbrev;
    public String dueDate;
    public String assignedDate;
    public float totalPoints;
    public boolean active;
    public boolean notGraded;
    //hidePortal
    //seq
    public float weight;
    public String scoringType;
    public boolean validScore;
    public String scoreID;
    public String score;
    public boolean late;
    public boolean missing;
    public boolean incomplete;
    public boolean turnedIn;
    public boolean exempt;
    public boolean cheated;
    public boolean dropped;
    public float percentage;
    public String letterGrade;
    public float weightedScore;
    public float weightedTotalPoints;
    public float weightedPercentage;
    public float numericScore;
    public boolean wysiwygSubmission;
    public boolean onlineAssessment;

    public String comments;
    public boolean custom;

    public int index;
    public int masterIndex;
    public int groupID;

    public ClassbookActivity(BasicClassbookActivity a) {
        dueDate = new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime());
        name = a.getTitle();
        score = String.valueOf(a.getPoints());
        totalPoints = (a.getTotalPoints());
        weight = 1;
        comments = "Custom assignment";
        active = true;
        custom = true;
        groupID = a.getGroupID();
    }

    public ClassbookActivity(JsonObject activity) {
        activityID = activity.get("activityID").getAsString();
        name = activity.get("name").getAsString();
        abbrev = activity.get("abbrev").getAsString();
        dueDate = activity.get("dueDate").getAsString();
        assignedDate = activity.get("assignedDate").getAsString();
        totalPoints = Float.parseFloat(activity.get("totalPoints").getAsString());
        active = activity.get("active").getAsString().equalsIgnoreCase("true");
        notGraded = activity.get("notGraded").getAsString().equalsIgnoreCase("true");
        weight = Float.parseFloat(activity.get("weight").getAsString());
        scoringType = activity.get("scoringType").getAsString();
        validScore = activity.get("validScore").getAsString().equalsIgnoreCase("validScore");
        if (activity.has("scoreID"))
            scoreID = activity.get("scoreID").getAsString();
        if (activity.has("score"))
            score = (activity.get("score").getAsString());
        late = activity.get("late").getAsString().equalsIgnoreCase("true");
        missing = activity.get("missing").getAsString().equalsIgnoreCase("true");
        incomplete = activity.get("incomplete").getAsString().equalsIgnoreCase("true");
        turnedIn = activity.get("turnedIn").getAsString().equalsIgnoreCase("true");
        cheated = activity.get("cheated").getAsString().equalsIgnoreCase("true");
        dropped = activity.get("dropped").getAsString().equalsIgnoreCase("true");

        if (activity.has("comments")) {
            comments = activity.get("comments").getAsString();
        } else {
            comments = "None";
        }
        if (!scoringType.equals("p") && !scoringType.equals("r")) {
            percentage = Float.parseFloat(activity.get("percentage").getAsString());
            letterGrade = activity.get("letterGrade").getAsString();
            weightedScore = Float.parseFloat(activity.get("weightedScore").getAsString());
            weightedTotalPoints = Float.parseFloat(activity.get("weightedTotalPoints").getAsString());
            weightedPercentage = Float.parseFloat(activity.get("weightedPercentage").getAsString());
            numericScore = Float.parseFloat(activity.get("numericScore").getAsString());
            wysiwygSubmission = activity.get("wysiwygSubmission").getAsString().equalsIgnoreCase("true");
            onlineAssessment = activity.get("onlineAssessment").getAsString().equalsIgnoreCase("true");
        } else if (scoringType.equals("r")) {
            percentage = Float.parseFloat(activity.get("percentage").getAsString());
            weightedScore = Float.parseFloat(activity.get("weightedScore").getAsString());
            weightedTotalPoints = Float.parseFloat(activity.get("weightedTotalPoints").getAsString());
            weightedPercentage = Float.parseFloat(activity.get("weightedPercentage").getAsString());
            if (activity.has("numericScore"))
                numericScore = Float.parseFloat(activity.get("numericScore").getAsString());
            wysiwygSubmission = activity.get("wysiwygSubmission").getAsString().equalsIgnoreCase("true");
            onlineAssessment = activity.get("onlineAssessment").getAsString().equalsIgnoreCase("true");
        }
    }

    public BasicClassbookActivity getBasicActivity() {
        return new BasicClassbookActivity(name, Integer.parseInt(score), (int) (totalPoints), groupID);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ClassbookActivity) {
            ClassbookActivity a = (ClassbookActivity) obj;
            return a.name.equals(name);
        } else if (obj instanceof BasicClassbookActivity) {
            BasicClassbookActivity a = (BasicClassbookActivity) obj;
            return a.getTitle().equals(name);
        }

        return false;
    }

    public String getInfoString() {
        return name + "\t\t" + letterGrade + " " + weightedPercentage + "%" + "\t(" + score + "/" + (int) totalPoints + ")";
    }
}
