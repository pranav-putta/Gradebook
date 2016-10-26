package net.codealizer.thegradebook.listeners;


import net.codealizer.thegradebook.apis.ic.xml.classbook.ClassbookTask;
import net.codealizer.thegradebook.apis.ic.xml.schedule.Term;

/**
 * Created by Pranav on 10/10/16.
 */

public interface OnTermSelectedListener {

    void onTermSelected(ClassbookTask task);

    void onTermUnavailable();

}
