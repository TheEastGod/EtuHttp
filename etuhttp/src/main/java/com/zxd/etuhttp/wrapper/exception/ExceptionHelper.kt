package com.zxd.etuhttp.wrapper.exception

import okhttp3.Response
import okhttp3.ResponseBody

/**
 * author: zxd
 * created on: 2021/6/24 15:43
 * description:
 */
class ExceptionHelper {

    companion object{

        fun throwIfFatal(response: Response): ResponseBody {
            val body = response.body ?: throw HttpStatusCodeException(response)

            if (!response.isSuccessful) {
                throw HttpStatusCodeException(response, body.string())
            }
            return body
        }


        fun wrapOrThrow(error: Throwable?): RuntimeException? {
            if (error is Error) {
                throw (error as Error?)!!
            }
            return if (error is RuntimeException) {
                error
            } else RuntimeException(error)
        }
    }

}