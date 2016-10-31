package net.codealizer.thegradebook.ui.login;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.apis.ic.RequestTask;
import net.codealizer.thegradebook.data.SessionManager;
import net.codealizer.thegradebook.data.StateSuggestion;
import net.codealizer.thegradebook.listeners.OnFindSuggestionsListener;
import net.codealizer.thegradebook.ui.dialogs.Alert;

import java.util.List;

public class DistrictSearchActivity extends AppCompatActivity implements FloatingSearchView.OnQueryChangeListener, OnFindSuggestionsListener, FloatingSearchView.OnSearchListener, FloatingSearchView.OnFocusChangeListener, AdapterView.OnItemClickListener {

    /**
     * Keys
     */
    public static final String KEY_STATE = "net.codealizer.ui.login.DistrictSearchActivity.KEY_STATE";

    /**
     * UI Device Objects
     */
    FloatingSearchView mSearchView;
    String mState;
    ListView list;
    TextView searchResultTitle;
    TextView noSearchResults;
    ArrayAdapter<StateSuggestion> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district_search);
    }

    @Override
    protected void onStart() {
        super.onStart();

        initialize();
    }

    @Override
    public void onSearchTextChanged(String oldQuery, String newQuery) {
        if (!oldQuery.equals("") && (newQuery.equals("") || newQuery.length() < 4)) {
            mSearchView.clearSuggestions();
        } else if (newQuery.length() > 3) {
            mSearchView.showProgress();

            RequestTask task = new RequestTask(this, RequestTask.OPTION_SEARCH_DISTRICT, SessionManager.mCoreManager, this, false, newQuery, mState);
            task.execute();

        }
    }

    @Override
    public void onFindSuggestion(List<StateSuggestion> suggestions) {
        mSearchView.swapSuggestions(suggestions);
        mSearchView.hideProgress();
    }

    @Override
    public void onNetworkError() {
        Alert.showNetworkErrorDialog(this);
    }

    @Override
    public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
        Alert.showDistrictClickedDialog(this, (StateSuggestion) searchSuggestion);
    }

    @Override
    public void onSearchAction(String currentQuery) {
        noSearchResults.setVisibility(View.INVISIBLE);
        if (currentQuery.length() > 3) {
            RequestTask task = new RequestTask(this, RequestTask.OPTION_SEARCH_DISTRICT, SessionManager.mCoreManager, new OnFindSuggestionsListener() {
                @Override
                public void onFindSuggestion(List<StateSuggestion> suggestions) {
                    adapter = new ArrayAdapter<>(DistrictSearchActivity.this, android.R.layout.simple_list_item_1, (suggestions));
                    list.setAdapter(adapter);

                    if (suggestions.isEmpty()) {
                        noSearchResults.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onNetworkError() {
                    Alert.showNetworkErrorDialog(DistrictSearchActivity.this);
                }
            }, false, currentQuery, mState);

            searchResultTitle.setVisibility(View.VISIBLE);
            task.execute();
        } else {
            Alert.showMessageDialog(this, "Error", "Try entering at least 4 letters of your school name");
        }
    }

    @Override
    public void onFocus() {
        searchResultTitle.setVisibility(View.INVISIBLE);
        noSearchResults.setVisibility(View.INVISIBLE);

        adapter = null;
        list.setAdapter(adapter);
    }

    @Override
    public void onFocusCleared() {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapter != null) {
            Alert.showDistrictClickedDialog(this, adapter.getItem(i));
        }
    }

    private void initialize() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setElevation(0);
        }

        mSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        list = (ListView) findViewById(R.id.district_search_list);
        searchResultTitle = (TextView) findViewById(R.id.searchResultsCaption);

        mSearchView.setDimBackground(false);
        mSearchView.setOnQueryChangeListener(this);
        mSearchView.setOnSearchListener(this);
        mSearchView.setOnFocusChangeListener(this);

        mState = getIntent().getStringExtra(KEY_STATE);

        searchResultTitle.setVisibility(View.INVISIBLE);
        noSearchResults = (TextView) findViewById(R.id.noSearchResults);
        noSearchResults.setVisibility(View.INVISIBLE);

        list.setOnItemClickListener(this);
    }
}
