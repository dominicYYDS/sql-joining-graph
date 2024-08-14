package com.dominicyyds.sqljoininggraph.ui;

import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBTabbedPane;


public class SqlJoiningGraphToolWindow extends JBTabbedPane {

    private final Project project;

    private final GraphvizPropertyPanel settingsTab;
    private final DetailPanel detailTab;

    public SqlJoiningGraphToolWindow(Project project) {
        this.project = project;

        settingsTab = new GraphvizPropertyPanel(project);
        detailTab = new DetailPanel(project);

        insertTab("Settings", null, settingsTab, null, 0);
//        insertTab(message(TOOLWINDOW_TITLE_DETAILS), null, detailTab, null, 1);

    }
}
