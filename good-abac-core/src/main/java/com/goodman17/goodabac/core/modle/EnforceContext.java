package com.goodman17.goodabac.core.modle;

import java.util.HashMap;
import java.util.Map;

/**
 * 权限校验上下文类
 * 包含授权决策所需的三个主要组件：主体、资源和环境
 * 用于在进行访问控制决策时传递所有必要的上下文信息
 */
public class EnforceContext {

    /**
     * 访问主体，通常代表用户或系统
     */
    private final Subject subject;

    /**
     * 被访问的资源对象
     */
    private final Resource resource;

    /**
     * 访问环境信息，如时间、位置等
     */
    private final Env env;

    /**
     * 上下文属性
     */
    private final Map<String, Object> attributes;

    /**
     * 构造一个完整的权限校验上下文
     * 
     * @param subject  访问主体
     * @param resource 被访问的资源
     * @param env      访问环境
     */
    public EnforceContext(Subject subject, Resource resource, Env env) {
        this.subject = subject;
        this.resource = resource;
        this.env = env;
        this.attributes = new HashMap<>();
        buildAttributes();
    }

    private void buildAttributes() {
        attributes.clear();
        // 将subject、resource和env的属性合并到attributes中
        attributes.putAll(subject.getEnforceAttrs());
        attributes.putAll(resource.getEnforceAttrs());
        attributes.putAll(env.getEnforceAttrs());
    }

    /**
     * 获取访问主体
     * 
     * @return 访问主体对象
     */
    public Subject getSubject() {
        return subject;
    }

    /**
     * 获取被访问的资源
     * 
     * @return 资源对象
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * 获取访问环境
     * 
     * @return 环境对象
     */
    public Env getEnv() {
        return env;
    }

    /**
     * 获取上下文属性
     * 
     * @return 上下文属性
     */
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
