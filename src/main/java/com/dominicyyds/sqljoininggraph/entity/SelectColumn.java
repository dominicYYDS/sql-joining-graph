package com.dominicyyds.sqljoininggraph.entity;


import net.sf.jsqlparser.schema.Column;

/**
 * select的列，真列，非计算、聚合
 */
public class SelectColumn {

    /** 列名 */
    private String column;

    /** 别名或列名 */
    private String alias;

    private Column origin;


    public String getColumn() {
        return column;
    }

    public SelectColumn setColumn(String column) {
        this.column = column;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public SelectColumn setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public Column getOrigin() {
        return origin;
    }

    public SelectColumn setOrigin(Column origin) {
        this.origin = origin;
        return this;
    }
}
