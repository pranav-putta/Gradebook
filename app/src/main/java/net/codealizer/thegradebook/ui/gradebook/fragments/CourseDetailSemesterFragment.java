package net.codealizer.thegradebook.ui.gradebook.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.apis.ic.calendar.SectionTask;
import net.codealizer.thegradebook.apis.ic.calendar.Term;
import net.codealizer.thegradebook.apis.ic.classbook.Course;
import net.codealizer.thegradebook.apis.ic.classbook.PortalClassbook;
import net.codealizer.thegradebook.data.Data;
import net.codealizer.thegradebook.listeners.OnTermSelectedListener;
import net.codealizer.thegradebook.ui.gradebook.CourseDetailsActivity;
import net.codealizer.thegradebook.ui.gradebook.GradesActivity;
import net.codealizer.thegradebook.ui.gradebook.adapters.CourseDetailsExpandableAdapter;
import net.codealizer.thegradebook.ui.gradebook.cards.BasicTerm;
import net.codealizer.thegradebook.ui.gradebook.cards.BasicTermDetail;

import java.util.ArrayList;

/**
 * Created by Pranav on 10/10/16.
 */

public class CourseDetailSemesterFragment extends Fragment implements ExpandableRecyclerAdapter.ExpandCollapseListener, OnTermSelectedListener {

    RecyclerView mRecyclerView;
    ExpandableRecyclerAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    private Course mCourse;
    private int mPosition;
    private PortalClassbook mClassbook;
    private int mSemester;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_details_semester, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        mCourse = (Course) getArguments().getSerializable(CourseDetailsActivity.KEY_COURSE);
        mPosition = getArguments().getInt(CourseDetailsActivity.KEY_COURSE_POSITION, 0);
        mClassbook = Data.mCoreManager.getGradebook(mCourse);
        mSemester = getArguments().getInt(CourseDetailsActivity.KEY_COURSE_SEMESTER, 0);

        mAdapter = new CourseDetailsExpandableAdapter(getActivity(), generate(), this);

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.term_cards);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setExpandCollapseListener(this);


    }

    private ArrayList<BasicTerm> generate() {
        ArrayList<BasicTerm> objects = new ArrayList<>();
        ArrayList<Term> mTerms = mClassbook.getTerm(mSemester);

        for (Term t : mTerms) {
            String title = "Term " + t.getTermNumber();
            String date = t.formatDate();
            String grade = t.getLetterGrade();

            ArrayList<BasicTermDetail> details = new ArrayList<>();

            for (SectionTask st : t.getWeekTasks()) {
                details.add(new BasicTermDetail(st.getName(), st.getScore(), st.getComments(), st.getScore(),
                        st.getPercent(), st.getDate().toString()));
            }

            BasicTerm term = new BasicTerm(title, date, grade, details, t);
            objects.add(term);
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
    public void onTermSelected(Term term) {
        Intent intent = new Intent(getActivity(), GradesActivity.class);
        intent.putExtra(GradesActivity.KEY_COURSE, mCourse);
        intent.putExtra(GradesActivity.KEY_TERM, term);
        getActivity().startActivity(intent);
    }
}
