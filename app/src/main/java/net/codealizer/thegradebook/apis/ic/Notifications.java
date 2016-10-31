package net.codealizer.thegradebook.apis.ic;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

/**
 * Created by Pranav on 10/28/16.
 */

public class Notifications {

    private ArrayList<StudentNotification> notifications;
    private String xml;

    public Notifications(ArrayList<StudentNotification> notifications, String xml) {
        this.notifications = new ArrayList<>(notifications);
        this.xml = xml;
    }

    public ArrayList<StudentNotification> getNotifications() {
        try {
            if (notifications.size() == 0) {
                Document xmlDocument = CoreManager.getDocument(xml);
                NodeList notifications = xmlDocument.getElementsByTagName("Notification");
                if (notifications.getLength() > 0) {
                    for (int i = 0; i < notifications.getLength(); i++) {
                        this.notifications.add(new StudentNotification((Element) notifications.item(i)));
                    }
                }
            }
        } catch (Exception ignored) {
        }

        return notifications;
    }

    public void setNotifications(ArrayList<StudentNotification> notifications) {
        this.notifications = notifications;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }
}
