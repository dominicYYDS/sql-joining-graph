package com.stzhangjk.ideaplugin.whojoinwho.entity;

public class TableAndColumn {

    private String table;

    private String column;

    public TableAndColumn(String table, String column) {
        this.table = table;
        this.column = column;
    }

    public String getTable() {
        return table;
    }

    public String getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "TableAndColumn{" +
                "table='" + table + '\'' +
                ", column='" + column + '\'' +
                '}';
    }
}
