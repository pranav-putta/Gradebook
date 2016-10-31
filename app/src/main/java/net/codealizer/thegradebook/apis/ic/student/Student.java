package net.codealizer.thegradebook.apis.ic.student;

import com.google.gson.Gson;

import net.codealizer.thegradebook.apis.ic.district.DistrictInfo;
import net.codealizer.thegradebook.apis.ic.calendar.Calendar;
import net.codealizer.thegradebook.apis.ic.classbook.Classbook;
import net.codealizer.thegradebook.apis.ic.classbook.GradingDetailSummary;

import org.w3c.dom.Element;

import java.util.ArrayList;

public class Student {
    public String studentNumber;
    public boolean hasSecurityRole = false;
    public String personID;
    public String lastName;
    public String firstName;
    public String middleName;
    public String isGuardian;

    public String mJson;

    public ArrayList<Calendar> calendars = new ArrayList<Calendar>();
    public GradingDetailSummary gradeDetailSummary;
    public ArrayList<Classbook> classbooks = new ArrayList<Classbook>();

    private DistrictInfo distInfo;

    public Student(Element userElement, String json) {
        this(userElement, null, json);
    }

    public Student(Element userElement, DistrictInfo info, String json) {
        distInfo = info;
        mJson = json;

        if (userElement.hasAttribute("studentNumber"))
            studentNumber = userElement.getAttribute("studentNumber");
        if (userElement.hasAttribute("personID"))
            personID = userElement.getAttribute("personID");
        if (userElement.hasAttribute("lastName"))
            lastName = userElement.getAttribute("lastName");
        if (userElement.hasAttribute("firstName"))
            firstName = userElement.getAttribute("firstName");
        if (userElement.hasAttribute("middleName"))
            middleName = userElement.getAttribute("middleName");
        if (userElement.hasAttribute("isGuardian"))
            isGuardian = userElement.getAttribute("isGuardian");

        for (int i = 0; i < userElement.getElementsByTagName("Calendar").getLength(); i++)
            calendars.add(new Calendar((Element) userElement.getElementsByTagName("Calendar").item(i)));

    }


    public String getPictureURL() {
        return distInfo.getDistrictBaseURL() + "personPicture.jsp?personID=" + personID;
    }


    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    //TODO: Load news items
    public String getInfoString() {
        String userInfo = "Information for " + firstName + " " + middleName + " " + lastName + ":\nStudent Number: " + studentNumber + "\nPerson ID: " + personID + "\nPicture URL: " + getPictureURL() + "\nIs Guardian? " + isGuardian + "\n\n===Calendars===";

        for (Calendar c : calendars) {
        }

        return userInfo;
    }
}