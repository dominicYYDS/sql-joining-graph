package com.stzhangjk.ideaplugin.whojoinwho.entity;

import java.util.Comparator;

public class JoinEntry {

    public static final Comparator<JoinEntry> COMPARATOR = (e1, e2) -> {
        int r1 = JoinEntry.c1.compare(e1, e2);
        return r1 == 0 ? 0 : JoinEntry.c2.compare(e1, e2);
    };

    private static final Comparator<JoinEntry> c1 = Comparator.comparing(JoinEntry::getTableLeft)
            .thenComparing(JoinEntry::getColumnLeft)
            .thenComparing(JoinEntry::getTableRight)
            .thenComparing(JoinEntry::getColumnRight);

    private static final Comparator<JoinEntry> c2 = (e1, e2) -> {
        String s1 = e1.getTableLeft()
                .concat(e1.getColumnLeft())
                .concat(e1.getTableRight())
                .concat(e1.getColumnRight());
        String s2 = e2.getTableRight()
                .concat(e2.getColumnRight())
                .concat(e2.getTableLeft())
                .concat(e2.getColumnLeft());
        return s1.compareTo(s2);
    };


    /** 左表 */
    private String tableLeft;

    private String columnLeft;

    private String tableRight;

    private String columnRight;

    public JoinEntry(String tableLeft, String columnLeft, String tableRight, String columnRight) {
        this.tableLeft = tableLeft;
        this.columnLeft = columnLeft;
        this.tableRight = tableRight;
        this.columnRight = columnRight;
    }

    public String getTableLeft() {
        return tableLeft;
    }

    public String getColumnLeft() {
        return columnLeft;
    }

    public String getTableRight() {
        return tableRight;
    }

    public String getColumnRight() {
        return columnRight;
    }

    @Override
    public String toString() {
        return String.format("%s.%s=%s.%s", tableLeft, columnLeft, tableRight, columnRight);
    }


}
