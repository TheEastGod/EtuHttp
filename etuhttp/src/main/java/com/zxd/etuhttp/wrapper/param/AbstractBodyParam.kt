package com.zxd.etuhttp.wrapper.param

import com.zxd.etuhttp.wrapper.callback.ProgressCallback
import com.zxd.etuhttp.wrapper.progess.ProgressRequestBody
import okhttp3.RequestBody
import java.io.IOException

/**
 * author: zxd
 * created on: 2021/2/23 11:12
 * description:
 */
abstract class AbstractBodyParam<P : AbstractParam<P>> constructor(
    url: String,
    method: Method) :
    AbstractParam<P>(url,method) {

    private var uploadMaxLength : Long = Long.MAX_VALUE
    private var mCallback :ProgressCallback? = null

    override fun buildRequestBody(): RequestBody? {
        val requestBody = getRequestBody()

        try {
            val contentLength = requestBody!!.contentLength()
            require(contentLength <= uploadMaxLength) {
                "The contentLength cannot be greater than " + uploadMaxLength + " bytes, " +
                        "the current contentLength is " + contentLength + " bytes"
            }
        } catch (e: IOException) {
            throw IllegalArgumentException(e)
        }

        val callback = mCallback
        if (callback != null){
            return ProgressRequestBody(requestBody,callback)
        }
        return requestBody
    }


    fun setProgressCallback(callback: ProgressCallback): P{
        mCallback = callback
        return this as P
    }

    fun setUploadMaxLength(maxLength: Long): P{
        uploadMaxLength = maxLength
        return this as P
    }

}