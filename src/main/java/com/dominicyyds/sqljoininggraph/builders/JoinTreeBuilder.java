package com.dominicyyds.sqljoininggraph.builders;

import com.dominicyyds.sqljoininggraph.entity.JoinNode;

public interface JoinTreeBuilder<T> {

    JoinNode build(T sql);

}
