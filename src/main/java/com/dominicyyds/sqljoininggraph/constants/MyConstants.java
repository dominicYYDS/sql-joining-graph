package com.dominicyyds.sqljoininggraph.constants;

import com.intellij.openapi.application.PathManager;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MyConstants {

    public static final String NOTIFICATION_GROUP = "SqlJoiningGraph Notification Group";

    public static final String NOTIFICATION_TITLE = "SqlJoiningGraph";

    public static final String PLUGIN_ID = "sql-joining-graph";

    public static Path getPluginLibDir() {
        return Paths.get(PathManager.getPluginsPath(), PLUGIN_ID);
    }
}
