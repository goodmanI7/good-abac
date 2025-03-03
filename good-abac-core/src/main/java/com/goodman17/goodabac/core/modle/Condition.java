package com.goodman17.goodabac.core.modle;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class Condition extends HashMap<String, Map<String, String>> {

    public String toJson() {
        return JSON.toJSONString(this);
    }
}
