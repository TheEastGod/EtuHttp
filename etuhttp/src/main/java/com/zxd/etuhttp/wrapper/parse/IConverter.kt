package com.zxd.etuhttp.wrapper.parse

import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.IOException
import java.lang.reflect.Type

/**
 * author: zxd
 * created on: 2021/2/7 11:18
 * description:
 */
interface IConverter {

    /**
     * 请求结束后拿到 ResponseBody 转 对象
     *
     * @param body             ResponseBody
     * @param type             对象类型
     * @param needDecodeResult 是否需要对结果进行解码/解密，可根据此字段判断,
     *                         可参考{@link GsonConverter#convert(ResponseBody, Type, boolean)}
     * @param <T>              T
     * @return T
     * @throws IOException 转换失败异常
     */
    @Throws(IOException::class)
    fun <T> convert(body: ResponseBody,type: Type,needDecodeResult: Boolean) : T


    /**
     * 对象转 RequestBody,发送{application/json; charset=utf-8}类型请求前，将会调用此方法
     *
     * @param value T
     * @param <T>   T
     * @return RequestBody
     * @throws IOException 转换失败异常
     */
    @Throws(IOException::class)
    fun <T> convert(value: T) : RequestBody {
        return RequestBody.create(null, ByteArray(0))
    }

}