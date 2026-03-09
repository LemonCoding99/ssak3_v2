package com.example.ssak3.common.utils;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * #request.productId 같은 값을 해석하기 위해 필요한 유틸리티 클래스
 */
public class CustomSpringELParser {

    public static Object getValue(String[] parameterNames, Object[] args, String key) {

        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        return parser.parseExpression(key).getValue(context);
    }
}
