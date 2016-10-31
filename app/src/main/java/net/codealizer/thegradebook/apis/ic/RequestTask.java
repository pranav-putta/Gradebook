package net.codealizer.thegradebook.apis.ic;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.JsonSyntaxException;

import net.codealizer.thegradebook.apis.ic.classbook.ClassbookManager;
import net.codealizer.thegradebook.apis.ic.student.Student;
import net.codealizer.thegradebook.data.ServiceManager;
import net.codealizer.thegradebook.data.SessionManager;
import net.codealizer.thegradebook.data.StateSuggestion;
import net.codealizer.thegradebook.listeners.OnFindSuggestionsListener;
import net.codealizer.thegradebook.listeners.OnGradebookRetrievedListener;
import net.codealizer.thegradebook.listeners.OnICActionListener;
import net.codealizer.thegradebook.listeners.OnNotificationRetrievedListener;
import net.codealizer.thegradebook.listeners.OnStudentInformationRetrievedListener;
import net.codealizer.thegradebook.listeners.onAuthenticationListener;

import org.json.JSONException;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

public class RequestTask extends AsyncTask<Void, Void, Boolean> {

    public static final int OPTION_SET_DISTRICT = 0;
    public static final int OPTION_SET_CREDENTIALS = 1;
    public static final int OPTION_RETRIEVE_STUDENT_INFO = 2;
    public static final int OPTION_RETRIEVE_GRADES_INFO = 3;
    public static final int OPTION_RELOAD_ALL = 4;
    public static final int OPTION_SEARCH_DISTRICT = 5;
    public static final int OPTION_RETRIEVE_NOTIFICATIONS = 6;

    ProgressDialog progressDialog;
    private int option;
    private String param[];
    private OnICActionListener listener;
    private CoreManager mCoreManager;
    private boolean networkError = false;
    private boolean gradebookerror = false;
    private boolean searchError = false;

    private Context mContext;

    private Student student;
    private ClassbookManager gradebook;
    private List<StateSuggestion> suggestions;
    private Notifications notifications;

    private boolean useProgressDialog = true;

    public RequestTask(Context ctx, int option, CoreManager manager, OnICActionListener listener, String... param) {
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);

        this.option = option;
        this.mCoreManager = manager;
        this.param = param;
        this.listener = listener;
        this.mContext = ctx;
    }

    public RequestTask(Context ctx, int option, CoreManager manager, OnICActionListener listener, boolean useProgressDialog, String... param) {
        if (useProgressDialog) {
            progressDialog = new ProgressDialog(ctx);
            progressDialog.setTitle("Loading Gradebook...");
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
        }
        this.option = option;
        this.mCoreManager = manager;
        this.param = param;
        this.listener = listener;
        this.mContext = ctx;
        this.useProgressDialog = useProgressDialog;
    }

    public RequestTask(Context ctx, CoreManager manager, int option, OnICActionListener listener, String progressTitle, String progressMessage, String... param) {
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setTitle(progressTitle);
        progressDialog.setMessage(progressMessage);
        progressDialog.setCancelable(false);

        this.option = option;
        this.mCoreManager = manager;
        this.param = param;
        this.listener = listener;
        this.mContext = ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (useProgressDialog) {
            progressDialog.show();
        }
    }

    @Override
    protected void onPostExecute(Boolean valid) {
        super.onPostExecute(valid);

        if (!networkError && !gradebookerror) {

            switch (option) {
                case OPTION_SET_CREDENTIALS:
                    if (valid) {
                        ((onAuthenticationListener) listener).onAuthenticated();
                    } else {
                        ((onAuthenticationListener) listener).onUnauthorized();
                    }
                    break;
                case OPTION_SET_DISTRICT:
                    if (valid) {
                        ((onAuthenticationListener) listener).onAuthenticated();
                    } else {
                        ((onAuthenticationListener) listener).onUnauthorized();
                    }
                    break;
                case OPTION_RETRIEVE_STUDENT_INFO:
                    ((OnStudentInformationRetrievedListener) listener).onStudentInformationRetrieved(student);
                    break;
                case OPTION_RETRIEVE_GRADES_INFO:
                    ((OnGradebookRetrievedListener) listener).onGradebookRetrieved(gradebook);
                    break;
                case OPTION_RELOAD_ALL:
                    ((OnGradebookRetrievedListener) listener).onGradebookRetrieved(gradebook);
                    break;
                case OPTION_SEARCH_DISTRICT:
                    ((OnFindSuggestionsListener) listener).onFindSuggestion(suggestions);
                    break;
                case OPTION_RETRIEVE_NOTIFICATIONS:
                    ((OnNotificationRetrievedListener) listener).onNotificationRetrieved(notifications);
            }

        } else if (!networkError && gradebookerror) {
            RequestTask task = new RequestTask(mContext, SessionManager.mCoreManager, RequestTask.OPTION_RELOAD_ALL, listener, "Please Wait", "Downloading gradebook...");
            task.execute();
        } else {
            listener.onNetworkError();
        }

        if (useProgressDialog)
            progressDialog.dismiss();

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        boolean success = false;

        if (ServiceManager.isNetworkAvailable(mContext)) {
            switch (option) {
                case OPTION_SET_DISTRICT:
                    try {
                        success = mCoreManager.setDistrictCode(param[0]);
                    } catch (IOException | JsonSyntaxException e) {
                        success = false;
                    }
                    break;
                case OPTION_SET_CREDENTIALS:
                    try {
                        success = mCoreManager.attemptLogin(param[0], param[1], mCoreManager.getDistrictInfo());
                    } catch (IOException e) {
                        networkError = true;
                        e.printStackTrace();
                    }
                    break;
                case OPTION_RETRIEVE_STUDENT_INFO:
                    try {
                        student = mCoreManager.retrieveStudentInformation();
                    } catch (IOException | SAXException | JSONException | ParserConfigurationException e) {
                        networkError = true;
                        e.printStackTrace();
                    }
                    break;
                case OPTION_RETRIEVE_GRADES_INFO:
                    try {
                        gradebook = mCoreManager.reloadData(mContext);
                    } catch (IOException e) {
                        success = false;
                        networkError = true;
                        e.printStackTrace();
                    } catch (ParserConfigurationException | SAXException e) {
                        gradebookerror = true;
                    }
                    break;
                case OPTION_RELOAD_ALL:
                    try {
                        gradebook = mCoreManager.reloadAll(mContext);
                    } catch (JSONException | SAXException | ParserConfigurationException e) {
                        gradebookerror = true;
                    } catch (IOException e) {
                        success = false;
                        networkError = true;
                        e.printStackTrace();
                    }
                    break;
                case OPTION_SEARCH_DISTRICT:
                    try {
                        suggestions = mCoreManager.searchDistricts(param[0], param[1]);
                    } catch (IOException ex) {
                        searchError = true;
                    }
                    break;
                case OPTION_RETRIEVE_NOTIFICATIONS:
                    try {
                        notifications = mCoreManager.retrieveNotifications();
                    } catch (SAXException | ParserConfigurationException | IOException e) {
                        networkError = true;
                    }
            }
        } else {
            networkError = true;
        }

        return success;
    }
}