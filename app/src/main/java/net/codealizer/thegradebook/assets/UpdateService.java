package net.codealizer.thegradebook.assets;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by Pranav on 10/22/16.
 */

public class UpdateService extends IntentService {

    public UpdateService() {
        super("update-service");
    }

    public UpdateService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    protected void onHandleIntent(Intent intent) {

        WakefulBroadcastReceiver.completeWakefulIntent(intent);
    }
}
