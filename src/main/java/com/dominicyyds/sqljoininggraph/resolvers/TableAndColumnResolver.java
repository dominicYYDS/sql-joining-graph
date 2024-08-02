package com.dominicyyds.sqljoininggraph.resolvers;

import com.dominicyyds.sqljoininggraph.entity.JoinNode;
import com.dominicyyds.sqljoininggraph.entity.TableAndColumn;

import java.util.List;

/**
 * 从select、union、table中解析出实际表名和列名，可能解析不出来
 */
public interface TableAndColumnResolver<T extends JoinNode> {

    /**
     * 执行解析
     *
     * @param joinNode      目标节点
     * @param tableAlias    表别名，可能空
     * @param columnOrAlias 列名或者别名
     * @return 对于union类型，可能解析出多个结果
     */
    List<TableAndColumn> resolve(T joinNode, String tableAlias, String columnOrAlias);

}
