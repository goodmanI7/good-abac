package com.goodman17.goodabac.core.function;

import java.util.Map;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;

public class MatchResourceFunction extends AbstractFunction {

    @Override
    public String getName() {
        return "m_res";
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject policyResource, AviatorObject requestResource) {
        return AviatorBoolean.valueOf(true);
    }
}
