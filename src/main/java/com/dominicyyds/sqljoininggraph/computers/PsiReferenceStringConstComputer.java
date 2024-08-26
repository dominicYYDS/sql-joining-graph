package com.dominicyyds.sqljoininggraph.computers;

import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PsiReferenceStringConstComputer implements PsiStringConstComputer<PsiReferenceExpression> {

    public static final PsiReferenceStringConstComputer INSTANCE = new PsiReferenceStringConstComputer();

    @Override
    public @NotNull List<String> compute(PsiReferenceExpression psiElement) {
        PsiElement resolved = psiElement.resolve();
        if ((resolved instanceof PsiField || resolved instanceof PsiLocalVariable)
                && ((PsiVariable) resolved).hasInitializer()) {
            return PsiVariableStringConstComputer.INSTANCE.compute((PsiVariable) resolved);
        }
        //TODO 支持两次赋值或跳转
        //String midd;
        //midd = "union all\n";
        //String sql = "12312" + midd + "123123";
        return List.of();
    }
}
