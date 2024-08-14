package com.dominicyyds.sqljoininggraph.ui;

import com.dominicyyds.sqljoininggraph.entity.JoinEntry;
import com.dominicyyds.sqljoininggraph.service.OutputService;
import com.dominicyyds.sqljoininggraph.service.Printer;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBTabbedPane;

import javax.swing.*;
import java.util.Collection;


public class SqlJoiningGraphToolWindow extends JBTabbedPane implements Printer {

    private final Project project;

    private final GraphvizPropertyPanel settingsTab;
    private final GraphvizPanel graphvizPanel;
    private final DetailPanel detailTab;

    public SqlJoiningGraphToolWindow(Project project) {
        this.project = project;

        settingsTab = new GraphvizPropertyPanel(project);
        graphvizPanel = new GraphvizPanel(project);
        detailTab = new DetailPanel(project);

//        insertTab("Settings", null, settingsTab, null, 0);
        insertTab("Image", null, graphvizPanel, null, 0);
//        insertTab("Detail", null, detailTab, null, 2);

        project.getService(OutputService.class).registerPrinter(this);
    }

    @Override
    public void printJoinEntries(Collection<JoinEntry> joinEntries) {
        SwingUtilities.invokeLater(() -> setSelectedComponent(graphvizPanel));
    }
}
