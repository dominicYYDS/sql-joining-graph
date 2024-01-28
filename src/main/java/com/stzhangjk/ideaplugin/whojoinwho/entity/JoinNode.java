package com.stzhangjk.ideaplugin.whojoinwho.entity;

import com.stzhangjk.ideaplugin.whojoinwho.enums.SubType;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;

import java.util.ArrayList;
import java.util.List;

public abstract class JoinNode {

    private SubType type;

    private List<JoinNode> children = new ArrayList<>();

    private String alias;

    public List<JoinNode>  getChildren() {
        return children;
    }

    public SubType getType() {
        return type;
    }

    public JoinNode setType(SubType type) {
        this.type = type;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public JoinNode setAlias(String alias) {
        this.alias = alias;
        return this;
    }
}
