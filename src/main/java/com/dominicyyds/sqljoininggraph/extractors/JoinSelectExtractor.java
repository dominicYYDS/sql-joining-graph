package com.dominicyyds.sqljoininggraph.extractors;

import com.dominicyyds.sqljoininggraph.builders.SelectBodyJoinTreeBuilder;
import com.dominicyyds.sqljoininggraph.entity.JoinEntry;
import com.dominicyyds.sqljoininggraph.entity.JoinNode;
import com.dominicyyds.sqljoininggraph.entity.JoinSelect;
import com.dominicyyds.sqljoininggraph.entity.TableAndColumn;
import com.dominicyyds.sqljoininggraph.resolvers.JoinSelectResolver;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SubSelect;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;
import java.util.stream.Stream;

public class JoinSelectExtractor implements JoinEntryExtractor<JoinSelect> {

    public static final JoinSelectExtractor INSTANCE = new JoinSelectExtractor();

    @Override
    public Set<JoinEntry> extract(JoinSelect select) {
        Set<JoinEntry> result = new TreeSet<>(JoinEntry.COMPARATOR);
        select.getColEqCols()
                .stream()
                .flatMap(on -> {
                    Column left = on.getLeft();
                    Column right = on.getRight();
                    List<TableAndColumn> tcls = JoinSelectResolver.INSTANCE.resolve(select, left.getTable() == null ? null : left.getTable().getName(), left.getColumnName());
                    List<TableAndColumn> tcrs = JoinSelectResolver.INSTANCE.resolve(select, right.getTable() == null ? null : right.getTable().getName(), right.getColumnName());
                    return cross(tcls, tcrs);
                })
                .filter(Objects::nonNull)
                .forEach(result::add);
        select.getColEqSubs()
                .stream()
                .flatMap(eq -> extractColumnEqualsSubSelect(select, eq.getColumn(), eq.getSubSelect()))
                .filter(Objects::nonNull)
                .forEach(result::add);
        select.getColInSubs()
                .stream()
                .flatMap(in -> extractColumnEqualsSubSelect(select, in.getColumn(), in.getSubSelect()))
                .filter(Objects::nonNull)
                .forEach(result::add);
        select.getChildren()
                .stream()
                .filter(child -> child instanceof JoinSelect)
                .map(child -> INSTANCE.extract((JoinSelect) child))
                .forEach(result::addAll);
        return result;
    }

    private static Stream<JoinEntry> extractColumnEqualsSubSelect(JoinSelect currentSelect, Column column, SubSelect subSelect) {
        JoinNode subTree = SelectBodyJoinTreeBuilder.INSTANCE.build(subSelect.getSelectBody());
        if (subTree instanceof JoinSelect) {
            JoinSelect mySubSelect = (JoinSelect) subTree;
            Column subSelectColumn = (Column) Optional.ofNullable(mySubSelect)
                    .map(JoinSelect::getColumns)
                    .filter(CollectionUtils::isNotEmpty)
                    .filter(cols -> cols.size() == 1) //子查询只能select 1个列才能解析
                    .map(cols -> cols.get(0))
                    .filter(col -> col instanceof SelectExpressionItem)
                    .map(col -> (SelectExpressionItem) col)
                    .map(SelectExpressionItem::getExpression)
                    .filter(col -> col instanceof Column) // *、t.* 也解析不了，排除
                    .orElse(null);
            if (subSelectColumn == null) {
                return Stream.empty();
            }
            List<TableAndColumn> tcls = JoinSelectResolver.INSTANCE.resolve(currentSelect, column.getTable() == null ? null : column.getTable().getName(), column.getColumnName());
            List<TableAndColumn> tcrs = JoinSelectResolver.INSTANCE.resolve(mySubSelect, subSelectColumn.getTable() == null ? null : subSelectColumn.getTable().getName(), subSelectColumn.getColumnName());
            return cross(tcls, tcrs);
        }
        return Stream.of();
    }

    private static Stream<JoinEntry> cross(List<TableAndColumn> tcls, List<TableAndColumn> tcrs) {
        if (tcls == null || tcls.size() < 1 || tcrs == null || tcrs.size() < 1) {
            return Stream.empty();
        }
        List<JoinEntry> joins = new ArrayList<>();
        for (TableAndColumn tcl : tcls) {
            for (TableAndColumn tcr : tcrs) {
                String tl = tcl.getTable().toLowerCase();
                String cl = tcl.getColumn().toLowerCase();
                String tr = tcr.getTable().toLowerCase();
                String cr = tcr.getColumn().toLowerCase();
                if (tl.equals(tr) && cl.equals(cr)) {  //排除同表同列
                    continue;
                }
                joins.add(new JoinEntry(tl, cl, tr, cr));
            }
        }
        return joins.stream();
    }
}
