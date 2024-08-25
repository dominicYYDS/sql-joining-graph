package com.dominicyyds.sqljoininggraph.computers;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiLiteralExpressionImpl;
import com.intellij.psi.util.PsiTreeUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class PsiVariableStringConstComputer implements PsiStringConstComputer<PsiVariable> {

    private static final Logger log = Logger.getInstance(PsiVariableStringConstComputer.class);

    private static final String strClassName = String.class.getCanonicalName();

    public static final PsiVariableStringConstComputer INSTANCE = new PsiVariableStringConstComputer();

    @Override
    public @NotNull List<String> compute(PsiVariable psiVariable) {
        if (!support(psiVariable)) {
            return List.of();
        }
        PsiExpression initializer = psiVariable.getInitializer();
        return computeRight(initializer);
    }

    public List<String> computeRight(PsiExpression stringExp) {
        if (stringExp == null) {
            return List.of();
        }
        if (stringExp instanceof PsiLiteralExpressionImpl) {
            Object value = ((PsiLiteralExpressionImpl) stringExp).getValue();
//            log.info("value=" + value);
            if (StringUtils.isBlank(value.toString())) {
                return List.of();
            }
            return List.of(value.toString());
        }
//        System.out.println("=============");
        String result = PsiTreeUtil.findChildrenOfAnyType(stringExp, PsiLiteralExpressionImpl.class, PsiReferenceExpression.class)
                .stream()
                .map(child -> {
                    if (child instanceof PsiLiteralExpressionImpl) {
                        return ((PsiLiteralExpressionImpl) child).getValue().toString();
                    } else if (child instanceof PsiReferenceExpression) {
                        return PsiReferenceStringConstComputer.INSTANCE.computeFirst((PsiReferenceExpression) child);
                    } else {
                        return null;
                    }
                })
//                .peek(System.out::println)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining());
        return StringUtils.isBlank(result) ? List.of() : List.of(result);
    }

    /**
     * 判断字段类型是否支持解析sql
     */
    private boolean support(PsiVariable variable) {
        return variable != null && isTypeSupport(variable) && variable.getInitializer() != null && isValueSupport(variable.getInitializer());
    }

    /**
     * 判断字段类型是否支持
     */
    public boolean isTypeSupport(PsiElement variable) {
        //只支持类属性、局部变量
        if (!(variable instanceof PsiField || variable instanceof PsiLocalVariable)) {
            return false;
        }
        PsiVariable psiVariable = (PsiVariable) variable;
        //只支持String
        String typeText = psiVariable.getTypeElement().getText();
        if (!strClassName.equals(typeText) && !"String".equals(typeText)) {
            return false;
        }
        return true;
    }

    /**
     * 判断值是否支持
     * @param expression 等号赋的值
     */
    public boolean isValueSupport(PsiExpression expression) {
        if (expression == null) {
            return false;
        }
        //不支持函数表达式，三目运算
        if (PsiTreeUtil.findChildOfAnyType(expression, PsiMethodCallExpression.class, PsiConditionalExpression.class) != null) {
            return false;
        }
        //TODO 如果有引用且引用解析后不支持也不行
        return true;
    }
}
