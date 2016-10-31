package net.codealizer.thegradebook.listeners;


import net.codealizer.thegradebook.apis.ic.classbook.ClassbookTask;

/**
 * Created by Pranav on 10/10/16.
 */

public interface OnTermSelectedListener {

    void onTermSelected(ClassbookTask task);

    void onTermUnavailable();

}
