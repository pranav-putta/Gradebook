package net.codealizer.thegradebook.data;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.apis.ic.CoreManager;
import net.codealizer.thegradebook.apis.ic.Notifications;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookManager;
import net.codealizer.thegradebook.apis.ic.district.DistrictInfo;
import net.codealizer.thegradebook.apis.ic.student.Student;
import net.codealizer.thegradebook.assets.UpdateResultReciever;
import net.codealizer.thegradebook.ui.launch.LaunchActivity;

/**
 * Created by Pranav on 10/8/16.
 */

public class SessionManager {

    /**
     * Infinite Campus Core Manager Instance
     */
    public static CoreManager mCoreManager = new CoreManager();

    /**
     * Long value representing the last time that the application has been refreshed with new data
     */
    private static long reloadedOnce = 0;

    /**
     * Notification AlarmManager
     */
    public static AlarmManager alarm;

    /**
     * Pending intent which manages the notificatino Alarm Manager
     */
    public static PendingIntent pendingIntent;

    /**
     * Creates and initializes static CoreManager by loading data from the application's
     * Shared Preferences
     *
     * @param ac Context to access the Shared Preferences
     */
    public static void loadData(Context ac) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ac.getApplicationContext());

        String districtJson = prefs.getString(ac.getString(R.string.key_data_district_info), null);
        String studentJson = prefs.getString(ac.getString(R.string.key_data_student_information), null);
        String gradebookJson = prefs.getString(ac.getString(R.string.key_data_gradebook_information), null);
        String cookies = prefs.getString(ac.getString(R.string.key_data_server_cookies), null);
        String notificationsXML = prefs.getString(ac.getString(R.string.key_data_notifications), null);

        mCoreManager = new CoreManager(districtJson, studentJson, gradebookJson, cookies, notificationsXML);
    }

    /**
     * Clears the Shared Preference data, and logs out the user from the application.
     * The application is reloaded, and the LaunchActivity is called
     *
     * @param activity Context to access the Shared Preferences, and reloading the application
     */
    public static void logout(Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        prefs.edit().clear().apply();

        mCoreManager = new CoreManager();
        reloadedOnce = 0;

        try {
            activity.stopService(new Intent(activity, UpdateResultReciever.class));
            alarm.cancel(pendingIntent);
        } catch (Exception ex) {

        }

        int versionCode = 0;
        try {
            PackageInfo packageInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        prefs.edit().putInt(activity.getString(R.string.key_data_version), versionCode).apply();

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
    public static String getUsername(Context activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());

        return prefs.getString(activity.getString(R.string.key_data_username), null);
    }

    /**
     * Retrieves the student password from Shared Preferences
     *
     * @param activity Context to access the Shared Preferences
     * @return student password
     */
    public static String getPassword(Context activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());

        return prefs.getString(activity.getString(R.string.key_data_password), null);
    }

    /**
     * Retrieves boolean value representing if the gradeBook data has been downloaded and saved
     *
     * @param activity Context to access the Shared Preferences
     * @return boolean value representing if gradebook data and information has been downloaded
     */
    public static boolean hasGradebookDataStored(Context activity) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
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
    public static void setCredentials(String username, String password, DistrictInfo info, Student student, Activity activity) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
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
    public static void saveGradebook(Context activity, ClassbookManager gradeBook) {
        String json = gradeBook.getXML();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(activity.getString(R.string.key_data_server_cookies), mCoreManager.cookies);
        editor.putString(activity.getString(R.string.key_data_gradebook_information), json);
        editor.putBoolean(activity.getString(R.string.key_data_gradebook_data_stored), true);

        editor.apply();

        mCoreManager.gradebookManager = gradeBook;

        reloadedOnce = System.currentTimeMillis();
    }

    /**
     * Saves notification values retrieved by the server into SharedPreferences
     *
     * @param a             Application context in which to store the data
     * @param notifications List of notifications, which will be processed into XML data
     */
    public static void saveNotifications(Context a, Notifications notifications) {
        String xml = notifications.getXml();

        if (notifications.getNotifications().size() > 0) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(a.getApplicationContext());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(a.getString(R.string.key_data_notifications), xml);

            editor.apply();

            mCoreManager.setNotifications(notifications);
            mCoreManager.notifications = notifications;
        }
    }

    /**
     * Checks if the user has the latest update of the application sync
     *
     * @param activity Application context in which to retrieve the data
     * @return Boolean value, representing if the user has the latest update or not
     */
    public static boolean hasLatestUpdate(Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        int versionCode = 0;
        try {
            PackageInfo packageInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return prefs.getInt(activity.getString(R.string.key_data_version), 0) == versionCode;
    }

    /**
     * Retrieves a boolean value from Shared Preferences determining if the user's information has
     * been put in the database
     *
     * @param activity Context to access the Shared Preferences
     * @return boolean value representing if the student has been logged in
     */
    public static boolean isStudentLoggedIn(Context activity) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        return sharedPref.getBoolean(activity.getString(R.string.key_data_is_logged_in), false);
    }

    /**
     * Checks the static value for @reloadedOnce to determine if the application should be refreshed.
     * This refresh takes place every 30 minutes
     *
     * @return boolean value representing if the application should refresh data
     */
    public static boolean shouldReload() {
        long time = System.currentTimeMillis();

        if (time - reloadedOnce > 1_800_000) {
            return true;
        }

        return false;
    }

}
