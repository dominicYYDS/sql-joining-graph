package com.dominicyyds.sqljoininggraph.entity.metadata;

import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SubSelect;

public class ColumnAndSubSelect {

    private Column column;

    private SubSelect subSelect;


    public ColumnAndSubSelect(Column column, SubSelect subSelect) {
        this.column = column;
        this.subSelect = subSelect;
    }

    public Column getColumn() {
        return column;
    }

    public SubSelect getSubSelect() {
        return subSelect;
    }
}
