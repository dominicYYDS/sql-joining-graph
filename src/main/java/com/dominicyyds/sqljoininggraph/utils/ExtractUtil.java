package com.dominicyyds.sqljoininggraph.utils;

import com.dominicyyds.sqljoininggraph.entity.JoinEntry;
import com.dominicyyds.sqljoininggraph.entity.JoinNode;
import com.dominicyyds.sqljoininggraph.entity.JoinSelect;
import com.dominicyyds.sqljoininggraph.entity.JoinUnion;
import com.dominicyyds.sqljoininggraph.builders.SelectBodyJoinTreeBuilder;
import com.dominicyyds.sqljoininggraph.extractors.JoinSelectExtractor;
import com.dominicyyds.sqljoininggraph.extractors.JoinUnionExtractor;
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
