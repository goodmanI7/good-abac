package com.goodman17.goodabac.core.function;

import java.util.Map;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;

public class MatchConditionFunction extends AbstractFunction {

    @Override
    public String getName() {
        return "m_cond";
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject policyCondition, AviatorObject requestSubject,
            AviatorObject requestResource, AviatorObject requestEnv) {
        return AviatorBoolean.valueOf(true);
    }
}
