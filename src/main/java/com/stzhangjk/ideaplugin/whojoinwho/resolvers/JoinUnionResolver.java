package com.stzhangjk.ideaplugin.whojoinwho.resolvers;

import com.stzhangjk.ideaplugin.whojoinwho.entity.JoinSelect;
import com.stzhangjk.ideaplugin.whojoinwho.entity.JoinUnion;
import com.stzhangjk.ideaplugin.whojoinwho.entity.TableAndColumn;

import java.util.List;
import java.util.stream.Collectors;

public class JoinUnionResolver implements TableAndColumnResolver<JoinUnion> {

    public static final JoinUnionResolver INSTANCE = new JoinUnionResolver();

    @Override
    public List<TableAndColumn> resolve(JoinUnion union, String tableAlias, String columnOrAlias) {
        return union.getChildren()
                .stream()
                .flatMap(child -> {
                    if (child instanceof JoinUnion) {
                        return INSTANCE.resolve((JoinUnion) child, null, columnOrAlias).stream();
                    } else { //JoinSelect
                        return JoinSelectResolver.INSTANCE.resolve((JoinSelect) child, null, columnOrAlias).stream();
                    }
                })
                .collect(Collectors.toList());
    }

}
