package com.zxd.etuhttp.wrapper

import android.content.Context
import android.net.Uri
import com.zxd.etuhttp.wrapper.callback.Function
import com.zxd.etuhttp.wrapper.entity.UriRequestBody
import com.zxd.etuhttp.wrapper.exception.HttpStatusCodeException
import com.zxd.etuhttp.wrapper.param.Param
import com.zxd.etuhttp.wrapper.parse.Parser
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException
import java.lang.reflect.Type
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * author: zxd
 * created on: 2021/2/4 14:29
 * description:
 */

@ExperimentalContracts
inline fun<T : Param<T>> Param<T>.apply(block:Param<T>.() -> Unit):Param<T>{
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    block(this)
    return this
}

/**
 * If the provided Throwable is an Error this method
 * throws it, otherwise returns a RuntimeException wrapping the error
 * if that error is a checked exception.
 * @param error the error to wrap or throw
 * @return the (wrapped) error
 */
fun wrapOrThrow(error: Throwable?): RuntimeException {
    if (error is Error) {
        throw (error as Error?)!!
    }
    return if (error is RuntimeException) {
        error
    } else RuntimeException(error)
}

@Throws(IOException::class)
fun throwIfFatal(response: Response): ResponseBody{
    val body = response.body ?: throw HttpStatusCodeException(response)

    if (!response.isSuccessful){
        throw HttpStatusCodeException(response,body.string())
    }

    return body
}

fun <R> Response.convert(type: Type): R{
    val body =  throwIfFatal(this)
    val needDecodeResult = OkHttpCompat.needDecodeResult(this)
    val converter = OkHttpCompat.getConverter(this)

    return converter!!.convert(body, type, needDecodeResult)
}


@JvmOverloads
fun Uri.asRequestBody(
    context: Context,
    contentType: MediaType? = null
): RequestBody = UriRequestBody(context, this, contentType)


