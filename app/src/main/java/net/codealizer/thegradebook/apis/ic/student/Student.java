package net.codealizer.thegradebook.apis.ic.student;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import net.codealizer.thegradebook.apis.ic.calendar.Calendar;
import net.codealizer.thegradebook.apis.ic.classbook.Classbook;
import net.codealizer.thegradebook.apis.ic.classbook.GradingDetailSummary;
import net.codealizer.thegradebook.apis.ic.district.DistrictInfo;

import java.util.ArrayList;

public class Student
{
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

	public Student(JsonObject userElement, String json)
	{
		this(userElement, null, json);
	}

	public Student(JsonObject userElement, DistrictInfo info, String json)
	{
		distInfo = info;
		mJson = json;

		if (userElement.has("studentNumber"))
			studentNumber = userElement.get("studentNumber").getAsString();
		if (userElement.has("personID"))
			personID = userElement.get("personID").getAsString();
		if (userElement.has("lastName"))
			lastName = userElement.get("lastName").getAsString();
		if (userElement.has("firstName"))
			firstName = userElement.get("firstName").getAsString();
		if (userElement.has("middleName"))
			middleName = userElement.get("middleName").getAsString();
		if (userElement.has("isGuardian"))
			isGuardian = userElement.get("isGuardian").getAsString();

		if (userElement.get("Calendar").isJsonArray())
		{
			for (int i = 0; i < userElement.get("Calendar").getAsJsonArray().size(); i++)
				calendars.add(new Calendar(userElement.get("Calendar").getAsJsonArray().get(i).getAsJsonObject()));
		} else
		{
			calendars.add(new Calendar(userElement.get("Calendar").getAsJsonObject()));
		}
	}


	public String getPictureURL()
	{
		return distInfo.getDistrictBaseURL() + "personPicture.jsp?personID=" + personID;
	}


	public String toJson()
	{
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	//TODO: Load news items
	public String getInfoString()
	{
		String userInfo = "Information for " + firstName + " " + middleName + " " + lastName + ":\nStudent Number: " + studentNumber + "\nPerson ID: " + personID + "\nPicture URL: " + getPictureURL() + "\nIs Guardian? " + isGuardian + "\n\n===Calendars===";

		for (Calendar c : calendars)
		{
			userInfo += "\n" + c.getInfoString();
		}

		return userInfo;
	}
}