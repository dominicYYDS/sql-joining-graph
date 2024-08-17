package com.dominicyyds.sqljoininggraph.ui;

import com.dominicyyds.sqljoininggraph.entity.JoinEntry;
import com.dominicyyds.sqljoininggraph.service.OutputService;
import com.dominicyyds.sqljoininggraph.service.Printer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;
import java.util.Comparator;

public class DetailPanel extends JBPanel<DetailPanel> implements Printer {

    private static final String[] colNames = {"Table 1", "Column 1", "Table 2", "Column 2"};

    private final Project project;
    private JBScrollPane tableScrollPane;

    public DetailPanel(Project project) {
        this.project = project;
        OutputService outputService = project.getService(OutputService.class);
        outputService.registerPrinter(this);

        setLayout(new BorderLayout());
    }

    @Override
    public void printJoinEntries(Collection<JoinEntry> joinEntries) {
        JBTable table = new JBTable(new DefaultTableModel(convertToTableData(joinEntries), colNames));
        tableScrollPane = new JBScrollPane(table);
        ToolWindowManager.getInstance(project).invokeLater(() -> {
            remove(tableScrollPane);
            add(tableScrollPane, BorderLayout.CENTER);
            table.setFillsViewportHeight(true);
        });
    }

    private String[][] convertToTableData(Collection<JoinEntry> joinEntries) {
        return joinEntries.stream()
                .sorted(Comparator.comparing(JoinEntry::getTableLeft)
                        .thenComparing(JoinEntry::getTableRight)
                        .thenComparing(JoinEntry::getColumnLeft)
                        .thenComparing(JoinEntry::getColumnRight))
                .map(joinEntry -> new String[]{
                        joinEntry.getTableLeft(), joinEntry.getColumnLeft(),
                        joinEntry.getTableRight(), joinEntry.getColumnRight()})
                .toArray(String[][]::new);
    }
}
