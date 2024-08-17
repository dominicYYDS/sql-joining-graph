package com.dominicyyds.sqljoininggraph.ui;

import com.dominicyyds.sqljoininggraph.entity.JoinEntry;
import com.dominicyyds.sqljoininggraph.service.OutputService;
import com.dominicyyds.sqljoininggraph.service.Printer;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBTabbedPane;

import java.util.Collection;


public class SqlJoiningGraphToolWindow extends JBTabbedPane implements Printer {


    public static final String ID = "sql-joining-graph";

    private final Project project;

    private final GraphvizPropertyPanel settingsTab;
    private final GraphvizPanel graphvizPanel;
    private final DetailPanel detailTab;
    private final ToolWindow toolWindow;

    public SqlJoiningGraphToolWindow(Project project, ToolWindow toolWindow) {
        this.project = project;
        this.toolWindow = toolWindow;

        settingsTab = new GraphvizPropertyPanel(project);
        graphvizPanel = new GraphvizPanel(project);
        detailTab = new DetailPanel(project);

//        insertTab("Settings", null, settingsTab, null, 0);
        insertTab("Image", null, graphvizPanel, null, 0);
        insertTab("Detail", null, detailTab, null, 1);

        project.getService(OutputService.class).registerPrinter(this);
    }

    @Override
    public void printJoinEntries(Collection<JoinEntry> joinEntries) {
        ApplicationManager.getApplication().invokeLater(() -> {
            toolWindow.show(() -> setSelectedComponent(graphvizPanel));
        });
    }
}
