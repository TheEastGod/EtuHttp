package com.zxd.etuhttp.wrapper.param

import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.Request
import okhttp3.RequestBody

/**
 * author: zxd
 * created on: 2021/1/29 15:46
 * description:
 */
interface IRequest {

    /**
     * @return 查询带参数的url
     */
    fun getUrl(): String

    /**
     * @return 查询不带参数的url
     */
    fun getSimpleUrl(): String

    /**
     * @return HttpUrl
     */
    fun getHttpUrl(): HttpUrl

    /**
     * @return 请求方法，GET、POST等
     */
    fun getMethod(): Method

    fun getRequestBody(): RequestBody?

    fun buildRequestBody(): RequestBody? {
        return getRequestBody()
    }

    /**
     * @return 请求头信息
     */
    fun getHeaders(): Headers?

    /**
     * @return 根据以上定义的方法构建一个请求
     */
    fun buildRequest(): Request
}