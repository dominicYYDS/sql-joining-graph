package com.dominicyyds.sqljoininggraph.resolvers;

import com.dominicyyds.sqljoininggraph.entity.JoinUnion;
import com.dominicyyds.sqljoininggraph.entity.JoinSelect;
import com.dominicyyds.sqljoininggraph.entity.TableAndColumn;

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
