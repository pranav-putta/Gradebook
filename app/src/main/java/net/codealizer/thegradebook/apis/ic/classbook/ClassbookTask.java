package net.codealizer.thegradebook.apis.ic.classbook;

import android.util.Pair;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;


public class ClassbookTask implements Serializable
{
	public String taskID;
	public String name;
	public float weight;
	public boolean isWeighted = false;
	// hasValidGroup
	// hasValidWeightedGroup
	// locked
	public boolean gradeBookPosted = false;
	public int taskSeq;
	public int termID;
	public String termName;
	public int termSeq;
	public float totalPointsPossible;
	public float pointsEarned;
	public float percentage;
	public String letterGrade;
	public String formattedPercentage;
	public int curveID;

	public ArrayList<ClassbookTask> tasks = new ArrayList<ClassbookTask>();
	public ArrayList<ClassbookGroup> groups = new ArrayList<ClassbookGroup>();

	public boolean isEBR = false;

	public ClassbookTask(JsonObject task)
	{

		name = task.get("name").getAsString();
		taskID = task.get("taskID").getAsString();
		weight = Float.parseFloat(task.get("weight").getAsString());
		isWeighted = task.get("isWeighted").getAsBoolean();
		gradeBookPosted = task.get("gradeBookPosted").getAsBoolean();
		taskSeq = Integer.parseInt(task.get("taskSeq").getAsString());
		termID = Integer.parseInt(task.get("termID").getAsString());
		termName = task.get("termName").getAsString();
		termSeq = Integer.parseInt(task.get("termSeq").getAsString());
		totalPointsPossible = Float.parseFloat(task.get("totalPointsPossible").getAsString());
		pointsEarned = Float.parseFloat(task.get("pointsEarned").getAsString());
		percentage = Float.parseFloat(task.get("percentage").getAsString());

		if (task.has("letterGrade") && task.has("formattedPercentage"))
		{
			letterGrade = task.get("letterGrade").getAsString();
			formattedPercentage = task.get("formattedPercentage").getAsString();
		}
		try
		{
			JsonElement classbook = task.getAsJsonObject("groups").get("ClassbookGroup");
			if (classbook.isJsonArray())
			{
				for (JsonElement e : classbook.getAsJsonArray())
				{
					groups.add(new ClassbookGroup(e.getAsJsonObject()));
				}
			} else
			{
				groups.add(new ClassbookGroup(classbook.getAsJsonObject()));
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		try
		{
			curveID = Integer.parseInt(task.get("curveID").getAsString());

			for (int i = 0; i < task.get("tasks").getAsJsonObject().get("ClassbookTask").getAsJsonArray().size(); i++)
				tasks.add(new ClassbookTask(task.get("tasks").getAsJsonObject().get("ClassbookTask").getAsJsonArray().get(i).getAsJsonObject()));
		} catch (Exception e)
		{
		}

		letterGrade = (letterGrade == null ? "?" : letterGrade);
		formattedPercentage = (formattedPercentage == null ? "?" : formattedPercentage);

		if (groups.size() > 0)
		{
			isEBR = groups.get(0).ebr;
		}
	}

	public String getInfoString()
	{
		String str = "Task: " + name + ", " + termName + " " + letterGrade + " " + formattedPercentage + "%";
		for (ClassbookTask t : tasks)
			str += "\n\t" + t.getInfoString().replace("\n", "\n\t");
		for (ClassbookGroup b : groups)
			str += "\n\t" + b.getInfoString().replace("\n", "\n\t");
		return str;
	}

	public ArrayList<ClassbookActivity> getAllActivities()
	{
		ArrayList<ClassbookActivity> activities = new ArrayList<>();

		for (ClassbookGroup group : groups)
		{
			activities.addAll(group.activities);
		}

		return activities;
	}

	public ArrayList<Pair<Integer, String>> getCategories()
	{
		int i = 0;
		ArrayList<Pair<Integer, String>> categories = new ArrayList<>();

		for (ClassbookGroup group : groups)
		{
			categories.add(new Pair<>(i, group.name + " (Weight: " + group.weight + ")"));
			i += group.activities.size();

		}

		return categories;
	}
}
