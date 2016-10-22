package net.codealizer.thegradebook.apis.ic.schedule;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.codealizer.thegradebook.apis.ic.schedule.Term;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Pranav on 10/10/16.
 */

public class Semester {

    private Date startDate;
    private Date endDate;

    private int semesterNumber;

    private ArrayList<Term> terms;

    public Semester(Date startDate, Date endDate, int semesterNumber, JsonObject grades, JsonObject g) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.semesterNumber = semesterNumber;

        //Calculate the term dates
        terms = new ArrayList<>(3);

        try {
            JsonArray array = g.getAsJsonObject("tasks").getAsJsonArray("ClassbookTask");
            int s = 0;
            for (int i = 1; i <= 3; i++) {

                if (i == 1) {
                    s = 260;
                } else if (i == 2) {
                    s = 320;
                } else if (i == 3) {
                    s = 380;
                }

                JsonObject object = null;

                for (JsonElement j : array) {
                    if (j.getAsJsonObject().get("taskID").getAsInt() < 1200 && j.getAsJsonObject().get("taskSeq").getAsInt() == s) {
                        if (j.getAsJsonObject().has("calcMethod") && !j.getAsJsonObject().get("calcMethod").getAsString().equals("no")) {
                            object = j.getAsJsonObject();
                            break;
                        }
                    }
                }

                if (object != null) {
                    terms.add(new Term(startDate, i, grades, object, false));
                } else {
                    terms.add(new Term(startDate, i, grades, array));
                }
            }
        } catch (Exception ex) {
            if (g.toString().contains("CONCERT")) {
                Log.e("BIG ERROR", "lol");
                ex.printStackTrace();
            }
            for (int i = 1; i <= 3; i++) {
                terms.add(new Term(startDate, i, grades));
            }

            Log.e("NIGGER", "ERROR");
            ex.printStackTrace();
        }
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

    public ArrayList<Term> getTerms() {
        return terms;
    }

    public void setTerms(ArrayList<Term> terms) {
        this.terms = terms;
    }

    public int getSemesterNumber() {
        return semesterNumber;
    }

    public void setSemesterNumber(int semesterNumber) {
        this.semesterNumber = semesterNumber;
    }

    @Override
    public String toString() {
        return "Semester " + semesterNumber;
    }
}
