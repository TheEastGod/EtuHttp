package com.zxd.etuhttp.wrapper.parse

import okhttp3.Response
import java.io.IOException

/**
 * author: zxd
 * created on: 2021/2/7 15:09
 * description: [okhttp3.Response] to T
 */
interface Parser<T> {

    @Throws(IOException::class)
    fun onParser(response: Response): T

}