package com.stzhangjk.ideaplugin.whojoinwho.extractors;

import com.stzhangjk.ideaplugin.whojoinwho.entity.JoinEntry;
import com.stzhangjk.ideaplugin.whojoinwho.entity.JoinNode;

import java.util.Set;

/**
 * 抽取select或union中的join或where
 */
public interface JoinEntryExtractor<T extends JoinNode> {

    Set<JoinEntry> extract(T joinNode);

}
