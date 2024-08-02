package com.dominicyyds.sqljoininggraph.extractors;

import com.dominicyyds.sqljoininggraph.entity.JoinEntry;
import com.dominicyyds.sqljoininggraph.entity.JoinNode;

import java.util.Set;

/**
 * 抽取select或union中的join或where
 */
public interface JoinEntryExtractor<T extends JoinNode> {

    Set<JoinEntry> extract(T joinNode);

}
