package net.codealizer.thegradebook.ui.launch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.codealizer.thegradebook.data.SessionManager;
import net.codealizer.thegradebook.ui.classbook.MainActivity;
import net.codealizer.thegradebook.ui.login.LoginActivity;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //If the user is logged in, start the MainActivity
        //Otherwise start the LoginActivity.
        Intent intent;
        if (SessionManager.isStudentLoggedIn(this) && SessionManager.hasLatestUpdate(this)) {
            intent = new Intent(this, MainActivity.class);
            SessionManager.loadData(this);
        } else {
            SessionManager.logout(this);
            intent = new Intent(this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }

}
