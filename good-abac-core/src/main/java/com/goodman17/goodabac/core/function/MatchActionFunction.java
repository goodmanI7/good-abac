package com.goodman17.goodabac.core.function;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;

public class MatchActionFunction extends AbstractFunction {

    @Override
    public String getName() {
        return "m_act";
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject policyAction, AviatorObject requestAction) {
        String policyActionStr = policyAction.getValue(env).toString();
        List<String> policyActionList = JSON.parseArray(policyActionStr, String.class);

        String requestActionStr = requestAction.getValue(env).toString();

        // 如果策略动作列表为空，不允许任何动作
        if (policyActionList == null || policyActionList.isEmpty()) {
            return AviatorBoolean.FALSE;
        }

        // 如果策略动作列表包含 "*"，表示允许所有动作
        if (policyActionList.contains("*")) {
            return AviatorBoolean.TRUE;
        }

        // 检查请求的动作是否在策略允许的动作列表中
        if (policyActionList.contains(requestActionStr)) {
            return AviatorBoolean.TRUE;
        }

        // 处理通配符模式匹配，例如 "order:*" 匹配 "order:111"
        for (String pattern : policyActionList) {
            if (pattern.contains("*")) {
                // 处理前缀匹配场景，例如 "order:*"
                if (pattern.endsWith("*")) {
                    String prefix = pattern.substring(0, pattern.length() - 1);
                    if (requestActionStr.startsWith(prefix)) {
                        return AviatorBoolean.TRUE;
                    }
                }
                // 处理后缀匹配场景，例如 "*:read"
                else if (pattern.startsWith("*")) {
                    String suffix = pattern.substring(1);
                    if (requestActionStr.endsWith(suffix)) {
                        return AviatorBoolean.TRUE;
                    }
                }
                // 处理中间带通配符的场景，例如 "order:*:read"
                else {
                    // 将通配符模式转换为正则表达式模式
                    String regexPattern = "^" +
                            Pattern.quote(pattern).replace("\\*", "\\E.*\\Q") +
                            "$";
                    // 删除开头和结尾多余的 \Q \E
                    regexPattern = regexPattern.replace("\\Q\\E", "");
                    if (requestActionStr.matches(regexPattern)) {
                        return AviatorBoolean.TRUE;
                    }
                }
            }
        }

        return AviatorBoolean.FALSE;
    }
}
