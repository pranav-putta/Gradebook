package net.codealizer.thegradebook.apis.ic.calendar;

import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ScheduleStructure
{
	public String id;
	public String name;
	public String label;
	public String grade;
	public boolean active;
	public String primary;
	public boolean is_default;
	public Date startDate;

	public ScheduleStructure(JsonObject sceduleElement)
	{
		id = sceduleElement.get("structureID").getAsString();
		name = sceduleElement.get("structureName").getAsString();
		label = sceduleElement.get("label").getAsString();
		grade = sceduleElement.get("grade").getAsString();
		active = sceduleElement.get("active").getAsBoolean();
		primary = sceduleElement.get("primary").getAsString();

		try
		{
			is_default = sceduleElement.get("default").getAsBoolean();
		} catch (NullPointerException ex)
		{
			is_default = false;
		}

		try
		{
			startDate = new SimpleDateFormat("MM/dd/yy", Locale.ENGLISH)
					.parse(sceduleElement.get("startDate").getAsString());

		} catch (Exception e)
		{
			startDate = new Date();

		}
	}

	public String getInfoString()
	{
		return "Information for Schedule \'" + name + "\' titled \'" + label + "\':\nGrade: " + grade + "\nID: " + id + "\nIs Active? " + active + "\nPrimary: " + primary + "\nIs Default? " + is_default + "\nEnding Date: " + startDate.toString();
	}
}
