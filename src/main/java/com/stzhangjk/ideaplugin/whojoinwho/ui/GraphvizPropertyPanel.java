package com.stzhangjk.ideaplugin.whojoinwho.ui;

import com.intellij.execution.util.EnvVariablesTable;
import com.intellij.execution.util.EnvironmentVariable;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.panels.VerticalLayout;
import com.stzhangjk.ideaplugin.whojoinwho.entity.SqlJoiningGraphSettings;
import com.stzhangjk.ideaplugin.whojoinwho.service.SqlJoiningGraphSettingsService;
import com.stzhangjk.ideaplugin.whojoinwho.utils.SettingsUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionListener;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GraphvizPropertyPanel extends JPanel {


    private final GraphvizPropertyTextFieldWithBrowseButton graphButton;
    private final GraphvizPropertyTextFieldWithBrowseButton nodeButton;
    private final GraphvizPropertyTextFieldWithBrowseButton edgeButton;
    private final TextFieldWithBrowseButton outputFileButton;

    private final ActionListener openDialogListener;

    private final List<EnvironmentVariable> graphProperties;
    private final List<EnvironmentVariable> nodeProperties;
    private final List<EnvironmentVariable> edgeProperties;
    private final Project project;
    private final SqlJoiningGraphSettingsService settingsService;


    public GraphvizPropertyPanel(Project project) {

        this.project = project;
        settingsService = project.getService(SqlJoiningGraphSettingsService.class);

        VerticalLayout layout = new VerticalLayout(10);
        setLayout(layout);

        openDialogListener = e -> createDialog().show();

        //三大graphviz属性
        graphProperties = new ArrayList<>();
        nodeProperties = new ArrayList<>();
        edgeProperties = new ArrayList<>();

        graphButton = new GraphvizPropertyTextFieldWithBrowseButton();
        nodeButton = new GraphvizPropertyTextFieldWithBrowseButton();
        edgeButton = new GraphvizPropertyTextFieldWithBrowseButton();

        graphButton.addActionListener(openDialogListener);
        nodeButton.addActionListener(openDialogListener);
        edgeButton.addActionListener(openDialogListener);

        JBLabel graphPropLabel = new JBLabel("Graph Properties");
        JBLabel nodePropLabel = new JBLabel("Node Properties");
        JBLabel edgePropLabel = new JBLabel("Edge Properties");
        graphPropLabel.setLabelFor(graphButton);
        nodePropLabel.setLabelFor(nodeButton);
        edgePropLabel.setLabelFor(edgeButton);

//        add(graphPropLabel);
//        add(graphButton);
//        add(nodePropLabel);
//        add(nodeButton);
//        add(edgePropLabel);
//        add(edgeButton);

        //输出文件名
        outputFileButton = new TextFieldWithBrowseButton();
        outputFileButton.addBrowseFolderListener("选择输出文件", "选择输出文件", null, FileChooserDescriptorFactory.createSingleFileOrFolderDescriptor(), TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT);
        outputFileButton.setText(Paths.get(project.getBasePath(), "sql-joining-graph.svg").toString());
        JBLabel outputFileLabel = new JBLabel("Choose Output File");
        outputFileLabel.setLabelFor(outputFileButton);
        add(outputFileLabel);
        add(outputFileButton);


        //设置初始值或保存的值
        if (settingsService.getState() != null) {
            //三大属性
            graphButton.setText(SettingsUtil.stringifyProps(settingsService.getState().getGraphAttrs()));
            nodeButton.setText(SettingsUtil.stringifyProps(settingsService.getState().getNodeAttrs()));
            edgeButton.setText(SettingsUtil.stringifyProps(settingsService.getState().getEdgeAttrs()));
            //输出文件
            outputFileButton.setText(settingsService.getState().getOutputFile());
        }


        //配置输入框文本改变时更新配置
        DocumentListener onTextChangeListener = new DocumentAdapter() {
            @Override
            protected void textChanged(@NotNull DocumentEvent e) {
                settingsService.setSettings(new SqlJoiningGraphSettings()
                        .setGraphAttrs(EnvVariablesTable.parseEnvsFromText(graphButton.getText()))
                        .setNodeAttrs(EnvVariablesTable.parseEnvsFromText(nodeButton.getText()))
                        .setEdgeAttrs(EnvVariablesTable.parseEnvsFromText(edgeButton.getText()))
                        .setOutputFile(outputFileButton.getText()));
            }
        };
        graphButton.getTextField().getDocument().addDocumentListener(onTextChangeListener);
        nodeButton.getTextField().getDocument().addDocumentListener(onTextChangeListener);
        edgeButton.getTextField().getDocument().addDocumentListener(onTextChangeListener);
        outputFileButton.getTextField().getDocument().addDocumentListener(onTextChangeListener);

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
