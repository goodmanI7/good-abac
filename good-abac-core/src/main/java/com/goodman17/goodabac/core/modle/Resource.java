package com.goodman17.goodabac.core.modle;

import java.util.HashMap;
import java.util.Map;

/**
 * 资源类，表示在ABAC(基于属性的访问控制)系统中的资源对象。
 * 每个资源都有一个唯一标识符和与之关联的属性集合。
 * 在权限验证过程中，资源的属性将用于评估访问规则。
 */
public class Resource {

    /**
     * 资源的唯一标识符
     */
    private String id;

    /**
     * 资源的属性集合，以键值对形式存储
     */
    private Map<String, String> attrs;

    /**
     * 默认构造函数，创建一个空的资源对象，
     * 初始化一个空的属性集合
     */
    public Resource() {
        this.attrs = new HashMap<>();
    }

    /**
     * 创建一个指定ID的资源对象，
     * 同时初始化一个空的属性集合
     * 
     * @param id 资源的唯一标识符
     */
    public Resource(String id) {
        this.id = id;
        this.attrs = new HashMap<>();
    }

    /**
     * 获取资源的唯一标识符
     * 
     * @return 资源ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置资源的唯一标识符
     * 
     * @param id 要设置的资源ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取资源的所有属性
     * 
     * @return 包含资源属性的Map集合
     */
    public Map<String, String> getAttrs() {
        return attrs;
    }

    /**
     * 设置资源的属性集合
     * 
     * @param attrs 要设置的属性Map集合
     */
    public void setAttrs(Map<String, String> attrs) {
        this.attrs = attrs;
    }

    /**
     * 向资源添加单个属性
     * 
     * @param key   属性名称
     * @param value 属性值
     */
    public void addAttr(String key, String value) {
        this.attrs.put(key, value);
    }
}
