package com.dominicyyds.sqljoininggraph.computers;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PsiClassStringConstComputer implements PsiStringConstComputer<PsiClass> {

    public static final PsiClassStringConstComputer INSTANCE = new PsiClassStringConstComputer();

    @Override
    public @NotNull List<String> compute(PsiClass psiClass) {
        return PsiTreeUtil.findChildrenOfAnyType(psiClass, PsiField.class, PsiLocalVariable.class, PsiAssignmentExpression.class)
                .stream()
                .flatMap(field -> {
                    try {
                        if (field instanceof PsiField || field instanceof PsiLocalVariable) {
                            //String sql = "123123"
                            return PsiVariableStringConstComputer.INSTANCE.compute((PsiVariable) field).stream();
                        } else if (field instanceof PsiAssignmentExpression) {
                            //String sql;
                            //sql = "123123";
                            return PsiAssignmentSqlConstComputer.INSTANCE.compute((PsiAssignmentExpression) field).stream();
                        } else {
                            return Stream.empty();
                        }
                    } catch (Exception ignore) {
                        //以字段为单位解析，所以需要单独捕获异常
                        return Stream.empty();
                    }
                })
                .collect(Collectors.toList());
    }
}
