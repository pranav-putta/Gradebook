package net.codealizer.thegradebook.apis.ic.classbook;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;


public class GradingDetailSummary implements Serializable {

    private static int nextNonClassPeriod = 300;

    private ArrayList<Task> tasks;

    public GradingDetailSummary(Element grades) {
        tasks = new ArrayList<>();

        if (grades != null) {
            NodeList list = grades.getElementsByTagName("Task");


            for (int i = 0; i < list.getLength(); i++) {
                Element element = (Element) list.item(i);
                tasks.add(new Task(element));
            }
        }
    }

    int getPeriodNumber() {

        int periodNumber = nextNonClassPeriod++;

        if (tasks.size() > 0) {
            try
            {
                periodNumber = Integer.parseInt(tasks.get(0).score.periodName.replaceAll("\\D+", ""));
            }
            catch (Exception ex) {
                //periodNumber will be next in sequence
            }
        }
        return periodNumber;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    private class Task implements Serializable {

        private int taskID;
        private String name;
        private int seq;
        private int termMask;
        private int activeMask;
        private int standardID;
        private boolean stateStandard;

        private Score score;

        public Task(Element element) {
            taskID = Integer.parseInt(element.getAttribute("taskID"));
            name = element.getAttribute("name");
            seq = Integer.parseInt(element.getAttribute("seq"));
            termMask = Integer.parseInt(element.getAttribute("termMask"));
            activeMask = Integer.parseInt(element.getAttribute("activeMask"));
            standardID = Integer.parseInt(element.getAttribute("standardID"));
            stateStandard = Boolean.valueOf(element.getAttribute("stateStandard"));

            NodeList list = element.getElementsByTagName("Score");
            if (list.getLength() > 0) {
                Element e = (Element) list.item(0);
                score = new Score(e);
            }
        }

        public int getTaskID() {
            return taskID;
        }

        public String getName() {
            return name;
        }

        public int getSeq() {
            return seq;
        }

        public int getTermMask() {
            return termMask;
        }

        public int getActiveMask() {
            return activeMask;
        }

        public int getStandardID() {
            return standardID;
        }

        public boolean isStateStandard() {
            return stateStandard;
        }

        public Score getScore() {
            return score;
        }
    }

    private class Score implements Serializable {

        private long scoreID;
        private String schedule;
        private String periodName;
        private int periodSeq;
        private String termName;
        private int termSeq;
        private int termID;
        private String score;
        private double percent;
        private Date date;

        public Score(Element e) {
            if (e.hasAttribute("scoreID"))
                scoreID = Long.parseLong(e.getAttribute("scoreID"));
            if (e.hasAttribute("schedule"))
                schedule = e.getAttribute("schedule");
            if (e.hasAttribute("periodName"))
                periodName = e.getAttribute("periodName");
            if (e.hasAttribute("periodSeq"))
                periodSeq = Integer.parseInt(e.getAttribute("periodSeq"));
            if (e.hasAttribute("termName"))
                termName = e.getAttribute("termName");
            if (e.hasAttribute("termSeq"))
                termSeq = Integer.parseInt(e.getAttribute("termSeq"));
            if (e.hasAttribute("termID"))
                termID = Integer.parseInt(e.getAttribute("termID"));
            if (e.hasAttribute("score"))
                score = e.getAttribute("score");
            if (e.hasAttribute("percent"))
                percent = Double.parseDouble(e.getAttribute("percent"));

        }

        public long getScoreID() {
            return scoreID;
        }

        public String getSchedule() {
            return schedule;
        }

        public String getPeriodName() {
            return periodName;
        }

        public int getPeriodSeq() {
            return periodSeq;
        }

        public String getTermName() {
            return termName;
        }

        public int getTermSeq() {
            return termSeq;
        }

        public int getTermID() {
            return termID;
        }

        public String getScore() {
            return score;
        }

        public double getPercent() {
            return percent;
        }

        public Date getDate() {
            return date;
        }
    }
}
