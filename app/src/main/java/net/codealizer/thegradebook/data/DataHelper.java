package net.codealizer.thegradebook.data;

import android.content.Context;
import android.widget.Filter;

import net.codealizer.thegradebook.listeners.OnFindSuggestionsListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pranav on 10/16/16.
 */

public class DataHelper
{

	public static void findSuggestions(Context ctx, String query, String state, int limit, final long simulatedDelay, final OnFindSuggestionsListener listener) throws IOException
	{

		final ArrayList<StateSuggestion> suggestions = SessionManager.mCoreManager.searchDistricts(query, state);

		new Filter()
		{
			@Override
			protected FilterResults performFiltering(CharSequence c)
			{
				FilterResults results = new FilterResults();
				results.values = suggestions;
				results.count = suggestions.size();

				return results;
			}

			@Override
			protected void publishResults(CharSequence charSequence, FilterResults filterResults)
			{
				if (listener != null)
				{
					listener.onFindSuggestion((List<StateSuggestion>) filterResults.values);
				}
			}
		}.filter(query);
	}
}
