package com.zxd.etuhttp.wrapper.entity;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import com.zxd.etuhttp.wrapper.utils.BuildUtil;
import com.zxd.etuhttp.wrapper.utils.UriUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;

/**
 * author: zxd
 * created on: 2021/2/23 16:03
 * description:
 */
public class UriRequestBody extends RequestBody {

    private final Uri uri;
    private final ContentResolver contentResolver;
    private final MediaType contentType;

    public UriRequestBody(Context context, Uri uri) {
        this(context, uri, null);
    }

    public UriRequestBody(Context context, Uri uri, @Nullable MediaType contentType) {
        this.uri = uri;
        this.contentType = contentType;
        contentResolver = context.getContentResolver();
    }

    @Override
    public MediaType contentType() {
        if (contentType != null) return contentType;
        if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
            return BuildUtil.getMediaType((uri.getLastPathSegment()));
        } else {
            String contentType = contentResolver.getType(uri);
            return contentType != null ? MediaType.parse(contentType) : null;
        }
    }

    @Override
    public long contentLength() throws IOException {
        return UriUtil.length(uri, contentResolver);
    }

    @Override
    public void writeTo(@NotNull BufferedSink sink) throws IOException {
        InputStream inputStream = contentResolver.openInputStream(uri);
        sink.writeAll(Okio.source(inputStream));
    }
}
