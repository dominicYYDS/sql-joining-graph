package com.dominicyyds.sqljoininggraph.compatibility;

import com.dominicyyds.sqljoininggraph.utils.SqlJoiningGraphBundle;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import static com.dominicyyds.sqljoininggraph.constants.MyConstants.NOTIFICATION_GROUP;

public class NotificationAdapter {

    private final Project project;

    private NotificationAdapter(Project project) {
        this.project = project;
    }

    public static NotificationAdapter getInstance(Project project) {
        return new NotificationAdapter(project);
    }

    public void notify(@NotNull NotificationType type, @NotNull String title, @PropertyKey(resourceBundle = SqlJoiningGraphBundle.BUNDLE) String contentKey, Object @NotNull ... args) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup(NOTIFICATION_GROUP)
                .createNotification(title, null, SqlJoiningGraphBundle.message(contentKey, args), type)
                .notify(project);
    }

    public void notify(String title, String content, NotificationType type) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup(NOTIFICATION_GROUP)
                .createNotification(title, null, content, type)
                .notify(project);
    }

}
