package com.dominicyyds.sqljoininggraph.computers;

import com.intellij.psi.PsiJavaFile;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PsiJavaFileStringConstComputer implements PsiStringConstComputer<PsiJavaFile> {

    public static final PsiJavaFileStringConstComputer INSTANCE = new PsiJavaFileStringConstComputer();

    @Override
    public @NotNull List<String> compute(PsiJavaFile psiJavaFile) {
        return Arrays.stream(psiJavaFile.getClasses())
                .flatMap(psiClass -> {
                    try {
                        return PsiClassSqlStringConstComputer.INSTANCE.compute(psiClass).stream();
                    } catch (Exception ignore) {
                        return Stream.empty();
                    }
                })
                .collect(Collectors.toList());
    }
}
