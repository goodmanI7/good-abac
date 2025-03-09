package com.goodman17.goodabac.core.modle;

import com.goodman17.goodabac.core.modle.enums.Effect;

/**
 * 条件类，用于定义ABAC（基于属性的访问控制）系统中的权限判断条件。
 * 每个条件包含脚本类型、条件脚本内容以及条件匹配后的效果（允许或拒绝）。
 */
public class Condition {

    /**
     * 条件脚本的类型，默认为"aviator"
     */
    private final String type;

    /**
     * 条件判断脚本，用于评估访问请求是否满足此条件
     */
    private final String script;

    /**
     * 条件匹配后的效果，可以是ALLOW（允许）或DENY（拒绝）
     */
    private final Effect effect;

    /**
     * 简化构造函数，创建一个默认类型为"aviator"且效果为ALLOW的条件
     *
     * @param script 条件判断脚本
     */
    public Condition(String script) {
        this("aviator", script, Effect.ALLOW);
    }

    /**
     * 完整构造函数，创建一个指定类型和效果的条件
     *
     * @param type   条件脚本类型
     * @param script 条件判断脚本
     * @param effect 条件匹配后的效果
     */
    public Condition(String type, String script, Effect effect) {
        this.type = type;
        this.effect = effect;
        this.script = script;
    }

    /**
     * 获取条件脚本类型
     *
     * @return 条件脚本类型
     */
    public String getType() {
        return type;
    }

    /**
     * 获取条件判断脚本
     *
     * @return 条件判断脚本
     */
    public String getScript() {
        return script;
    }

    /**
     * 获取条件匹配后的效果
     *
     * @return 条件匹配效果
     */
    public Effect getEffect() {
        return effect;
    }
}
