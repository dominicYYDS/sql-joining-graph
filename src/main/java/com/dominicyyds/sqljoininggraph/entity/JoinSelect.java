package com.dominicyyds.sqljoininggraph.entity;

import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.statement.select.SelectItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoinSelect extends JoinNode {

    /** select的列，只筛选出 *、t.*、列 三种，可能为空 */
    private List<SelectItem> columns = new ArrayList<>();

    /** 当前select层级的列相等匹配 */
    private List<EqualsTo> eqColumns = new ArrayList<>();

    /** 当前select层级的等于子查询，例如 c1 = (select 1 from t1) */
    private List<EqualsTo> eqSubSelect = new ArrayList<>();

    //TODO 当前select层级的  xxx=func() 匹配

    private Map<String, List<TableAndColumn>> aliasCache = new HashMap<>();


    //getter ↓↓↓↓↓

    public List<SelectItem> getColumns() {
        return columns;
    }

    public List<EqualsTo> getEqColumns() {
        return eqColumns;
    }

    public Map<String, List<TableAndColumn>> getAliasCache() {
        return aliasCache;
    }

    public List<EqualsTo> getEqSubSelect() {
        return eqSubSelect;
    }
}
