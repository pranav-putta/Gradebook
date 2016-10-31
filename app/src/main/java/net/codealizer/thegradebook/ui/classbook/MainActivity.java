package net.codealizer.thegradebook.ui.classbook;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.apis.ic.Notifications;
import net.codealizer.thegradebook.apis.ic.StudentNotification;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookManager;
import net.codealizer.thegradebook.assets.UpdateResultReciever;
import net.codealizer.thegradebook.data.SessionManager;
import net.codealizer.thegradebook.listeners.OnGradebookRetrievedListener;
import net.codealizer.thegradebook.listeners.OnNotificationRetrievedListener;
import net.codealizer.thegradebook.ui.classbook.fragments.GradebookFragment;
import net.codealizer.thegradebook.ui.classbook.fragments.RecentActivityFragment;
import net.codealizer.thegradebook.ui.dialogs.Alert;
import net.codealizer.thegradebook.ui.settings.SettingsActivity;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity implements OnGradebookRetrievedListener, OnNotificationRetrievedListener {

    ActionBar actionBar;

    private ViewPager viewPager;

    private SwipeRefreshLayout mRefreshLayout;

    private ArrayList<StudentNotification> oldNotifications;

    private int mPosition;

    public static boolean active = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.gradebook_toolbar);
        setSupportActionBar(toolbar);

        initialize();


        //Schedule notifications if the user wants them
        //Never assume the user wants alarms
        if (PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(getString(R.string.pref_key_notifications_new_message), true)) {
            scheduleAlarm();
        }

        active = true;

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onStart();

        if (SessionManager.shouldReload()) {
            new RefreshTask().execute();
        } else {
            refresh();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        active = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        active = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_gradebook, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.gradebookRefreshItem:
                new RefreshTask().execute();
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

    private void initialize() {
        actionBar = getSupportActionBar();
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.blue_primary_dark, R.color.turquoise_primary_dark);

        actionBar.setTitle(getString(R.string.title_activity_main));


        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new RefreshTask().execute();
            }
        });
    }


    private void selectTheme() {
        Resources res = getResources();

        Integer colors[] = {res.getColor(R.color.blue_primary), res.getColor(R.color.orange_primary), res.getColor(R.color.green_primary),
                res.getColor(R.color.yellow_primary), res.getColor(R.color.turquoise_primary), res.getColor(R.color.purple_colorPrimary)};
        String colorNames[] = {"Light Blue", "Deep Orange", "Green", "Yellow", "Turquoise", "Deep Purple"};

        Alert.showThemeSelectionDialog(this, Arrays.asList(colors), Arrays.asList(colorNames));
    }


    public void scheduleAlarm() {

        Intent intent = new Intent(getApplicationContext(), UpdateResultReciever.class);

        SessionManager.pendingIntent = PendingIntent.getBroadcast(this, UpdateResultReciever.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long firstMillis = System.currentTimeMillis();
        SessionManager.alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        int time = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("sync_frequency", "30"));

        if (time != -1) {
            SessionManager.alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, time * (60_000),
                    SessionManager.pendingIntent);

        }
    }

    @Override
    public void onGradebookRetrieved(ClassbookManager gradebookManager) {
        SessionManager.saveGradebook(this, gradebookManager);
    }

    @Override
    public void onNotificationRetrieved(Notifications notifications) {
        SessionManager.saveNotifications(this, notifications);
    }

    @Override
    public void onNetworkError() {
        Alert.showNetworkErrorDialog(this);
        if (SessionManager.hasGradebookDataStored(this)) {
            refresh();
        }
    }

    public void refresh() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.gradebook_tabs);

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                MainActivity.this.mPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(mPosition);
    }


    public void onCompleteRefresh() {
        refresh();
        checkForUpdates();
    }

    private void checkForUpdates() {
        if (oldNotifications != null) {
            Collection<StudentNotification> old = oldNotifications;
            Collection<StudentNotification> newTasks = SessionManager.mCoreManager.getNotifications().getNotifications();
            newTasks.removeAll(old);

            ArrayList<StudentNotification> newUpdates = new ArrayList<>(newTasks);

            if (newUpdates.size() > 0)
                Alert.showUpdates(this, newUpdates);

        }
    }

    public class RefreshTask extends AsyncTask<Boolean, Boolean, Boolean> {

        private ProgressDialog p;

        @Override
        protected void onPreExecute() {
            p = new ProgressDialog(MainActivity.this);
            p.setMessage("Downloading gradebook data ...");
            p.setTitle("Please Wait");
            p.setCancelable(false);
            p.show();

            if (SessionManager.hasGradebookDataStored(MainActivity.this))
                oldNotifications = new ArrayList<>(SessionManager.mCoreManager.notifications.getNotifications());

        }

        @Override
        protected Boolean doInBackground(Boolean... strings) {

            boolean success = true;
            try {
                try {
                    ClassbookManager gradebook = SessionManager.mCoreManager.reloadData(MainActivity.this);
                    MainActivity.this.onGradebookRetrieved(gradebook);
                } catch (IOException | ParserConfigurationException | SAXException e) {
                    try {
                        ClassbookManager gradebook = SessionManager.mCoreManager.reloadAll(MainActivity.this);
                        MainActivity.this.onGradebookRetrieved(gradebook);
                    } catch (IOException | ParserConfigurationException | SAXException ex) {
                        success = false;
                    }
                }
                try {
                    Notifications notifications = SessionManager.mCoreManager.retrieveNotifications();
                    if (notifications != null)
                        MainActivity.this.onNotificationRetrieved(notifications);
                } catch (SAXException | ParserConfigurationException | IOException e) {
                    success = false;
                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return success;

        }

        @Override
        protected void onPostExecute(Boolean success) {
            p.dismiss();
            mRefreshLayout.setRefreshing(false);

            if (success) {
                MainActivity.this.onCompleteRefresh();
            } else {
                MainActivity.this.onNetworkError();
            }
        }
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        GradebookFragment fragment;
        RecentActivityFragment activityFragment;


        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragment = new GradebookFragment();
            activityFragment = new RecentActivityFragment();
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return fragment;
                case 1:
                    return activityFragment;
            }

            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.main_activity_classes_tab_name);
                case 1:
                    return getString(R.string.main_activity_recent_activity_tab_name);
            }

            return "";
        }
    }

}



