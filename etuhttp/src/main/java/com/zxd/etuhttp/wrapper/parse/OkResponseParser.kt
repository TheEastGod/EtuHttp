package com.zxd.etuhttp.wrapper.parse

import com.zxd.etuhttp.wrapper.exception.ExceptionHelper
import okhttp3.Response
import java.io.IOException

/**
 * author: zxd
 * created on: 2021/6/24 15:56
 * description:
 */
class OkResponseParser: Parser<Response> {
    @Throws(IOException::class)
    override fun onParser(response: Response): Response {
        ExceptionHelper.throwIfFatal(response)

        return response
    }
}