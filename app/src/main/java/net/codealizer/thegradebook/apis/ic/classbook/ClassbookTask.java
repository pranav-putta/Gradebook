package net.codealizer.thegradebook.apis.ic.classbook;

import android.util.Pair;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ClassbookTask implements Serializable {
    private final String personID;
    private String score;
    private final String scorePercentage;
    private final String scorePercentageDisplay;
    private final String scoreDate;
    private String scoreComment;
    private final Boolean groupWeighted;
    private final String calcMethod;
    public String taskID;
    public String name;
    public float weight;
    public boolean isWeighted = false;
    private boolean hasValidGroup;
    private boolean hasValidWeightedGroup;
    private boolean hasValidWeightedTask;
    private boolean locked;
    private boolean hasGradingTaskCalculation;
    private boolean standard;
    public boolean gradeBookPosted = false;
    public int taskSeq;
    public int termID;
    public String termName;
    public int termSeq;
    public float totalPointsPossible;
    public float pointsEarned;
    public float percentage;
    public String letterGrade;
    public String formattedPercentage;
    public TaskType type;
    public int curveID;

    public ArrayList<ClassbookGroup> groups = new ArrayList<ClassbookGroup>();


    public ClassbookTask(Element task) {

        name = task.getAttribute("name");
        taskID = task.getAttribute("taskID");
        weight = Float.parseFloat(task.getAttribute("weight"));
        isWeighted = Boolean.valueOf(task.getAttribute("isWeighted"));
        hasValidGroup = Boolean.valueOf(task.getAttribute("hasValidGroup"));
        hasValidWeightedGroup = Boolean.valueOf(task.getAttribute("hasValidWeightedGroup"));
        hasValidWeightedTask = Boolean.valueOf(task.getAttribute("hasValidWeightedTask"));
        locked = Boolean.valueOf(task.getAttribute("locked"));
        gradeBookPosted = Boolean.valueOf(task.getAttribute("gradeBookPosted"));
        taskSeq = Integer.parseInt(task.getAttribute("taskSeq"));
        standard = Boolean.valueOf(task.getAttribute("standard"));
        hasGradingTaskCalculation = Boolean.valueOf(task.getAttribute("hasGradingTaskCalculation"));
        termID = Integer.parseInt(task.getAttribute("termID"));
        termName = task.getAttribute("termName");
        termSeq = Integer.parseInt(task.getAttribute("termSeq"));
        totalPointsPossible = Float.parseFloat(task.getAttribute("totalPointsPossible"));
        pointsEarned = Float.parseFloat(task.getAttribute("pointsEarned"));
        percentage = Float.parseFloat(task.getAttribute("percentage"));
        score = task.getAttribute("score");
        scorePercentage = task.getAttribute("scorePercentage");
        scorePercentageDisplay = task.getAttribute("scorePercentageDisplay");
        scoreDate = task.getAttribute("scoreDate");
        groupWeighted = Boolean.valueOf(task.getAttribute("groupWeighted"));
        calcMethod = task.getAttribute("calcMethod");
        personID = task.getAttribute("personID");
        scoreComment = task.getAttribute("scoreComments");

        if (task.hasAttribute("letterGrade") && task.hasAttribute("formattedPercentage")) {
            letterGrade = task.getAttribute("letterGrade");
            formattedPercentage = task.getAttribute("formattedPercentage");
        }

        NodeList list = task.getElementsByTagName("ClassbookGroup");

        for (int i = 0; i < list.getLength(); i++) {
            Element element = (Element) list.item(i);
            groups.add(new ClassbookGroup(element));

        }

        letterGrade = (letterGrade == null ? "-" : letterGrade);
        formattedPercentage = (formattedPercentage == null ? "?" : formattedPercentage);
        scoreComment = (scoreComment == null ? "No comments" : scoreComment);
        scoreComment = (scoreComment.isEmpty() ? "No comments" : scoreComment);
        score = (score.isEmpty() ? "-" : score);

        if (name.toLowerCase().contains("week")) {
            type = TaskType.WEEK;
        } else if (name.toLowerCase().contains("term")) {
            type = TaskType.TERM;
        } else if (name.toLowerCase().contains("exam")) {
            type = TaskType.FINAL_EXAM;
        } else {
            type = TaskType.DEFAULT;
        }

    }

    public String getPersonID() {
        return personID;
    }

    public String getScore() {
        return score;
    }

    public String getScorePercentage() {
        return scorePercentage;
    }

    public String getScorePercentageDisplay() {
        return scorePercentageDisplay;
    }

    public String getScoreDate() {
        return scoreDate;
    }

    public Boolean getGroupWeighted() {
        return groupWeighted;
    }

    public String getCalcMethod() {
        return calcMethod;
    }

    public String getTaskID() {
        return taskID;
    }

    public String getName() {
        return name;
    }

    public float getWeight() {
        return weight;
    }

    public boolean isWeighted() {
        return isWeighted;
    }

    public boolean isHasValidGroup() {
        return hasValidGroup;
    }

    public boolean isHasValidWeightedGroup() {
        return hasValidWeightedGroup;
    }

    public boolean isHasValidWeightedTask() {
        return hasValidWeightedTask;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isHasGradingTaskCalculation() {
        return hasGradingTaskCalculation;
    }

    public boolean isStandard() {
        return standard;
    }

    public boolean isGradeBookPosted() {
        return gradeBookPosted;
    }

    public int getTaskSeq() {
        return taskSeq;
    }

    public int getTermID() {
        return termID;
    }

    public String getTermName() {
        return termName;
    }

    public int getTermSeq() {
        return termSeq;
    }

    public float getTotalPointsPossible() {
        return totalPointsPossible;
    }

    public float getPointsEarned() {
        return pointsEarned;
    }

    public float getPercentage() {
        return percentage;
    }

    public String getLetterGrade() {
        if (!letterGrade.equals("-"))
            return letterGrade;
        else if (!score.isEmpty())
            return score;
        else
            return "-";
    }

    public String getFormattedPercentage() {
        return formattedPercentage;
    }

    public TaskType getType() {
        return type;
    }

    public int getCurveID() {
        return curveID;
    }

    public ArrayList<ClassbookGroup> getGroups() {
        return groups;
    }

    public String getScoreComment() {
        return scoreComment;
    }

    public static class Compare implements Comparator<ClassbookTask> {
        @Override
        public int compare(ClassbookTask o, ClassbookTask t1) {
            Integer p1 = o.getTaskSeq();
            Integer p2 = t1.getTaskSeq();

            if (p1 % 10 != 0) {
                return 999;
            }

            return p1.compareTo(p2);
        }
    }

    public ArrayList<ClassbookActivity> getAllActivities() {
        ArrayList<ClassbookActivity> activities = new ArrayList<>();
        for (ClassbookGroup group : groups) {
            activities.addAll(group.activities);
        }

        return activities;
    }

    public ArrayList<Pair<Integer, String>> getCategories() {
        int i = 0;
        ArrayList<Pair<Integer, String>> categories = new ArrayList<>();

        for (ClassbookGroup group : groups) {
            categories.add(new Pair<>(i, group.name + " (Weight: " + group.weight + ")"));
            i += group.activities.size();

        }

        return categories;
    }

    public static ArrayList<ArrayList<Pair<Boolean, ClassbookTask>>> formatTasks(ArrayList<ClassbookTask> tasks) {

        ArrayList<ArrayList<Pair<Boolean, ClassbookTask>>> terms = new ArrayList<>();

        Collections.sort(tasks, new ClassbookTask.Compare());

        for (int i = 0; i < tasks.size(); ) {
            if (tasks.get(i).getType() == TaskType.TERM) {
                ArrayList<Pair<Boolean, ClassbookTask>> classbooks = new ArrayList<>();

                classbooks.add(new Pair<Boolean, ClassbookTask>(true, tasks.get(i)));

                boolean cont = true;
                int j = i;
                while (cont) {
                    j--;
                    if (!(j < 0) && tasks.get(j).getType() == TaskType.WEEK) {
                        classbooks.add(new Pair<>(false, tasks.get(j)));
                    } else {
                        cont = false;
                    }
                }

                i++;

                terms.add(classbooks);
            } else if (tasks.get(i).getType() == TaskType.FINAL_EXAM || tasks.get(i).getType() == TaskType.DEFAULT) {
                ArrayList<Pair<Boolean, ClassbookTask>> t = new ArrayList<>();
                if (tasks.get(i).getAllActivities().size() > 0)
                    t.add(new Pair<>(true, tasks.get(i)));
                else
                    t.add(new Pair<>(false, tasks.get(i)));

                i++;

                terms.add(t);
            } else {
                i++;
            }
        }
        return terms;
    }

    public boolean doWeightsAddUp() {
        float totalWeight = 0;
        for (ClassbookGroup group : groups) {
            totalWeight += group.weight;
        }

        return totalWeight == 1f;
    }

    public static enum TaskType {
        WEEK, TERM, FINAL_EXAM, DEFAULT
    }
}
