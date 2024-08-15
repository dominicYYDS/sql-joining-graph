package com.dominicyyds.sqljoininggraph.ui;

import com.dominicyyds.sqljoininggraph.entity.JoinEntry;
import com.dominicyyds.sqljoininggraph.service.OutputService;
import com.dominicyyds.sqljoininggraph.service.Printer;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBPanel;

import java.util.Collection;

public class DetailPanel extends JBPanel<DetailPanel> implements Printer {

    private final Project project;

    public DetailPanel(Project project) {
        this.project = project;
        OutputService outputService = project.getService(OutputService.class);
        outputService.registerPrinter(this);
    }

    @Override
    public void printJoinEntries(Collection<JoinEntry> joinEntries) {

    }
}
