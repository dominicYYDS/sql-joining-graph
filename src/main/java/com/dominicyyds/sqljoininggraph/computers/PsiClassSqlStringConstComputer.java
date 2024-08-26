package com.dominicyyds.sqljoininggraph.computers;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PsiClassSqlStringConstComputer implements PsiStringConstComputer<PsiClass> {

    public static final PsiClassSqlStringConstComputer INSTANCE = new PsiClassSqlStringConstComputer();
    private static final String STRING_CLASS_NAME = String.class.getCanonicalName();

    @Override
    public @NotNull List<String> compute(PsiClass psiClass) {
        return PsiTreeUtil.findChildrenOfAnyType(psiClass, PsiField.class, PsiLocalVariable.class, PsiAssignmentExpression.class)
                .stream()
                .flatMap(field -> {
                    try {
                        if (field instanceof PsiField || field instanceof PsiLocalVariable) {
                            //String sql = "123123"
                            if (isTypeSupport((PsiVariable) field)) {
                                return PsiVariableStringConstComputer.INSTANCE.compute((PsiVariable) field).stream();
                            } else {
                                return Stream.empty();
                            }
                        } else if (field instanceof PsiAssignmentExpression) {
                            //String sql;
                            //sql = "123123";
                            PsiAssignmentExpression psiAssignment = (PsiAssignmentExpression) field;
                            if (psiAssignment == null || psiAssignment.getRExpression() == null || psiAssignment.getLExpression() == null) {
                                return Stream.empty();
                            }
                            if (!(psiAssignment.getLExpression() instanceof PsiReferenceExpression)) {
                                return Stream.empty();
                            }
                            PsiReferenceExpression typeRefExp = (PsiReferenceExpression) psiAssignment.getLExpression();
                            if (!isTypeSupport(typeRefExp.resolve())) {
                                return Stream.empty();
                            }
                            return PsiAssignmentStringConstComputer.INSTANCE.compute((PsiAssignmentExpression) field).stream();
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

    /**
     * 判断字段是否支持解析为sql string
     */
    private boolean isTypeSupport(PsiElement psiElement) {
        //只支持类属性、局部变量
        if (!(psiElement instanceof PsiField || psiElement instanceof PsiLocalVariable)) {
            return false;
        }
        PsiVariable variable = (PsiVariable) psiElement;
        //只支持String，如果只需要解析为string其实基本类型也可以，但是这里只要sql，所以只能string
        String typeText = variable.getTypeElement().getText();
        if (!STRING_CLASS_NAME.equals(typeText) && !"String".equals(typeText)) {
            return false;
        }
        return true;
    }
}
