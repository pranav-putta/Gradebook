package net.codealizer.thegradebook.ui.gradebook;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.apis.ic.calendar.Term;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookTask;
import net.codealizer.thegradebook.data.Data;
import net.codealizer.thegradebook.listeners.OnAssignmentEdittedListener;
import net.codealizer.thegradebook.listeners.OnClassbookClickListener;
import net.codealizer.thegradebook.ui.dialogs.Alert;
import net.codealizer.thegradebook.ui.gradebook.adapters.GradesEbrRecyclerViewAdapter;
import net.codealizer.thegradebook.ui.gradebook.adapters.GradesRecyclerViewAdapter;
import net.codealizer.thegradebook.ui.gradebook.cards.BasicClassbookActivity;
import net.codealizer.thegradebook.ui.gradebook.cards.SimpleSectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class EBRGradesActivity extends AppCompatActivity implements OnAssignmentEdittedListener {

    public static final String KEY_COURSE = "net.codealizer.thegradebook.ui.gradebook.GradesActivity.KEY_COURSE";
    public static final String KEY_TASK = "net.codealizer.thegradebook.ui.gradebook.GradesActivity.KEY_TASK";

    private ClassbookTask task;

    private RecyclerView mRecyclerView;
    private TextView gone;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                super.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initialize() {
        task = (ClassbookTask) getIntent().getSerializableExtra(KEY_TASK);

        mRecyclerView = (RecyclerView) findViewById(R.id.grades_list);
        gone = (TextView) findViewById(R.id.grades_empty);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (task != null) {
            mAdapter = new GradesRecyclerViewAdapter(this, task.getAllActivities(), true, this, task);
            List<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();

            for (Pair<Integer, String> pair : task.getCategories()) {
                //Sections
                sections.add(new SimpleSectionedRecyclerViewAdapter.Section(pair.first, pair.second));
            }

            SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
            SimpleSectionedRecyclerViewAdapter mSectionedAdapter = new
                    SimpleSectionedRecyclerViewAdapter(this, R.layout.section, R.id.section_text, mAdapter);
            mSectionedAdapter.setSections(sections.toArray(dummy));
            mRecyclerView.setAdapter(mSectionedAdapter);
        } else {
            gone.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            Alert.showEmptyClassDialog(this);
        }


    }

    @Override
    public void onAssignmentEditted(BasicClassbookActivity activity, int index, int masterIndex) {

    }

    @Override
    public void onAssignmentDeleted(BasicClassbookActivity activity, int index) {

    }
}
