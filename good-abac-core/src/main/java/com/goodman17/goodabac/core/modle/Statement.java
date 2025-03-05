package com.goodman17.goodabac.core.modle;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * ABAC权限策略声明类
 * 表示特定主体对特定资源可执行的操作及其效果
 */
public class Statement {

    /**
     * 允许或拒绝的操作列表
     */
    private List<String> action;

    /**
     * 操作适用的资源列表
     */
    private List<String> resource;

    /**
     * 策略效果，通常为"allow"或"deny"
     */
    private String effect;

    /**
     * 条件表达式
     */
    private Condition condition;

    public Statement() {
        this.action = new ArrayList<>();
        this.resource = new ArrayList<>();
        this.effect = "allow";
        this.condition = new Condition();
    }

    /**
     * 获取允许或拒绝的操作列表
     * 
     * @return 操作列表
     */
    public List<String> getAction() {
        return action;
    }

    /**
     * 获取操作列表的JSON字符串表示
     * 
     * @return 操作列表的JSON字符串
     */
    public String getActionAsJson() {
        return JSON.toJSONString(action);
    }

    /**
     * 设置允许或拒绝的操作列表
     * 
     * @param action 操作列表
     */
    public void setAction(List<String> action) {
        this.action = action;
    }

    /**
     * 添加一个操作
     * 
     * @param action 操作
     */
    public void addAction(String action) {
        this.action.add(action);
    }

    /**
     * 获取操作适用的资源列表
     * 
     * @return 资源列表
     */
    public List<String> getResource() {
        return resource;
    }

    /**
     * 获取资源列表的JSON字符串表示
     * 
     * @return 资源列表的JSON字符串
     */
    public String getResourceAsJson() {
        return JSON.toJSONString(resource);
    }

    public void setResource(List<String> resource) {
        this.resource = resource;
    }

    /**
     * 添加一个资源
     * 
     * @param resource 资源
     */
    public void addResource(String resource) {
        this.resource.add(resource);
    }

    /**
     * 获取策略效果
     * 
     * @return 策略效果，通常为"allow"或"deny"
     */
    public String getEffect() {
        return effect;
    }

    /**
     * 设置策略效果
     * 
     * @param effect 策略效果，通常为"allow"或"deny"
     */
    public void setEffect(String effect) {
        this.effect = effect;
    }

    /**
     * 获取条件表达式
     * 
     * @return 条件表达式
     */
    public Condition getCondition() {
        return condition;
    }

    /**
     * 设置条件表达式
     * 
     * @param condition 条件表达式
     */
    public void setCondition(Condition condition) {
        this.condition = condition;
    }
}
