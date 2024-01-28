package com.stzhangjk.ideaplugin.whojoinwho.utils;

import com.stzhangjk.ideaplugin.whojoinwho.builders.SelectBodyJoinTreeBuilder;
import com.stzhangjk.ideaplugin.whojoinwho.entity.JoinEntry;
import com.stzhangjk.ideaplugin.whojoinwho.entity.JoinNode;
import com.stzhangjk.ideaplugin.whojoinwho.entity.JoinSelect;
import com.stzhangjk.ideaplugin.whojoinwho.entity.JoinUnion;
import com.stzhangjk.ideaplugin.whojoinwho.extractors.JoinSelectExtractor;
import com.stzhangjk.ideaplugin.whojoinwho.extractors.JoinUnionExtractor;
import net.sf.jsqlparser.statement.select.Select;

import java.util.Set;
import java.util.TreeSet;

public class ExtractUtil {

    public static Set<JoinEntry> extract(Select select) {
        JoinNode tree = SelectBodyJoinTreeBuilder.INSTANCE.build(select.getSelectBody());
        if (tree == null) {
            return new TreeSet<>(JoinEntry.COMPARATOR);
        }
        if (tree instanceof JoinSelect) {
            return JoinSelectExtractor.INSTANCE.extract((JoinSelect) tree);
        } else if (tree instanceof JoinUnion) {
            return JoinUnionExtractor.INSTANCE.extract((JoinUnion) tree);
        } else {
            return new TreeSet<>(JoinEntry.COMPARATOR);
        }
    }

}
