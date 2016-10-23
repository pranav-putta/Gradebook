package net.codealizer.thegradebook.ui.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Pair;

import net.codealizer.thegradebook.apis.ic.classbook.ClassbookActivity;
import net.codealizer.thegradebook.ui.classbook.fragments.SimpleFragment;

import java.util.ArrayList;

/**
 * Created by Pranav on 10/16/16.
 */

public class UpdatesPagerAdapter extends FragmentStatePagerAdapter
{

	ArrayList<Pair<String, ClassbookActivity>> activities;

	public UpdatesPagerAdapter(FragmentManager fm, ArrayList<Pair<String, ClassbookActivity>> activities)
	{
		super(fm);

		this.activities = activities;
	}

	@Override
	public Fragment getItem(int position)
	{
		SimpleFragment fragment = new SimpleFragment();

		Bundle arguments = new Bundle();
		arguments.putSerializable(SimpleFragment.KEY_TASK, activities.get(position).second);
		arguments.putString(SimpleFragment.KEY_TASK_CLASS, activities.get(position).first);

		fragment.setArguments(arguments);

		return fragment;
	}

	@Override
	public int getCount()
	{
		return activities.size();
	}
}