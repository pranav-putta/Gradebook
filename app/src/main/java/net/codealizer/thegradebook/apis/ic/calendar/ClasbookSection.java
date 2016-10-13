package net.codealizer.thegradebook.apis.ic.calendar;

import net.codealizer.thegradebook.apis.ic.classbook.ClassbookGroup;

import java.util.ArrayList;

/**
 * Created by Pranav on 10/10/16.
 */

public class ClasbookSection {

    private int taskID;
    private String name;
    private int weight;
    private boolean isWeighted;
    private boolean hasValidWeightedGroup;
    private boolean hasValidWeightedTask;
    private boolean isLocked;
    private boolean gradeBookPosted;
    private int taskSeq;
    private boolean standard;
    private boolean hasGradingTaskCalculation;
    private int termID;
    private String termName;
    private int termSeq;
    private int totalPointsPossible;
    private int pointsEarned;
    private double percentage;
    private long scoreID;
    private boolean groupWeighted;
    private int curveID;
    private boolean usePercent;
    private long personID;
    private String calcMethod;

    ArrayList<ClassbookGroup> groups;

}
