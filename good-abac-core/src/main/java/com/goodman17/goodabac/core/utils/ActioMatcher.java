package com.goodman17.goodabac.core.utils;

public class ActioMatcher {

    /**
     * 匹配操作与模式
     * 支持的通配符：
     * '*' - 匹配冒号(:)之前或之后的任意字符序列
     * 
     * @param action  要检查的操作字符串，例如 "user:create"
     * @param pattern 匹配模式，例如 "user:*"（匹配所有user相关操作）
     * @return 如果操作与模式匹配，则返回true
     */
    public static boolean match(String action, String pattern) {
        // 参数有效性检查
        if (pattern == null || pattern.isEmpty() || action == null || action.isEmpty()) {
            return false;
        }

        // 特殊情况：精确匹配
        if (pattern.equals(action)) {
            return true;
        }

        // 处理通配符情况
        if (pattern.contains("*")) {
            // 按冒号分割字符串
            String[] patternParts = pattern.split(":");
            String[] actionParts = action.split(":");

            // 如果分段数量不一致且不是因为通配符，则不匹配
            if (patternParts.length != actionParts.length) {
                return false;
            }

            // 逐段比较
            for (int i = 0; i < patternParts.length; i++) {
                String patternPart = patternParts[i];
                String actionPart = actionParts[i];

                // 如果当前段不是通配符，且两段不相等，则不匹配
                if (!patternPart.equals("*") && !patternPart.equals(actionPart)) {
                    return false;
                }
            }

            // 所有段都匹配
            return true;
        }

        // 没有通配符且不相等，则不匹配
        return false;
    }

    public static void main(String[] args) {
        // 测试用例
        System.out.println("user:create 与 user:* 匹配结果: " + match("user:create",
                "user:*"));
        System.out.println("user:create 与 user:create 匹配结果: " + match("user:create",
                "user:create"));
        System.out.println("user:delete 与 user:* 匹配结果: " + match("user:delete",
                "user:*"));
        System.out.println("resource:view 与 user:* 匹配结果: " + match("resource:view",
                "user:*"));
        System.out.println("user:admin:create 与 user:*:create 匹配结果: " +
                match("user:admin:create", "user:*:create"));
        System.out.println("resource:view 与 *:view 匹配结果: " + match("resource:view", "*:view"));
    }
}
