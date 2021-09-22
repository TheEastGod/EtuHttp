package com.zxd.etuhttp.wrapper.param

import okhttp3.Headers

/**
 * author: zxd
 * created on: 2021/1/29 15:46
 * description:
 */
interface IHeaders<P :Param<P>>{

    fun getHeaders() : Headers?

    fun getHeadersBuilder() : Headers.Builder

    fun setHeadersBuilder(builder: Headers.Builder) : P

    fun getHeader(key: String) : String?{
        return getHeadersBuilder()[key]
    }

    fun addHeader(key: String,value: String): P {
        getHeadersBuilder().add(key,value)
        return this as P
    }

    fun addNonAsciiHeader(key: String,value: String): P{
        getHeadersBuilder().addUnsafeNonAscii(key,value)
        return this as P
    }

    fun setNonAsciiHeader(key: String,value: String): P{
        val builder = getHeadersBuilder()
        builder.removeAll(key)
        builder.addUnsafeNonAscii(key,value)
        return this as P
    }

    fun addHeader(line: String) : P{
        getHeadersBuilder().add(line)
        return this as P
    }

    fun addAllHeader(headers: Map<String,String>): P{
        for (entry : Map.Entry<String,String> in headers){
            addHeader(entry.key,entry.value)
        }
        return this as P
    }

    fun addHeader(headers: Headers): P{
        getHeadersBuilder().addAll(headers)
        return this as P
    }

    fun setHeader(key: String,value: String): P{
        getHeadersBuilder()[key] = value
        return this as P
    }

    fun setAllHeader(headers: Map<String,String>): P{
        for (entry : Map.Entry<String,String> in headers){
            setHeader(entry.key,entry.value)
        }
        return this as P
    }


    fun removeAllHeader(key: String): P{
        getHeadersBuilder().removeAll(key)
        return this as P
    }

    /**
     * 设置断点下载的开始位置，结束位置默认为文件末尾
     * @param startIndex 开始位置
     */
    fun setRangeHeader(startIndex: Long): P{
        return setRangeHeader(startIndex,-1)
    }

    /**
     * 设置断点下载范围
     * 注：
     * 1、开始位置小于0，及代表下载完整文件
     * 2、结束位置要大于开始位置，否则结束位置默认为文件末尾
     *
     * @param startIndex 开始位置
     * @param endIndex   结束位置
     * @return Param
     */
    fun setRangeHeader(startIndex: Long, endIndex: Long): P {
        var tempEndIndex = endIndex
        if (tempEndIndex < startIndex) tempEndIndex = -1
        var headerValue = "bytes=$startIndex-"
        if (tempEndIndex >= 0) {
            headerValue += tempEndIndex
        }
        return addHeader("RANGE", headerValue)
    }

}