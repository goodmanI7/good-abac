package com.goodman17.goodabac.core.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通配符匹配工具类
 * <p>
 * 支持的通配符：
 * <ul>
 * <li>'*': 匹配零个或多个任意字符</li>
 * <li>'?': 匹配单个任意字符</li>
 * </ul>
 * 例如：
 * 
 * <pre>
 * "abc*" 可以匹配 "abc", "abcd", "abcdef" 等
 * "a?c" 可以匹配 "abc", "adc" 等，但不匹配 "ac", "abbc" 等
 * </pre>
 */
public class WildcardMatcher {

    /** 正则表达式中的特殊字符，需要进行转义 */
    private static final String SPECIAL_CHARS = ".^$+{}[]\\|()";

    /** 缓存大小限制，防止内存泄漏 */
    private static final int MAX_CACHE_SIZE = 1000;

    /** 使用LRU策略的缓存，优先移除最近最少使用的项 */
    private static final Map<String, Pattern> PATTERN_CACHE = new LinkedHashMap<String, Pattern>(16, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Pattern> eldest) {
            return size() > MAX_CACHE_SIZE;
        }
    };

    /** 读写锁，用于保护缓存的并发访问 */
    private static final ReentrantReadWriteLock CACHE_LOCK = new ReentrantReadWriteLock();
    private static final ReentrantReadWriteLock.ReadLock READ_LOCK = CACHE_LOCK.readLock();
    private static final ReentrantReadWriteLock.WriteLock WRITE_LOCK = CACHE_LOCK.writeLock();

    /**
     * 判断字符串是否匹配指定的通配符模式
     *
     * @param pattern 通配符模式，支持 '*'（匹配零个或多个字符）和 '?'（匹配单个字符）
     * @param str     要匹配的字符串
     * @return 如果字符串匹配模式则返回true，否则返回false
     */
    public static boolean isMatch(String pattern, String str) {
        // 处理边界情况
        if (pattern == null || pattern.isEmpty()) {
            return str == null || str.isEmpty();
        }
        if (str == null) {
            return false;
        }

        // 先执行下全量匹配
        if (pattern.equals(str)) {
            return true;
        }

        // 尝试从缓存获取已编译的模式
        Pattern compiledPattern = getCompiledPattern(pattern);

        // 执行匹配
        Matcher m = compiledPattern.matcher(str);
        return m.matches();
    }

    /**
     * 获取编译后的正则表达式模式
     * 
     * @param wildcardPattern 通配符模式
     * @return 编译后的正则表达式模式
     */
    private static Pattern getCompiledPattern(String wildcardPattern) {
        Pattern pattern;

        // 先尝试使用读锁从缓存中获取
        READ_LOCK.lock();
        try {
            pattern = PATTERN_CACHE.get(wildcardPattern);
            if (pattern != null) {
                return pattern;
            }
        } finally {
            READ_LOCK.unlock();
        }

        // 缓存中没有找到，需要创建新的Pattern
        WRITE_LOCK.lock();
        try {
            // 双重检查，避免其他线程已经添加了相同的pattern
            pattern = PATTERN_CACHE.get(wildcardPattern);
            if (pattern != null) {
                return pattern;
            }

            // 将通配符模式转换为正则表达式
            String regexPattern = convertWildcardToRegex(wildcardPattern);

            // 编译正则表达式
            pattern = Pattern.compile(regexPattern);

            // 将模式放入缓存（LinkedHashMap自动处理LRU逻辑）
            PATTERN_CACHE.put(wildcardPattern, pattern);

            return pattern;
        } finally {
            WRITE_LOCK.unlock();
        }
    }

    /**
     * 将通配符模式转换为正则表达式
     * 
     * @param wildcardPattern 通配符模式
     * @return 对应的正则表达式字符串
     */
    private static String convertWildcardToRegex(String wildcardPattern) {
        StringBuilder regexPattern = new StringBuilder();

        // 添加开始标记，确保匹配整个字符串
        regexPattern.append('^');

        for (char c : wildcardPattern.toCharArray()) {
            if (c == '*') {
                regexPattern.append(".*"); // 通配符 * 替换为正则表达式的 ".*"
            } else if (c == '?') {
                regexPattern.append('.'); // 通配符 ? 替换为正则表达式的 "."
            } else if (SPECIAL_CHARS.indexOf(c) != -1) {
                regexPattern.append('\\').append(c); // 转义正则特殊字符
            } else {
                regexPattern.append(c); // 直接添加普通字符
            }
        }

        // 添加结束标记，确保匹配整个字符串
        regexPattern.append('$');

        return regexPattern.toString();
    }
}
