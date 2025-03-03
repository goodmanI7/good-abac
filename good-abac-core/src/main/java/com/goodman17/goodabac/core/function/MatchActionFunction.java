package com.goodman17.goodabac.core.function;

import java.util.Map;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;

public class MatchActionFunction extends AbstractFunction {

    @Override
    public String getName() {
        return "m_act";
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject policyAction, AviatorObject requestAction) {
        String policyActionStr = policyAction.getValue(env).toString();
        String requestActionStr = requestAction.getValue(env).toString();
        return AviatorBoolean.valueOf(policyActionStr.equals(requestActionStr));
    }
}
