package net.codealizer.thegradebook.ui.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import net.codealizer.thegradebook.apis.ic.classbook.Course;
import net.codealizer.thegradebook.apis.ic.classbook.PortalClassbook;
import net.codealizer.thegradebook.ui.classbook.CourseDetailsActivity;
import net.codealizer.thegradebook.ui.classbook.fragments.CourseDetailSemesterFragment;

/**
 * Created by Pranav on 10/10/16.
 */

public class CourseDetailsPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    private PortalClassbook mClassbook;
    private Course mCourse;
    private int mPosition;

    public CourseDetailsPagerAdapter(FragmentManager fm, int mNumOfTabs,
                                     PortalClassbook classbook, Course c, int position) {
        super(fm);

        this.mNumOfTabs = mNumOfTabs;
        this.mClassbook = classbook;
        this.mCourse = c;
        this.mPosition = position;

    }

    @Override
    public Fragment getItem(int position) {
        CourseDetailSemesterFragment fragment = new CourseDetailSemesterFragment();

        Bundle args = new Bundle();
        args.putSerializable(CourseDetailsActivity.KEY_COURSE, mCourse);
        args.putInt(CourseDetailsActivity.KEY_COURSE_POSITION, mPosition);
        args.putInt(CourseDetailsActivity.KEY_COURSE_SEMESTER, position);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mClassbook.getSemesters().get(position).toString();
    }
}
