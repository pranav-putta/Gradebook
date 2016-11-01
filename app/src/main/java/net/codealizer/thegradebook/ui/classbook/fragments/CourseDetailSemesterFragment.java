package net.codealizer.thegradebook.ui.classbook.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookTask;
import net.codealizer.thegradebook.apis.ic.classbook.CourseDetail;
import net.codealizer.thegradebook.apis.ic.classbook.PortalClassbook;
import net.codealizer.thegradebook.assets.BasicTerm;
import net.codealizer.thegradebook.assets.BasicTermDetail;
import net.codealizer.thegradebook.data.SessionManager;
import net.codealizer.thegradebook.listeners.OnTermSelectedListener;
import net.codealizer.thegradebook.ui.adapters.CourseDetailsExpandableAdapter;
import net.codealizer.thegradebook.ui.classbook.CourseDetailsActivity;
import net.codealizer.thegradebook.ui.classbook.GradesActivity;
import net.codealizer.thegradebook.ui.dialogs.Alert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Pranav on 10/10/16.
 */

public class CourseDetailSemesterFragment extends Fragment implements ExpandableRecyclerAdapter.ExpandCollapseListener, OnTermSelectedListener {

    RecyclerView mRecyclerView;
    ExpandableRecyclerAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    private CourseDetail mCourse;
    private int mPosition;
    private PortalClassbook mClassbook;
    private int mSemester;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_details_semester, container, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_calculate_ebr:
                Alert.showEBRGradeDialog(mClassbook.getClassbook().getTasks(mPosition), mClassbook, getActivity());
        }

        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        mCourse = (CourseDetail) getArguments().getSerializable(CourseDetailsActivity.KEY_COURSE);
        mPosition = getArguments().getInt(CourseDetailsActivity.KEY_COURSE_POSITION, 0);
        mClassbook = SessionManager.mCoreManager.getGradebook(mCourse);
        mSemester = getArguments().getInt(CourseDetailsActivity.KEY_COURSE_SEMESTER, 0);

        ArrayList<BasicTerm> terms = generate();

        mAdapter = new CourseDetailsExpandableAdapter(getActivity(), terms, this);

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.term_cards);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setExpandCollapseListener(this);

        if (terms.isEmpty()) {
            TextView empty = (TextView) getView().findViewById(R.id.grades_empty);
            empty.setVisibility(View.VISIBLE);
        }

    }

    private ArrayList<BasicTerm> generate() {
        ArrayList<BasicTerm> objects = new ArrayList<>();
        ArrayList<ArrayList<Pair<Boolean, ClassbookTask>>> tasks = ClassbookTask.formatTasks(mClassbook.getClassbook().getTasks(mSemester + 1));

        Collections.sort(tasks, new Comparator<ArrayList<Pair<Boolean, ClassbookTask>>>() {
            @Override
            public int compare(ArrayList<Pair<Boolean, ClassbookTask>> pairs, ArrayList<Pair<Boolean, ClassbookTask>> t1) {
                Pair<Boolean, ClassbookTask> t = pairs.get(0);
                Pair<Boolean, ClassbookTask> u = t1.get(0);

                if (t.second.getType() == ClassbookTask.TaskType.TERM) {
                    if (u.second.getType() == ClassbookTask.TaskType.TERM) {
                        return t.second.getName().compareTo(u.second.getName());
                    }

                    return -999;
                } else {
                    return Integer.valueOf(t.second.getTaskSeq()).compareTo(u.second.getTaskSeq());
                }
            }
        });

        for (ArrayList<Pair<Boolean, ClassbookTask>> term : tasks) {
            ClassbookTask t = term.get(0).second;
            String title = term.get(0).second.getName();
            String date = "";
            String grade = term.get(0).second.getLetterGrade();
            String gradePercentage = String.valueOf(term.get(0).second.getPercentage());
            ArrayList<BasicTermDetail> details = new ArrayList<>();

            if (term.size() > 1) {
                // Add Week Data

                Collections.sort(term, new Comparator<Pair<Boolean, ClassbookTask>>() {
                    @Override
                    public int compare(Pair<Boolean, ClassbookTask> t, Pair<Boolean, ClassbookTask> t1) {
                        Integer tt = t.second.getTaskSeq();
                        Integer t1t = t1.second.getTaskSeq();

                        return tt.compareTo(t1t);
                    }
                });

                for (Pair<Boolean, ClassbookTask> pair : term) {
                    if (!pair.first) {
                        double percent = 0;
                        try {
                            percent = Double.parseDouble(pair.second.getScorePercentage());
                        } catch (Exception ex) {
                            percent = 0;
                        }
                        details.add(new BasicTermDetail(pair.second.getName(), pair.second.getScore(), pair.second.getScoreComment(), String.valueOf(percent), percent, pair.second.getScoreDate()));
                    }
                }
            }


            BasicTerm basicTerm = new BasicTerm(title, date, grade, details, t);
            objects.add(basicTerm);
        }

        return objects;
    }

    @Override
    public void onListItemExpanded(int position) {
        mAdapter.collapseAllParents();
        mAdapter.expandParent(position);
    }

    @Override
    public void onListItemCollapsed(int position) {

    }


    @Override
    public void onTermSelected(ClassbookTask task) {
        Intent intent = new Intent(getActivity(), GradesActivity.class);
        intent.putExtra(GradesActivity.KEY_COURSE, mCourse);
        intent.putExtra(GradesActivity.KEY_TERM, task);
        intent.putExtra(GradesActivity.KEY_CLASSBOOK, mClassbook);
        getActivity().startActivity(intent);
    }

    @Override
    public void onTermUnavailable() {
        Alert.showMessageDialog(getActivity(),
                "Grades Unavailable", "No grades have been posted here. If this is an EBR class, scroll down to see group scores");
    }
}
