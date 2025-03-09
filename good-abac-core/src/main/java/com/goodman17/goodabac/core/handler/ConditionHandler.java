package com.goodman17.goodabac.core.handler;

import com.goodman17.goodabac.core.modle.Condition;
import com.goodman17.goodabac.core.modle.EnforceContext;

public interface ConditionHandler {

    /**
     * 获取条件处理器的类型
     * 
     * @return 条件处理器的类型
     */
    String type();

    /**
     * 处理条件
     * 
     * @param context   执行上下文
     * @param condition 条件
     * @return 处理结果
     */
    boolean handle(EnforceContext context, Condition condition);
}
