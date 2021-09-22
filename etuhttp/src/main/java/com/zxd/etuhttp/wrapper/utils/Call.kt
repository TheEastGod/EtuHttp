package com.zxd.etuhttp.wrapper.utils

import com.zxd.etuhttp.wrapper.parse.Parser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * author: zxd
 * created on: 2021/6/24 14:54
 * description:
 */

internal suspend fun Call.await(): Response {

    return suspendCancellableCoroutine {
        it.invokeOnCancellation {
            cancel()
        }
        enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                it.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                it.resume(response)
            }
        })
    }
}

internal suspend fun <T> Call.await(parser: Parser<T>): T{
    return suspendCancellableCoroutine {
        it.invokeOnCancellation {
            cancel()
        }

        enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                it.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    it.resume(parser.onParser(response))
                } catch (t: Throwable) {
                    it.resumeWithException(t)
                }
            }
        })
    }
}
