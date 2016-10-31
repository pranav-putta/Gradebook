package net.codealizer.thegradebook.apis.ic.classbook;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.Serializable;
import java.util.ArrayList;

public class ClassbookGroup implements Serializable {
    public String activityID;
    public String name;
    public float weight;
    public int seq;
    public boolean notGraded = false;
    //hidePortal
    private boolean hasValidScore;
    private boolean composite;
    private boolean calcEclude;
    public int termID;
    public int taskID;
    public float percentage;
    public String formattedPercentage;
    public String letterGrade;
    public float pointsEarned;
    public float totalPointsPossible;
    public boolean ebr;

    public ArrayList<ClassbookActivity> activities = new ArrayList<ClassbookActivity>();

    public ClassbookGroup(Element group) {
        activityID = group.getAttribute("activityID");
        name = group.getAttribute("name");
        weight = Float.parseFloat(group.getAttribute("weight"));
        seq = Integer.parseInt(group.getAttribute("seq"));
        notGraded = group.getAttribute("notGraded").equalsIgnoreCase("true");
        hasValidScore = Boolean.valueOf(group.getAttribute("hasValidScore"));
        composite = Boolean.valueOf(group.getAttribute("composite"));
        calcEclude = Boolean.valueOf(group.getAttribute("calcExclude"));
        termID = Integer.parseInt(group.getAttribute("termID"));
        taskID = Integer.parseInt(group.getAttribute("taskID"));
        percentage = Float.parseFloat(group.getAttribute("percentage"));
        ebr = false;
        if (group.hasAttribute("formattedPercentage") && group.hasAttribute("letterGrade")) {
            formattedPercentage = group.getAttribute("formattedPercentage");
            letterGrade = group.getAttribute("letterGrade");
        }

        pointsEarned = Float.parseFloat(group.getAttribute("pointsEarned"));
        totalPointsPossible = Float.parseFloat(group.getAttribute("totalPointsPossible"));

        NodeList list = group.getElementsByTagName("ClassbookActivity");

        for (int i = 0; i < list.getLength(); i++) {
            Element element = (Element) list.item(i);
            activities.add(new ClassbookActivity(element));
        }

        letterGrade = (letterGrade == null ? "?" : letterGrade);
        formattedPercentage = (formattedPercentage == null ? "?" : formattedPercentage);
    }


}
