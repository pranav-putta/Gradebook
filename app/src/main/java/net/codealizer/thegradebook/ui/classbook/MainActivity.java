package net.codealizer.thegradebook.ui.classbook;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.apis.ic.RequestTask;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookActivity;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookManager;
import net.codealizer.thegradebook.assets.UpdateResultReciever;
import net.codealizer.thegradebook.data.SessionManager;
import net.codealizer.thegradebook.listeners.OnGradebookRetrievedListener;
import net.codealizer.thegradebook.listeners.RecyclerItemClickListener;
import net.codealizer.thegradebook.ui.adapters.DividerItemDecoration;
import net.codealizer.thegradebook.ui.adapters.GradebookRecyclerViewAdapter;
import net.codealizer.thegradebook.ui.adapters.SimpleSectionedRecyclerViewAdapter;
import net.codealizer.thegradebook.ui.dialogs.Alert;
import net.codealizer.thegradebook.ui.settings.SettingsActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnGradebookRetrievedListener, RecyclerItemClickListener.OnItemClickListener
{

	ActionBar actionBar;

	private RecyclerView mRecyclerView;
	private RecyclerView.Adapter mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;

	private SwipeRefreshLayout mRefreshLayout;

	private GestureDetector mGestureDetector;

	private ArrayList<Pair<String, ClassbookActivity>> oldTasks;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.gradebook_toolbar);
		setSupportActionBar(toolbar);

		if (!SessionManager.reloadedOnce)
		{
			if (SessionManager.hasGradebookDataStored(this))
			{
				oldTasks = SessionManager.mCoreManager.getAllActivities();
			} else
			{
				oldTasks = null;
			}
			refreshData();
		}
		if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("notifications_new_message", true))
		{
			scheduleAlarm();
		}

	}

	@Override
	protected void onResume()
	{
		super.onStart();
		initialize();
		refreshInterface();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_gradebook, menu);
		return true;
	}

	@Override
	public void onGradebookRetrieved(ClassbookManager gradebookManager)
	{
		SessionManager.saveGradebook(this, gradebookManager);
		SessionManager.reloadedOnce = true;

		mRefreshLayout.setRefreshing(false);

		refreshInterface();
		checkForUpdates();

	}

	@Override
	public void onNetworkError()
	{
		Alert.showNetworkErrorDialog(this);
	}

	@Override
	public void onItemClick(View view, int position)
	{
		if (SessionManager.mCoreManager.getStudentClasses().get(position - 1).isActive())
		{
			Intent intent = new Intent(this, CourseDetailsActivity.class);
			intent.putExtra(CourseDetailsActivity.KEY_COURSE, SessionManager.mCoreManager.getStudentClasses().get(position - 1));
			intent.putExtra(CourseDetailsActivity.KEY_COURSE_POSITION, position);
			startActivity(intent);
		} else
		{
			Alert.showEmptyClassDialog(this);
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		switch (item.getItemId())
		{
			case R.id.gradebookLogoutItem:
				Alert.showLogoutConfirmationDialog(this);
				break;
			case R.id.gradebookRefreshItem:
				refreshData();
				break;
			case R.id.gradebookThemeItem:
				//selectTheme();
				break;
			case R.id.gradebookSettingsItem:
				Intent intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void initialize()
	{
		actionBar = getSupportActionBar();
		mRecyclerView = (RecyclerView) findViewById(R.id.gradebook_cards);
		mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

		mRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.blue_primary_dark, R.color.turquoise_primary_dark);

		mRecyclerView.setHasFixedSize(true);

		mLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLayoutManager);

		actionBar.setTitle(getString(R.string.title_activity_main));

		mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
		{
			@Override
			public void onRefresh()
			{
				refreshData();
			}
		});
		mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, this));

	}

	private void refreshData()
	{
		RequestTask task = new RequestTask(this, SessionManager.mCoreManager, RequestTask.OPTION_RETRIEVE_GRADES_INFO, this, "Please Wait", "Downloading gradebook...");
		task.execute();
	}

	private void selectTheme()
	{
		Resources res = getResources();

		Integer colors[] = {res.getColor(R.color.blue_primary), res.getColor(R.color.orange_primary), res.getColor(R.color.green_primary),
				res.getColor(R.color.yellow_primary), res.getColor(R.color.turquoise_primary), res.getColor(R.color.purple_colorPrimary)};
		String colorNames[] = {"Light Blue", "Deep Orange", "Green", "Yellow", "Turquoise", "Deep Purple"};

		Alert.showThemeSelectionDialog(this, Arrays.asList(colors), Arrays.asList(colorNames));
	}

	private void refreshInterface()
	{
		if (SessionManager.hasGradebookDataStored(this))
		{
			mAdapter = new GradebookRecyclerViewAdapter(this, SessionManager.mCoreManager.getStudentClasses());
			mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

			List<SimpleSectionedRecyclerViewAdapter.Section> sections =
					new ArrayList<>();

			//Sections
			sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, "Courses"));
			sections.add(new SimpleSectionedRecyclerViewAdapter.Section(SessionManager.mCoreManager.gradebookManager.getClasses(), "Clubs/Extra Courses"));

			SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
			SimpleSectionedRecyclerViewAdapter mSectionedAdapter = new
					SimpleSectionedRecyclerViewAdapter(this, R.layout.section, R.id.section_text, mAdapter);
			mSectionedAdapter.setSections(sections.toArray(dummy));

			mRecyclerView.setAdapter(mSectionedAdapter);

		}
	}

	private void checkForUpdates()
	{
		if (oldTasks != null)
		{
			Collection<Pair<String, ClassbookActivity>> old = oldTasks;
			Collection<Pair<String, ClassbookActivity>> newTasks = SessionManager.mCoreManager.getAllActivities();

			newTasks.removeAll(old);

			ArrayList<Pair<String, ClassbookActivity>> newUpdates = new ArrayList<>(newTasks);

			if (newUpdates.size() > 0)
				Alert.showUpdates(this, newUpdates);

		}
	}

	public void scheduleAlarm()
	{

		Intent intent = new Intent(getApplicationContext(), UpdateResultReciever.class);

		final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, UpdateResultReciever.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		long firstMillis = System.currentTimeMillis();
		AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

		int time = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("sync_frequency", "30"));

		if (time != -1)
		{
			alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, time * (60_000), pendingIntent);
		}
	}


}
