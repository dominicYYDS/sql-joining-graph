package com.dominicyyds.sqljoininggraph.ui;

import com.dominicyyds.sqljoininggraph.entity.JoinEntry;
import com.dominicyyds.sqljoininggraph.service.OutputService;
import com.dominicyyds.sqljoininggraph.service.Printer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;
import java.util.Comparator;

public class DetailPanel extends JBPanel<DetailPanel> implements Printer {

    private static final String[] COLUMN_NAMES = {"Table 1", "Column 1", "Table 2", "Column 2"};
    private static final JBLabel NOTHING = new JBLabel("nothing to show yet", JBLabel.CENTER);

    private final Project project;
    private JBScrollPane tableScrollPane;

    public DetailPanel(Project project) {
        this.project = project;
        OutputService outputService = project.getService(OutputService.class);
        outputService.registerPrinter(this);

        setLayout(new BorderLayout());
        add(NOTHING, BorderLayout.CENTER);
    }

    @Override
    public void printJoinEntries(Collection<JoinEntry> joinEntries) {
        JBTable table = new JBTable(new DefaultTableModel(convertToTableData(joinEntries), COLUMN_NAMES));
        tableScrollPane = new JBScrollPane(table);
        ToolWindowManager.getInstance(project).invokeLater(() -> {
            removeAll();
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
