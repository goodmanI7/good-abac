package com.goodman17.goodabac.core.modle;

import java.util.HashMap;
import java.util.Map;

/**
 * Subject 类表示 ABAC (基于属性的访问控制) 系统中的主体实体。
 * 主体通常是指发起访问请求的用户、进程或系统。
 * 每个主体有一个唯一标识符和一组描述其特性的属性。
 */
public class Subject {

    /**
     * 主体的唯一标识符
     */
    private String id;

    /**
     * 主体的属性集合，以键值对形式存储
     */
    private Map<String, String> attrs;

    /**
     * 默认构造函数，创建一个空的主体实例，
     * 初始化空的属性映射
     */
    public Subject() {
        this.attrs = new HashMap<>();
    }

    /**
     * 构造带有ID的主体实例
     * 
     * @param id 主体的唯一标识符
     */
    public Subject(String id) {
        this.id = id;
        this.attrs = new HashMap<>();
    }

    /**
     * 获取主体ID
     * 
     * @return 主体的唯一标识符
     */
    public String getId() {
        return id;
    }

    /**
     * 设置主体ID
     * 
     * @param id 要设置的主体唯一标识符
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取主体的所有属性
     * 
     * @return 包含主体属性的Map
     */
    public Map<String, String> getAttrs() {
        return attrs;
    }

    /**
     * 设置主体的属性集合
     * 
     * @param attrs 要设置的属性Map
     */
    public void setAttrs(Map<String, String> attrs) {
        this.attrs = attrs;
    }

    /**
     * 添加一个属性
     * 
     * @param key   属性键
     * @param value 属性值
     */
    public void addAttr(String key, String value) {
        this.attrs.put(key, value);
    }

    /**
     * 判断当前主体是否为管理员
     * 
     * @return 如果当前主体具有管理员角色，则返回true，否则返回false
     */
    public boolean isAdmin() {
        return false;
    }
}
