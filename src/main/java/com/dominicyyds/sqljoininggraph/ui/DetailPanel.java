package com.dominicyyds.sqljoininggraph.ui;

import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBPanel;

public class DetailPanel extends JBPanel<DetailPanel> {

    private final Project project;

    public DetailPanel(Project project) {
        this.project = project;
    }
}
