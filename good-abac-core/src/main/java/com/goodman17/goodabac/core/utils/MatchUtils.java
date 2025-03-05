package com.goodman17.goodabac.core.utils;

public class MatchUtils {

    public static boolean matchPath(String pattern, String requestPath) {
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
    
}
