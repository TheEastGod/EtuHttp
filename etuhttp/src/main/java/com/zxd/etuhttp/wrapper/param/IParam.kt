package com.zxd.etuhttp.wrapper.param

import okhttp3.CacheControl

/**
 * author: zxd
 * created on: 2021/1/29 15:45
 * description:
 */
interface IParam<P : Param<P>>{

    fun setUrl(url :String) : P

    fun add(key :String,value :Any?) : P

    fun addAll(map: Map<String, *>): P {
        for ((key, value) in map) {
            add(key, value!!)
        }
        return this as P
    }

    fun removeAllQuery() : P

    fun removeAllQuery(key :String) : P

    fun addQuery(key: String,value: Any) : P

    fun addEncodedQuery(key: String,value: Any) : P

    fun setQuery(key: String,value: Any) : P{
        removeAllQuery(key)
        return addQuery(key, value)
    }

    fun setEncodedQuery(key: String,value: Any): P {
        removeAllQuery(key)
        return addEncodedQuery(key, value)
    }

    fun addAllQuery(map: Map<String, *>): P {
        for ((key, value) in map) {
            addQuery(key, value!!)
        }
        return this as P
    }

    fun setAllQuery(map: Map<String, *>): P {
        for ((key, value) in map) {
            setQuery(key, value!!)
        }
        return this as P
    }

    fun addAllEncodedQuery(map :Map<String,*>) :P{
        for ((key, value) in map) {
            addEncodedQuery(key, value!!)
        }
        return this as P
    }

    fun setAllEncodedQuery(map: Map<String, *>): P {
        for ((key, value) in map) {
            setEncodedQuery(key, value!!)
        }
        return this as P
    }

    /**
     * @return 判断是否对参数添加装饰，即是否添加公共参数
     */
    fun getAssemblyEnabled() : Boolean

    /**
     * 设置是否对参数添加装饰，即是否添加公共参数
     *
     * @param enabled true 是
     * @return Param
     */
    fun setAssemblyEnabled(enabled : Boolean) : P


    fun <T> tag(type: Class<in T>?, tag: T): P

    fun cacheControl(cacheControl: CacheControl): P
}