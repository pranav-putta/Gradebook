package net.codealizer.thegradebook.ui.gradebook;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.apis.ic.classbook.Course;
import net.codealizer.thegradebook.apis.ic.classbook.PortalClassbook;
import net.codealizer.thegradebook.data.Data;
import net.codealizer.thegradebook.ui.gradebook.adapters.CourseDetailsPagerAdapter;

public class CourseDetailsActivity extends AppCompatActivity {
    ActionBar actionBar;

    ViewPager viewPager;
    CourseDetailsPagerAdapter adapter;

    private Course mCourse;
    private int mPosition;
    private PortalClassbook mClassbook;

    public static final String KEY_COURSE = "net.codealizer.thegradebook.ui.gradebook.CourseDetailsActivity.KEY_COURSE";
    public static final String KEY_COURSE_POSITION = "net.codealizer.thegradebook.ui.gradebook.CourseDetailsActivity.KEY_COURSE_POSITION";
    public static final String KEY_COURSE_SEMESTER = "net.codealizer.thegradebook.ui.gradebook.CourseDetailsActivity.KEY_COURSE_SEMESTER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();

        initialize();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initialize() {
        //Retrieve Data
        mCourse = (Course) getIntent().getSerializableExtra(KEY_COURSE);
        mPosition = getIntent().getIntExtra(KEY_COURSE_POSITION, 0);
        mClassbook = Data.mCoreManager.getGradebook(mCourse);
        actionBar = getSupportActionBar();

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(mCourse.getCourseName());

        //Setup Tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.gradebook_tabs);

        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new CourseDetailsPagerAdapter(getSupportFragmentManager(), mClassbook.getSemesters().size(), mClassbook, mCourse, mPosition);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);


    }

}
