package net.codealizer.thegradebook.ui.login;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.ui.login.fragments.DistrictFragment;

public class LoginActivity extends AppCompatActivity {

    public static FragmentManager mLoginFragmentManager;

    public static ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialize();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "lol", Toast.LENGTH_LONG).show();
                LoginActivity.mLoginFragmentManager.popBackStackImmediate();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Initializes UI elements in the Login Activity
     */
    private void initialize() {
        //Initialize UI
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mActionBar = getSupportActionBar();

        DistrictFragment districtFragment = new DistrictFragment();
        mLoginFragmentManager = getSupportFragmentManager();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.login_container, districtFragment)
                .addToBackStack("DistrictFragment")
                .commit();
    }

}
