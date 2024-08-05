package com.dominicyyds.sqljoininggraph.entity.metadata;

import net.sf.jsqlparser.schema.Column;

public class ColumnAndColumn {

    private Column left;

    private Column right;

    public ColumnAndColumn(Column left, Column right) {
        this.left = left;
        this.right = right;
    }

    public Column getLeft() {
        return left;
    }

    public Column getRight() {
        return right;
    }
}
