package com.dominicyyds.sqljoininggraph.builders;

import com.dominicyyds.sqljoininggraph.entity.JoinNode;
import com.dominicyyds.sqljoininggraph.entity.JoinSelect;
import com.dominicyyds.sqljoininggraph.entity.JoinTable;
import com.dominicyyds.sqljoininggraph.entity.metadata.ColumnAndColumn;
import com.dominicyyds.sqljoininggraph.entity.metadata.ColumnAndSubSelect;
import com.dominicyyds.sqljoininggraph.enums.SubType;
import com.intellij.openapi.diagnostic.Logger;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import org.apache.commons.collections.CollectionUtils;

import java.util.stream.Collectors;

public class PlainSelectJoinTreeBuilder implements JoinTreeBuilder<PlainSelect> {

    private static final Logger log = Logger.getInstance(PlainSelectJoinTreeBuilder.class);

    public static final PlainSelectJoinTreeBuilder INSTANCE = new PlainSelectJoinTreeBuilder();

    /**
     * 解析出JoinSelect
     */
    @Override
    public JoinNode build(PlainSelect select) {

        if (select == null) {
            return null;
        }

        JoinSelect currentNode = new JoinSelect();

        //处理from到children
        if (select.getFromItem() instanceof Table) {
            JoinNode child = new JoinTable()
                    .setTable((Table) select.getFromItem())
                    .setType(SubType.FROM)
                    .setAlias(select.getFromItem().getAlias() == null ? null : select.getFromItem().getAlias().getName());
            currentNode.getChildren().add(child);
        } else if (select.getFromItem() instanceof SubSelect) {
            JoinNode child = SelectBodyJoinTreeBuilder.INSTANCE.build(((SubSelect) select.getFromItem()).getSelectBody());
            if (child != null) {
                child.setAlias(select.getFromItem().getAlias() == null ? null : select.getFromItem().getAlias().getName());
                currentNode.getChildren().add(child);
            }
        }

        //处理join到children
        if (CollectionUtils.isNotEmpty(select.getJoins())) {
            for (int i = 0; i < select.getJoins().size(); i++) {
                Join join = select.getJoins().get(i);
                JoinNode child;
                if (join.getRightItem() instanceof Table) {
                    child = new JoinTable()
                            .setTable((Table) join.getRightItem())
                            .setType(SubType.JOIN);
                } else if (join.getRightItem() instanceof SubSelect) {
                    child = SelectBodyJoinTreeBuilder.INSTANCE.build(((SubSelect) join.getRightItem()).getSelectBody());
                } else {
                    log.debug(String.format("join类型不支持：[%s]", join.getRightItem().getClass().getSimpleName()));
                    continue;
                }
                if (child != null) {
                    child.setAlias(join.getRightItem().getAlias() == null ? null : join.getRightItem().getAlias().getName());
                    currentNode.getChildren().add(child);
                }

                //处理on到eq
                if (CollectionUtils.isNotEmpty(join.getOnExpressions())) {
                    join.getOnExpressions().forEach(exp -> exp.accept(new ExpressionVisitorAdapter() {
                        @Override
                        public void visit(EqualsTo eq) {
                            if (eq.getLeftExpression() instanceof Column && eq.getRightExpression() instanceof Column) {
                                currentNode.getColEqCols().add(new ColumnAndColumn(eq.getLeftExpression(Column.class), eq.getRightExpression(Column.class)));
                            }
                        }
                    }));
                }
            }
        }

        //select的列
        if (CollectionUtils.isNotEmpty(select.getSelectItems())) {
            currentNode.getColumns().addAll(select.getSelectItems()
                    .stream()
                    .filter(item -> item instanceof AllColumns
                            || item instanceof AllTableColumns
                            || (item instanceof SelectExpressionItem && ((SelectExpressionItem) item).getExpression() instanceof Column))
                            //TODO select 函数
                    .collect(Collectors.toList()));
        }

        //where
        if (select.getWhere() != null) {

            //where InExpression
            select.getWhere().accept(new ExpressionVisitorAdapter() {

                @Override
                public void visit(InExpression in) {
                    //列in子查询、列 in (子查询 union 子查询)
                    if (in.getLeftExpression() instanceof Column && in.getRightExpression() instanceof SubSelect) {
                        currentNode.getColInSubs().add(new ColumnAndSubSelect(
                                (Column) in.getLeftExpression(),
                                (SubSelect) in.getRightExpression()));
                    }
                }

                @Override
                public void visit(EqualsTo eq) {
                    //列=列
                    if (eq.getLeftExpression() instanceof Column && eq.getRightExpression() instanceof Column) {
                        currentNode.getColEqCols().add(new ColumnAndColumn(eq.getLeftExpression(Column.class), eq.getRightExpression(Column.class)));
                    }
                    //列in子查询、列 in (子查询 union 子查询)
                    if (eq.getLeftExpression() instanceof Column && eq.getRightExpression() instanceof SubSelect) {
                        currentNode.getColInSubs().add(new ColumnAndSubSelect(
                                (Column) eq.getLeftExpression(),
                                (SubSelect) eq.getRightExpression()));
                    }
                    //子查询=列、（子查询 union 子查询）=列
                    if (eq.getLeftExpression() instanceof SubSelect && eq.getRightExpression() instanceof Column) {
                        currentNode.getColInSubs().add(new ColumnAndSubSelect(
                                (Column) eq.getRightExpression(),
                                (SubSelect) eq.getLeftExpression()));
                    }
                    //TODO xxx=func()
                }
            });
        }

        return currentNode;
    }
}
