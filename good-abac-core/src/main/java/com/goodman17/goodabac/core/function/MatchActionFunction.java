package com.goodman17.goodabac.core.function;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.goodman17.goodabac.core.utils.WildcardMatcher;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 动作匹配函数
 * <p>
 * 该函数用于判断请求的动作是否与策略中定义的动作列表匹配。
 * 策略动作支持通配符匹配（如："get*"、"*"等）。
 * </p>
 * 
 * @author goodman17
 */
public class MatchActionFunction extends AbstractFunction {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchActionFunction.class);

    @Override
    public String getName() {
        return "m_act";
    }

    /**
     * 执行动作匹配逻辑
     *
     * @param env           执行环境
     * @param policyAction  策略动作列表，JSON数组字符串
     * @param requestAction 请求动作，字符串
     * @return 如果请求动作与任一策略动作匹配，则返回TRUE；否则返回FALSE
     */
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject policyAction, AviatorObject requestAction) {
        // 获取策略动作字符串并解析为列表
        String policyActionStr = policyAction.getValue(env).toString();
        List<String> policyActionList = parseActionList(policyActionStr);

        // 获取请求动作字符串
        String requestActionStr = requestAction.getValue(env).toString();

        // 验证输入参数
        if (policyActionList.isEmpty()) {
            // 策略动作列表为空，不允许任何动作
            LOGGER.debug("策略动作列表为空，拒绝访问");
            return AviatorBoolean.FALSE;
        }

        if (requestActionStr == null || requestActionStr.isEmpty()) {
            // 请求动作为空，不允许访问
            LOGGER.debug("请求动作为空，拒绝访问");
            return AviatorBoolean.FALSE;
        }

        // 遍历策略动作列表，检查是否有匹配的动作
        for (String action : policyActionList) {
            if (WildcardMatcher.isMatch(action, requestActionStr)) {
                LOGGER.debug("动作匹配成功: 策略动作 [{}] 匹配请求动作 [{}]", action, requestActionStr);
                return AviatorBoolean.TRUE;
            }
        }

        // 没有找到匹配的动作
        LOGGER.debug("动作匹配失败: 请求动作 [{}] 不在允许列表中", requestActionStr);
        return AviatorBoolean.FALSE;
    }

    /**
     * 解析策略动作JSON字符串为列表
     *
     * @param actionStr 策略动作JSON字符串
     * @return 策略动作列表
     */
    private List<String> parseActionList(String actionStr) {
        if (actionStr == null || actionStr.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            List<String> actionList = JSON.parseArray(actionStr, String.class);
            return actionList != null ? actionList : Collections.emptyList();
        } catch (JSONException e) {
            LOGGER.error("解析策略动作列表失败: {}", actionStr, e);
            return Collections.emptyList();
        }
    }
}
