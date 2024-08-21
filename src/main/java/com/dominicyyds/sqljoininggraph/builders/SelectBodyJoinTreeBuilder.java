package com.dominicyyds.sqljoininggraph.builders;

import com.dominicyyds.sqljoininggraph.entity.JoinNode;
import com.dominicyyds.sqljoininggraph.entity.JoinUnion;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectBody;
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

        if (select instanceof SetOperationList) {  //处理union
            SetOperationList selectInUnion = (SetOperationList) select;
            List<JoinNode> selects = new ArrayList<>();
            if (selectInUnion.getSelects() == null || selectInUnion.getSelects().isEmpty()) {
                return null;
            }
            selectInUnion.getSelects()
                    .stream()
                    .map(SelectBodyJoinTreeBuilder.INSTANCE::build)
                    .forEach(selects::add);
            JoinUnion union = new JoinUnion();
            union.getChildren().addAll(selects);
            return union;
        } else if (select instanceof PlainSelect) {
            return PlainSelectJoinTreeBuilder.INSTANCE.build((PlainSelect) select);
        } else {
            //其他暂不支持
            return null;
        }
    }
}
