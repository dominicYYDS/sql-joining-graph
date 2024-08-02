package com.dominicyyds.sqljoininggraph.ui;

import com.dominicyyds.sqljoininggraph.utils.SettingsUtil;
import com.intellij.execution.util.EnvVariablesTable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.panels.VerticalLayout;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public class GraphvizPropertyDialog extends DialogWrapper {

    private JPanel whole;
    private EnvVariablesTable graphTable;
    private EnvVariablesTable nodeTable;
    private EnvVariablesTable edgeTable;

    private GraphvizPropertyPanel parentPanel;

    public GraphvizPropertyDialog(Project project, GraphvizPropertyPanel parentPanel) {
        super(parentPanel, true);

        this.parentPanel = parentPanel;
        Map<String, String> gProps = EnvVariablesTable.parseEnvsFromText(parentPanel.getGraphButton().getText());
        Map<String, String> nProps = EnvVariablesTable.parseEnvsFromText(parentPanel.getNodeButton().getText());
        Map<String, String> eProps = EnvVariablesTable.parseEnvsFromText(parentPanel.getEdgeButton().getText());

        whole = new JPanel(new VerticalLayout(10));

        JBLabel graphPropLabel = new JBLabel("Graph Properties");
        graphTable = new EnvVariablesTable();
        graphTable.setValues(SettingsUtil.convertToVariables(gProps, false));
        graphPropLabel.setLabelFor(graphTable.getComponent());
        whole.add(graphPropLabel);
        whole.add(graphTable.getComponent());

        JBLabel nodePropLabel = new JBLabel("Node Properties");
        nodeTable = new EnvVariablesTable();
        nodeTable.setValues(SettingsUtil.convertToVariables(nProps, false));
        nodePropLabel.setLabelFor(nodeTable.getComponent());
        whole.add(nodePropLabel);
        whole.add(nodeTable.getComponent());

        JBLabel edgePropLabel = new JBLabel("Edge Properties");
        edgeTable = new EnvVariablesTable();
        edgeTable.setValues(SettingsUtil.convertToVariables(eProps, false));
        edgePropLabel.setLabelFor(edgeTable.getComponent());
        whole.add(edgePropLabel);
        whole.add(edgeTable.getComponent());

        setTitle("Graphviz Properties Configuration");
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return whole;
    }

    @Override
    protected void doOKAction() {
        graphTable.stopEditing();
        nodeTable.stopEditing();
        edgeTable.stopEditing();

        parentPanel.getGraphButton().setText(SettingsUtil.stringifyProps(graphTable.getEnvironmentVariables()));
        parentPanel.getNodeButton().setText(SettingsUtil.stringifyProps(nodeTable.getEnvironmentVariables()));
        parentPanel.getEdgeButton().setText(SettingsUtil.stringifyProps(edgeTable.getEnvironmentVariables()));

        super.doOKAction();
    }



}
