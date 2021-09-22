package com.zxd.httpdemo;

import com.zxd.etuhttp.wrapper.param.Param;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * author: zxd
 * created on: 2021/7/26 15:31
 * description:
 */
public class EtuHttp<P extends Param,R extends EtuHttp> extends BaseEtuHttp {

    protected P param;

    protected EtuHttp(P param) {
        this.param = param;
    }

    @NotNull
    @Override
    public okhttp3.Call newCall() {
        return null;
    }

    public R removeAllQuery() {
        param.removeAllQuery();
        return (R)this;
    }

    public R removeAllQuery(String key) {
        param.removeAllQuery(key);
        return (R)this;
    }

    public R addQuery(String key, Object value) {
        param.addQuery(key,value);
        return (R)this;
    }

    public R setQuery(String key, Object value) {
        param.setQuery(key,value);
        return (R)this;
    }

    public R addEncodedQuery(String key, Object value) {
        param.addEncodedQuery(key,value);
        return (R)this;
    }

    public R setEncodedQuery(String key, Object value) {
        param.setEncodedQuery(key,value);
        return (R)this;
    }

    public R addAllQuery(Map<String, ?> map) {
        param.addAllQuery(map);
        return (R)this;
    }

    public R setAllQuery(Map<String, ?> map) {
        param.setAllQuery(map);
        return (R)this;
    }

    public R addAllEncodedQuery(Map<String, ?> map) {
        param.addAllEncodedQuery(map);
        return (R)this;
    }

    public R setAllEncodedQuery(Map<String, ?> map) {
        param.setAllEncodedQuery(map);
        return (R)this;
    }
}
