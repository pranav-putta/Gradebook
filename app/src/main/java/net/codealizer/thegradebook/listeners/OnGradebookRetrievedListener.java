package net.codealizer.thegradebook.listeners;

import net.codealizer.thegradebook.apis.ic.classbook.ClassbookManager;

/**
 * Created by Pranav on 10/9/16.
 */

public interface OnGradebookRetrievedListener extends OnICActionListener {

    void onGradebookRetrieved(ClassbookManager gradebookManager);

}
