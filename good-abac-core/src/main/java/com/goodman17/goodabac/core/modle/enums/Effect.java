package com.goodman17.goodabac.core.modle.enums;

/**
 * 权限效果枚举
 * <p>
 * 定义了访问控制策略的效果结果，表示是允许还是拒绝访问请求。
 * 在访问控制策略评估中，此枚举用于确定最终的授权决定。
 * </p>
 * 
 * @author goodman17
 * @since 1.0.0
 */
public enum Effect {
    /**
     * 允许访问
     * 表示授予对请求资源或操作的访问权限
     */
    ALLOW("允许"),

    /**
     * 拒绝访问
     * 表示拒绝对请求资源或操作的访问权限
     */
    DENY("拒绝");

    /**
     * 效果的描述信息
     */
    private final String description;

    /**
     * 构造函数
     * 
     * @param description 效果的描述信息
     */
    Effect(String description) {
        this.description = description;
    }

    /**
     * 获取效果的描述信息
     * 
     * @return 描述信息
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * 根据字符串值获取对应的枚举实例
     * 不区分大小写
     * 
     * @param value 效果字符串值
     * @return 对应的Effect枚举实例，如果无法识别则返回null
     */
    public static Effect fromString(String value) {
        if (value == null) {
            return null;
        }

        try {
            return Effect.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * 判断当前效果是否为允许
     * 
     * @return 如果是ALLOW则返回true，否则返回false
     */
    public boolean isAllow() {
        return this == ALLOW;
    }

    /**
     * 判断当前效果是否为拒绝
     * 
     * @return 如果是DENY则返回true，否则返回false
     */
    public boolean isDeny() {
        return this == DENY;
    }
}
