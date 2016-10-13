package net.codealizer.thegradebook.ui.login.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.apis.ic.RequestTask;
import net.codealizer.thegradebook.data.Data;
import net.codealizer.thegradebook.listeners.onAuthenticationListener;
import net.codealizer.thegradebook.ui.dialogs.Alert;
import net.codealizer.thegradebook.ui.login.LoginActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class DistrictFragment extends Fragment implements View.OnClickListener, onAuthenticationListener {

    private static EditText districtCodeEditText;
    private static Button goButton;

    public DistrictFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_login_district, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();

    }

    @Override
    public void onClick(View view) {
        attemptLogin();
    }

    @Override
    public void onAuthenticated() {
        CredentialsFragment fragment = new CredentialsFragment();

        LoginActivity
                .mLoginFragmentManager
                .beginTransaction()
                .replace(R.id.login_container, fragment)
                .addToBackStack(getClass().getName())
                .commit();
    }

    @Override
    public void onUnauthorized() {
        districtCodeEditText.setError(getString(R.string.login_prompt_district_id_error));
    }

    private void attemptLogin() {
        String districtCode = districtCodeEditText.getText().toString();

        if (!districtCode.isEmpty() && districtCode.length() > 2) {
            RequestTask background = new RequestTask(getActivity(), RequestTask.OPTION_SET_DISTRICT,
                    Data.mCoreManager, this,districtCode);
            background.execute();
        } else {
            districtCodeEditText.setError(getString(R.string.error_field_required));
        }


    }

    private void initialize() {
        districtCodeEditText = (EditText) getView().findViewById(R.id.district_id);
        goButton = (Button) getView().findViewById(R.id.accept_district_id_button);

        LoginActivity.mActionBar.setDisplayHomeAsUpEnabled(false);
        LoginActivity.mActionBar.setDisplayShowHomeEnabled(false);
        LoginActivity.mActionBar.setTitle("Sign in");

        goButton.setOnClickListener(this);
    }


    @Override
    public void onNetworkError() {
        Alert.showNetworkErrorDialog(getActivity());
    }
}
