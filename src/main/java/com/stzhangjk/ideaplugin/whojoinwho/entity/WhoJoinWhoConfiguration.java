package com.stzhangjk.ideaplugin.whojoinwho.entity;

import guru.nidi.graphviz.engine.Engine;

import java.util.Map;

public class WhoJoinWhoConfiguration {

    private Map<String, String> graphAttrs;

    private Map<String, String> edgeAttrs;

    private Map<String, String> nodeAttrs;

    private String engine;

    public Map<String, String> getGraphAttrs() {
        return graphAttrs;
    }

    public WhoJoinWhoConfiguration setGraphAttrs(Map<String, String> graphAttrs) {
        this.graphAttrs = graphAttrs;
        return this;
    }

    public Map<String, String> getEdgeAttrs() {
        return edgeAttrs;
    }

    public WhoJoinWhoConfiguration setEdgeAttrs(Map<String, String> edgeAttrs) {
        this.edgeAttrs = edgeAttrs;
        return this;
    }

    public Map<String, String> getNodeAttrs() {
        return nodeAttrs;
    }

    public WhoJoinWhoConfiguration setNodeAttrs(Map<String, String> nodeAttrs) {
        this.nodeAttrs = nodeAttrs;
        return this;
    }

    public String getEngine() {
        return engine;
    }

    public WhoJoinWhoConfiguration setEngine(String engine) {
        this.engine = engine;
        return this;
    }
}
