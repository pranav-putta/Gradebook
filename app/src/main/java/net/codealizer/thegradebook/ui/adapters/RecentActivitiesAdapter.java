package net.codealizer.thegradebook.ui.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.apis.ic.StudentNotification;
import net.codealizer.thegradebook.apis.ic.classbook.CourseDetail;
import net.codealizer.thegradebook.apis.ic.classbook.PortalClassbook;
import net.codealizer.thegradebook.data.SessionManager;
import net.codealizer.thegradebook.ui.classbook.CourseDetailsActivity;

import java.util.ArrayList;

/**
 * Created by Pranav on 10/29/16.
 */

public class RecentActivitiesAdapter extends RecyclerView.Adapter<RecentActivitiesAdapter.ViewHolder> {

    ArrayList<StudentNotification> mNotifications;

    Activity activity;

    public RecentActivitiesAdapter(ArrayList<StudentNotification> notifications, Activity activity) {
        mNotifications = new ArrayList<StudentNotification>(notifications);

        this.activity = activity;
    }

    @Override
    public RecentActivitiesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_recent_activity, parent, false);
        return new RecentActivitiesAdapter.ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(final RecentActivitiesAdapter.ViewHolder holder, final int position) {
        StudentNotification notification = mNotifications.get(position);

        holder.title.setText(notification.getNotificationText());
        holder.date.setText(notification.getDisplayDate());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String context = mNotifications.get(holder.getAdapterPosition()).getLinkContext();

                if (context.contains("sectionID=")) {
                    String sectionID = "";

                    for (String key : context.split(String.valueOf("&"))) {
                        if (key.contains("sectionID=")) {
                            sectionID = key.substring(key.indexOf("=") + 1);
                            break;
                        }
                    }

                    if (!sectionID.isEmpty()) {
                        PortalClassbook classbook = PortalClassbook.find(sectionID);

                        if (classbook != null) {
                            int position = SessionManager.mCoreManager.gradebookManager.getPortalClassbooks().indexOf(classbook);
                            CourseDetail course = classbook.getCourse();

                            Intent intent = new Intent(activity, CourseDetailsActivity.class);
                            intent.putExtra(CourseDetailsActivity.KEY_COURSE, course);
                            intent.putExtra(CourseDetailsActivity.KEY_COURSE_POSITION, position);
                            activity.startActivity(intent);
                        }
                    }

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, date;
        RelativeLayout card;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.list_item_recent_activity_title);
            date = (TextView) itemView.findViewById(R.id.list_item_recent_activity_due_date);

            card = (RelativeLayout) itemView.findViewById(R.id.list_item_recent_activity_card);
        }
    }
}
