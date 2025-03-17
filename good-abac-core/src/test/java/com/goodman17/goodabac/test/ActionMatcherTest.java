package com.goodman17.goodabac.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.goodman17.goodabac.core.utils.ActionMatcher;

public class ActionMatcherTest {
    
    @Test
    public void testExactMatch() {
        // 精确匹配测试
        assertTrue("精确匹配应当成功", ActionMatcher.match("user:create", "user:create"));
        assertTrue("精确匹配应当成功", ActionMatcher.match("resource:view", "resource:view"));
        assertFalse("不同操作应当匹配失败", ActionMatcher.match("user:create", "user:delete"));
    }
    
    @Test
    public void testWildcardMatch() {
        // 通配符匹配测试
        assertTrue("通配符应当匹配任意操作", ActionMatcher.match("user:create", "user:*"));
        assertTrue("通配符应当匹配任意资源", ActionMatcher.match("resource:view", "*:view"));
        assertTrue("通配符应当匹配多段操作", ActionMatcher.match("user:admin:create", "user:*:create"));
        
        assertFalse("不同资源前缀应当匹配失败", ActionMatcher.match("resource:view", "user:*"));
        assertFalse("不同操作后缀应当匹配失败", ActionMatcher.match("user:delete", "user:create"));
    }
    
    @Test
    public void testEmptyAndNullInputs() {
        // 空输入和null测试
        assertFalse("空操作应当匹配失败", ActionMatcher.match("", "user:*"));
        assertFalse("null操作应当匹配失败", ActionMatcher.match(null, "user:*"));
        assertFalse("空模式应当匹配失败", ActionMatcher.match("user:create", ""));
        assertFalse("null模式应当匹配失败", ActionMatcher.match("user:create", null));
    }
    
    @Test
    public void testMultiSegmentActions() {
        // 多段操作测试
        assertTrue("应当匹配多段操作", ActionMatcher.match("user:admin:create", "user:admin:create"));
        assertTrue("通配符应当匹配第二段", ActionMatcher.match("user:admin:create", "user:*:create"));
        assertTrue("通配符应当匹配第三段", ActionMatcher.match("user:admin:create", "user:admin:*"));
        
        assertFalse("段数不同应当匹配失败", ActionMatcher.match("user:create", "user:admin:create"));
        assertFalse("段数不同应当匹配失败", ActionMatcher.match("user:admin:create:post", "user:admin:create"));
    }
}
