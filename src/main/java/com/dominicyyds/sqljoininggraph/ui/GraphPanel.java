package com.dominicyyds.sqljoininggraph.ui;

import com.dominicyyds.sqljoininggraph.entity.JoinEntry;
import com.dominicyyds.sqljoininggraph.service.OutputService;
import com.dominicyyds.sqljoininggraph.service.Printer;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBImageIcon;
import guru.nidi.graphviz.attribute.Font;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Records;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.engine.Engine;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.*;
import java.util.function.Function;

import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class GraphPanel extends JBPanel<GraphPanel> implements Printer {

    private final Project project;

    private static final String FONT_NAME = "Microsoft YaHei UI";
    private static final JBLabel NOTHING = new JBLabel("nothing to show yet", JBLabel.CENTER);
    private static JBScrollPane imageScroll;

    public GraphPanel(Project project) {
        this.project = project;
        OutputService outputService = project.getService(OutputService.class);
        outputService.registerPrinter(this);

        setLayout(new BorderLayout());
        add(NOTHING, BorderLayout.CENTER);


        JButton clearBtn = new JButton("Clear");
        Box northBox = Box.createHorizontalBox();
        northBox.add(clearBtn);
        northBox.add(Box.createHorizontalGlue());
        add(northBox, BorderLayout.NORTH);
        clearBtn.addActionListener(e -> {
            ToolWindowManager.getInstance(project).invokeLater(() -> {
                clearImage();
                add(NOTHING, BorderLayout.CENTER);
            });
        });
    }

    private void clearImage() {
        if (imageScroll != null) {
            remove(imageScroll);
            imageScroll = null;
        }
    }

    @Override
    public void printJoinEntries(Collection<JoinEntry> joins) {
        if (joins == null || joins.size() == 0) {
            return;
        }

        Map<String, Set<String>> tables = new HashMap<>();
        for (JoinEntry join : joins) {
            Set<String> columns = tables.computeIfAbsent(join.getTableLeft(), k -> new TreeSet<>());
            columns.add(join.getColumnLeft());
            columns = tables.computeIfAbsent(join.getTableRight(), k -> new TreeSet<>());
            columns.add(join.getColumnRight());
        }
        Map<String, MutableNode> nodes = tables.entrySet()
                .stream()
                .map(entry -> {
                    List<String> recs = new ArrayList<>(entry.getValue().size() + 1);
                    recs.add(Records.rec(entry.getKey(), entry.getKey()));
                    recs.addAll(entry.getValue()
                            .stream()
                            .map(col -> Records.rec(col, col))
                            .collect(toList()));
                    return mutNode(entry.getKey()).add(Records.of(recs.toArray(new String[0])));
                })
                .collect(toMap(n -> n.name().toString(), Function.identity(), (n1, n2) -> n1));

        MutableGraph g = mutGraph("tables").setDirected(false)
                .graphAttrs().add(Rank.dir(Rank.RankDir.LEFT_TO_RIGHT))
                .graphAttrs().add(Font.name(FONT_NAME))
                .graphAttrs().add("overlap", "false")
                .nodeAttrs().add(Font.name(FONT_NAME))
                .nodeAttrs().add(Shape.RECORD)
                .linkAttrs().add(Font.name(FONT_NAME));
        for (JoinEntry join : joins) {
            MutableNode left = nodes.get(join.getTableLeft());
            MutableNode right = nodes.get(join.getTableRight());
            left.addLink(left.port(join.getColumnLeft()).linkTo(right.port(join.getColumnRight())));
        }
        g.add(nodes.values().toArray(new MutableNode[0]));
        ClassLoader origin = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(Graphviz.class.getClassLoader());
        BufferedImage image = Graphviz.fromGraph(g)
                .engine(Engine.DOT)
                .notValidating()
                .render(Format.SVG)
                .toImage();
        ApplicationManager.getApplication().invokeLater(() -> {
            clearImage();
            imageScroll = new JBScrollPane(new JBLabel(new JBImageIcon(image)));
            remove(NOTHING);
            add(imageScroll, BorderLayout.CENTER);
        });
        Thread.currentThread().setContextClassLoader(origin);
    }
}
