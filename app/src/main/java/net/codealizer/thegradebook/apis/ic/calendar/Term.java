package net.codealizer.thegradebook.apis.ic.calendar;

import android.util.Pair;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.codealizer.thegradebook.apis.ic.classbook.Classbook;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookActivity;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookGroup;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookTask;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Pranav on 10/10/16.
 */

public class Term implements Serializable {

    private Date startDate;
    private Date endDate;

    private int termNumber;
    private static final int WEEKS_IN_TERM = 6;

    private double gradePercentage;
    private String gradeLetter;

    private ArrayList<SectionTask> mTasks;

    private ClassbookTask mTask;
    private ArrayList<ClassbookTask> mClassbookTasks;

    public boolean isEBR = false;

    public Term(Date startDate, Date endDate, int termNumber) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.termNumber = termNumber;
    }

    public Term(Date s, int t, JsonObject g) {
        this(s, t, g, null, false);
    }

    public Term(Date semesterStartDate, int termNumber, JsonObject grades, JsonObject g, boolean ebr) {
        int startDays = (termNumber - 1) * WEEKS_IN_TERM * 7;
        int endDays = (termNumber * WEEKS_IN_TERM * 7) - 3;

        Calendar c = Calendar.getInstance();
        c.setTime(semesterStartDate);
        c.add(Calendar.DATE, startDays);

        startDate = c.getTime();

        c.setTime(semesterStartDate);
        c.add(Calendar.DATE, endDays);

        endDate = c.getTime();

        this.termNumber = termNumber;
        this.gradePercentage = 0;
        this.gradeLetter = "-";

        mTasks = new ArrayList<>();

        try {
            JsonArray tasks = grades.getAsJsonObject("Section").getAsJsonArray("Task");

            for (JsonElement el : tasks) {
                mTasks.add(new SectionTask(el.getAsJsonObject()));
            }

            Collections.sort(mTasks, new SectionTask.Compare());

            for (SectionTask task : mTasks) {
                if (task.getType() == TaskType.TERM && task.getName().equals("Term " + termNumber)) {
                    gradeLetter = task.getScore();
                    gradePercentage = task.getPercent();
                    break;
                }
            }
        } catch (Exception ex) {
            gradePercentage = 0;
            gradeLetter = "-";
        }

        if (g != null) {
            if (g.has("groups")) {
                mTask = new ClassbookTask(g);

            } else {
                mTask = null;
            }
        } else {
            mTask = null;
        }

    }

    public Term(Date semesterStartDate, int termNumber, JsonObject grades, JsonArray g) {
        int startDays = (termNumber - 1) * WEEKS_IN_TERM * 7;
        int endDays = (termNumber * WEEKS_IN_TERM * 7) - 3;

        Calendar c = Calendar.getInstance();
        c.setTime(semesterStartDate);
        c.add(Calendar.DATE, startDays);

        startDate = c.getTime();

        c.setTime(semesterStartDate);
        c.add(Calendar.DATE, endDays);

        endDate = c.getTime();

        this.termNumber = termNumber;
        this.gradePercentage = 0;
        this.gradeLetter = "-";

        mTasks = new ArrayList<>();

        try {
            JsonArray tasks = grades.getAsJsonObject("Section").getAsJsonArray("Task");

            for (JsonElement el : tasks) {
                mTasks.add(new SectionTask(el.getAsJsonObject()));
            }

            Collections.sort(mTasks, new SectionTask.Compare());

            for (SectionTask task : mTasks) {
                if (task.getType() == TaskType.TERM && task.getName().equals("Term " + termNumber)) {
                    gradeLetter = task.getScore();
                    gradePercentage = task.getPercent();
                    break;
                }
            }
        } catch (Exception ex) {
            gradePercentage = 0;
            gradeLetter = "-";
        }
        isEBR = true;
        mClassbookTasks = new ArrayList<>();

        if (g != null) {
            for (JsonElement x : g) {
                if (x.getAsJsonObject().get("hasValidWeightedGroup").getAsBoolean() && x.getAsJsonObject().has("groups")) {
                    mClassbookTasks.add(new ClassbookTask(x.getAsJsonObject()));
                }
            }
        } else {
            mClassbookTasks = null;
        }


    }

    public ClassbookTask getTask() {
        return mTask;
    }

    public ArrayList<ClassbookTask> getAllTasks() {
        return mClassbookTasks;
    }

    public ArrayList<ClassbookActivity> getAllActivities() {
        ArrayList<ClassbookActivity> activities = new ArrayList<>();

        for (ClassbookGroup group : mTask.groups) {
            activities.addAll(group.activities);
        }

        return activities;
    }

    public ArrayList<Pair<String, ClassbookActivity>> getAllActivities(String className) {
        ArrayList<Pair<String, ClassbookActivity>> activities = new ArrayList<>();

        if (mTask != null) {
            if (mTask.groups != null) {
                for (ClassbookGroup group : mTask.groups) {
                    for (ClassbookActivity activity : group.activities) {
                        activities.add(new Pair<>(className, activity));
                    }
                }
            }
        } else if (mClassbookTasks != null) {
            for (ClassbookTask task : mClassbookTasks) {
                if (task.groups != null) {
                    for (ClassbookGroup group : task.groups) {
                        for (ClassbookActivity activity : group.activities) {
                            activities.add(new Pair<>(className, activity));
                        }
                    }
                }

            }
        }

        return activities;
    }

    public ArrayList<Pair<Integer, String>> getCategories() {
        int i = 0;
        ArrayList<Pair<Integer, String>> categories = new ArrayList<>();

        for (ClassbookGroup group : mTask.groups) {
            categories.add(new Pair<>(i, group.name + " (Weight: " + group.weight + ")"));
            i += group.activities.size();

        }

        return categories;
    }

    public void getTask(ClassbookTask mTask) {
        this.mTask = mTask;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getTermNumber() {
        return termNumber;
    }

    public void setTermNumber(int termNumber) {
        this.termNumber = termNumber;
    }

    public String formatDate() {
        SimpleDateFormat format = new SimpleDateFormat("MMM dd");

        String start = format.format(startDate);
        String end = format.format(endDate);

        return start + " - " + end;
    }

    public String getLetterGrade() {
        return gradeLetter;
    }

    public ArrayList<SectionTask> getWeekTasks() {
        int term1[] = {210, 220, 230, 240, 250};
        int term2[] = {270, 280, 290, 300, 310};
        int term3[] = {330, 340, 350, 360, 370};

        int term[] = {};

        switch (termNumber) {
            case 1:
                term = term1;
                break;
            case 2:
                term = term2;
                break;
            case 3:
                term = term3;
                break;
        }

        ArrayList<SectionTask> tasks = new ArrayList<>();

        for (SectionTask s : mTasks) {
            for (int i : term) {
                if (s.getSeq() == i) {
                    tasks.add(s);
                    break;
                }
            }
        }

        return tasks;
    }
}
