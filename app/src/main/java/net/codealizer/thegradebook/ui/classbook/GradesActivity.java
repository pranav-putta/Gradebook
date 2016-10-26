package net.codealizer.thegradebook.ui.classbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.apis.ic.xml.classbook.ClassbookActivity;
import net.codealizer.thegradebook.apis.ic.xml.classbook.ClassbookGroup;
import net.codealizer.thegradebook.apis.ic.xml.classbook.ClassbookTask;
import net.codealizer.thegradebook.apis.ic.xml.classbook.PortalClassbook;
import net.codealizer.thegradebook.apis.ic.xml.schedule.Term;
import net.codealizer.thegradebook.assets.BasicClassbookActivity;
import net.codealizer.thegradebook.assets.Grade;
import net.codealizer.thegradebook.data.GradesManager;
import net.codealizer.thegradebook.listeners.OnAssignmentAddedListener;
import net.codealizer.thegradebook.listeners.OnAssignmentEdittedListener;
import net.codealizer.thegradebook.listeners.OnClassbookClickListener;
import net.codealizer.thegradebook.ui.adapters.GradesEbrRecyclerViewAdapter;
import net.codealizer.thegradebook.ui.adapters.GradesRecyclerViewAdapter;
import net.codealizer.thegradebook.ui.adapters.SimpleSectionedRecyclerViewAdapter;
import net.codealizer.thegradebook.ui.dialogs.Alert;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.format;

public class GradesActivity extends AppCompatActivity implements OnClassbookClickListener, OnAssignmentAddedListener, OnAssignmentEdittedListener {

    public static final String KEY_COURSE = "net.codealizer.thegradebook.ui.gradebook.GradesActivity.KEY_COURSE";
    public static final String KEY_TERM = "net.codealizer.thegradebook.ui.gradebook.GradesActivity.KEY_TERM";
    public static final String KEY_CLASSBOOK = "net.codealizer.thegradebook.ui.gradebook.GradesActivity.KEY_CLASSBOOK";

    private ClassbookTask task;

    private RecyclerView mRecyclerView;
    private TextView gone;
    private RecyclerView.Adapter mAdapter;
    private SimpleSectionedRecyclerViewAdapter mSectionedAdapter;

    private LinearLayoutManager mLayoutManager;

    private ArrayList<ClassbookActivity> customAssignments;
    private TextView grade;
    private RelativeLayout gradeToolbar;
    private TextView gradeText;
    private PortalClassbook classbook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        task = (ClassbookTask) getIntent().getSerializableExtra(KEY_TERM);
        classbook = (PortalClassbook) getIntent().getSerializableExtra(KEY_CLASSBOOK);

    }

    @Override
    protected void onStart() {
        super.onStart();

        initialize();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_grades, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                finish();
                return true;
            case R.id.action_add_fake_assignment:
                Alert.showNewAssignmentDialog(task, this, this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initialize() {
        customAssignments = new ArrayList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.grades_list);
        gone = (TextView) findViewById(R.id.grades_empty);
        grade = (TextView) findViewById(R.id.toolbar_section_grade);
        gradeToolbar = (RelativeLayout) findViewById(R.id.toolbar_grade);
        gradeText = (TextView) findViewById(R.id.toolbar_grade_name);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        boolean isEBR = classbook.isEBR();


        if (task != null && task.groups.size() > 0) {
            mAdapter = new GradesRecyclerViewAdapter(this, task.getAllActivities(), isEBR, this, task);
            List<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();

            for (Pair<Integer, String> pair : task.getCategories()) {
                //Sections
                sections.add(new SimpleSectionedRecyclerViewAdapter.Section(pair.first, pair.second));
            }

            SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
            mSectionedAdapter = new
                    SimpleSectionedRecyclerViewAdapter(this, R.layout.section, R.id.section_text, mAdapter);
            mSectionedAdapter.setSections(sections.toArray(dummy));
            mRecyclerView.setAdapter(mSectionedAdapter);

            if (!isEBR) {
                gradeToolbar.setVisibility(View.VISIBLE);

                String percent = task.formattedPercentage + "%";
                grade.setText(percent);
            } else {
                gradeToolbar.setVisibility(View.VISIBLE);

                int profScore = GradesManager.calculateProfScore(task);

                this.grade.setText(String.valueOf(profScore));

                if (profScore == 4 || profScore == 3) {
                    this.grade.setBackground(getResources().getDrawable(R.drawable.progress_counter_green));
                } else if (profScore == 2) {
                    this.grade.setBackground(getResources().getDrawable(R.drawable.progress_counter_yellow));
                } else if (profScore == 1) {
                    this.grade.setBackground(getResources().getDrawable(R.drawable.progress_counter_red));
                }

                gradeText.setText("Average Proficiency Score");
            }
        } else {
            gone.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            gradeToolbar.setVisibility(View.INVISIBLE);

            Alert.showEmptyClassDialog(this);
        }
    }


    @Override
    public void onClassbookClicked(ClassbookTask task) {
        Intent intent = new Intent(this, EBRGradesActivity.class);
        intent.putExtra(EBRGradesActivity.KEY_TASK, task);
        //intent.putExtra(EBRGradesActivity.KEY_TASKS, term.getAllTasks());
        startActivity(intent);
    }

    @Override
    public void onAssignmentAdded(BasicClassbookActivity activity) {
        ClassbookActivity classbookActivity = new ClassbookActivity(activity);
        classbookActivity.theoreticalGrade = true;

        if (!customAssignments.contains(classbookActivity)) {
            customAssignments.add(classbookActivity);
            classbookActivity.index = customAssignments.indexOf(classbookActivity);

            task.groups.get(activity.getGroupID()).activities.add(classbookActivity);

            classbookActivity.masterIndex = task.groups.get(activity.getGroupID()).activities.indexOf(classbookActivity);

            refresh();

            mRecyclerView.scrollToPosition(task.getAllActivities().indexOf(classbookActivity));
            DecimalFormat format = new DecimalFormat(".##");

            String percent = String.valueOf(format.format(calculateGrade())) + "%";
            grade.setText(percent);
        } else {
            Alert.showInvalidNameDialog(this);
        }
    }

    public void refresh(ArrayList<ClassbookActivity> activities) {
        mAdapter = new GradesRecyclerViewAdapter(this, activities, false, this, task);
        List<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();

        for (Pair<Integer, String> pair : task.getCategories()) {
            //Sections
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(pair.first, pair.second));
        }

        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
        mSectionedAdapter = new
                SimpleSectionedRecyclerViewAdapter(this, R.layout.section, R.id.section_text, mAdapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));
        mRecyclerView.setAdapter(mSectionedAdapter);

        DecimalFormat format = new DecimalFormat(".##");
        String percent = String.valueOf(format.format(calculateGrade())) + "%";
        grade.setText(percent);
    }

    public void refresh() {
        refresh(task.getAllActivities());
    }


    @Override
    public void onAssignmentEditted(BasicClassbookActivity activity, int index, int masterIndex) {
        ClassbookActivity classbookActivity = new ClassbookActivity(activity);
        ClassbookActivity c = customAssignments.get(index);

        task.groups.get(c.getGroupID()).activities.remove(c);

        if (c.getGroupID() == classbookActivity.getGroupID()) {
            task.groups.get(activity.getGroupID()).activities.add(masterIndex, classbookActivity);
        } else {
            task.groups.get(activity.getGroupID()).activities.add(classbookActivity);
        }
        for (ClassbookGroup g : task.groups) {
            for (int i = 0; i < g.activities.size(); i++) {
                g.activities.get(i).masterIndex = g.activities.indexOf(g.activities.get(i));
            }
        }

        classbookActivity.masterIndex = task.groups.get(activity.getGroupID()).activities.indexOf(classbookActivity);

        customAssignments.remove(c);
        customAssignments.add(index, classbookActivity);


        refresh();

        mRecyclerView.scrollToPosition(task.getAllActivities().indexOf(classbookActivity));

        DecimalFormat format = new DecimalFormat(".##");

        String percent = String.valueOf(format.format(calculateGrade())) + "%";
        grade.setText(percent);


    }

    @Override
    public void onAssignmentDeleted(BasicClassbookActivity activity, int index) {
        ClassbookActivity c = customAssignments.get(index);

        task.groups.get(c.getGroupID()).activities.remove(c);


        for (ClassbookGroup g : task.groups) {
            for (int i = 0; i < g.activities.size(); i++) {
                g.activities.get(i).masterIndex = g.activities.indexOf(g.activities.get(i));
            }
        }

        customAssignments.remove(c);

        refresh();
    }

    private double calculateGrade() {
        ArrayList<ClassbookGroup> groups = task.groups;
        double finalGrade = 0;

        double weight = 0;
        double aggPointsRecieved = 0;
        double aggPointsTotal = 0;
        for (ClassbookGroup g : groups) {
            double groupGrade = 0;
            double totalPoints = 0;
            weight += g.weight;
            for (ClassbookActivity activity : g.activities) {
                if(activity.theoreticalGrade)
                {
                    try
                    {
                        totalPoints += activity.getTotalPoints();
                        groupGrade += Double.parseDouble(activity.getScore());
                    }
                    catch(Exception e) {}

                }

            }

            totalPoints += g.totalPointsPossible;
            groupGrade += g.pointsEarned;
            aggPointsTotal += totalPoints;
            aggPointsRecieved += groupGrade;
            finalGrade += ((groupGrade / totalPoints) * g.weight);

        }

        if(weight - 100 < .001)
        {
            if(weight - 100 < 0.001)
                finalGrade = (aggPointsRecieved / aggPointsTotal)*100;
            else finalGrade = (finalGrade/weight) *100;
        }
        return finalGrade;
    }
}
