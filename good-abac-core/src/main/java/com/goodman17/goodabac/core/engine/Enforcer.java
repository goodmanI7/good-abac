package com.goodman17.goodabac.core.engine;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.goodman17.goodabac.core.handler.ConditionHandler;
import com.goodman17.goodabac.core.modle.Condition;
import com.goodman17.goodabac.core.modle.EnforceContext;
import com.goodman17.goodabac.core.modle.Env;
import com.goodman17.goodabac.core.modle.Resource;
import com.goodman17.goodabac.core.modle.Statement;
import com.goodman17.goodabac.core.modle.Subject;
import com.goodman17.goodabac.core.modle.enums.Effect;
import com.goodman17.goodabac.core.utils.ActioMatcher;

/**
 * 基于属性的访问控制(ABAC)强制执行器
 * 
 * <p>
 * Enforcer 负责根据提供的策略声明(Statement)判断主体(Subject)是否有权限
 * 对特定资源(Resource)执行特定操作(Action)。执行过程会考虑环境条件(Env)
 * 以及策略中定义的条件(Condition)。
 * </p>
 * 
 * <p>
 * 执行过程遵循以下规则:
 * </p>
 * <ul>
 * <li>如果主体是管理员(Admin)，直接允许所有操作</li>
 * <li>如果任何适用的策略声明明确拒绝访问，则整个请求被拒绝</li>
 * <li>如果至少有一个适用的策略声明允许访问且没有拒绝声明，则允许访问</li>
 * <li>默认情况下，所有访问都被拒绝</li>
 * </ul>
 */
public class Enforcer {

    /**
     * 条件处理器
     */
    private final Map<String, ConditionHandler> conditionHandlers;

    /**
     * 创建一个新的Enforcer实例
     */
    public Enforcer() {
        this.conditionHandlers = new ConcurrentHashMap<>();
    }

    /**
     * 执行访问控制决策
     * 
     * @param action     请求执行的操作标识符
     * @param subject    执行操作的主体
     * @param resource   操作针对的资源
     * @param env        执行环境上下文
     * @param statements 用于决策的策略声明列表
     * @return 如果允许访问返回true，否则返回false
     */
    public boolean enforce(String action, Subject subject, Resource resource, Env env, List<Statement> statements) {
        if (subject.isAdmin()) {
            // 如果主体是管理员，则直接返回true
            return true;
        }

        boolean anyAllowed = false;
        EnforceContext context = new EnforceContext(subject, resource, env);
        for (Statement statement : statements) {
            // 如果操作列表为空，则跳过
            if (statement.getAction() == null || statement.getAction().isEmpty()) {
                continue;
            }
            // 如果操作列表中没有匹配的操作，则跳过
            if (!statement.getAction().stream().filter(act -> ActioMatcher.match(action, act)).findFirst()
                    .isPresent()) {
                continue;
            }
            // 如果资源列表为空，则跳过
            if (statement.getResource() == null || statement.getResource().isEmpty()) {
                continue;
            }
            // 如果资源列表中没有匹配的资源，则跳过
            if (!statement.getResource().stream().filter(res -> "*".equals(res) || res.equals(resource.getId()))
                    .findFirst().isPresent()) {
                continue;
            }

            if (fireCondition(context, statement.getConditions())) {
                if (statement.getEffect().isDeny()) {
                    return false;
                }
                anyAllowed = true;
            }

        }
        return anyAllowed;
    }

    /**
     * 注册条件处理器
     * 
     * <p>
     * 将指定的条件处理器注册到强制执行器中，使其能够处理特定类型的条件判断。
     * 条件处理器根据其类型被存储在内部映射中，当遇到相应类型的条件时会被调用。
     * </p>
     * 
     * @param conditionHandler 要注册的条件处理器实例
     */
    public void registerConditionHandler(ConditionHandler conditionHandler) {
        conditionHandlers.put(conditionHandler.type(), conditionHandler);
    }

    /**
     * 评估条件是否满足
     * 
     * @param context   执行上下文，包含主体、资源和环境信息
     * @param condition 要评估的条件
     * @return 如果条件满足或为空返回true，否则返回false
     */
    private boolean fireCondition(EnforceContext context, List<Condition> conditions) {
        boolean isAnyAllowed = false;
        for (Condition condition : conditions) {
            ConditionHandler conditionHandler = conditionHandlers.get(condition.getType());
            if (conditionHandler == null) {
                throw new RuntimeException("Condition handler not found for type: " + condition.getType());
            }
            if (conditionHandler.handle(context, condition)) {
                if (Effect.DENY.equals(condition.getEffect())) {
                    return false;
                }
                isAnyAllowed = true;
            }
        }
        return isAnyAllowed;
    }
}
