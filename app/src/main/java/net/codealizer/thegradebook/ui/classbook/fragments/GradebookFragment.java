package net.codealizer.thegradebook.ui.classbook.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.apis.ic.xml.Notifications;
import net.codealizer.thegradebook.apis.ic.xml.RequestTask;
import net.codealizer.thegradebook.apis.ic.xml.StudentNotification;
import net.codealizer.thegradebook.apis.ic.xml.classbook.ClassbookActivity;
import net.codealizer.thegradebook.apis.ic.xml.classbook.ClassbookManager;
import net.codealizer.thegradebook.data.SessionManager;
import net.codealizer.thegradebook.listeners.OnGradebookRetrievedListener;
import net.codealizer.thegradebook.listeners.OnNotificationRetrievedListener;
import net.codealizer.thegradebook.listeners.RecyclerItemClickListener;
import net.codealizer.thegradebook.ui.adapters.DividerItemDecoration;
import net.codealizer.thegradebook.ui.adapters.GradebookRecyclerViewAdapter;
import net.codealizer.thegradebook.ui.adapters.SimpleSectionedRecyclerViewAdapter;
import net.codealizer.thegradebook.ui.classbook.CourseDetailsActivity;
import net.codealizer.thegradebook.ui.dialogs.Alert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Pranav on 10/28/16.
 */

public class GradebookFragment extends Fragment implements RecyclerItemClickListener.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private LinearLayoutManager mLayoutManager;

    private GradebookRecyclerViewAdapter mAdapter;
    private ArrayList<Pair<String, ClassbookActivity>> oldTasks;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_main, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();



        initialize();
        refreshInterface();
    }

    @Override
    public void onResume() {
        super.onResume();
        initialize();
        refreshInterface();
    }


    @Override
    public void onItemClick(View view, int position) {
        if (position != 0 && (position - 1) != SessionManager.mCoreManager.gradebookManager.getClasses() && (position - 1) != SessionManager.mCoreManager.getStudentClasses().size()) {
            if (SessionManager.mCoreManager.getStudentClasses().get(position - 1).isActive()) {
                Intent intent = new Intent(getActivity(), CourseDetailsActivity.class);
                intent.putExtra(CourseDetailsActivity.KEY_COURSE, SessionManager.mCoreManager.getStudentClasses().get(position - 1));
                intent.putExtra(CourseDetailsActivity.KEY_COURSE_POSITION, position);
                startActivity(intent);
            }
        }
    }

    /**
     * @Override public boolean onOptionsItemSelected(MenuItem item) {
     * switch (item.getItemId()) {
     * case R.id.gradebookRefreshItem:
     * refreshData();
     * break;
     * }
     * return super.onOptionsItemSelected(item);
     * }
     * @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
     * super.onCreateOptionsMenu(menu, inflater);
     * }
     **/



    private void initialize() {
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.gradebook_cards);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), this));

    }

    private void refreshInterface() {
        if (SessionManager.hasGradebookDataStored(getActivity())) {
            mAdapter = new GradebookRecyclerViewAdapter(getActivity(), SessionManager.mCoreManager.getStudentClasses());
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

            List<SimpleSectionedRecyclerViewAdapter.Section> sections =
                    new ArrayList<>();

            //Sections
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, "Courses"));
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(SessionManager.mCoreManager.gradebookManager.getClasses(), "Clubs/Extra Courses"));

            SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
            SimpleSectionedRecyclerViewAdapter mSectionedAdapter = new
                    SimpleSectionedRecyclerViewAdapter(getActivity(), R.layout.section, R.id.section_text, mAdapter);
            mSectionedAdapter.setSections(sections.toArray(dummy));

            mRecyclerView.setAdapter(mSectionedAdapter);

        }
    }

    private void checkForUpdates() {
        if (oldTasks != null) {
            Collection<Pair<String, ClassbookActivity>> old = oldTasks;
            Collection<Pair<String, ClassbookActivity>> newTasks = SessionManager.mCoreManager.getAllActivities();

            newTasks.removeAll(old);

            ArrayList<Pair<String, ClassbookActivity>> newUpdates = new ArrayList<>(newTasks);

            if (newUpdates.size() > 0)
                Alert.showUpdates(getActivity(), newUpdates);

        }
    }

}
