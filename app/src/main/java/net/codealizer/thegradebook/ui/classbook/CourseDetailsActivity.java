package net.codealizer.thegradebook.ui.classbook;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.apis.ic.classbook.CourseDetail;
import net.codealizer.thegradebook.apis.ic.classbook.PortalClassbook;
import net.codealizer.thegradebook.apis.ic.schedule.Term;
import net.codealizer.thegradebook.data.SessionManager;
import net.codealizer.thegradebook.ui.adapters.CourseDetailsPagerAdapter;
import net.codealizer.thegradebook.ui.dialogs.Alert;

import java.util.ArrayList;

public class CourseDetailsActivity extends AppCompatActivity {
    public static final String KEY_COURSE = "net.codealizer.thegradebook.ui.gradebook.CourseDetailsActivity.KEY_COURSE";
    public static final String KEY_COURSE_POSITION = "net.codealizer.thegradebook.ui.gradebook.CourseDetailsActivity.KEY_COURSE_POSITION";
    public static final String KEY_COURSE_SEMESTER = "net.codealizer.thegradebook.ui.gradebook.CourseDetailsActivity.KEY_COURSE_SEMESTER";
    ActionBar actionBar;
    ViewPager viewPager;
    CourseDetailsPagerAdapter adapter;
    private CourseDetail mCourse;
    private int mPosition;
    private PortalClassbook mClassbook;

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
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                super.onBackPressed();
                finish();
                break;
            case R.id.action_calculate_ebr:
                if (mClassbook.isEBR())
                    Alert.showEBRGradeDialog(mClassbook.getClassbook().getTasks((viewPager.getCurrentItem() + 1)), mClassbook, this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mClassbook.isEBR())
            getMenuInflater().inflate(R.menu.menu_course_details, menu);
        return true;

    }

    private void initialize() {
        //Retrieve SessionManager
        mCourse = (CourseDetail) getIntent().getSerializableExtra(KEY_COURSE);
        mPosition = getIntent().getIntExtra(KEY_COURSE_POSITION, 0);
        mClassbook = SessionManager.mCoreManager.getGradebook(mCourse);
        actionBar = getSupportActionBar();

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(mCourse.getName());

        //Setup Tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.gradebook_tabs);

        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new CourseDetailsPagerAdapter(getSupportFragmentManager(), mClassbook.getCalendar().getTermNames().size(), mClassbook, mCourse, mPosition);
        viewPager.setAdapter(adapter);

        ArrayList<Term> terms = mClassbook.getCalendar().getTerms();

        int position = 0;

        for (int i = 0; i < terms.size(); i++) {
            Term t = terms.get(i);

            long d1 = t.getEndDate().getTime();
            long d2 = t.getStartDate().getTime();

            long now = System.currentTimeMillis();

            if (now > d2 && now < d1) {
                position = i;
                break;
            }
        }

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(position);


    }

}
