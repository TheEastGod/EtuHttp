package com.zxd.etuhttp.wrapper.param

import okhttp3.CacheControl
import okhttp3.Headers
import okhttp3.RequestBody

/**
 * author: zxd
 * created on: 2021/2/8 15:16
 * description:
 */
class NoBodyParam(url: String, method: Method) : AbstractParam<NoBodyParam>(url, method) {


    override fun add(key: String, value: Any?): NoBodyParam {
        var mValue = value
        if (mValue == null) mValue = ""
       return addQuery(key, mValue)
    }

    fun addEncoded(key: String,value: Any): NoBodyParam{
        return addEncodedQuery(key, value)
    }

    fun addALLEncoded(map: Map<String,*>): NoBodyParam{
        return addAllEncodedQuery(map)
    }

    fun set(key: String,value: Any): NoBodyParam{
        return setQuery(key, value)
    }

    fun setEncoded(key: String,value: Any): NoBodyParam{
        return setEncodedQuery(key, value)
    }

    override fun getRequestBody(): RequestBody? {
       return  null
    }

    override fun toString(): String {
        return getUrl()
    }

}