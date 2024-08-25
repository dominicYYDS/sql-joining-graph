package com.dominicyyds.sqljoininggraph.computers;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiLocalVariable;
import com.intellij.psi.PsiReferenceExpression;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PsiReferenceStringConstComputer implements PsiStringConstComputer<PsiReferenceExpression> {

    public static final PsiReferenceStringConstComputer INSTANCE = new PsiReferenceStringConstComputer();

    @Override
    public @NotNull List<String> compute(PsiReferenceExpression psiElement) {
        PsiElement resolved = psiElement.resolve();
        if (resolved instanceof PsiField && ((PsiField) resolved).getInitializer() != null) {
            return PsiVariableStringConstComputer.INSTANCE.compute((PsiField) resolved);
        }
        if (resolved instanceof PsiLocalVariable && ((PsiLocalVariable) resolved).hasInitializer()) {
            return PsiVariableStringConstComputer.INSTANCE.compute((PsiLocalVariable) resolved);
        }
        //TODO 支持两次赋值或跳转
        //String midd;
        //midd = "union all\n";
        //String sql = "12312" + midd + "123123";
        return List.of();
    }
}
