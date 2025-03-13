package com.goodman17.goodabac.core.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.goodman17.goodabac.core.modle.Condition;
import com.goodman17.goodabac.core.modle.EnforceContext;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;

/**
 * 基于Aviator表达式引擎的条件处理器
 * <p>
 * 该处理器使用Aviator表达式引擎来评估和执行条件脚本，从而判断权限规则是否适用。
 * Aviator是一个高性能的表达式计算引擎，支持在运行时动态编译和执行表达式。
 * </p>
 */
public class AviatorConditionHandler implements ConditionHandler {

    private static final Logger logger = LoggerFactory.getLogger(AviatorConditionHandler.class);

    /**
     * 处理条件表达式并返回结果
     * 
     * @param context 执行上下文，包含评估条件所需的属性
     * @param condition 要评估的条件，包含Aviator表达式脚本
     * @return 条件评估结果，true表示条件满足，false表示条件不满足或评估出错
     */
    @Override
    public boolean handle(EnforceContext context, Condition condition) {
        // 检查条件脚本是否为空
        if (condition.getScript() == null || condition.getScript().isEmpty()) {
            return false;
        }

        try {
            // 编译条件表达式
            Expression expr = AviatorEvaluator.compile(condition.getScript(), true);
            // 执行表达式，使用上下文中的属性作为环境变量
            Object result = expr.execute(context.getAttributes());
            
            // 处理不同类型的返回值
            if (result instanceof Boolean) {
                return (Boolean) result;
            } else if (result instanceof String) {
                return Boolean.parseBoolean((String) result);
            } else {
                logger.error("条件表达式返回值类型不支持");
            }
        } catch (Exception e) {
            // 记录执行过程中的异常
            logger.error("执行条件表达式失败", e);
        }

        // 默认返回false，表示条件不满足
        return false;
    }

    /**
     * 返回处理器类型标识
     * 
     * @return 返回字符串"aviator"，表示这是一个Aviator表达式处理器
     */
    @Override
    public String type() {
        return "aviator";
    }
}
