package com.zxd.httpdemo;

import com.zxd.etuhttp.wrapper.annotations.NonNull;
import com.zxd.etuhttp.wrapper.param.NoBodyParam;

import java.util.Map;

/**
 * author: zxd
 * created on: 2021/7/28 13:55
 * description:
 */
public class EtuHttpNoBodyParam extends EtuHttp<NoBodyParam,EtuHttpNoBodyParam> {

    public EtuHttpNoBodyParam(NoBodyParam param) {
        super(param);
    }

    public EtuHttpNoBodyParam add(String key, Object value) {
        return addQuery(key, value);
    }

    public EtuHttpNoBodyParam add(String key, Object value, boolean isAdd) {
        if (isAdd) {
            addQuery(key, value);
        }
        return this;
    }

    public EtuHttpNoBodyParam addAll(Map<String, ?> map) {
        return addAllQuery(map);
    }

    public EtuHttpNoBodyParam addEncoded(String key, Object value) {
        return addEncodedQuery(key, value);
    }

    public EtuHttpNoBodyParam addAllEncoded(@NonNull Map<String, ?> map) {
        return addAllEncodedQuery(map);
    }

    public EtuHttpNoBodyParam set(String key, Object value) {
        return setQuery(key, value);
    }

    public EtuHttpNoBodyParam setEncoded(String key, Object value) {
        return setEncodedQuery(key, value);
    }

}
