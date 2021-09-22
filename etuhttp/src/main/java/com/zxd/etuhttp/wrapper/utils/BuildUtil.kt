package com.zxd.etuhttp.wrapper.utils

import com.zxd.etuhttp.wrapper.entity.KeyValuePair
import com.zxd.etuhttp.wrapper.param.IRequest
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import java.net.URLConnection

/**
 * author: zxd
 * created on: 2021/2/2 11:12
 * description:
 */
class BuildUtil {

    companion object{

        fun getHttpUrl(url: String,list: List<KeyValuePair>): HttpUrl{
            val httpUrl = url.toHttpUrl()
            if (list.isNullOrEmpty()) return httpUrl
            val builder = httpUrl.newBuilder()
            for (pair in list){
                if (pair.isEncoded){
                    builder.addEncodedQueryParameter(pair.key,pair.value.toString())
                }else{
                    builder.addQueryParameter(pair.key,pair.value.toString())
                }
            }
            return builder.build()
        }

        fun buildRequest(request: IRequest, builder: Request.Builder):Request{
            builder.url(request.getHttpUrl())
                    .method(request.getMethod().name,request.buildRequestBody());
            val headers = request.getHeaders()
            if (headers != null){
                builder.headers(headers)
            }
            return builder.build()
        }


        @JvmStatic
        fun getMediaType(filename: String): MediaType? {
            val index = filename.lastIndexOf(".") + 1
            val fileSuffix = filename.substring(index)
            val contentType =
                URLConnection.guessContentTypeFromName(fileSuffix)
            return contentType?.toMediaTypeOrNull()
        }
    }
}