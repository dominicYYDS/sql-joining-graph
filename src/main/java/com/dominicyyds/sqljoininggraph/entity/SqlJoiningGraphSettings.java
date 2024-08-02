package com.dominicyyds.sqljoininggraph.entity;

import java.util.Map;

public class SqlJoiningGraphSettings {

    private Map<String, String> graphAttrs;

    private Map<String, String> edgeAttrs;

    private Map<String, String> nodeAttrs;

    private String engine;
    /** 输出文件 */
    private String outputFile;

    public Map<String, String> getGraphAttrs() {
        return graphAttrs;
    }

    public SqlJoiningGraphSettings setGraphAttrs(Map<String, String> graphAttrs) {
        this.graphAttrs = graphAttrs;
        return this;
    }

    public Map<String, String> getEdgeAttrs() {
        return edgeAttrs;
    }

    public SqlJoiningGraphSettings setEdgeAttrs(Map<String, String> edgeAttrs) {
        this.edgeAttrs = edgeAttrs;
        return this;
    }

    public Map<String, String> getNodeAttrs() {
        return nodeAttrs;
    }

    public SqlJoiningGraphSettings setNodeAttrs(Map<String, String> nodeAttrs) {
        this.nodeAttrs = nodeAttrs;
        return this;
    }

    public String getEngine() {
        return engine;
    }

    public SqlJoiningGraphSettings setEngine(String engine) {
        this.engine = engine;
        return this;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public SqlJoiningGraphSettings setOutputFile(String outputFile) {
        this.outputFile = outputFile;
        return this;
    }
}
