package com.stzhangjk.ideaplugin.whojoinwho.entity;

import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.statement.select.SelectItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoinSelect extends JoinNode {

    /** select的列，只筛选出 *、t.*、列 三种，可能为空 */
    private List<SelectItem> columns = new ArrayList<>();

    private List<EqualsTo> eqs = new ArrayList<>();

    private Map<String, List<TableAndColumn>> aliasCache = new HashMap<>();

    public List<SelectItem> getColumns() {
        return columns;
    }

    public List<EqualsTo> getEqs() {
        return eqs;
    }

    public Map<String, List<TableAndColumn>> getAliasCache() {
        return aliasCache;
    }
}
