package net.codealizer.thegradebook.ui.gradebook;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.colorpicker.ColorPickerDialog;
import com.android.colorpicker.ColorPickerSwatch;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.apis.ic.RequestTask;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookActivity;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookManager;
import net.codealizer.thegradebook.data.Data;
import net.codealizer.thegradebook.listeners.OnGradebookRetrievedListener;
import net.codealizer.thegradebook.ui.dialogs.Alert;
import net.codealizer.thegradebook.ui.gradebook.cards.DividerItemDecoration;
import net.codealizer.thegradebook.ui.gradebook.adapters.GradebookRecyclerViewAdapter;
import net.codealizer.thegradebook.ui.gradebook.cards.RecyclerItemClickListener;
import net.codealizer.thegradebook.ui.gradebook.cards.SimpleSectionedRecyclerViewAdapter;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnGradebookRetrievedListener, RecyclerItemClickListener.OnItemClickListener {

    ActionBar actionBar;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SwipeRefreshLayout mRefreshLayout;

    private GestureDetector mGestureDetector;

    private ArrayList<Pair<String, ClassbookActivity>> oldTasks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.gradebook_toolbar);
        setSupportActionBar(toolbar);

        if (!Data.reloadedOnce) {
            if (Data.hasGradebookDataStored(this)) {
                oldTasks = Data.mCoreManager.getAllActivities();
            } else {
                oldTasks = null;
            }
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

    @Override
    public void onGradebookRetrieved(ClassbookManager gradebookManager) {
        Data.saveGradebook(this, gradebookManager);
        Data.reloadedOnce = true;

        mRefreshLayout.setRefreshing(false);

        refreshInterface();
        checkForUpdates();

    }

    @Override
    public void onNetworkError() {
        Alert.showNetworkErrorDialog(this);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.gradebookLogoutItem:
                Alert.showLogoutConfirmationDialog(this);
                break;
            case R.id.gradebookRefreshItem:
                refreshData();
                break;
            case R.id.gradebookThemeItem:
                //selectTheme();
                break;
        }

        return super.onOptionsItemSelected(item);
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

    private void selectTheme() {
        ColorPickerDialog dialog = new ColorPickerDialog();
        Resources res = getResources();

        Integer colors[] = {res.getColor(R.color.blue_primary), res.getColor(R.color.orange_primary), res.getColor(R.color.green_primary),
                res.getColor(R.color.yellow_primary), res.getColor(R.color.turquoise_primary), res.getColor(R.color.purple_colorPrimary)};
        String colorNames[] = {"Light Blue", "Deep Orange", "Green", "Yellow", "Turquoise", "Deep Purple"};

        Alert.showThemeSelectionDialog(this, Arrays.asList(colors), Arrays.asList(colorNames));
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

    private void checkForUpdates() {
        if (oldTasks != null) {
            Collection<Pair<String, ClassbookActivity>> old = oldTasks;
            Collection<Pair<String, ClassbookActivity>> newTasks = Data.mCoreManager.getAllActivities();

            newTasks.removeAll(old);

            ArrayList<Pair<String, ClassbookActivity>> newUpdates = new ArrayList<>(newTasks);

            if (newUpdates.size() > 0)
                Alert.showUpdates(this, newUpdates);

        }
    }


}
