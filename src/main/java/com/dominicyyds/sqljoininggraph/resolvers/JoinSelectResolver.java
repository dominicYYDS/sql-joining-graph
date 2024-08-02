package com.dominicyyds.sqljoininggraph.resolvers;

import com.dominicyyds.sqljoininggraph.entity.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class JoinSelectResolver implements TableAndColumnResolver<JoinSelect> {

    public static final JoinSelectResolver INSTANCE = new JoinSelectResolver();

    @Override
    public List<TableAndColumn> resolve(JoinSelect select, String tableAlias, String columnOrAlias) {
        List<TableAndColumn> result = select.getAliasCache().get(getCacheKey(tableAlias, columnOrAlias));
        if (result != null) {
            return result;
        }

        //确定select的表或子查询
        JoinNode sub;
        if (StringUtils.isBlank(tableAlias)) {
            if (select.getChildren().size() != 1) {
                return new ArrayList<>();  //无指定表且有多个子表，无法解析
            }
            sub = select.getChildren().get(0);
        } else {
            sub = select.getChildren()
                    .stream()
                    .filter(child -> tableAlias.equals(child.getAlias()))
                    .findFirst()
                    .orElse(null);
            if (sub == null) {
                return new ArrayList<>();  //找不到子表或子查询
            }
        }

        if (sub instanceof JoinTable) {
            //JoinTable 不单独抽出Resolver实现类，因为需要上面确定表之后才有意义
            TableAndColumn tc = new TableAndColumn(((JoinTable) sub).getTable().getName(), columnOrAlias);
            result = new ArrayList<>();
            result.add(tc);
        } else if (sub instanceof JoinUnion) {
            result = JoinUnionResolver.INSTANCE.resolve((JoinUnion) sub, null, columnOrAlias);
        } else {
            JoinSelect subSelect = (JoinSelect) sub;
            result = subSelect.getColumns()
                    .stream()
                    .filter(selectItem -> (selectItem instanceof SelectExpressionItem) && ((SelectExpressionItem) selectItem).getExpression() instanceof Column)
                    .map(selectItem -> (SelectExpressionItem) selectItem)
                    .filter(selectItem -> {
                        if (selectItem.getAlias() != null && selectItem.getAlias().getName().equals(columnOrAlias)) {
                            return true;  //子查询的列别名是本层使用的列名
                        } else {
                            //子查询的列名是本层使用的列名，没有起别名
                            return ((Column) selectItem.getExpression()).getColumnName().equals(columnOrAlias);
                        }
                    })
                    .findFirst()
                    .map(selectItem -> (Column) selectItem.getExpression())
                    //转化为子查询中的例如 select t1.c1 或者 select c1
                    .map(selectItem -> JoinSelectResolver.INSTANCE.resolve(subSelect, selectItem.getTable() == null ? null : selectItem.getTable().getName(), selectItem.getColumnName()))
                    .orElse(null);
            if (result == null
                    && subSelect.getColumns().size() == 1
                    && (subSelect.getColumns().get(0) instanceof AllColumns || subSelect.getColumns().get(0) instanceof AllTableColumns)) {
                //如果子查询是 select * 或 t.* ，转化为到子查询中处理
                result = JoinSelectResolver.INSTANCE.resolve(subSelect, null, columnOrAlias);
            }
        }

        if (result != null && result.size() > 0) {
            select.getAliasCache().put(getCacheKey(tableAlias, columnOrAlias), result);
        }
        return result;
    }


    private static String getCacheKey(String tableAlias, String columnOrAlias) {
        return String.format("%s.%s", tableAlias == null ? "" : tableAlias, columnOrAlias);
    }
}
