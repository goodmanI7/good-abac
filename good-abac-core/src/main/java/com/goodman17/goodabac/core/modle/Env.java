package com.goodman17.goodabac.core.modle;

import java.util.HashMap;
import java.util.Map;

/**
 * 环境类，用于存储与访问控制相关的环境属性信息。
 * 在基于属性的访问控制（ABAC）系统中，Env代表了访问决策可能依赖的环境上下文，
 * 如时间、位置、系统状态等信息。
 */
public class Env {

    /**
     * 存储环境属性的键值对映射
     */
    private Map<String, String> attrs;

    /**
     * 构造函数，初始化一个空的环境属性映射
     */
    public Env() {
        this.attrs = new HashMap<>();
    }

    /**
     * 获取所有环境属性
     * 
     * @return 包含所有环境属性的映射
     */
    public Map<String, String> getAttrs() {
        return attrs;
    }

    /**
     * 设置环境属性映射
     * 
     * @param attrs 要设置的环境属性映射
     */
    public void setAttrs(Map<String, String> attrs) {
        this.attrs = attrs;
    }

    /**
     * 添加单个环境属性
     * 
     * @param key   属性键
     * @param value 属性值
     */
    public void addAttr(String key, String value) {
        this.attrs.put(key, value);
    }

    /**
     * 获取用于执行访问控制的环境属性
     * 该方法返回一个新的Map，其中包含所有环境属性，但每个键都添加了"env."前缀，
     * 以便在访问控制执行期间明确标识这些属性来自环境上下文。
     * 
     * @return 带有"env."前缀的环境属性映射
     */
    public Map<String, String> getEnforceAttrs() {
        Map<String, String> enforceAttrs = new HashMap<>();
        if (attrs != null && !attrs.isEmpty()) {
            attrs.forEach((key, value) -> {
                enforceAttrs.put("env." + key, value);
            });
        }
        return enforceAttrs;
    }
}
