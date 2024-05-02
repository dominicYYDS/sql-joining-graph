package com.stzhangjk.ideaplugin.whojoinwho.extractors;

import com.stzhangjk.ideaplugin.whojoinwho.entity.JoinEntry;
import com.stzhangjk.ideaplugin.whojoinwho.entity.JoinSelect;
import com.stzhangjk.ideaplugin.whojoinwho.entity.TableAndColumn;
import com.stzhangjk.ideaplugin.whojoinwho.resolvers.JoinSelectResolver;
import net.sf.jsqlparser.schema.Column;

import java.util.*;
import java.util.stream.Stream;

public class JoinSelectExtractor implements JoinEntryExtractor<JoinSelect> {

    public static final JoinSelectExtractor INSTANCE = new JoinSelectExtractor();

    @Override
    public Set<JoinEntry> extract(JoinSelect select) {
        Set<JoinEntry> result = new TreeSet<>(JoinEntry.COMPARATOR);
        select.getEqs()
                .stream()
                .flatMap(on -> {
                    Column left = (Column) on.getLeftExpression();
                    Column right = (Column) on.getRightExpression();
                    List<TableAndColumn> tcls = JoinSelectResolver.INSTANCE.resolve(select, left.getTable() == null ? null : left.getTable().getName(), left.getColumnName());
                    List<TableAndColumn> tcrs = JoinSelectResolver.INSTANCE.resolve(select, right.getTable() == null ? null : right.getTable().getName(), right.getColumnName());
                    if (tcls == null || tcls.size() < 1 || tcrs == null || tcrs.size() < 1) {
                        return Stream.empty();
                    }
                    List<JoinEntry> joins = new ArrayList<>();
                    for (TableAndColumn tcl : tcls) {
                        for (TableAndColumn tcr : tcrs) {
                            String tl = tcl.getTable().toLowerCase();
                            String cl = tcl.getColumn().toLowerCase();
                            String tr = tcr.getTable().toLowerCase();
                            String cr = tcr.getColumn().toLowerCase();
                            if (!tl.equals(tr) && !cl.equals(cr)) {  //排除同表同列
                                joins.add(new JoinEntry(tl, cl, tr, cr));
                            }
                        }
                    }
                    return joins.stream();
                })
                .filter(Objects::nonNull)
                .forEach(result::add);
        select.getChildren()
                .stream()
                .filter(child -> child instanceof JoinSelect)
                .map(child -> INSTANCE.extract((JoinSelect) child))
                .forEach(result::addAll);
        return result;
    }
}
