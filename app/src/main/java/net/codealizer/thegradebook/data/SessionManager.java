package net.codealizer.thegradebook.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.apis.ic.CoreManager;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookManager;
import net.codealizer.thegradebook.apis.ic.district.DistrictInfo;
import net.codealizer.thegradebook.apis.ic.student.Student;
import net.codealizer.thegradebook.ui.splash.LaunchActivity;

/**
 * Created by Pranav on 10/8/16.
 */

public class SessionManager
{

	/**
	 * Infinite Campus Core Manager Instance
	 */
	public static CoreManager mCoreManager = new CoreManager();
	/**
	 * Boolean value representing if the Main Application has been refreshed
	 */
	public static boolean reloadedOnce = false;

	/**
	 * Creates and initializes static CoreManager by loading data from the application's
	 * Shared Preferences
	 *
	 * @param ac Context to access the Shared Preferences
	 */
	public static void loadData(Context ac)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ac);

		String districtJson = prefs.getString(ac.getString(R.string.key_data_district_info), null);
		String studentJson = prefs.getString(ac.getString(R.string.key_data_student_information), null);
		String gradebookJson = prefs.getString(ac.getString(R.string.key_data_gradebook_information), null);
		String cookies = prefs.getString(ac.getString(R.string.key_data_server_cookies), null);

		mCoreManager = new CoreManager(districtJson, studentJson, gradebookJson, cookies);
	}


	/**
	 * Clears the Shared Preference data, and logs out the user from the application.
	 * The application is reloaded, and the LaunchActivity is called
	 *
	 * @param activity Context to access the Shared Preferences, and reloading the application
	 */
	public static void logout(Activity activity)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
		prefs.edit().clear().apply();

		mCoreManager = new CoreManager();
		reloadedOnce = false;

		Intent intent = new Intent(activity, LaunchActivity.class);
		activity.startActivity(intent);
		activity.finish();
	}

	/**
	 * Retrieves the student username from Shared Preferences
	 *
	 * @param activity Context to access the Shared Preferences
	 * @return student username
	 */
	public static String getUsername(Context activity)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);

		return prefs.getString(activity.getString(R.string.key_data_username), null);
	}

	/**
	 * Retrieves the student password from Shared Preferences
	 *
	 * @param activity Context to access the Shared Preferences
	 * @return student password
	 */
	public static String getPassword(Context activity)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);

		return prefs.getString(activity.getString(R.string.key_data_password), null);
	}

	/**
	 * Retrieves a boolean value from Shared Preferences determining if the user's information has
	 * been put in the database
	 *
	 * @param activity Context to access the Shared Preferences
	 * @return boolean value representing if the student has been logged in
	 */
	public static boolean isStudentLoggedIn(Activity activity)
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
		return sharedPref.getBoolean(activity.getString(R.string.key_data_is_logged_in), false);
	}

	/**
	 * Retrieves boolean value representing if the gradeBook data has been downloaded and saved
	 *
	 * @param activity Context to access the Shared Preferences
	 * @return boolean value representing if gradebook data and information has been downloaded
	 */
	public static boolean hasGradebookDataStored(Activity activity)
	{
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
		return sharedPreferences.getBoolean("GRADEBOOK_DATA_STORED", false);
	}

	/**
	 * Stores student information as serializable objects into the application's
	 * Shared Preferences. This data is only stored if the user is logged in with
	 * the "Remember me" function is checked
	 *
	 * @param username Student username for Infinite Campus
	 * @param password Student password for Infinite Campus
	 * @param info     District Information for the student's Infinite Campus
	 * @param student  Student details and information
	 * @param activity Context to access the Shared Preferences
	 */
	public static void setCredentials(String username, String password, DistrictInfo info, Student student, Activity activity)
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(activity.getString(R.string.key_data_username), username);
		editor.putString(activity.getString(R.string.key_data_password), password);
		editor.putString(activity.getString(R.string.key_data_district_info), info.toJson());
		editor.putString(activity.getString(R.string.key_data_student_information), student.mJson);
		editor.putBoolean(activity.getString(R.string.key_data_is_logged_in), true);
		editor.putString(activity.getString(R.string.key_data_server_cookies), mCoreManager.cookies);

		editor.apply();

	}

	/**
	 * Saves gradeBook data and information into the application's Shared Preferences
	 *
	 * @param activity  Context to access the Shared Preferences
	 * @param gradeBook ClassbookManager class containing grade book information
	 */
	public static void saveGradebook(Activity activity, ClassbookManager gradeBook)
	{
		String json = gradeBook.getJson();

		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(activity.getString(R.string.key_data_server_cookies), mCoreManager.cookies);
		editor.putString(activity.getString(R.string.key_data_gradebook_information), json);
		editor.putBoolean(activity.getString(R.string.key_data_gradebook_data_stored), true);

		editor.apply();

	}


}
