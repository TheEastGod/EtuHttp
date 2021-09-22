package com.zxd.httpdemo;

import com.google.gson.JsonObject;
import com.zxd.etuhttp.wrapper.param.JsonParam;

import java.util.Map;

/**
 * author: zxd
 * created on: 2021/7/28 14:29
 * description:
 */
public class EtuHttpJsonParam extends EtuHttpAbstractBodyParam<JsonParam,EtuHttpJsonParam>{

    public EtuHttpJsonParam(JsonParam param) {
        super(param);
    }

    public EtuHttpJsonParam add(String key, Object value) {
        param.add(key,value);
        return this;
    }

    public EtuHttpJsonParam add(String key, Object value, boolean isAdd) {
        if(isAdd) {
            param.add(key,value);
        }
        return this;
    }

    public EtuHttpJsonParam addAll(Map<String, ?> map) {
        param.addAll(map);
        return this;
    }

    /**
     * 将Json对象里面的key-value逐一取出，添加到另一个Json对象中，
     * 输入非Json对象将抛出{@link IllegalStateException}异常
     */
    public EtuHttpJsonParam addAll(String jsonObject) {
        param.addAll(jsonObject);
        return this;
    }

    /**
     * 将Json对象里面的key-value逐一取出，添加到另一个Json对象中
     */
    public EtuHttpJsonParam addAll(JsonObject jsonObject) {
        param.addAll(jsonObject);
        return this;
    }

    /**
     * 添加一个JsonElement对象(Json对象、json数组等)
     */
    public EtuHttpJsonParam addJsonElement(String key, String jsonElement) {
        param.addJsonElement(key, jsonElement);
        return this;
    }

}
