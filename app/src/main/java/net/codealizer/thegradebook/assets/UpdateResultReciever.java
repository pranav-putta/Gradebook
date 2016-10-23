package net.codealizer.thegradebook.assets;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.util.Pair;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookActivity;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookManager;
import net.codealizer.thegradebook.data.SessionManager;
import net.codealizer.thegradebook.listeners.OnGradebookRetrievedListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Pranav on 10/22/16.
 */

public class UpdateResultReciever extends BroadcastReceiver implements OnGradebookRetrievedListener
{

	public static final int REQUEST_CODE = 285548;
	public static final String ACTION = "net.codealizer.thegradebook.alarm";

	private Context context;
	private ArrayList<Pair<String, ClassbookActivity>> oldTasks;

	private SharedPreferences preferences;
	private MediaPlayer mMediaPlayer;

	@Override
	public void onReceive(final Context context, Intent intent)
	{
		SessionManager.loadData(context);
		android.os.Debug.waitForDebugger();  // this line is key

		this.context = context;
		this.oldTasks = SessionManager.mCoreManager.getAllActivities();


		try
		{
			ClassbookManager classbookManager = SessionManager.mCoreManager.reloadAll(context);
			onGradebookRetrieved(classbookManager);
		} catch (Exception e)
		{
			onNetworkError();
		}

	}

	@Override
	public void onGradebookRetrieved(ClassbookManager gradebookManager)
	{
		android.os.Debug.waitForDebugger();  // this line is key

		try
		{
			preferences = PreferenceManager.getDefaultSharedPreferences(context);

			if (oldTasks != null)
			{
				Collection<Pair<String, ClassbookActivity>> old = oldTasks;
				Collection<Pair<String, ClassbookActivity>> newTasks = SessionManager.mCoreManager.getAllActivities();

				newTasks.removeAll(old);

				ArrayList<Pair<String, ClassbookActivity>> newUpdates = new ArrayList<>(newTasks);

				if (newUpdates.size() > 0 && preferences.getBoolean("notifications_new_message", true))
				{
					buildNotifications(newUpdates);
				}
			}


			Intent i = new Intent(context, UpdateService.class);
			context.startService(i);
		} catch (Exception ex)
		{

		}
	}

	@Override
	public void onNetworkError()
	{

	}

	private void buildNotifications(ArrayList<Pair<String, ClassbookActivity>> updates)
	{

		for (Pair<String, ClassbookActivity> activityPair : updates)
		{

			String className = activityPair.first;
			ClassbookActivity activity = activityPair.second;

			NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
			builder.setSmallIcon(R.mipmap.ic_launcher);
			builder.setContentTitle("New Assignment");

			String message;

			if (activity.letterGrade != null && activity.letterGrade.contains("A"))
			{
				message = "You received an " + activity.letterGrade + " for " + activity.name + " in " + className;
			} else if (activity.letterGrade != null)
			{
				message = "You received a " + activity.letterGrade + " for " + activity.name + " in " + className;
			} else
			{
				message = "You received a " + activity.letterGrade + " for " + activity.name + " in " + className;
			}

			builder.setContentText(message);

			NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			manager.notify(1, builder.build());

			if (preferences.getBoolean("notifications_new_message_vibrate", true))
			{
				Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
				vibrator.vibrate(500);
			}

			if (!preferences.getString("notifications_new_message_ringtone", "").equals(""))
			{
				playSound(context, Uri.parse(preferences.getString("ringtone", "default ringtone")));
			}
		}

	}

	private void playSound(Context context, Uri alert)
	{
		mMediaPlayer = new MediaPlayer();
		try
		{
			mMediaPlayer.setDataSource(context, alert);
			final AudioManager audioManager = (AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0)
			{
				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
				mMediaPlayer.prepare();
				mMediaPlayer.start();
			}
		} catch (IOException e)
		{
			System.out.println("OOPS");
		}
	}
}
