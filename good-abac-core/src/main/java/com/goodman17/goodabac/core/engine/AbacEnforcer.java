package com.goodman17.goodabac.core.engine;

import org.casbin.jcasbin.main.Enforcer;

import com.goodman17.goodabac.core.function.MatchActionFunction;
import com.goodman17.goodabac.core.function.MatchConditionFunction;
import com.goodman17.goodabac.core.function.MatchResourceFunction;
import com.goodman17.goodabac.core.modle.Statement;

/**
 * AbacEnforcer 类是基于属性的访问控制(ABAC)执行器的实现。
 * 该类继承自 jCasbin 框架的 Enforcer 类，用于执行访问控制策略。
 * ABAC 是一种访问控制模型，它根据用户、资源、操作和环境的属性来决定访问权限。
 */
public class AbacEnforcer extends Enforcer {

    /**
     * 构造一个新的 AbacEnforcer 实例。
     *
     * @param modelPath 模型配置文件的路径，用于定义访问控制模型的结构
     */
    public AbacEnforcer(String modelPath) {
        super(modelPath);
        addFunction("m_act", new MatchActionFunction());
        addFunction("m_res", new MatchResourceFunction());
        addFunction("m_cond", new MatchConditionFunction());
    }

    /**
     * 添加一个策略
     * 
     * @param statement 策略对象
     */
    public void addStatement(Statement statement) {
        String effect = statement.getEffect();
        String condition = statement.getCondition().toJson();
        for (String action : statement.getAction()) {
            for (String resource : statement.getResource()) {
                this.addPolicy(action, resource, condition, effect);
            }
        }
    }
}
