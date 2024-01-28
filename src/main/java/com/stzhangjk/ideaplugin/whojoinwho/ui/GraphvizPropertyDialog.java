package com.stzhangjk.ideaplugin.whojoinwho.ui;

import com.intellij.execution.util.EnvVariablesTable;
import com.intellij.execution.util.EnvironmentVariable;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.panels.VerticalLayout;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GraphvizPropertyDialog extends DialogWrapper {

    private JPanel whole;
    private EnvVariablesTable graphTable;
    private EnvVariablesTable nodeTable;
    private EnvVariablesTable edgeTable;

    private GraphvizPropertyPanel parentPanel;

    public GraphvizPropertyDialog(GraphvizPropertyPanel parentPanel) {
        super(parentPanel, true);

        this.parentPanel = parentPanel;
        Map<String, String> gProps = EnvVariablesTable.parseEnvsFromText(parentPanel.getGraphButton().getText());
        Map<String, String> nProps = EnvVariablesTable.parseEnvsFromText(parentPanel.getNodeButton().getText());
        Map<String, String> eProps = EnvVariablesTable.parseEnvsFromText(parentPanel.getEdgeButton().getText());

        whole = new JPanel(new VerticalLayout(10));

        JBLabel graphPropLabel = new JBLabel("Graph Properties");
        graphTable = new EnvVariablesTable();
        graphTable.setValues(convertToVariables(gProps, false));
        graphPropLabel.setLabelFor(graphTable.getComponent());
        whole.add(graphPropLabel);
        whole.add(graphTable.getComponent());

        JBLabel nodePropLabel = new JBLabel("Node Properties");
        nodeTable = new EnvVariablesTable();
        nodeTable.setValues(convertToVariables(nProps, false));
        nodePropLabel.setLabelFor(nodeTable.getComponent());
        whole.add(nodePropLabel);
        whole.add(nodeTable.getComponent());

        JBLabel edgePropLabel = new JBLabel("Edge Properties");
        edgeTable = new EnvVariablesTable();
        edgeTable.setValues(convertToVariables(eProps, false));
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

        parentPanel.getGraphButton().setText(convertEnvironmentVariableToString(graphTable.getEnvironmentVariables()));
        parentPanel.getNodeButton().setText(convertEnvironmentVariableToString(nodeTable.getEnvironmentVariables()));
        parentPanel.getEdgeButton().setText(convertEnvironmentVariableToString(edgeTable.getEnvironmentVariables()));

        super.doOKAction();
    }

    private String convertEnvironmentVariableToString(List<EnvironmentVariable> envs) {
        return envs.stream().map(prop -> String.format("%s=%s", StringUtil.escapeChar(prop.getName(), ';'), StringUtil.escapeChar(prop.getValue(), ';'))).collect(Collectors.joining(";"));
    }

    @NotNull
    protected String stringifyEnvs(@NotNull Map<String, String> props) {
        if (props.isEmpty()) {
            return "";
        }
        StringBuilder buf = new StringBuilder();
        for (Map.Entry<String, String> entry : props.entrySet()) {
            if (buf.length() > 0) {
                buf.append(";");
            }
            buf.append(StringUtil.escapeChar(entry.getKey(), ';')).append("=").append(StringUtil.escapeChar(entry.getValue(), ';'));
        }
        return buf.toString();
    }

    protected static List<EnvironmentVariable> convertToVariables(Map<String, String> map, final boolean readOnly) {
        return ContainerUtil.map(map.entrySet(), entry -> new EnvironmentVariable(entry.getKey(), entry.getValue(), readOnly) {
            @Override
            public boolean getNameIsWriteable() {
                return !readOnly;
            }
        });
    }
}
