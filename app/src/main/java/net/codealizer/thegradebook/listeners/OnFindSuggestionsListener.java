package net.codealizer.thegradebook.listeners;

import net.codealizer.thegradebook.data.StateSuggestion;

import java.util.List;

/**
 * Created by Pranav on 10/16/16.
 */

public interface OnFindSuggestionsListener extends OnICActionListener {
    void onFindSuggestion(List<StateSuggestion> suggestions);
}
