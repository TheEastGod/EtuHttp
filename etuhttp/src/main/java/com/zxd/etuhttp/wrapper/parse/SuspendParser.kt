package com.zxd.etuhttp.wrapper.parse

import okhttp3.Response
import java.io.IOException

/**
 * author: zxd
 * created on: 2021/6/24 14:52
 * description:
 */
abstract class SuspendParser<T>: Parser<T> {

    override fun onParser(response: Response): T {
        throw UnsupportedOperationException("Should be call onSuspendParse fun")
    }

    @Throws(IOException::class)
    abstract suspend fun onSuspendParse(response: Response): T
}