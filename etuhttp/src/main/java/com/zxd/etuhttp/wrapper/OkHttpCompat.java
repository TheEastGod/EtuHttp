package com.zxd.etuhttp.wrapper;

import com.zxd.etuhttp.wrapper.param.Param;
import com.zxd.etuhttp.wrapper.parse.IConverter;

import java.io.Closeable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.internal.Util;

/**
 * author: zxd
 * created on: 2021/2/8 11:10
 * description:
 */
public class OkHttpCompat {

    private static String OKHTTP_USER_AGENT;

    public static IConverter getConverter(Response response) {
        return response.request().tag(IConverter.class);
    }

    public static boolean needDecodeResult(Response response) {
        return !"false".equals(response.request().header(Param.DATA_DECRYPT));
    }

    //获取OkHttp版本号
    public static String getOkHttpUserAgent() {
        if (OKHTTP_USER_AGENT != null) return OKHTTP_USER_AGENT;
        try {
            //4.7.x及以上版本获取userAgent方式
            Class<?> clazz = Class.forName("okhttp3.internal.Util");
            return OKHTTP_USER_AGENT = (String) clazz.getDeclaredField("userAgent").get(null);
        } catch (Throwable ignore) {
        }
        try {
            Class<?> clazz = Class.forName("okhttp3.internal.Version");
            try {
                //4.x.x及以上版本获取userAgent方式
                Field userAgent = clazz.getDeclaredField("userAgent");
                return OKHTTP_USER_AGENT = (String) userAgent.get(null);
            } catch (Exception ignore) {
            }
            //4.x.x以下版本获取userAgent方式
            Method userAgent = clazz.getDeclaredMethod("userAgent");
            return OKHTTP_USER_AGENT = (String) userAgent.invoke(null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return OKHTTP_USER_AGENT = "okhttp/4.2.0";
    }

    public static Headers headers(Response response) {
        return response.headers();
    }


    public static void closeQuietly(Response response) {
        if (response == null) return;
        closeQuietly(response.body());
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable == null) return;
        Util.closeQuietly(closeable);
    }

}
