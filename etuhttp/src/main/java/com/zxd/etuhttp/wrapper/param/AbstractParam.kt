package com.zxd.etuhttp.wrapper.param

import com.zxd.etuhttp.wrapper.EtuHttpPlugins
import com.zxd.etuhttp.wrapper.entity.KeyValuePair
import com.zxd.etuhttp.wrapper.parse.IConverter
import com.zxd.etuhttp.wrapper.utils.BuildUtil
import okhttp3.*
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

/**
 * author: zxd
 * created on: 2021/1/29 15:47
 * description:
 */
abstract class AbstractParam <P :Param<P>> constructor(url: String,method: Method) :Param<P>{

    private var mUrl  = url  //链接地址
    private var mMethod = method  //请求方法
    private var mBuilder: Headers.Builder? = null //请求头构造器
    private var queryParam: ArrayList<KeyValuePair> = ArrayList() //查询参数 拼接在url后
    private val requestBuilder = Request.Builder() //请求构造器
    private var assemblyEnabled = true //是否添加公共参数


    override fun setUrl(url: String): P {
        mUrl = url
        return this as P
    }

    override fun addQuery(key: String, value: Any?): P {
        var mValue = value
        if (mValue == null) mValue = ""
        return addQuery(KeyValuePair(key,mValue))
    }

    override fun addEncodedQuery(key: String, value: Any): P {
        return addQuery(KeyValuePair(key,value,true))
    }

    override fun removeAllQuery(): P {
        val pairs = queryParam
        if (!pairs.isNullOrEmpty()) pairs.clear()
        return this as P
    }

    override fun removeAllQuery(key: String): P {
        val pairs = queryParam
        if(!pairs.isNullOrEmpty()){
            val iterator = queryParam.iterator()
            while (iterator.hasNext()){
                val next = iterator.next()
                if (next.equals(key)){
                    iterator.remove()
                }
            }
        }
        return this as P
    }


    override fun cacheControl(cacheControl: CacheControl): P {
        requestBuilder.cacheControl(cacheControl)
        return this as P
    }

    private fun addQuery(keyValuePair: KeyValuePair): P{
        queryParam.add(keyValuePair)
        return this as P
    }

    fun getQueryParam(): List<KeyValuePair>{
        return queryParam
    }

    override fun getUrl(): String {
        return getHttpUrl().toString()
    }

    override fun getSimpleUrl(): String {
        return mUrl
    }

    override fun getHttpUrl(): HttpUrl {
        return BuildUtil.getHttpUrl(mUrl,queryParam)
    }

    override fun getMethod(): Method {
        return mMethod
    }

    override fun getHeaders(): Headers? {
        return if (mBuilder == null) null else mBuilder!!.build()
    }

    override fun getHeadersBuilder(): Headers.Builder {
        if (mBuilder == null)  mBuilder = Headers.Builder()
        return mBuilder as Headers.Builder
    }

    override fun setHeadersBuilder(builder: Headers.Builder): P {
        mBuilder  = builder
        return this as P
    }

    override fun <T> tag(type: Class<in T>?, tag: T): P {
        if (type != null) {
            requestBuilder.tag(type,tag)
        }
        return this as P
    }

    override fun getAssemblyEnabled(): Boolean {
        return assemblyEnabled
    }

    override fun setAssemblyEnabled(enabled: Boolean): P {
        assemblyEnabled = enabled
        return this as P
    }


   fun getRequestBuilder():Request.Builder{
       return requestBuilder
   }

    override fun buildRequest(): Request {
        val p = EtuHttpPlugins.onParamAssembly(this)

        return BuildUtil.buildRequest(p,requestBuilder)
    }

    protected open fun getConverter(): IConverter? {
        val request = getRequestBuilder().build()
        return request.tag(IConverter::class.java)
    }

    protected fun convert(any: Any): RequestBody {
        val converter: IConverter =  if (getConverter() != null) getConverter()!! else throw IllegalArgumentException("converter can not be null")
        return try {
            converter.convert(any)
        } catch (e: IOException) {
            throw IllegalArgumentException(
                "Unable to convert $any to RequestBody",
                e
            )
        }
    }


}