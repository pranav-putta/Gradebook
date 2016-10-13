package net.codealizer.thegradebook.ui.gradebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.apis.ic.RequestTask;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookManager;
import net.codealizer.thegradebook.data.Data;
import net.codealizer.thegradebook.listeners.OnGradebookRetrievedListener;
import net.codealizer.thegradebook.ui.dialogs.Alert;
import net.codealizer.thegradebook.ui.gradebook.cards.DividerItemDecoration;
import net.codealizer.thegradebook.ui.gradebook.adapters.GradebookRecyclerViewAdapter;
import net.codealizer.thegradebook.ui.gradebook.cards.RecyclerItemClickListener;
import net.codealizer.thegradebook.ui.gradebook.cards.SimpleSectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnGradebookRetrievedListener, RecyclerItemClickListener.OnItemClickListener {

    ActionBar actionBar;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SwipeRefreshLayout mRefreshLayout;

    private GestureDetector mGestureDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.gradebook_toolbar);
        setSupportActionBar(toolbar);

        if (!Data.reloadedOnce) {
            refreshData();
        }

    }

    @Override
    protected void onResume() {
        super.onStart();
        initialize();
        refreshInterface();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_gradebook, menu);
        return true;
    }

    private void initialize() {
        actionBar = getSupportActionBar();
        mRecyclerView = (RecyclerView) findViewById(R.id.gradebook_cards);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        actionBar.setTitle(getString(R.string.title_activity_main));

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, this));


    }

    private void refreshData() {
        RequestTask task = new RequestTask(this, Data.mCoreManager, RequestTask.OPTION_RETRIEVE_GRADES_INFO, this, "Please Wait", "Downloading gradebook...");
        task.execute();
    }

    @Override
    public void onGradebookRetrieved(ClassbookManager gradebookManager) {
        Data.saveGradebook(this, gradebookManager);
        Data.reloadedOnce = true;

        mRefreshLayout.setRefreshing(false);

        refreshInterface();
    }

    @Override
    public void onNetworkError() {
        Alert.showNetworkErrorDialog(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.gradebookLogoutItem:
                Alert.showLogoutConfirmationDialog(this);
                break;
            case R.id.gradebookRefreshItem:
                refreshData();
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshInterface() {
        if (Data.hasGradebookDataStored(this)) {
            mAdapter = new GradebookRecyclerViewAdapter(this, Data.mCoreManager.getStudentClasses());
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

            List<SimpleSectionedRecyclerViewAdapter.Section> sections =
                    new ArrayList<>();

            //Sections
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, "Courses"));
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(Data.mCoreManager.gradebookManager.getClasses(), "Clubs/Extra Courses"));

            SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
            SimpleSectionedRecyclerViewAdapter mSectionedAdapter = new
                    SimpleSectionedRecyclerViewAdapter(this, R.layout.section, R.id.section_text, mAdapter);
            mSectionedAdapter.setSections(sections.toArray(dummy));

            mRecyclerView.setAdapter(mSectionedAdapter);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (Data.mCoreManager.getStudentClasses().get(position - 1).isActive()) {
            Intent intent = new Intent(this, CourseDetailsActivity.class);
            intent.putExtra(CourseDetailsActivity.KEY_COURSE, Data.mCoreManager.getStudentClasses().get(position - 1));
            intent.putExtra(CourseDetailsActivity.KEY_COURSE_POSITION, position);
            startActivity(intent);
        } else {
            Alert.showEmptyClassDialog(this);
        }

    }
}
