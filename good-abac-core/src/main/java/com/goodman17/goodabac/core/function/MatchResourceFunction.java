package com.goodman17.goodabac.core.function;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.goodman17.goodabac.core.modle.Resource;
import com.goodman17.goodabac.core.utils.WildcardMatcher;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;

/**
 * 资源匹配函数类
 * <p>
 * 该类用于检查请求的资源是否与策略中定义的资源列表匹配。
 * 支持通配符匹配模式，如果请求资源与策略资源列表中的任何一项匹配，则返回真。
 * 通配符支持：
 * <ul>
 * <li>'*': 匹配零个或多个任意字符</li>
 * <li>'?': 匹配单个任意字符</li>
 * </ul>
 * 例如："order:*" 可以匹配 "order:123", "order:create" 等资源ID。
 * </p>
 * 
 * @author goodman17
 */
public class MatchResourceFunction extends AbstractFunction {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchResourceFunction.class);

    @Override
    public String getName() {
        return "m_res";
    }

    /**
     * 执行资源匹配逻辑
     * 
     * @param env                执行环境上下文
     * @param policyResourceObj  策略中定义的资源列表（JSON格式的字符串数组）
     * @param requestResourceObj 请求访问的资源对象
     * @return 如果请求资源与策略资源匹配返回TRUE，否则返回FALSE
     */
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject policyResourceObj,
            AviatorObject requestResourceObj) {
        try {
            // 解析策略中的资源列表
            List<String> policyResources = parseResourceList(policyResourceObj.getValue(env).toString());

            // 如果策略资源列表为空，则拒绝访问
            if (policyResources.isEmpty()) {
                LOGGER.info("策略资源列表为空，拒绝访问");
                return AviatorBoolean.FALSE;
            }

            // 获取请求的资源对象
            Resource resource = (Resource) requestResourceObj.getValue(env);
            if (resource == null || resource.getId() == null || resource.getId().isEmpty()) {
                LOGGER.info("请求资源为空或ID为空，拒绝访问");
                return AviatorBoolean.FALSE;
            }

            String requestResourceId = resource.getId();

            // 遍历策略资源列表，使用通配符匹配检查是否有匹配的资源
            for (String policyResource : policyResources) {
                if (WildcardMatcher.isMatch(policyResource, requestResourceId)) {
                    LOGGER.info("资源匹配成功: 策略资源 [{}] 匹配请求资源 [{}]", policyResource, requestResourceId);
                    return AviatorBoolean.TRUE;
                }
            }

            // 没有找到匹配的资源
            LOGGER.info("资源匹配失败: 请求资源 [{}] 不在允许列表中", requestResourceId);
            return AviatorBoolean.FALSE;
        } catch (ClassCastException e) {
            LOGGER.info("请求资源类型错误，期望类型为Resource", e);
            return AviatorBoolean.FALSE;
        } catch (Exception e) {
            LOGGER.info("资源匹配过程发生异常", e);
            return AviatorBoolean.FALSE;
        }
    }

    /**
     * 解析策略资源JSON字符串为列表
     *
     * @param resourceStr 策略资源JSON字符串
     * @return 策略资源列表
     */
    private List<String> parseResourceList(String resourceStr) {
        if (resourceStr == null || resourceStr.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            List<String> resourceList = JSON.parseArray(resourceStr, String.class);
            return resourceList != null ? resourceList : Collections.emptyList();
        } catch (JSONException e) {
            LOGGER.error("解析策略资源列表失败: {}", resourceStr, e);
            return Collections.emptyList();
        }
    }
}
