package net.codealizer.thegradebook.listeners;

import java.io.Serializable;

/**
 * Created by Pranav on 10/8/16.
 */

public interface onAuthenticationListener extends OnICActionListener, Serializable {

    public static final String KEY = "net.codealizer.thegradebook.listeners.onAuthenticationListener.KEY";

    void onAuthenticated();
    void onUnauthorized();

}
