package com.goodman17.goodabac.test;

import org.casbin.jcasbin.main.Enforcer;
import org.junit.Test;

/**
 * @author lirenhao
 * date: 2025/2/27 19:17
 */
public class AbacTest {

    @Test
    public void testEval() {
        Enforcer e = new Enforcer("examples/abac_rule_model.conf");
        System.out.println("111");
        e.addPolicy()
    }
}
