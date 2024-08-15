package com.dominicyyds.sqljoininggraph.service;

import com.dominicyyds.sqljoininggraph.entity.JoinEntry;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service(Service.Level.PROJECT)
public class OutputService implements Printer {

    private final Project project;
    private final List<Printer> printers;

    public OutputService(Project project) {
        this.project = project;
        this.printers = new ArrayList<>();
    }

    public void registerPrinter(Printer printer) {
        printers.add(printer);
    }

    @Override
    public void printJoinEntries(Collection<JoinEntry> joinEntries) {
        printers.forEach(p -> p.printJoinEntries(joinEntries));
    }
}
