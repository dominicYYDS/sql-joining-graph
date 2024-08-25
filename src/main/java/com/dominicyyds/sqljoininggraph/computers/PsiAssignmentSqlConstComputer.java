package com.dominicyyds.sqljoininggraph.computers;

import com.intellij.psi.PsiAssignmentExpression;
import com.intellij.psi.PsiReferenceExpression;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PsiAssignmentSqlConstComputer implements PsiStringConstComputer<PsiAssignmentExpression> {

    public static final PsiAssignmentSqlConstComputer INSTANCE = new PsiAssignmentSqlConstComputer();

    @Override
    public @NotNull List<String> compute(PsiAssignmentExpression psiAssignment) {
        if (!support(psiAssignment)) {
            return List.of();
        }
        return PsiVariableStringConstComputer.INSTANCE.computeRight(psiAssignment.getRExpression());
    }


    private boolean support(PsiAssignmentExpression psiAssignment) {
        if (psiAssignment == null || psiAssignment.getRExpression() == null || psiAssignment.getLExpression() == null) {
            return false;
        }
        if (!(psiAssignment.getLExpression() instanceof PsiReferenceExpression)) {
            return false;
        }
        PsiReferenceExpression typeRefExp = (PsiReferenceExpression) psiAssignment.getLExpression();
        if (!PsiVariableStringConstComputer.INSTANCE.isTypeSupport(typeRefExp.resolve())) {
            return false;
        }
        return PsiVariableStringConstComputer.INSTANCE.isValueSupport(psiAssignment.getRExpression());
    }
}
