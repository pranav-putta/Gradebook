package net.codealizer.thegradebook.assets;

/**
 * Created by Pranav on 10/12/16.
 */

public class BasicClassbookActivity {

    private String title;
    private int points;
    private int totalPoints;
    private int groupID;

    public BasicClassbookActivity(String title, int points, int totalPoints, int groupID) {
        this.title = title;
        this.points = points;
        this.totalPoints = totalPoints;
        this.groupID = groupID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }
}
