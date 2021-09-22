package com.zxd.httpdemo;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zxd.etuhttp.wrapper.param.JsonArrayParam;

import java.util.List;
import java.util.Map;

/**
 * author: zxd
 * created on: 2021/7/28 14:15
 * description:
 */
public class EtuHttpJsonArrayParam extends EtuHttpAbstractBodyParam<JsonArrayParam,EtuHttpJsonArrayParam>{

    public EtuHttpJsonArrayParam(JsonArrayParam param) {
        super(param);
    }

    public EtuHttpJsonArrayParam add(String key,Object value){
        param.add(key, value);
        return this;
    }

    public EtuHttpJsonArrayParam add(String key, Object value, boolean isAdd) {
        if(isAdd) {
            param.add(key,value);
        }
        return this;
    }

    public EtuHttpJsonArrayParam addAll(Map<String, ?> map) {
        param.addAll(map);
        return this;
    }

    public EtuHttpJsonArrayParam add(Object object) {
        param.add(object);
        return this;
    }

    public EtuHttpJsonArrayParam addAll(List<?> list) {
        param.addAll(list);
        return this;
    }

    /**
     * 添加多个对象，将字符串转JsonElement对象,并根据不同类型,执行不同操作,可输入任意非空字符串
     */
    public EtuHttpJsonArrayParam addAll(String jsonElement) {
        param.addAll(jsonElement);
        return this;
    }

    public EtuHttpJsonArrayParam addAll(JsonArray jsonArray) {
        param.addAll(jsonArray);
        return this;
    }

    /**
     * 将Json对象里面的key-value逐一取出，添加到Json数组中，成为单独的对象
     */
    public EtuHttpJsonArrayParam addAll(JsonObject jsonObject) {
        param.addAll(jsonObject);
        return this;
    }

    public EtuHttpJsonArrayParam addJsonElement(String jsonElement) {
        param.addJsonElement(jsonElement);
        return this;
    }

    /**
     * 添加一个JsonElement对象(Json对象、json数组等)
     */
    public EtuHttpJsonArrayParam addJsonElement(String key, String jsonElement) {
        param.addJsonElement(key, jsonElement);
        return this;
    }

}
