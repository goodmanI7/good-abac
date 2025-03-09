package com.goodman17.goodabac.core.handler;

import com.goodman17.goodabac.core.modle.Condition;
import com.goodman17.goodabac.core.modle.EnforceContext;

public class AviatorConditionHandler implements ConditionHandler {

    @Override
    public boolean handle(EnforceContext context, Condition condition) {
        return true;
    }

    @Override
    public String type() {
        return "aviator";
    }
}
