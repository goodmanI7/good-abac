package com.goodman17.goodabac.core.function;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.goodman17.goodabac.core.utils.WildcardMatcher;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;

/**
 * 资源匹配函数类
 * <p>
 * 该类用于检查请求的资源是否与策略中定义的资源列表匹配。
 * 支持通配符匹配模式，如果请求资源与策略资源列表中的任何一项匹配，则返回真。
 * </p>
 */
public class MatchResourceFunction extends AbstractFunction {

    private static final Logger logger = LoggerFactory.getLogger(MatchResourceFunction.class);

    @Override
    public String getName() {
        return "m_res";
    }

    /**
     * 执行资源匹配逻辑
     * 
     * @param env             执行环境上下文
     * @param policyResource  策略中定义的资源列表（JSON格式的字符串数组）
     * @param requestResource 请求访问的资源
     * @return 如果请求资源与策略资源匹配返回TRUE，否则返回FALSE
     */
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject policyResource, AviatorObject requestResource) {
        String policyResourceStr = policyResource.getValue(env).toString();
        String requestResourceStr = requestResource.getValue(env).toString();

        // 记录调试信息
        if (logger.isDebugEnabled()) {
            logger.debug("比较策略资源[{}]与请求资源[{}]", policyResourceStr, requestResourceStr);
        }

        List<String> policyResourceList;
        try {
            // 解析策略资源列表
            policyResourceList = JSON.parseArray(policyResourceStr, String.class);

            // 检查列表为空的情况
            if (policyResourceList == null) {
                policyResourceList = Collections.emptyList();
            }
        } catch (JSONException e) {
            logger.error("解析策略资源列表失败: " + policyResourceStr, e);
            return AviatorBoolean.FALSE;
        }

        // 遍历策略资源列表进行匹配
        for (String resource : policyResourceList) {
            if (WildcardMatcher.isMatch(resource, requestResourceStr)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("资源匹配成功: 策略资源[{}]与请求资源[{}]", resource, requestResourceStr);
                }
                return AviatorBoolean.TRUE;
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("资源匹配失败: 请求资源[{}]与任何策略资源都不匹配", requestResourceStr);
        }
        return AviatorBoolean.FALSE;
    }
}
