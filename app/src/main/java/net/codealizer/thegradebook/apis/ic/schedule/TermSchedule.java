package net.codealizer.thegradebook.apis.ic.schedule;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Pranav on 10/23/16.
 */

public class TermSchedule implements Serializable {

    private int termScheduleID;
    private int structureID;
    private String name;
    private boolean primary;

    private ArrayList<Term> terms;

    public TermSchedule(Element termSchedule) {
        terms = new ArrayList<>();
        termScheduleID = Integer.parseInt(termSchedule.getAttribute("termScheduleID"));
        structureID = Integer.parseInt(termSchedule.getAttribute("structureID"));
        name = termSchedule.getAttribute("name");
        primary = Boolean.valueOf(termSchedule.getAttribute("primary"));

        NodeList list = termSchedule.getElementsByTagName("Term");

        for (int i = 0; i < list.getLength(); i++) {
            Element element = (Element) list.item(i);
            terms.add(new Term(element));
        }
    }

    public int getTermScheduleID() {
        return termScheduleID;
    }

    public int getStructureID() {
        return structureID;
    }

    public String getName() {
        return name;
    }

    public boolean isPrimary() {
        return primary;
    }

    public ArrayList<Term> getTerms() {
        return terms;
    }
}
