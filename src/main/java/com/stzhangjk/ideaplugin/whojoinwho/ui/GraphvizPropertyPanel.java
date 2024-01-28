package com.stzhangjk.ideaplugin.whojoinwho.ui;

import com.intellij.execution.util.EnvironmentVariable;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.panels.VerticalLayout;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GraphvizPropertyPanel extends JPanel {


    private GraphvizPropertyTextFieldWithBrowseButton graphButton;
    private GraphvizPropertyTextFieldWithBrowseButton nodeButton;
    private GraphvizPropertyTextFieldWithBrowseButton edgeButton;

    private ActionListener openDialogListener;

    private List<EnvironmentVariable> graphProperties;
    private List<EnvironmentVariable> nodeProperties;
    private List<EnvironmentVariable> edgeProperties;


    public GraphvizPropertyPanel() {
        VerticalLayout layout = new VerticalLayout(10);
        setLayout(layout);

        openDialogListener = e -> createDialog().show();

        graphProperties = new ArrayList<>();
        nodeProperties = new ArrayList<>();
        edgeProperties = new ArrayList<>();

        graphButton = new GraphvizPropertyTextFieldWithBrowseButton();
        graphButton.addActionListener(openDialogListener);
        JBLabel graphPropLabel = new JBLabel("Graph Properties");
        graphPropLabel.setLabelFor(graphButton);

        nodeButton = new GraphvizPropertyTextFieldWithBrowseButton();
        nodeButton.addActionListener(openDialogListener);
        JBLabel nodePropLabel = new JBLabel("Node Properties");
        nodePropLabel.setLabelFor(nodeButton);

        edgeButton = new GraphvizPropertyTextFieldWithBrowseButton();
        edgeButton.addActionListener(openDialogListener);
        JBLabel edgePropLabel = new JBLabel("Edge Properties");
        edgePropLabel.setLabelFor(edgeButton);

        add(graphPropLabel);
        add(graphButton);
        add(nodePropLabel);
        add(nodeButton);
        add(edgePropLabel);
        add(edgeButton);

    }

    private GraphvizPropertyDialog createDialog() {
        return new GraphvizPropertyDialog(this);
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
