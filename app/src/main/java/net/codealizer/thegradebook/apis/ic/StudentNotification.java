package net.codealizer.thegradebook.apis.ic;

import org.w3c.dom.Element;

import java.io.Serializable;

/**
 * Created by Pranav on 10/28/16.
 */

public class StudentNotification implements Serializable {

    private String notificationID;
    private String creationTimestamp;
    private int notificationTypeiD;
    private String notificationText;
    private String linkContext;
    private String displayDate;

    public StudentNotification(Element notification) {
        notificationID = notification.getAttribute("notificationID");
        creationTimestamp = notification.getAttribute("creationTimestamp");
        notificationTypeiD = Integer.parseInt(notification.getAttribute("notificationTypeID"));
        notificationText = notification.getAttribute("notificationText");
        linkContext = notification.getAttribute("linkContext");
        displayDate = notification.getAttribute("displayedDate");

        if (!linkContext.contains("sectionID") && notification.hasAttribute("condenseKey")) {
            linkContext = notification.getAttribute("condenseKey");
        }
    }

    public StudentNotification() {
        notificationID = "1";
        creationTimestamp = "5";
        notificationTypeiD = 1;
        notificationText = "Lol you died";
        displayDate = "lmao";
    }

    public String getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(String notificationID) {
        this.notificationID = notificationID;
    }

    public String getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(String creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public int getNotificationTypeiD() {
        return notificationTypeiD;
    }

    public void setNotificationTypeiD(int notificationTypeiD) {
        this.notificationTypeiD = notificationTypeiD;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public String getLinkContext() {
        return linkContext;
    }

    public void setLinkContext(String linkContext) {
        this.linkContext = linkContext;
    }

    public String getDisplayDate() {
        return displayDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StudentNotification) {
            return getNotificationID().equals(((StudentNotification) obj).getNotificationID());
        }

        return false;
    }
}
