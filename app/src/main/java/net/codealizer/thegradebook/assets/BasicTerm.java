package net.codealizer.thegradebook.assets;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;


import net.codealizer.thegradebook.apis.ic.classbook.ClassbookTask;

import java.util.List;

/**
 * Created by Pranav on 10/10/16.
 */

public class BasicTerm implements ParentListItem
{

	private List mChildrenList;

	private String title;
	private String dates;
	private String grade;

	private ClassbookTask mTask;

	public BasicTerm(String title, String dates, String grade, List mChildrenList, ClassbookTask t)
	{
		this.title = title;
		this.dates = dates;
		this.grade = grade;
		this.mChildrenList = mChildrenList;
		this.mTask = t;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDates()
	{
		return dates;
	}

	public void setDates(String dates)
	{
		this.dates = dates;
	}

	public String getGrade()
	{
		return grade;
	}

	public void setGrade(String grade)
	{
		this.grade = grade;
	}


	@Override
	public List<?> getChildItemList()
	{
		return mChildrenList;
	}

	public ClassbookTask getTask() {
		return mTask;
	}

	public void setmTask(ClassbookTask mTask) {
		this.mTask = mTask;
	}

	@Override
	public boolean isInitiallyExpanded()
	{
		return false;
	}
}
