package com.goodman17.goodabac.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.Assert;

import com.goodman17.goodabac.core.engine.Enforcer;
import com.goodman17.goodabac.core.handler.AviatorConditionHandler;
import com.goodman17.goodabac.core.modle.Condition;
import com.goodman17.goodabac.core.modle.Env;
import com.goodman17.goodabac.core.modle.Resource;
import com.goodman17.goodabac.core.modle.Statement;
import com.goodman17.goodabac.core.modle.Subject;
import com.goodman17.goodabac.core.modle.enums.Effect;

/**
 * @author lirenhao
 *         date: 2025/2/27 19:17
 */
public class AbacTest {

    @Test
    public void testEval() {
        Subject subject = createSubject();
        Resource resource = createResource();
        Env env = createEnv();
        Statement statement = createStatement();
        List<Statement> statements = new ArrayList<>();
        statements.add(statement);
        long start = System.currentTimeMillis();

        Enforcer enforcer = new Enforcer();
        // 注册Aviator条件处理器
        enforcer.registerConditionHandler(new AviatorConditionHandler());
        for (int i = 0; i < 100; i++) {
            boolean result = enforcer.enforce("order:update", subject, resource, env, statements);
            System.out.println(i + " result: " + result);
        }
        long end = System.currentTimeMillis();
        System.out.println("time: " + (end - start));
    }
    
    /**
     * 测试管理员用户访问控制
     * 管理员用户应该能够访问任何资源和执行任何操作
     */
    @Test
    public void testAdminAccess() {
        // 创建一个管理员用户，通过继承Subject类并覆盖isAdmin方法
        Subject adminSubject = new Subject("Admin_User") {
            @Override
            public boolean isAdmin() {
                return true;
            }
        };
        
        Resource resource = createResource();
        Env env = createEnv();
        List<Statement> statements = new ArrayList<>();
        // 即使没有任何策略声明，管理员也应该能够访问
        
        Enforcer enforcer = new Enforcer();
        enforcer.registerConditionHandler(new AviatorConditionHandler());
        
        boolean result = enforcer.enforce("order:delete", adminSubject, resource, env, statements);
        Assert.assertTrue("管理员应当能够执行任何操作", result);
    }
    
    /**
     * 测试明确拒绝的策略
     * 如果有一个策略明确拒绝访问，则整个请求应该被拒绝
     */
    @Test
    public void testExplicitDeny() {
        Subject subject = createSubject();
        Resource resource = createResource();
        Env env = createEnv();
        
        // 创建一个允许访问的策略
        Statement allowStatement = new Statement();
        allowStatement.addAction("order:*");
        allowStatement.addResource("*");
        allowStatement.setEffect(Effect.ALLOW);
        
        // 创建一个拒绝访问的策略
        Statement denyStatement = new Statement();
        denyStatement.addAction("order:delete");
        denyStatement.addResource("*");
        denyStatement.setEffect(Effect.DENY);
        
        List<Statement> statements = new ArrayList<>();
        statements.add(allowStatement);
        statements.add(denyStatement);
        
        Enforcer enforcer = new Enforcer();
        enforcer.registerConditionHandler(new AviatorConditionHandler());
        
        // 测试允许的操作
        boolean updateResult = enforcer.enforce("order:update", subject, resource, env, statements);
        Assert.assertTrue("应该允许更新操作", updateResult);
        
        // 测试拒绝的操作
        boolean deleteResult = enforcer.enforce("order:delete", subject, resource, env, statements);
        Assert.assertFalse("应该拒绝删除操作", deleteResult);
    }
    
    /**
     * 测试条件处理
     * 测试不同的条件表达式的评估结果
     */
    @Test
    public void testConditionEvaluation() {
        Subject subject = createSubject();
        Resource resource = createResource();
        Env env = createEnv();
        
        // 创建一个带有满足条件的策略
        Statement statement1 = new Statement();
        statement1.addAction("order:read");
        statement1.addResource("*");
        statement1.setEffect(Effect.ALLOW);
        Condition condition1 = new Condition("sub.department == res.department");
        statement1.addCondition(condition1);
        
        // 创建一个带有不满足条件的策略
        Statement statement2 = new Statement();
        statement2.addAction("order:read");
        statement2.addResource("*");
        statement2.setEffect(Effect.ALLOW);
        Condition condition2 = new Condition("sub.role == 'user'");
        statement2.addCondition(condition2);
        
        List<Statement> statements1 = new ArrayList<>();
        statements1.add(statement1);
        
        List<Statement> statements2 = new ArrayList<>();
        statements2.add(statement2);
        
        Enforcer enforcer = new Enforcer();
        enforcer.registerConditionHandler(new AviatorConditionHandler());
        
        // 测试满足条件的情况
        boolean result1 = enforcer.enforce("order:read", subject, resource, env, statements1);
        Assert.assertTrue("条件满足时应该允许访问", result1);
        
        // 测试不满足条件的情况
        boolean result2 = enforcer.enforce("order:read", subject, resource, env, statements2);
        Assert.assertFalse("条件不满足时应该拒绝访问", result2);
    }
    
    /**
     * 测试资源匹配
     * 测试资源标识符的匹配规则
     */
    @Test
    public void testResourceMatching() {
        Subject subject = createSubject();
        Env env = createEnv();
        
        // 创建一个特定资源
        Resource specificResource = new Resource("order_111");
        specificResource.addAttr("department", "1");
        
        // 创建一个不同的资源
        Resource differentResource = new Resource("order_222");
        differentResource.addAttr("department", "1");
        
        // 创建一个只针对特定资源的策略
        Statement statement = new Statement();
        statement.addAction("order:read");
        statement.addResource("order_111");
        statement.setEffect(Effect.ALLOW);
        
        List<Statement> statements = new ArrayList<>();
        statements.add(statement);
        
        Enforcer enforcer = new Enforcer();
        enforcer.registerConditionHandler(new AviatorConditionHandler());
        
        // 测试匹配的资源
        boolean result1 = enforcer.enforce("order:read", subject, specificResource, env, statements);
        Assert.assertTrue("应该允许访问匹配的资源", result1);
        
        // 测试不匹配的资源
        boolean result2 = enforcer.enforce("order:read", subject, differentResource, env, statements);
        Assert.assertFalse("应该拒绝访问不匹配的资源", result2);
    }
    
    /**
     * 测试操作匹配
     * 测试操作标识符的匹配规则，包括通配符
     */
    @Test
    public void testActionMatching() {
        Subject subject = createSubject();
        Resource resource = createResource();
        Env env = createEnv();
        
        // 创建一个允许所有订单操作的策略
        Statement statement = new Statement();
        statement.addAction("order:*");
        statement.addResource("*");
        statement.setEffect(Effect.ALLOW);
        
        List<Statement> statements = new ArrayList<>();
        statements.add(statement);
        
        Enforcer enforcer = new Enforcer();
        enforcer.registerConditionHandler(new AviatorConditionHandler());
        
        // 测试各种操作
        boolean readResult = enforcer.enforce("order:read", subject, resource, env, statements);
        Assert.assertTrue("应该允许读取操作", readResult);
        
        boolean updateResult = enforcer.enforce("order:update", subject, resource, env, statements);
        Assert.assertTrue("应该允许更新操作", updateResult);
        
        boolean otherResult = enforcer.enforce("inventory:read", subject, resource, env, statements);
        Assert.assertFalse("应该拒绝非订单操作", otherResult);
    }

    private Statement createStatement() {
        Statement statement = new Statement();
        statement.addAction("order:*");
        statement.addAction("order:update");
        statement.addResource("*");
        statement.setEffect(Effect.ALLOW);
        Condition condition = new Condition("sub.department == res.department");
        statement.addCondition(condition);
        return statement;
    }

    private Subject createSubject() {
        Subject subject = new Subject("User_111");
        subject.addAttr("department", "1");
        subject.addAttr("role", "admin");
        return subject;
    }

    private Resource createResource() {
        Resource resource = new Resource("order_111");
        resource.addAttr("amount", "100");
        resource.addAttr("status", "paid");
        resource.addAttr("department", "1");
        return resource;
    }

    private Env createEnv() {
        Env env = new Env();
        env.addAttr("time", "2025-01-01");
        return env;
    }
}
