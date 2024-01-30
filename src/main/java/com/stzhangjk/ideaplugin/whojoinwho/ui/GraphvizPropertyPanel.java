package com.stzhangjk.ideaplugin.whojoinwho.ui;

import com.intellij.execution.util.EnvVariablesTable;
import com.intellij.execution.util.EnvironmentVariable;
import com.intellij.openapi.project.Project;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.panels.VerticalLayout;
import com.stzhangjk.ideaplugin.whojoinwho.entity.WhoJoinWhoSettings;
import com.stzhangjk.ideaplugin.whojoinwho.service.WhoJoinWhoSettingsService;
import com.stzhangjk.ideaplugin.whojoinwho.utils.SettingsUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GraphvizPropertyPanel extends JPanel {


    private GraphvizPropertyTextFieldWithBrowseButton graphButton;
    private GraphvizPropertyTextFieldWithBrowseButton nodeButton;
    private GraphvizPropertyTextFieldWithBrowseButton edgeButton;

    private ActionListener openDialogListener;

    private final List<EnvironmentVariable> graphProperties;
    private final List<EnvironmentVariable> nodeProperties;
    private final List<EnvironmentVariable> edgeProperties;
    private Project project;
    private WhoJoinWhoSettingsService settingsService;


    public GraphvizPropertyPanel(Project project) {

        this.project = project;
        settingsService = project.getService(WhoJoinWhoSettingsService.class);

        VerticalLayout layout = new VerticalLayout(10);
        setLayout(layout);

        openDialogListener = e -> createDialog().show();
        //配置输入框文本改变时更新配置
        DocumentListener onTextChangeListener = new DocumentAdapter() {
            @Override
            protected void textChanged(@NotNull DocumentEvent e) {
                settingsService.setSettings(new WhoJoinWhoSettings()
                        .setGraphAttrs(EnvVariablesTable.parseEnvsFromText(graphButton.getText()))
                        .setNodeAttrs(EnvVariablesTable.parseEnvsFromText(nodeButton.getText()))
                        .setEdgeAttrs(EnvVariablesTable.parseEnvsFromText(edgeButton.getText())));
            }
        };

        graphProperties = new ArrayList<>();
        nodeProperties = new ArrayList<>();
        edgeProperties = new ArrayList<>();

        graphButton = new GraphvizPropertyTextFieldWithBrowseButton();
        nodeButton = new GraphvizPropertyTextFieldWithBrowseButton();
        edgeButton = new GraphvizPropertyTextFieldWithBrowseButton();

        if (settingsService.getState() != null) {
            graphButton.setText(SettingsUtil.stringifyProps(settingsService.getState().getGraphAttrs()));
            nodeButton.setText(SettingsUtil.stringifyProps(settingsService.getState().getNodeAttrs()));
            edgeButton.setText(SettingsUtil.stringifyProps(settingsService.getState().getEdgeAttrs()));
        }

        graphButton.getTextField().getDocument().addDocumentListener(onTextChangeListener);
        nodeButton.getTextField().getDocument().addDocumentListener(onTextChangeListener);
        edgeButton.getTextField().getDocument().addDocumentListener(onTextChangeListener);

        graphButton.addActionListener(openDialogListener);
        nodeButton.addActionListener(openDialogListener);
        edgeButton.addActionListener(openDialogListener);

        JBLabel graphPropLabel = new JBLabel("Graph Properties");
        JBLabel nodePropLabel = new JBLabel("Node Properties");
        JBLabel edgePropLabel = new JBLabel("Edge Properties");
        graphPropLabel.setLabelFor(graphButton);
        nodePropLabel.setLabelFor(nodeButton);
        edgePropLabel.setLabelFor(edgeButton);

        add(graphPropLabel);
        add(graphButton);
        add(nodePropLabel);
        add(nodeButton);
        add(edgePropLabel);
        add(edgeButton);

    }

    private GraphvizPropertyDialog createDialog() {
        return new GraphvizPropertyDialog(project, this);
    }

    public GraphvizPropertyTextFieldWithBrowseButton getGraphButton() {
        return graphButton;
    }

    public GraphvizPropertyTextFieldWithBrowseButton getNodeButton() {
        return nodeButton;
    }

    public GraphvizPropertyTextFieldWithBrowseButton getEdgeButton() {
        return edgeButton;
    }

    public List<EnvironmentVariable> getGraphProperties() {
        return graphProperties;
    }

    public List<EnvironmentVariable> getNodeProperties() {
        return nodeProperties;
    }

    public List<EnvironmentVariable> getEdgeProperties() {
        return edgeProperties;
    }
}
