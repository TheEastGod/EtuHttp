package com.zxd.etuhttp.wrapper.exception;

import com.zxd.etuhttp.wrapper.OkHttpCompat;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author: zxd
 * created on: 2021/2/8 11:09
 * description: Http 状态码 小于200或者大于等于300时,或者ResponseBody等于null，抛出此异常
 */
public final class HttpStatusCodeException extends IOException {

    private final Protocol protocol; //http协议
    private final String statusCode; //Http响应状态吗
    private final String result;    //返回结果
    private final String requestMethod; //请求方法，Get/Post等
    private final HttpUrl httpUrl; //请求Url及查询参数
    private final Headers responseHeaders; //响应头

    public HttpStatusCodeException(Response response) {
        this(response, null);
    }

    public HttpStatusCodeException(Response response, String result) {
        super(response.message());
        protocol = response.protocol();
        statusCode = String.valueOf(response.code());
        Request request = response.request();
        requestMethod = request.method();
        httpUrl = request.url();
        responseHeaders = response.headers();
        this.result = result;
    }

    @Override
    public String getLocalizedMessage() {
        return statusCode;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestUrl() {
        return httpUrl.toString();
    }

    public HttpUrl getHttpUrl() {
        return httpUrl;
    }

    public Headers getResponseHeaders() {
        return responseHeaders;
    }

    public String getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "<------ " + OkHttpCompat.getOkHttpUserAgent() +
                " request end ------>" +
                "\n" + getClass().getName() + ":" +
                "\n" + requestMethod + " " + httpUrl +
                "\n\n" + protocol + " " + statusCode + " " + getMessage() +
                "\n" + responseHeaders +
                "\n" + result;
    }
}
