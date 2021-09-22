package com.zxd.etuhttp.wrapper.param

import android.content.Context
import android.net.Uri
import com.zxd.etuhttp.wrapper.asRequestBody
import com.zxd.etuhttp.wrapper.utils.BuildUtil
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.ByteString
import java.io.File

/**
 * author: zxd
 * created on: 2021/2/23 11:11
 * description:
 */
class BodyParam (url: String, method: Method) : AbstractBodyParam<BodyParam>(url, method) {


    private var requestBody: RequestBody? = null

    fun setBody(requestBody: RequestBody): BodyParam {
        this.requestBody = requestBody
        return this
    }

    @JvmOverloads
    fun setBody(content: String, mediaType: MediaType? = null): BodyParam {
        requestBody = RequestBody.create(mediaType, content)
        return this
    }

    @JvmOverloads
    fun setBody(content: ByteString, mediaType: MediaType? = null): BodyParam {
        requestBody = RequestBody.create(mediaType, content)
        return this
    }

    @JvmOverloads
    fun setBody(
        content: ByteArray,
        mediaType: MediaType? = null,
        offset: Int = 0,
        byteCount: Int = content.size
    ): BodyParam {
        requestBody = RequestBody.create(mediaType, content, offset, byteCount)
        return this
    }

    @JvmOverloads
    fun setBody(
        file: File,
        mediaType: MediaType? = BuildUtil.getMediaType(file.name)
    ): BodyParam {
        requestBody = RequestBody.create(mediaType, file)
        return this
    }

    @JvmOverloads
    fun setBody(
        uri: Uri,
        context: Context,
        contentType: MediaType? = null
    ): BodyParam {
        requestBody = uri.asRequestBody(context, contentType)
        return this
    }

    fun <T> setJsonBody(any: T): BodyParam {
        requestBody = convert(any!!)
        return this
    }

    override fun getRequestBody(): RequestBody {
        return requestBody
            ?: throw NullPointerException("requestBody cannot be null, please call the setBody series methods")
    }

    override fun add(key: String, value: Any?): BodyParam {
       return this
    }
}