package com.zxd.httpdemo;

import android.content.Context;
import android.net.Uri;

import com.zxd.etuhttp.wrapper.annotations.Nullable;
import com.zxd.etuhttp.wrapper.param.BodyParam;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.ByteString;

/**
 * author: zxd
 * created on: 2021/7/26 15:59
 * description:
 */
public class EtuHttpBodyParam extends EtuHttpAbstractBodyParam<BodyParam,EtuHttpBodyParam>{

    public EtuHttpBodyParam(BodyParam param) {
        super(param);
    }

    public EtuHttpBodyParam setBody(RequestBody requestBody){
        param.setBody(requestBody);
        return this;
    }

    public EtuHttpBodyParam setBody(String content, @Nullable MediaType mediaType){
        param.setBody(content, mediaType);
        return this;
    }

    public EtuHttpBodyParam setBody(ByteString content,@Nullable MediaType mediaType){
        param.setBody(content, mediaType);
        return this;
    }

    public EtuHttpBodyParam setBody(byte[] content, @Nullable MediaType mediaType){
        param.setBody(content, mediaType);
        return this;
    }

    public EtuHttpBodyParam setBody(byte[] content, @Nullable MediaType mediaType, int offset, int byteCount){
        param.setBody(content, mediaType, offset, byteCount);
        return this;
    }

    public EtuHttpBodyParam setBody(File file){
        param.setBody(file);
        return this;
    }

    public EtuHttpBodyParam setBody(File file, @Nullable MediaType mediaType){
        param.setBody(file, mediaType);
        return this;
    }

    public EtuHttpBodyParam setBody(Uri uri, Context context){
        param.setBody(uri, context);
        return this;
    }

    public EtuHttpBodyParam setBody(Uri uri, Context context, @Nullable MediaType contentType){
        param.setBody(uri, context, contentType);
        return this;
    }

    public <T> EtuHttpBodyParam setBody(T object){
        param.setJsonBody(object);
        return this;
    }

}
