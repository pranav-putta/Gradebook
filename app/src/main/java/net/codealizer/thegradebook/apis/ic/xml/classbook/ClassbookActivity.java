package net.codealizer.thegradebook.apis.ic.xml.classbook;


import android.text.Html;
import android.text.Spanned;

import com.google.gson.JsonObject;

import net.codealizer.thegradebook.assets.BasicClassbookActivity;
import net.codealizer.thegradebook.assets.BasicGradeDetail;

import org.apache.commons.lang3.text.WordUtils;
import org.w3c.dom.Element;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ClassbookActivity implements Serializable {
    private String activityID;
    public String name;
    private String abbrev;
    private String dueDate;
    private String assignedDate;
    private float totalPoints;
    private boolean active;
    private boolean notGraded;
    //hidePortal
    //seq
    public float weight;
    private String scoringType;
    private boolean validScore;
    private String scoreID;
    public String score;
    private boolean late;
    private boolean missing;
    private boolean incomplete;
    private boolean turnedIn;
    public boolean exempt;
    private boolean cheated;
    private boolean dropped;
    private float percentage;
    private String letterGrade;
    private float weightedScore;
    private float weightedTotalPoints;
    private float weightedPercentage;
    private String numericScore;
    private boolean wysiwygSubmission;
    private boolean onlineAssessment;

    private String comments;
    public boolean custom;

    public int index;
    public int masterIndex;
    private int groupID;

    public boolean theoreticalGrade = false;

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

    public ClassbookActivity(Element activity) {
        activityID = activity.getAttribute("activityID");
        name = activity.getAttribute("name");
        abbrev = activity.getAttribute("abbrev");
        dueDate = activity.getAttribute("dueDate");
        assignedDate = activity.getAttribute("assignedDate");
        totalPoints = Float.parseFloat(activity.getAttribute("totalPoints"));
        active = activity.getAttribute("active").equalsIgnoreCase("true");
        notGraded = activity.getAttribute("notGraded").equalsIgnoreCase("true");
        weight = Float.parseFloat(activity.getAttribute("weight"));
        scoringType = activity.getAttribute("scoringType");
        validScore = activity.getAttribute("validScore").equalsIgnoreCase("validScore");
        if (activity.hasAttribute("scoreID"))
            scoreID = activity.getAttribute("scoreID");
        if (activity.hasAttribute("score"))
            score = (activity.getAttribute("score"));
        late = activity.getAttribute("late").equalsIgnoreCase("true");
        missing = activity.getAttribute("missing").equalsIgnoreCase("true");
        incomplete = activity.getAttribute("incomplete").equalsIgnoreCase("true");
        turnedIn = activity.getAttribute("turnedIn").equalsIgnoreCase("true");
        cheated = activity.getAttribute("cheated").equalsIgnoreCase("true");
        dropped = activity.getAttribute("dropped").equalsIgnoreCase("true");

        if (activity.hasAttribute("comments")) {
            comments = activity.getAttribute("comments");
        } else {
            comments = "None";
        }
        if (!scoringType.equals("p") && !scoringType.equals("r")) {
            percentage = Float.parseFloat(activity.getAttribute("percentage"));
            letterGrade = activity.getAttribute("letterGrade");
            weightedScore = Float.parseFloat(activity.getAttribute("weightedScore"));
            weightedTotalPoints = Float.parseFloat(activity.getAttribute("weightedTotalPoints"));
            weightedPercentage = Float.parseFloat(activity.getAttribute("weightedPercentage"));
            numericScore = (activity.getAttribute("numericScore"));
            wysiwygSubmission = activity.getAttribute("wysiwygSubmission").equalsIgnoreCase("true");
            onlineAssessment = activity.getAttribute("onlineAssessment").equalsIgnoreCase("true");
        } else if (scoringType.equals("r")) {
            percentage = Float.parseFloat(activity.getAttribute("percentage"));
            weightedScore = Float.parseFloat(activity.getAttribute("weightedScore"));
            weightedTotalPoints = Float.parseFloat(activity.getAttribute("weightedTotalPoints"));
            weightedPercentage = Float.parseFloat(activity.getAttribute("weightedPercentage"));
            if (activity.hasAttribute("numericScore"))
                numericScore = (activity.getAttribute("numericScore"));
            wysiwygSubmission = activity.getAttribute("wysiwygSubmission").equalsIgnoreCase("true");
            onlineAssessment = activity.getAttribute("onlineAssessment").equalsIgnoreCase("true");
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

    public String getActivityID() {
        return activityID;
    }

    public String getName() {
        return name;
    }

    public String getAbbrev() {
        return abbrev;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getAssignedDate() {
        return assignedDate;
    }

    public float getTotalPoints() {
        return totalPoints;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isNotGraded() {
        return notGraded;
    }

    public float getWeight() {
        return weight;
    }

    public String getScoringType() {
        return scoringType;
    }

    public boolean isValidScore() {
        return validScore;
    }

    public String getScoreID() {
        return scoreID;
    }

    public String getScore() {
        return score;
    }

    public boolean isLate() {
        return late;
    }

    public boolean isMissing() {
        return missing;
    }

    public boolean isIncomplete() {
        return incomplete;
    }

    public boolean isTurnedIn() {
        return turnedIn;
    }

    public boolean isExempt() {
        return exempt;
    }

    public boolean isCheated() {
        return cheated;
    }

    public boolean isDropped() {
        return dropped;
    }

    public float getPercentage() {
        return percentage;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public float getWeightedScore() {
        return weightedScore;
    }

    public float getWeightedTotalPoints() {
        return weightedTotalPoints;
    }

    public float getWeightedPercentage() {
        return weightedPercentage;
    }

    public String getNumericScore() {
        return numericScore;
    }

    public boolean isWysiwygSubmission() {
        return wysiwygSubmission;
    }

    public boolean isOnlineAssessment() {
        return onlineAssessment;
    }

    public String getComments() {
        return comments;
    }

    public boolean isCustom() {
        return custom;
    }

    public int getIndex() {
        return index;
    }

    public int getMasterIndex() {
        return masterIndex;
    }

    public int getGroupID() {
        return groupID;
    }

    public Spanned getInfoString(String className) {
        String percent;
        String letter;

        try {
            percent = new DecimalFormat(".##").format((Float.parseFloat(score) / totalPoints) * 100);
            letter = BasicGradeDetail.calculateGrade(percent);
        } catch (Exception ex) {
            percent = score;
            letter = "";
        }

        if (score != null && !percent.equals(score)) {
            if (letter != null && letter.contains("A")) {
                return Html.fromHtml("You have earned an <strong>" + letter + " (" + percent + "%)</strong> for \"" + name + "\" in <strong>" + WordUtils.capitalize(className.toLowerCase()) + "</strong>");
            } else {
                return Html.fromHtml("You have earned a <strong>" + letter + " (" + percent + "%)</strong> for \"" + name + "\" in <strong>" + WordUtils.capitalize(className.toLowerCase()) + "</strong>");
            }
        } else {
            return Html.fromHtml("You have earned a <strong>" + percent + "</strong> for \"" + name + "\" in <strong>" + WordUtils.capitalize(className.toLowerCase()) + "</strong>");

        }
    }
}
