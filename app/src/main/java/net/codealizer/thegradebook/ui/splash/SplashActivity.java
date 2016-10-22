package net.codealizer.thegradebook.ui.splash;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.codealizer.thegradebook.ui.classbook.MainActivity;
import net.codealizer.thegradebook.data.Data;
import net.codealizer.thegradebook.ui.login.LoginActivity;
import net.codealizer.thegradebook.R;

public class SplashActivity extends AppCompatActivity implements Runnable {

    /**
     * Timeout delay for the Splash Activity
     */
    private static final int SPLASH_DELAY = 1000;

    /**
     * Intent to run proceeding the Splash Activity
     */
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /**
         * Check if the student has already been logged in.
         *
         * If student is logged in, run the MainActivity
         *
         * If the student is not logged in, run the LoginActivity
         */
        if (Data.isStudentLoggedIn(this)) {
            intent = new Intent(this, MainActivity.class);
            Data.loadData(this);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }

        new Handler().postDelayed(this, SPLASH_DELAY);

    }

    /**
     * Run implementation to switch the activity from the SplashActivity to the next Activity
     */
    @Override
    public void run() {
        startActivity(intent);
        finish();
    }
}
