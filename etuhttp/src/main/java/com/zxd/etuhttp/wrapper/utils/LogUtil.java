package com.zxd.etuhttp.wrapper.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.zxd.etuhttp.wrapper.EtuHttpPlugins;
import com.zxd.etuhttp.wrapper.OkHttpCompat;
import com.zxd.etuhttp.wrapper.Platform;
import com.zxd.etuhttp.wrapper.annotations.NonNull;
import com.zxd.etuhttp.wrapper.exception.HttpStatusCodeException;
import com.zxd.etuhttp.wrapper.exception.ParseException;
import com.zxd.etuhttp.wrapper.progess.ProgressRequestBody;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import kotlin.text.Charsets;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * author: zxd
 * created on: 2021/11/3 11:41
 * description:
 */
public class LogUtil {

    private static final String TAG = "RxHttp";
    private static final String TAG_RXJAVA = "RxJava";

    private static boolean isDebug = false;
    //日志长度超出logcat单条日志打印长度时，是否分段打印，默认false
    private static boolean isSegmentPrint = false;

    public static void setDebug(boolean debug) {
        setDebug(debug, false);
    }

    public static void setDebug(boolean debug, boolean segmentPrint) {
        isDebug = debug;
        isSegmentPrint = segmentPrint;
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public static boolean isSegmentPrint() {
        return isSegmentPrint;
    }

    //打印Http请求连接失败异常日志
    public static void log(Throwable throwable) {
        if (!isDebug) return;
        Platform.get().loge(TAG_RXJAVA, throwable.toString());
    }

    //打印Http请求连接失败异常日志
    public static void log(String url, Throwable throwable) {
        if (!isDebug) return;
        try {
            throwable.printStackTrace();
            StringBuilder builder = new StringBuilder(throwable.toString());
            if (!(throwable instanceof ParseException) && !(throwable instanceof HttpStatusCodeException)) {
                builder.append("\n\n").append(url);
            }
            Platform.get().loge(TAG, builder.toString());
        } catch (Throwable e) {
            Platform.get().logd(TAG, "Request error Log printing failed", e);
        }
    }

    //请求前，打印日志
    public static void log(@NonNull Request userRequest, CookieJar cookieJar) {
        if (!isDebug) return;
        try {
            Request.Builder requestBuilder = userRequest.newBuilder();
            StringBuilder builder = new StringBuilder("<------ ")
//                    .append(RxHttpVersion.userAgent).append(" ")
                    .append(OkHttpCompat.getOkHttpUserAgent())
                    .append(" request start ------>\n")
                    .append(userRequest.method())
                    .append(" ").append(userRequest.url());
            RequestBody body = userRequest.body();
            if (body != null) {
                MediaType contentType = body.contentType();
                if (contentType != null) {
                    requestBuilder.header("Content-Type", contentType.toString());
                }
                long contentLength = body.contentLength();
                if (contentLength != -1L) {
                    requestBuilder.header("Content-Length", String.valueOf(contentLength));
                    requestBuilder.removeHeader("Transfer-Encoding");
                } else {
                    requestBuilder.header("Transfer-Encoding", "chunked");
                    requestBuilder.removeHeader("Content-Length");
                }
            }

            if (userRequest.header("Host") == null) {
                requestBuilder.header("Host", hostHeader(userRequest.url()));
            }

            if (userRequest.header("Connection") == null) {
                requestBuilder.header("Connection", "Keep-Alive");
            }

            // If we add an "Accept-Encoding: gzip" header field we're responsible for also decompressing
            // the transfer stream.
            if (userRequest.header("Accept-Encoding") == null
                    && userRequest.header("Range") == null) {
                requestBuilder.header("Accept-Encoding", "gzip");
            }
            List<Cookie> cookies = cookieJar.loadForRequest(userRequest.url());
            if (!cookies.isEmpty()) {
                requestBuilder.header("Cookie", cookieHeader(cookies));
            }
            if (userRequest.header("User-Agent") == null) {
                requestBuilder.header("User-Agent", OkHttpCompat.getOkHttpUserAgent());
            }
            builder.append("\n").append(requestBuilder.build().headers());
            if (body != null) builder.append("\n").append(requestBody2Str(body));

            Platform.get().logd(TAG, builder.toString());
        } catch (Throwable e) {
            Platform.get().logd(TAG, "Request start log printing failed", e);
        }
    }

    //打印Http返回的正常结果
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void log(@NonNull Response response, String body) {
        if (!isDebug) return;
        try {
            Request request = response.request();
            LogTime logTime = request.tag(LogTime.class);
            long tookMs = logTime != null ? logTime.tookMs() : 0;
            String result = body != null ? body :
                    getResult(OkHttpCompat.requireBody(response), OkHttpCompat.needDecodeResult(response));
            StringBuilder builder = new StringBuilder("<------ ")
//                    .append(RxHttpVersion.userAgent).append(" ")
                    .append(OkHttpCompat.getOkHttpUserAgent())
                    .append(" request end ------>\n")
                    .append(request.method()).append(" ").append(request.url())
                    .append("\n\n").append(response.protocol()).append(" ")
                    .append(response.code()).append(" ").append(response.message())
                    .append(tookMs > 0 ? " " + tookMs + "ms" : "")
                    .append("\n").append(response.headers())
                    .append("\n").append(result);
            Platform.get().logi(TAG, builder.toString());
        } catch (Throwable e) {
            Platform.get().logd(TAG, "Request end Log printing failed", e);
        }
    }

    private static String requestBody2Str(@NonNull RequestBody body) throws IOException {
        if (body instanceof ProgressRequestBody) {
            body = ((ProgressRequestBody) body).getRequestBody();
        }
        if (body instanceof MultipartBody) {
            return multipartBody2Str((MultipartBody) body);
        }
        Buffer buffer = new Buffer();
        body.writeTo(buffer);
        if (!isProbablyUtf8(buffer)) {
            return "(binary " + body.contentLength() + "-byte body omitted)";
        } else {
            return buffer.readString(getCharset(body));
        }
    }

    private static String multipartBody2Str(MultipartBody multipartBody) {
        final byte[] colonSpace = {':', ' '};
        final byte[] CRLF = {'\r', '\n'};
        final byte[] dashDash = {'-', '-'};
        Buffer sink = new Buffer();
        for (MultipartBody.Part part : multipartBody.parts()) {
            Headers headers = part.headers();
            RequestBody body = part.body();
            sink.write(dashDash)
                    .writeUtf8(multipartBody.boundary())
                    .write(CRLF);
            if (headers != null) {
                for (int i = 0, size = headers.size(); i < size; i++) {
                    sink.writeUtf8(headers.name(i))
                            .write(colonSpace)
                            .writeUtf8(headers.value(i))
                            .write(CRLF);
                }
            }
            MediaType contentType = body.contentType();
            if (contentType != null) {
                sink.writeUtf8("Content-Type: ")
                        .writeUtf8(contentType.toString())
                        .write(CRLF);
            }
            long contentLength = -1;
            try {
                contentLength = body.contentLength();
            } catch (IOException e) {
                e.printStackTrace();
            }
            sink.writeUtf8("Content-Length: ")
                    .writeDecimalLong(contentLength)
                    .write(CRLF);

            if (contentLength > 1024) {
                sink.writeUtf8("(binary " + contentLength + "-byte body omitted)");
            } else {
                if (body instanceof MultipartBody) {
                    sink.write(CRLF)
                            .writeUtf8(multipartBody2Str((MultipartBody) body));
                } else {
                    try {
                        body.writeTo(sink);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (contentLength > 0) sink.write(CRLF);
            sink.write(CRLF);
        }
        sink.write(dashDash)
                .writeUtf8(multipartBody.boundary())
                .write(dashDash);
        return sink.readString(getCharset(multipartBody));
    }

    @SuppressWarnings("deprecation")
    private static String getResult(ResponseBody body, boolean onResultDecoder) throws IOException {
        BufferedSource source = body.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        String result;
        if (isProbablyUtf8(buffer)) {
            result = buffer.clone().readString(getCharset(body));
            if (onResultDecoder) {
                result = EtuHttpPlugins.onResultDecoder(result);
            }
        } else {
            result = "(binary " + buffer.size() + "-byte body omitted)";
        }
        return result;
    }

    private static boolean isProbablyUtf8(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private static Charset getCharset(RequestBody requestBody) {
        MediaType mediaType = requestBody.contentType();
        return mediaType != null ? mediaType.charset(Charsets.UTF_8) : Charsets.UTF_8;
    }

    private static Charset getCharset(ResponseBody responseBody) {
        MediaType mediaType = responseBody.contentType();
        return mediaType != null ? mediaType.charset(Charsets.UTF_8) : Charsets.UTF_8;
    }


    private static String hostHeader(HttpUrl url) {
        String host = url.host().contains(":")
                ? "[" + url.host() + "]"
                : url.host();
        return host + ":" + url.port();
    }

    /**
     * Returns a 'Cookie' HTTP request header with all cookies, like {@code a=b; c=d}.
     */
    private static String cookieHeader(List<Cookie> cookies) {
        StringBuilder cookieHeader = new StringBuilder();
        for (int i = 0, size = cookies.size(); i < size; i++) {
            if (i > 0) {
                cookieHeader.append("; ");
            }
            Cookie cookie = cookies.get(i);
            cookieHeader.append(cookie.name()).append('=').append(cookie.value());
        }
        return cookieHeader.toString();
    }

}
