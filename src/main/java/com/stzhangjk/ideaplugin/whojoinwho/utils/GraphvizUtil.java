package com.stzhangjk.ideaplugin.whojoinwho.utils;

import com.stzhangjk.ideaplugin.whojoinwho.entity.JoinEntry;
import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.Engine;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class GraphvizUtil {

    private static final String fontname = "Microsoft YaHei UI";

    public static void draw(Set<JoinEntry> joins, File outputFile) throws IOException {
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
                .graphAttrs().add(Font.name(fontname))
                .graphAttrs().add("overlap", "false")
                .nodeAttrs().add(Font.name(fontname))
                .nodeAttrs().add(Shape.RECORD)
                .linkAttrs().add(Font.name(fontname));
        for (JoinEntry join : joins) {
            MutableNode left = nodes.get(join.getTableLeft());
            MutableNode right = nodes.get(join.getTableRight());
            left.addLink(left.port(join.getColumnLeft()).linkTo(right.port(join.getColumnRight())));
        }
        g.add(nodes.values().toArray(new MutableNode[0]));

        System.out.println(outputFile);
        Graphviz.fromGraph(g)
                .engine(Engine.NEATO)
                .notValidating()
                .render(Format.SVG)
                .toFile(outputFile);
    }

}
