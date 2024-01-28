package com.stzhangjk.ideaplugin.whojoinwho.extractors;

import com.stzhangjk.ideaplugin.whojoinwho.entity.JoinEntry;
import com.stzhangjk.ideaplugin.whojoinwho.entity.JoinSelect;
import com.stzhangjk.ideaplugin.whojoinwho.entity.JoinUnion;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class JoinUnionExtractor implements JoinEntryExtractor<JoinUnion> {

    public static final JoinUnionExtractor INSTANCE = new JoinUnionExtractor();

    @Override
    public Set<JoinEntry> extract(JoinUnion joinNode) {
        return joinNode.getChildren()
                .stream()
                .flatMap(child -> {
                    if (child instanceof JoinUnion) {
                        return INSTANCE.extract((JoinUnion) child).stream();
                    } else { //JoinSelect
                        return JoinSelectExtractor.INSTANCE.extract((JoinSelect) child).stream();
                    }
                })
                .collect(Collectors.toCollection(() -> new TreeSet<>(JoinEntry.COMPARATOR)));
    }
}
