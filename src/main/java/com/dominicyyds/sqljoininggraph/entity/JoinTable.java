package com.dominicyyds.sqljoininggraph.entity;

import net.sf.jsqlparser.schema.Table;

public class JoinTable extends JoinNode {

    private Table table;

    public Table getTable() {
        return table;
    }

    public JoinTable setTable(Table table) {
        this.table = table;
        return this;
    }
}
