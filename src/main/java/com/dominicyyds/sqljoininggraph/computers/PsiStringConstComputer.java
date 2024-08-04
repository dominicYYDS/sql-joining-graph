package com.dominicyyds.sqljoininggraph.computers;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface PsiStringConstComputer<T extends PsiElement> {

    /**
     * 解析字符串常量值
     */
    @NotNull List<String> compute(T psiElement);

    /**
     * 解析字符串常量值
     */
    default String computeFirst(T psiElement) {
        return Optional.ofNullable(compute(psiElement))
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(0))
                .orElse(null);
    }

}
