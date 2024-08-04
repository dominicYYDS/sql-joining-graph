package com.dominicyyds.sqljoininggraph.computers;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiLocalVariable;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PsiClassStringConstComputer implements PsiStringConstComputer<PsiClass> {

    public static final PsiClassStringConstComputer INSTANCE = new PsiClassStringConstComputer();

    @Override
    public @NotNull List<String> compute(PsiClass psiClass) {
        return PsiTreeUtil.findChildrenOfAnyType(psiClass, PsiField.class, PsiLocalVariable.class)
                .stream()
                .flatMap(field -> {
                    try {
                        return PsiVariableStringConstComputer.INSTANCE.compute(field).stream();
                    } catch (Exception ignore) {
                        return Stream.empty();
                    }
                })
                .collect(Collectors.toList());
    }
}
