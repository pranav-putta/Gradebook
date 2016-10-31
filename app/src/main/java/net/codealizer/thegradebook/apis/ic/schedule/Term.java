package net.codealizer.thegradebook.apis.ic.schedule;

import org.w3c.dom.Element;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pranav on 10/10/16.
 */

public class Term implements Serializable {


    private Date startDate;
    private Date endDate;
    private int termID;
    private int termScheduleID;
    private double gradePercentage;
    private String gradeLetter;
    private String name;
    private boolean current;
    private TermType type;

    public Term(Element term) {
        termID = Integer.parseInt(term.getAttribute("termID"));
        termScheduleID = Integer.parseInt(term.getAttribute("termScheduleID"));
        name = term.getAttribute("name");

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        try {
            startDate = format.parse(term.getAttribute("startDate"));
            endDate = format.parse(term.getAttribute("endDate"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (term.hasAttribute("current")) {
            current = true;
        } else {
            current = false;
        }

        if (name.contains("week")) {
            type = TermType.WEEK;
        } else if (name.contains("term")) {
            type = TermType.TERM;
        } else if (name.contains("exam")) {
            type = TermType.FINAL_EXAM;
        } else {
            type = TermType.DEFAULT;
        }
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getTermID() {
        return termID;
    }

    public int getTermScheduleID() {
        return termScheduleID;
    }

    public double getGradePercentage() {
        return gradePercentage;
    }

    public String getGradeLetter() {
        return gradeLetter;
    }

    public String getName() {
        return name;
    }

    public boolean isCurrent() {
        return current;
    }

    public TermType getType() {
        return type;
    }

    public enum TermType {
        WEEK, TERM, FINAL_EXAM, DEFAULT
    }
}
