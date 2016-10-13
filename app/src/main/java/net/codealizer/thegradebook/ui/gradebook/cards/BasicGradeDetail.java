package net.codealizer.thegradebook.ui.gradebook.cards;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Pranav on 10/10/16.
 */

public class BasicGradeDetail {

    private String name;
    private String dueDate;
    private String score;
    private String percent;
    private String multiplier;
    private String comments;
    private  boolean isEBR;

    public BasicGradeDetail(String name, String dueDate, String score, String percent, String multiplier, String comments, boolean ebr) {
        this.name = name;
        this.dueDate = dueDate;
        this.score = score;
        this.percent = percent ;
        this.multiplier = multiplier + "x";
        this.comments = comments;
        this.isEBR = ebr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(String multiplier) {
        this.multiplier = multiplier + "x";
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isEBR() {
        return isEBR;
    }

    public void setEBR(boolean EBR) {
        isEBR = EBR;
    }

    public List<String> getHeaders() {
        return Arrays.asList("Score", "Due Date", "Multiplier", "Comments");
    }

    public List<String> getValues() {
        String score = getScore();
        if (!score.equals("N/A") && !score.equals("IP") && !score.equals("AG") ) {
            if (!isEBR) {
                score = score + " (" + getPercent() + "%)";
            }
        }

        return Arrays.asList(score, getDueDate(), getMultiplier(), getComments());
    }
}
