package com.goodman17.goodabac.core.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.goodman17.goodabac.core.modle.Condition;
import com.goodman17.goodabac.core.modle.EnforceContext;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;

public class AviatorConditionHandler implements ConditionHandler {

    private static final Logger logger = LoggerFactory.getLogger(AviatorConditionHandler.class);

    @Override
    public boolean handle(EnforceContext context, Condition condition) {
        if (condition.getScript() == null || condition.getScript().isEmpty()) {
            return false;
        }

        try {
            Expression expr = AviatorEvaluator.compile(condition.getScript(), true);
            Object result = expr.execute(context.getAttributes());
            if (result instanceof Boolean) {
                return (Boolean) result;
            } else if (result instanceof String) {
                return Boolean.parseBoolean((String) result);
            } else {
                logger.error("条件表达式返回值类型不支持");
            }
        } catch (Exception e) {
            logger.error("执行条件表达式失败", e);
        }

        return false;
    }

    @Override
    public String type() {
        return "aviator";
    }
}
