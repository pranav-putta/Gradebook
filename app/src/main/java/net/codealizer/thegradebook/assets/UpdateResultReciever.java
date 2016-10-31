package net.codealizer.thegradebook.assets;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.apis.ic.Notifications;
import net.codealizer.thegradebook.apis.ic.StudentNotification;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookManager;
import net.codealizer.thegradebook.data.SessionManager;
import net.codealizer.thegradebook.ui.launch.LaunchActivity;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Pranav on 10/22/16.
 */

public class UpdateResultReciever extends BroadcastReceiver {

    public static final int REQUEST_CODE = 285548;
    public static final String ACTION = "net.codealizer.thegradebook.alarm";

    private Context context;
    private Notifications oldNotifications;

    private SharedPreferences preferences;
    private MediaPlayer mMediaPlayer;

    @Override
    public void onReceive(final Context context, Intent intent) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);


        if (SessionManager.hasGradebookDataStored(context)) {
            SessionManager.loadData(context);

            oldNotifications = SessionManager.mCoreManager.notifications;
            new DownloadTask().execute();
        }

    }

    public void onNotificationsRetrieved(Notifications notifications) {
        try {
            preferences = PreferenceManager.getDefaultSharedPreferences(context);

            if (oldNotifications != null && notifications != null && notifications.getNotifications().size() > 0) {
                Collection<StudentNotification> old = oldNotifications.getNotifications();
                Collection<StudentNotification> newTasks = notifications.getNotifications();

                newTasks.removeAll(old);

                ArrayList<StudentNotification> newUpdates = new ArrayList<>(newTasks);

                if (newUpdates.size() > 0 && preferences.getBoolean("notifications_new_message", true)) {
                    buildNotifications(newUpdates);
                }

                SessionManager.saveNotifications(context, notifications);
            }

            Intent i = new Intent(context, UpdateService.class);
            context.startService(i);
        } catch (Exception ex) {
        }
    }

    public void onNetworkError() {
    }

    private void buildNotifications(ArrayList<StudentNotification> updates) {

        for (StudentNotification activityPair : updates) {

            String message = activityPair.getNotificationText();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle("Gradebook");
            builder.setContentText(message);
            builder.setAutoCancel(true);

            builder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;


            Intent notificationIntent = new Intent(context, LaunchActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    notificationIntent, 0);

            builder.setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            int id = 0;
            try {
                id = Integer.parseInt(activityPair.getNotificationID());
            } catch (Exception ignore) {

            }

            manager.notify(id, builder.build());

            if (preferences.getBoolean("notifications_new_message_vibrate", true)) {
                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(500);
            }

            if (!preferences.getString("notifications_new_message_ringtone", "").equals("")) {
                Uri notification = Uri.parse(preferences.getString("notifications_new_message_ringtone", "default ringtone"));
                Ringtone r = RingtoneManager.getRingtone(context, notification);
                r.play();
            }
        }

    }

    private class DownloadTask extends AsyncTask<Boolean, Boolean, Boolean> {

        @Override
        protected Boolean doInBackground(Boolean... booleen) {
            UpdateResultReciever.this.context = context;
            UpdateResultReciever.this.oldNotifications = SessionManager.mCoreManager.getNotifications();

            boolean success = true;

            Notifications notifications = null;

            try {
                try {
                    try {
                        ClassbookManager gradebook = SessionManager.mCoreManager.reloadData(context);
                        SessionManager.saveGradebook(context, gradebook);
                    } catch (IOException | ParserConfigurationException | SAXException e) {
                        try {
                            ClassbookManager gradebook = SessionManager.mCoreManager.reloadAll(context);
                            SessionManager.saveGradebook(context, gradebook);
                        } catch (IOException | ParserConfigurationException | SAXException ex) {
                            success = false;
                        }
                    }
                    try {
                        notifications = SessionManager.mCoreManager.retrieveNotifications();
                        if (notifications.getNotifications().size() > 5) {
                        }
                    } catch (SAXException | ParserConfigurationException | IOException e) {
                        success = false;
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } catch (Exception e) {
                onNetworkError();
            }

            if (success && notifications != null) {
                onNotificationsRetrieved(notifications);
            }

            return null;
        }
    }

}
