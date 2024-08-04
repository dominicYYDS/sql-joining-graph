package com.dominicyyds.sqljoininggraph.computers;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiReferenceExpression;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PsiReferenceStringConstComputer implements PsiStringConstComputer<PsiReferenceExpression> {

    public static final PsiReferenceStringConstComputer INSTANCE = new PsiReferenceStringConstComputer();

    @Override
    public @NotNull List<String> compute(PsiReferenceExpression psiElement) {
        PsiElement resolved = psiElement.resolve();
        if (!(resolved instanceof PsiField)) {
            return List.of();
        }
        return PsiVariableStringConstComputer.INSTANCE.compute((PsiField) resolved);
    }
}
