package com.zxd.httpdemo;

import android.content.Context;
import android.net.Uri;

import com.zxd.etuhttp.wrapper.annotations.NonNull;
import com.zxd.etuhttp.wrapper.annotations.Nullable;
import com.zxd.etuhttp.wrapper.entity.UpFile;
import com.zxd.etuhttp.wrapper.param.FormParam;
import com.zxd.etuhttp.wrapper.utils.UriUtil;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * author: zxd
 * created on: 2021/7/27 15:18
 * description:
 */
public class EtuHttpFormParam extends EtuHttpAbstractBodyParam<FormParam, EtuHttpFormParam> {

    public EtuHttpFormParam(FormParam param) {
        super(param);
    }

    public EtuHttpFormParam add(String key, Object value) {
        param.add(key, value);
        return this;
    }

    public EtuHttpFormParam add(String key, Object value, Boolean isAdd) {
        if (isAdd) {
            param.add(key, value);
        }
        return this;
    }

    public EtuHttpFormParam addAll(Map<String, ?> map) {
        param.addAll(map);
        return this;
    }

    public EtuHttpFormParam addEncoded(String key, Object value) {
        param.addEncoded(key, value);
        return this;
    }

    public EtuHttpFormParam addAllEncoded(@NonNull Map<String, ?> map) {
        param.addAllEncoded(map);
        return this;
    }

    public EtuHttpFormParam removeAllBody() {
        param.removeAllBody();
        return this;
    }

    public EtuHttpFormParam removeAllBody(String key) {
        param.removeAllBody(key);
        return this;
    }

    public EtuHttpFormParam set(String key, Object value) {
        param.set(key, value);
        return this;
    }

    public EtuHttpFormParam setEncoded(String key, Object value) {
        param.setEncoded(key, value);
        return this;
    }

    public EtuHttpFormParam addFile(String key, File file) {
        param.addFile(key, file);
        return this;
    }

    public EtuHttpFormParam addFile(String key, String filePath) {
        param.addFile(key, filePath);
        return this;
    }

    public EtuHttpFormParam addFile(String key, String fileName, String filePath) {
        param.addFile(key, fileName, filePath);
        return this;
    }

    public EtuHttpFormParam addFile(String key, String fileName, File file) {
        param.addFile(key, fileName, file);
        return this;
    }

    public EtuHttpFormParam addFile(UpFile file){
        param.addFile(file);
        return this;
    }

    public EtuHttpFormParam addFiles(List<? extends UpFile> fileList){
        param.addFiles(fileList);
        return this;
    }

    public <T> EtuHttpFormParam addFiles(Map<String,T> fileMap){
        param.addFiles(fileMap);
        return this;
    }

    public <T> EtuHttpFormParam addFiles(String key,List<T> fileList){
        param.addFiles(key, fileList);
        return this;
    }

    public EtuHttpFormParam addPart(MultipartBody.Part part){
        param.addPart(part);
        return this;
    }

    public EtuHttpFormParam addPart(RequestBody requestBody){
        param.addPart(requestBody);
        return this;
    }

    public EtuHttpFormParam addPart(Headers headers, RequestBody requestBody) {
        param.addPart(headers, requestBody);
        return this;
    }

    public EtuHttpFormParam addFormDataPart(String key, String fileName, RequestBody requestBody) {
        param.addFormDataPart(key, fileName, requestBody);
        return this;
    }

    public EtuHttpFormParam addPart(@Nullable MediaType contentType, byte[] content) {
        param.addPart(contentType, content);
        return this;
    }

    public EtuHttpFormParam addPart(@Nullable MediaType contentType, byte[] content, int offset,
                                   int byteCount) {
        param.addPart(contentType, content, offset, byteCount);
        return this;
    }

    public EtuHttpFormParam addPart(Context context, Uri uri) {
        param.addPart(UriUtil.asRequestBody(uri, context));
        return this;
    }

    public EtuHttpFormParam addPart(Context context, String key, Uri uri) {
        param.addPart(UriUtil.asPart(uri, context, key));
        return this;
    }

    public EtuHttpFormParam addPart(Context context, String key, String fileName, Uri uri) {
        param.addPart(UriUtil.asPart(uri, context, key, fileName));
        return this;
    }

    public EtuHttpFormParam addPart(Context context, Uri uri, @Nullable MediaType contentType) {
        param.addPart(UriUtil.asRequestBody(uri, context, contentType));
        return this;
    }

    public EtuHttpFormParam addPart(Context context, String key, Uri uri,
                                   @Nullable MediaType contentType) {
        param.addPart(UriUtil.asPart(uri, context, key, null, contentType));
        return this;
    }

    public EtuHttpFormParam addPart(Context context, String key, String filename, Uri uri,
                                   @Nullable MediaType contentType) {
        param.addPart(UriUtil.asPart(uri, context, key, filename, contentType));
        return this;
    }


    public EtuHttpFormParam addParts(Context context, Map<String, Uri> uriMap) {
        for (Map.Entry<String, Uri> entry : uriMap.entrySet()) {
            addPart(context, entry.getKey(), entry.getValue());
        }
        return this;
    }

    public EtuHttpFormParam addParts(Context context, List<Uri> uris) {
        for (Uri uri : uris) {
            addPart(context, uri);
        }
        return this;
    }

    public EtuHttpFormParam addParts(Context context, String key, List<Uri> uris) {
        for (Uri uri : uris) {
            addPart(context, key, uri);
        }
        return this;
    }

    public EtuHttpFormParam addParts(Context context, List<Uri> uris,
                                    @Nullable MediaType contentType) {
        for (Uri uri : uris) {
            addPart(context, uri, contentType);
        }
        return this;
    }

    public EtuHttpFormParam addParts(Context context, String key, List<Uri> uris,
                                    @Nullable MediaType contentType) {
        for (Uri uri : uris) {
            addPart(context, key, uri, contentType);
        }
        return this;
    }

    //Set content-type to multipart/mixed
    public EtuHttpFormParam setMultiMixed() {
        param.setMultiMixed();
        return this;
    }

    //Set content-type to multipart/alternative
    public EtuHttpFormParam setMultiAlternative() {
        param.setMultiAlternative();
        return this;
    }

    //Set content-type to multipart/digest
    public EtuHttpFormParam setMultiDigest() {
        param.setMultiDigest();
        return this;
    }

    //Set content-type to multipart/parallel
    public EtuHttpFormParam setMultiParallel() {
        param.setMultiParallel();
        return this;
    }

    //Set the MIME type
    public EtuHttpFormParam setMultiType(MediaType multiType) {
        param.setMultiType(multiType);
        return this;
    }

}
