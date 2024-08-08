package com.dominicyyds.sqljoininggraph.compatibility;

import com.intellij.notification.*;
import com.intellij.openapi.project.Project;

import static com.dominicyyds.sqljoininggraph.constants.MyConstants.NOTIFICATION_GROUP;

public class NotificationAdapter {

    private final Project project;

    private NotificationAdapter(Project project) {
        this.project = project;
    }

    public static NotificationAdapter getInstance(Project project) {
        return new NotificationAdapter(project);
    }

    public void notify(String title, String content, NotificationType type) {
        NotificationGroup group = new NotificationGroup(NOTIFICATION_GROUP, NotificationDisplayType.BALLOON);
        Notification notification = group.createNotification(title, null, content, type);
        Notifications.Bus.notify(notification, project);
    }

}
