package com.stzhangjk.ideaplugin.whojoinwho.builders;

import com.stzhangjk.ideaplugin.whojoinwho.entity.JoinNode;

public interface JoinTreeBuilder<T> {

    JoinNode build(T sql);

}
