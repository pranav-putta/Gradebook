package net.codealizer.thegradebook.ui.login.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.apis.ic.xml.RequestTask;
import net.codealizer.thegradebook.apis.ic.xml.district.DistrictInfo;
import net.codealizer.thegradebook.apis.ic.xml.student.Student;
import net.codealizer.thegradebook.data.SessionManager;
import net.codealizer.thegradebook.listeners.OnStudentInformationRetrievedListener;
import net.codealizer.thegradebook.listeners.onAuthenticationListener;
import net.codealizer.thegradebook.ui.classbook.MainActivity;
import net.codealizer.thegradebook.ui.dialogs.Alert;
import net.codealizer.thegradebook.ui.login.LoginActivity;

/**
 * Created by Pranav on 10/8/16.
 */

public class CredentialsFragment extends Fragment implements View.OnClickListener, onAuthenticationListener, OnStudentInformationRetrievedListener
{

	private Button signIn;
	private TextInputLayout username;
	private TextInputLayout password;
	private CheckBox rememberMe;

	public CredentialsFragment()
	{

	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_login_credentials, container, false);
	}

	@Override
	public void onStart()
	{
		super.onStart();

		initialize();
	}


	private void initialize()
	{
		LoginActivity.mActionBar.setDisplayHomeAsUpEnabled(true);
		LoginActivity.mActionBar.setDisplayShowHomeEnabled(true);
		LoginActivity.mActionBar.setTitle("Sign in to " + SessionManager.mCoreManager.getDistrictInfo().getDistrictName());

		signIn = (Button) getView().findViewById(R.id.email_sign_in_button);
		username = (TextInputLayout) getView().findViewById(R.id.username);
		password = (TextInputLayout) getView().findViewById(R.id.password);
		rememberMe = (CheckBox) getView().findViewById(R.id.rememberMe);

		signIn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{

		String username = this.username.getEditText().getText().toString();
		String password = this.password.getEditText().getText().toString();

		if (!username.isEmpty() && !password.isEmpty())
		{
			this.username.setErrorEnabled(false);
			this.password.setErrorEnabled(false);

			RequestTask task = new RequestTask(getActivity(), RequestTask.OPTION_SET_CREDENTIALS, SessionManager.mCoreManager,
					this, username, password);
			task.execute();


		} else
		{
			if (username.isEmpty())
			{
				this.username.setErrorEnabled(true);
				this.username.setError(getString(R.string.error_field_required));
			} else
			{
				this.username.setErrorEnabled(true);
				this.password.setError(getString(R.string.error_field_required));
			}
		}
	}

	@Override
	public void onAuthenticated()
	{
		RequestTask task = new RequestTask(getActivity(), RequestTask.OPTION_RETRIEVE_STUDENT_INFO,
				SessionManager.mCoreManager, this);
		task.execute();
	}

	@Override
	public void onUnauthorized()
	{
		password.setError(getString(R.string.error_incorrect_password));
		Alert.showInvalidPasswordDialog(getActivity());
	}

	@Override
	public void onNetworkError()
	{
		Alert.showNetworkErrorDialog(getActivity());
	}


	@Override
	public void onStudentInformationRetrieved(Student student)
	{
		String mUsername = this.username.getEditText().getText().toString();
		String mPassword = this.password.getEditText().getText().toString();
		boolean isRememberMeChecked = this.rememberMe.isChecked();
		DistrictInfo districtInfo = SessionManager.mCoreManager.getDistrictInfo();

		if (isRememberMeChecked)
		{
			SessionManager.setCredentials(mUsername, mPassword, districtInfo, student, getActivity());
		}
		Toast.makeText(getActivity(), "Successful Login", Toast.LENGTH_LONG).show();

		Intent intent = new Intent(getActivity(), MainActivity.class);
		getActivity().startActivity(intent);
	}
}
