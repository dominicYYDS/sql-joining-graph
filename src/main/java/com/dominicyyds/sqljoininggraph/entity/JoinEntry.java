package com.dominicyyds.sqljoininggraph.entity;

import java.util.Comparator;

public class JoinEntry {

    public static final Comparator<JoinEntry> COMPARATOR = Comparator.comparing(JoinEntry::getTableLeft)
            .thenComparing(JoinEntry::getColumnLeft)
            .thenComparing(JoinEntry::getTableRight)
            .thenComparing(JoinEntry::getColumnRight);


    /** 左表 */
    private final String tableLeft;

    /** 左列 */
    private final String columnLeft;

    /** 右表 */
    private final String tableRight;

    /** 右列 */
    private final String columnRight;

    public JoinEntry(String tableLeft, String columnLeft, String tableRight, String columnRight) {
        //字母顺序小的放左边
        if (tableLeft.concat(columnLeft).compareTo(tableRight.concat(columnRight)) <= 0) {
            this.tableLeft = tableLeft;
            this.columnLeft = columnLeft;
            this.tableRight = tableRight;
            this.columnRight = columnRight;
        } else {
            this.tableLeft = tableRight;
            this.columnLeft = columnRight;
            this.tableRight = tableLeft;
            this.columnRight = columnLeft;
        }
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
