package net.codealizer.thegradebook.assets;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by Pranav on 10/22/16.
 */

public class BootBroadcastReceiver extends WakefulBroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("notifications_new_message", true))
		{
			Intent startServiceIntent = new Intent(context, UpdateService.class);

			final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, UpdateResultReciever.REQUEST_CODE, startServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);

			long firstMillis = System.currentTimeMillis();
			AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

			int time = PreferenceManager.getDefaultSharedPreferences(context).getInt("sync_frequency", 30);

			if (time != -1)
			{
				alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, time * (60_000), pendingIntent);
			}
			startWakefulService(context, startServiceIntent);
		}
	}
}
