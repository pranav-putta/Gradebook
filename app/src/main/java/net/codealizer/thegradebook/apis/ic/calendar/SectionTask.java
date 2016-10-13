package net.codealizer.thegradebook.apis.ic.calendar;

import com.google.gson.JsonObject;

import net.codealizer.thegradebook.apis.ic.classbook.Course;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Pranav on 10/10/16.
 */

public class SectionTask implements Serializable {

    private int termMask;
    private int activeMask;
    private String name;
    private int standardID;
    private int taskID;
    private int seq;
    private boolean stateStandard;

    // Score
    private long scoreID;
    private String date;
    private String schedule;
    private int termID;
    private String score;
    private int termSeq;
    private String periodName;
    private int periodSeq;
    private String termName;
    private double percent;
    private String comments;

    private TaskType type;

    private HashMap<Integer, Integer> weekKeys;

    /*
    Variables that do not work for an empty task:
        {date, score, percent, scoreID}
     */


    public SectionTask(JsonObject task) {
        termMask = task.get("termMask").getAsInt();
        activeMask = task.get("activeMask").getAsInt();
        name = task.get("name").getAsString();
        standardID = task.get("standardID").getAsInt();
        taskID = task.get("taskID").getAsInt();
        seq = task.get("seq").getAsInt();
        stateStandard = task.get("stateStandard").getAsBoolean();

        //All Items under the score object
        JsonObject score = task.getAsJsonObject("Score");
        schedule = score.get("schedule").getAsString();
        termID = score.get("termID").getAsInt();
        termSeq = score.get("termSeq").getAsInt();
        periodName = score.get("periodName").getAsString();
        termName = score.get("termName").getAsString();

        if (name.contains("Term")) {
            type = TaskType.TERM;
        } else if (name.contains("Week")) {
            type = TaskType.WEEK;
        } else if (name.contains("Final")) {
            type = TaskType.FINAL_GRADE;
        } else if (name.contains("Exam")) {
            type = TaskType.EXAM;
        } else {
            type = TaskType.EMPTY;
        }

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

        try {
            date = (score.get("date").getAsString());

            this.score = score.get("score").getAsString();
            if (score.has("percent")) {
                percent = score.get("percent").getAsDouble();
            } else {
                percent = 0;
            }
            scoreID = score.get("scoreID").getAsLong();

            if (score.has("comments")) {
                comments = score.get("comments").getAsString();
            } else {
                comments = "None";
            }
        } catch (Exception ex) {
            date = "-";
            percent = 0.0;
            this.score = "-";
            this.scoreID = 0;

        }


    }

    public int getTermMask() {
        return termMask;
    }

    public void setTermMask(int termMask) {
        this.termMask = termMask;
    }

    public int getActiveMask() {
        return activeMask;
    }

    public void setActiveMask(int activeMask) {
        this.activeMask = activeMask;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStandardID() {
        return standardID;
    }

    public void setStandardID(int standardID) {
        this.standardID = standardID;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public boolean isStateStandard() {
        return stateStandard;
    }

    public void setStateStandard(boolean stateStandard) {
        this.stateStandard = stateStandard;
    }

    public long getScoreID() {
        return scoreID;
    }

    public void setScoreID(long scoreID) {
        this.scoreID = scoreID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public int getTermID() {
        return termID;
    }

    public void setTermID(int termID) {
        this.termID = termID;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getTermSeq() {
        return termSeq;
    }

    public void setTermSeq(int termSeq) {
        this.termSeq = termSeq;
    }

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public int getPeriodSeq() {
        return periodSeq;
    }

    public void setPeriodSeq(int periodSeq) {
        this.periodSeq = periodSeq;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public static ArrayList<String> getNamesFromList(ArrayList<SectionTask> list) {
        ArrayList<String> newList = new ArrayList<>();
        for (SectionTask s : list) {
            newList.add(s.getName());
        }

        return newList;
    }

    public static class Compare implements Comparator<SectionTask> {
        @Override
        public int compare(SectionTask o, SectionTask t1) {
            Integer p1 = o.getSeq();
            Integer p2 = t1.getSeq();

            return p1.compareTo(p2);
        }
    }
}
