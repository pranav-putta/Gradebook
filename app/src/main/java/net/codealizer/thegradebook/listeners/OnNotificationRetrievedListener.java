package net.codealizer.thegradebook.listeners;

import net.codealizer.thegradebook.apis.ic.Notifications;

/**
 * Created by Pranav on 10/28/16.
 */

public interface OnNotificationRetrievedListener extends OnICActionListener {

    void onNotificationRetrieved(Notifications notifications);

}
