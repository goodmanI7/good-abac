package com.goodman17.goodabac.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.goodman17.goodabac.core.engine.Enforcer;
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
        for (int i = 0; i < 1000; i++) {
            boolean result = enforcer.enforce("order:update", subject, resource, env, statements);
            System.out.println(i + " result: " + result);
        }
        long end = System.currentTimeMillis();
        System.out.println("time: " + (end - start));
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
