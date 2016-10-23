package net.codealizer.thegradebook.ui.classbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookActivity;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookGroup;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookTask;
import net.codealizer.thegradebook.apis.ic.schedule.Term;
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

public class GradesActivity extends AppCompatActivity implements OnClassbookClickListener, OnAssignmentAddedListener, OnAssignmentEdittedListener
{

	public static final String KEY_COURSE = "net.codealizer.thegradebook.ui.gradebook.GradesActivity.KEY_COURSE";
	public static final String KEY_TERM = "net.codealizer.thegradebook.ui.gradebook.GradesActivity.KEY_TERM";

	private Term term;

	private RecyclerView mRecyclerView;
	private TextView gone;
	private RecyclerView.Adapter mAdapter;
	private SimpleSectionedRecyclerViewAdapter mSectionedAdapter;

	private LinearLayoutManager mLayoutManager;

	private ArrayList<ClassbookActivity> customAssignments;
	private TextView grade;
	private RelativeLayout gradeToolbar;
	private TextView gradeText;

	private boolean isEBR;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grades);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void onStart()
	{
		super.onStart();

		initialize();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		if (!isEBR)
		{
			inflater.inflate(R.menu.menu_grades, menu);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				super.onBackPressed();
				return true;
			case R.id.action_add_fake_assignment:
				Alert.showNewAssignmentDialog(term, this, this);
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void initialize()
	{
		term = (Term) getIntent().getSerializableExtra(KEY_TERM);
		customAssignments = new ArrayList<>();

		mRecyclerView = (RecyclerView) findViewById(R.id.grades_list);
		gone = (TextView) findViewById(R.id.grades_empty);
		grade = (TextView) findViewById(R.id.toolbar_section_grade);
		gradeToolbar = (RelativeLayout) findViewById(R.id.toolbar_grade);
		gradeText = (TextView) findViewById(R.id.toolbar_grade_name);

		mRecyclerView.setHasFixedSize(true);
		mLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLayoutManager);


		isEBR = term.isEBR;

		if (!term.isEBR)
		{

			if (term.getTask() != null)
			{
				mAdapter = new GradesRecyclerViewAdapter(this, term.getAllActivities(), false, this, term.getTask());
				List<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();

				for (Pair<Integer, String> pair : term.getCategories())
				{
					//Sections
					sections.add(new SimpleSectionedRecyclerViewAdapter.Section(pair.first, pair.second));
				}

				SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
				mSectionedAdapter = new
						SimpleSectionedRecyclerViewAdapter(this, R.layout.section, R.id.section_text, mAdapter);
				mSectionedAdapter.setSections(sections.toArray(dummy));
				mRecyclerView.setAdapter(mSectionedAdapter);
				gradeToolbar.setVisibility(View.VISIBLE);

				String percent = term.getTask().formattedPercentage + "%";
				grade.setText(percent);
			} else
			{
				gone.setVisibility(View.VISIBLE);
				mRecyclerView.setVisibility(View.INVISIBLE);
				gradeToolbar.setVisibility(View.INVISIBLE);

				Alert.showEmptyClassDialog(this);
			}
		} else
		{
			gradeToolbar.setVisibility(View.VISIBLE);
			gradeText.setText("Calculated EBR Grade");

			Grade grade = GradesManager.calculateEBR(term.getAllTasks());

			this.grade.setText(Grade.valueOf(grade));

			if (grade == Grade.A || grade == Grade.AB)
			{
				this.grade.setBackground(getResources().getDrawable(R.drawable.progress_counter_green));
			} else if (grade == Grade.B || grade == Grade.BC)
			{
				this.grade.setBackground(getResources().getDrawable(R.drawable.progress_counter_yellow));
			} else if (grade == Grade.C || grade == Grade.CD || grade == Grade.D)
			{
				this.grade.setBackground(getResources().getDrawable(R.drawable.progress_counter_red));
			}

			mAdapter = new GradesEbrRecyclerViewAdapter(this, term.getAllTasks(), this);

			mRecyclerView.setAdapter(mAdapter);
		}


	}

	@Override
	public void onClassbookClicked(ClassbookTask task)
	{
		Intent intent = new Intent(this, EBRGradesActivity.class);
		intent.putExtra(EBRGradesActivity.KEY_TASK, task);
		intent.putExtra(EBRGradesActivity.KEY_TASKS, term.getAllTasks());
		startActivity(intent);
	}

	@Override
	public void onAssignmentAdded(BasicClassbookActivity activity)
	{
		ClassbookActivity classbookActivity = new ClassbookActivity(activity);

		if (!customAssignments.contains(classbookActivity))
		{
			customAssignments.add(classbookActivity);
			classbookActivity.index = customAssignments.indexOf(classbookActivity);

			term.getTask().groups.get(activity.getGroupID()).activities.add(classbookActivity);

			classbookActivity.masterIndex = term.getTask().groups.get(activity.getGroupID()).activities.indexOf(classbookActivity);

			refresh();

			mRecyclerView.scrollToPosition(term.getAllActivities().indexOf(classbookActivity));
			DecimalFormat format = new DecimalFormat(".##");

			String percent = String.valueOf(format.format(calculateGrade())) + "%";
			grade.setText(percent);
		} else
		{
			Alert.showInvalidNameDialog(this);
		}
	}

	public void refresh(ArrayList<ClassbookActivity> activities)
	{
		mAdapter = new GradesRecyclerViewAdapter(this, activities, false, this, term.getTask());
		List<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();

		for (Pair<Integer, String> pair : term.getCategories())
		{
			//Sections
			sections.add(new SimpleSectionedRecyclerViewAdapter.Section(pair.first, pair.second));
		}

		SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
		mSectionedAdapter = new
				SimpleSectionedRecyclerViewAdapter(this, R.layout.section, R.id.section_text, mAdapter);
		mSectionedAdapter.setSections(sections.toArray(dummy));
		mRecyclerView.setAdapter(mSectionedAdapter);
	}

	public void refresh()
	{
		refresh(term.getAllActivities());
	}


	@Override
	public void onAssignmentEditted(BasicClassbookActivity activity, int index, int masterIndex)
	{
		ClassbookActivity classbookActivity = new ClassbookActivity(activity);
		ClassbookActivity c = customAssignments.get(index);

		term.getTask().groups.get(c.groupID).activities.remove(c);

		if (c.groupID == classbookActivity.groupID)
		{
			term.getTask().groups.get(activity.getGroupID()).activities.add(masterIndex, classbookActivity);
		} else
		{
			term.getTask().groups.get(activity.getGroupID()).activities.add(classbookActivity);
		}
		for (ClassbookGroup g : term.getTask().groups)
		{
			for (int i = 0; i < g.activities.size(); i++)
			{
				g.activities.get(i).masterIndex = g.activities.indexOf(g.activities.get(i));
			}
		}

		classbookActivity.masterIndex = term.getTask().groups.get(activity.getGroupID()).activities.indexOf(classbookActivity);

		customAssignments.remove(c);
		customAssignments.add(index, classbookActivity);


		refresh();

		mRecyclerView.scrollToPosition(term.getAllActivities().indexOf(classbookActivity));

		DecimalFormat format = new DecimalFormat(".##");

		String percent = String.valueOf(format.format(calculateGrade())) + "%";
		grade.setText(percent);
	}

	@Override
	public void onAssignmentDeleted(BasicClassbookActivity activity, int index)
	{
		ClassbookActivity c = customAssignments.get(index);

		term.getTask().groups.get(c.groupID).activities.remove(c);


		for (ClassbookGroup g : term.getTask().groups)
		{
			for (int i = 0; i < g.activities.size(); i++)
			{
				g.activities.get(i).masterIndex = g.activities.indexOf(g.activities.get(i));
			}
		}

		customAssignments.remove(c);

		refresh();
	}

	private double calculateGrade()
	{
		ArrayList<ClassbookGroup> groups = term.getTask().groups;
		double finalGrade = 0;

		for (ClassbookGroup g : groups)
		{
			double groupGrade = 0;
			double totalPoints = 0;
			for (ClassbookActivity activity : g.activities)
			{
				double score = 0;
				double total = 0;
				try
				{
					score = Double.parseDouble(activity.score);
					total = (activity.totalPoints);
				} catch (Exception ex)
				{
				}

				groupGrade += score;
				totalPoints += total;
			}

			finalGrade += ((groupGrade / totalPoints) * g.weight);

		}
		return finalGrade;
	}
}
