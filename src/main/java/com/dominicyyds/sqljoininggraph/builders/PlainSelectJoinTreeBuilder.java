package com.dominicyyds.sqljoininggraph.builders;

import com.dominicyyds.sqljoininggraph.entity.JoinNode;
import com.dominicyyds.sqljoininggraph.entity.JoinSelect;
import com.dominicyyds.sqljoininggraph.entity.JoinTable;
import com.dominicyyds.sqljoininggraph.enums.SubType;
import com.intellij.openapi.diagnostic.Logger;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;

import java.util.stream.Collectors;

public class PlainSelectJoinTreeBuilder implements JoinTreeBuilder<PlainSelect> {

    private static final Logger log = Logger.getInstance(PlainSelectJoinTreeBuilder.class);

    public static final PlainSelectJoinTreeBuilder INSTANCE = new PlainSelectJoinTreeBuilder();

    @Override
    public JoinNode build(PlainSelect select) {

        if (select == null) {
            return null;
        }

        JoinSelect currentNode = new JoinSelect();

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

        if (select.getJoins() != null && select.getJoins().size() > 0) {
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

                if (join.getOnExpressions() != null && join.getOnExpressions().size() > 0) {
                    join.getOnExpressions().forEach(exp -> exp.accept(new ExpressionVisitorAdapter() {
                        @Override
                        public void visit(EqualsTo eq) {
                            if (eq.getLeftExpression() instanceof Column && eq.getRightExpression() instanceof Column) {
                                currentNode.getEqs().add(eq);
                            }
                        }
                    }));
                }
            }
        }

        //select的列
        if (select.getSelectItems() != null && select.getSelectItems().size() > 0) {
            currentNode.getColumns().addAll(select.getSelectItems()
                    .stream()
                    .filter(item -> item instanceof AllColumns
                            || item instanceof AllTableColumns
                            || (item instanceof SelectExpressionItem && ((SelectExpressionItem) item).getExpression() instanceof Column))
                    .collect(Collectors.toList()));
        }

        //where
        if (select.getWhere() != null) {
            select.getWhere().accept(new ExpressionVisitorAdapter() {
                @Override
                public void visit(EqualsTo eq) {
                    if (eq.getLeftExpression() instanceof Column && eq.getRightExpression() instanceof Column) {
                        currentNode.getEqs().add(eq);
                    }
                }
            });
        }

        return currentNode;
    }
}
