package net.codealizer.thegradebook.ui.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import net.codealizer.thegradebook.apis.ic.StudentNotification;
import net.codealizer.thegradebook.ui.classbook.fragments.SimpleFragment;

import java.util.ArrayList;

/**
 * Created by Pranav on 10/16/16.
 */

public class UpdatesPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<StudentNotification> activities;

    public UpdatesPagerAdapter(FragmentManager fm, ArrayList<StudentNotification> activities) {
        super(fm);

        this.activities = activities;
    }

    @Override
    public Fragment getItem(int position) {
        SimpleFragment fragment = new SimpleFragment();

        Bundle arguments = new Bundle();
        arguments.putString(SimpleFragment.KEY_TASK_CLASS, activities.get(position).getNotificationText());
        arguments.putString(SimpleFragment.KEY_DATE, activities.get(position).getDisplayDate());

        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public int getCount() {
        return activities.size();
    }
}