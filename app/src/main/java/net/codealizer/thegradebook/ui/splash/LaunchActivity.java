package net.codealizer.thegradebook.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.data.SessionManager;
import net.codealizer.thegradebook.ui.classbook.MainActivity;
import net.codealizer.thegradebook.ui.login.LoginActivity;

public class LaunchActivity extends AppCompatActivity
{

	/**
	 * Intent to run proceeding the Splash Activity
	 */
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		/**
		 * Check if the student has already been logged in.
		 *
		 * If student is logged in, run the MainActivity
		 *
		 * If the student is not logged in, run the LoginActivity
		 */
		if (SessionManager.isStudentLoggedIn(this))
		{
			intent = new Intent(this, MainActivity.class);
			SessionManager.loadData(this);
		} else
		{
			intent = new Intent(this, LoginActivity.class);
		}
		startActivity(intent);
		finish();

	}

}
