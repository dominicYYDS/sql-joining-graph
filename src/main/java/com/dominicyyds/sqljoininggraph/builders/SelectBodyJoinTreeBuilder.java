package com.dominicyyds.sqljoininggraph.builders;

import com.dominicyyds.sqljoininggraph.entity.JoinNode;
import com.dominicyyds.sqljoininggraph.entity.JoinUnion;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectVisitorAdapter;
import net.sf.jsqlparser.statement.select.SetOperationList;

import java.util.ArrayList;
import java.util.List;

public class SelectBodyJoinTreeBuilder implements JoinTreeBuilder<SelectBody> {

    public static final SelectBodyJoinTreeBuilder INSTANCE = new SelectBodyJoinTreeBuilder();

    /**
     * 解析出JoinSelect或JoinUnion
     */
    @Override
    public JoinNode build(SelectBody select) {
        if (select == null) {
            return null;
        }
        List<JoinNode> selects = new ArrayList<>();
        if (select instanceof SetOperationList) {  //处理union
            SetOperationList selectInUnion = (SetOperationList) select;
            if (selectInUnion.getSelects() != null && selectInUnion.getSelects().size() > 1) {
                selectInUnion.getSelects()
                        .stream()
                        .map(SelectBodyJoinTreeBuilder.INSTANCE::build)
                        .forEach(selects::add);
            }
        } else {
            select.accept(new SelectVisitorAdapter() {  //union不适用
                @Override
                public void visit(PlainSelect plainSelect) {
                    selects.add(PlainSelectJoinTreeBuilder.INSTANCE.build(plainSelect));
                }
            });
        }

        if (selects.size() == 1) {
            return selects.get(0);
        } else if (selects.size() > 1) {
            JoinUnion union = new JoinUnion();
            union.getChildren().addAll(selects);
            return union;
        } else return null; //理论上不会
    }
}
