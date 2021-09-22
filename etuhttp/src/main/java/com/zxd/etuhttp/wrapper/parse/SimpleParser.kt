package com.zxd.etuhttp.wrapper.parse

import com.zxd.etuhttp.wrapper.convert
import okhttp3.Response
import java.io.IOException
import java.lang.reflect.Type

/**
 * author: zxd
 * created on: 2021/2/8 14:45
 * description:
 */
open class SimpleParser<T> :AbstractParser<T>{

    protected constructor() : super()
    constructor(type: Type) : super(type)

    @Throws(IOException::class)
    override fun onParser(response: Response): T {
       return response.convert(mType)
    }

    companion object{
        @JvmStatic
        operator fun <T> get(type: Class<T>) = SimpleParser<T>(type)
    }

}