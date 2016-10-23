package net.codealizer.thegradebook.assets;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Pranav on 10/10/16.
 */

public class BasicGradeDetail
{

	public static LinkedHashMap<Double, String> grades;

	static
	{
		grades = new LinkedHashMap<>();
		grades.put(92.5, "A+");
		grades.put(89.5, "A");
		grades.put(86.5, "A-");
		grades.put(82.5, "B+");
		grades.put(79.5, "B");
		grades.put(76.5, "B-");
		grades.put(72.5, "C+");
		grades.put(69.5, "C");
		grades.put(66.5, "C-");
		grades.put(62.5, "D+");
		grades.put(59.5, "D");
		grades.put(56.5, "D");
		grades.put(52.5, "D-");
		grades.put(49.5, "F");
	}

	private String name;
	private String dueDate;
	private String score;
	private String percent;
	private String multiplier;
	private String comments;
	private boolean isEBR;
	private String letterGrade;

	public BasicGradeDetail(String name, String dueDate, String score, String percent, String multiplier, String comments, boolean ebr, String letterGrade)
	{
		this.name = name;
		this.dueDate = dueDate;
		this.score = score;
		this.percent = percent;
		this.multiplier = multiplier + "x";
		this.comments = comments;
		this.isEBR = ebr;
		this.letterGrade = letterGrade;
	}

	public BasicGradeDetail(String name, String dueDate, String score, String percent, String multiplier, String comments, boolean ebr)
	{
		this.name = name;
		this.dueDate = dueDate;
		this.score = score;
		this.percent = percent;
		this.multiplier = multiplier + "x";
		this.comments = comments;
		this.isEBR = ebr;
		this.letterGrade = calculateGrade();
	}

	public static String calculateGrade(String percent)
	{
		for (Map.Entry<Double, String> entry : grades.entrySet())
		{
			double limit = entry.getKey();
			if (Double.parseDouble(percent) > limit)
			{
				return entry.getValue();
			}
		}
		return "";
	}

	public String calculateGrade()
	{
		for (Map.Entry<Double, String> entry : grades.entrySet())
		{
			double limit = entry.getKey();
			if (!percent.equals("∞"))
			{
				if (Double.parseDouble(percent) > limit)
				{
					return entry.getValue();
				}
			}
		}
		return "Not Applicable";
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDueDate()
	{
		return dueDate;
	}

	public void setDueDate(String dueDate)
	{
		this.dueDate = dueDate;
	}

	public String getScore()
	{
		return score;
	}

	public void setScore(String score)
	{
		this.score = score;
	}

	public String getPercent()
	{
		return percent;
	}

	public void setPercent(String percent)
	{
		this.percent = percent;
	}

	public String getMultiplier()
	{
		return multiplier;
	}

	public void setMultiplier(String multiplier)
	{
		this.multiplier = multiplier + "x";
	}

	public String getComments()
	{
		return comments;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
	}

	public boolean isEBR()
	{
		return isEBR;
	}

	public void setEBR(boolean EBR)
	{
		isEBR = EBR;
	}

	public String getLetterGrade()
	{
		return letterGrade;
	}

	public void setLetterGrade(String letterGrade)
	{
		this.letterGrade = letterGrade;
	}

	public List<String> getHeaders()
	{
		return Arrays.asList("Score", "Letter Grade", "Due Date", "Multiplier", "Comments");
	}

	public List<String> getValues()
	{
		String score = getScore();
		if (!score.equals("N/A") && !score.equals("IP") && !score.equals("AG"))
		{
			if (!isEBR)
			{
				score = score + " (" + getPercent() + "%)";
			}
		}

		return Arrays.asList(score, getLetterGrade(), getDueDate(), getMultiplier(), getComments());
	}
}
