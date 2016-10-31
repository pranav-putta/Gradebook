package net.codealizer.thegradebook.ui.classbook.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.apis.ic.xml.Notifications;
import net.codealizer.thegradebook.apis.ic.xml.RequestTask;
import net.codealizer.thegradebook.apis.ic.xml.StudentNotification;
import net.codealizer.thegradebook.apis.ic.xml.classbook.ClassbookActivity;
import net.codealizer.thegradebook.data.SessionManager;
import net.codealizer.thegradebook.listeners.OnNotificationRetrievedListener;
import net.codealizer.thegradebook.ui.adapters.DividerItemDecoration;
import net.codealizer.thegradebook.ui.adapters.RecentActivitiesAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pranav on 10/28/16.
 */

public class RecentActivityFragment extends Fragment {

    RecyclerView list;
    TextView gone;

    public RecentActivityFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_recent_activity, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        initialize();
    }

    private void initialize() {
        list = (RecyclerView) getView().findViewById(R.id.recent_activity_list);
        gone = (TextView) getView().findViewById(R.id.recent_activity_no_updates);

        if (SessionManager.mCoreManager.getNotifications() != null) {
            refresh();
        } else {
            list.setVisibility(View.INVISIBLE);
            gone.setVisibility(View.VISIBLE);
        }
    }


    private void refresh() {
        ArrayList<StudentNotification> updates = SessionManager.mCoreManager.getNotifications().getNotifications();

        if (updates.size() > 0) {
            list.setVisibility(View.VISIBLE);
            gone.setVisibility(View.INVISIBLE);

            list.setHasFixedSize(true);
            list.setLayoutManager(new LinearLayoutManager(getActivity()));

            list.setAdapter(new RecentActivitiesAdapter(updates, getActivity()));

            list.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        } else {
            list.setVisibility(View.INVISIBLE);
            gone.setVisibility(View.VISIBLE);
        }
    }
}
