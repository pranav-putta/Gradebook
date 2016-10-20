package net.codealizer.thegradebook.ui.gradebook.cards;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by Pranav on 10/10/16.
 */

public class BasicTermDetail {

    private String title;
    private String score;
    private String comments;
    private String grade;
    private double percent;
    private String date;

    public BasicTermDetail(String title, String score, String comments, String grade, double percent, String date) {
        this.title = title;
        this.score = score;
        this.comments = comments;
        this.grade = grade;
        this.percent = percent;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getHeaders() {
        return Arrays.asList("Score", "Comments", "Date");
    }

    public List<String> getValues() {
        String score = getScore();
        if (!score.equals("N/A") && !score.equals("IP") && !score.equals("AG")) {
            score = score + " (" + getPercent() + "%)";
        }

        return Arrays.asList(score, getComments(), getDate());
    }
}
