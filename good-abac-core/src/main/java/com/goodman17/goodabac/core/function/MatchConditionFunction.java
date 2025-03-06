package com.goodman17.goodabac.core.function;

import java.util.Map;

import com.goodman17.goodabac.core.modle.Env;
import com.goodman17.goodabac.core.modle.Resource;
import com.goodman17.goodabac.core.modle.Subject;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;

public class MatchConditionFunction extends AbstractFunction {

    @Override
    public String getName() {
        return "m_cond";
    }

    @Override
    public AviatorObject call(Map<String, Object> context, AviatorObject policyCondition, AviatorObject requestSubject,
            AviatorObject requestResource, AviatorObject requestEnv) {
        String conditionStr = policyCondition.getValue(context).toString();
        Subject subject = (Subject) requestSubject.getValue(context);
        Resource resource = (Resource) requestResource.getValue(context);
        Env env = (Env) requestEnv.getValue(context);
        return AviatorBoolean.valueOf(true);
    }
}
