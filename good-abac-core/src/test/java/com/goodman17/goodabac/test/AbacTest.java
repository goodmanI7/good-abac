package com.goodman17.goodabac.test;

import org.junit.Test;

import com.goodman17.goodabac.core.engine.AbacEnforcer;
import com.goodman17.goodabac.core.modle.Condition;
import com.goodman17.goodabac.core.modle.Env;
import com.goodman17.goodabac.core.modle.Resource;
import com.goodman17.goodabac.core.modle.Statement;
import com.goodman17.goodabac.core.modle.Subject;

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
        long start = System.currentTimeMillis();
        AbacEnforcer enforcer = new AbacEnforcer("../examples/abac_rule_model.conf");
        enforcer.addStatement(statement);
        boolean result = enforcer.enforce(subject, resource, "order:update", env);
        System.out.println("result: " + result);
        long end = System.currentTimeMillis();
        System.out.println("time: " + (end - start));

    }

    private Statement createStatement() {
        Statement statement = new Statement();
        statement.addAction("order:create");
        statement.addAction("order:update");
        statement.addResource("order:*");
        statement.setEffect("allow");
        Condition condition = new Condition();
        statement.setCondition(condition);
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
        return resource;
    }

    private Env createEnv() {
        Env env = new Env();
        env.addAttr("time", "2025-01-01");
        return env;
    }
}
