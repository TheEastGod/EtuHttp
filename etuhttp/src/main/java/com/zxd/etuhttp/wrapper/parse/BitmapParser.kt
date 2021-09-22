package com.zxd.etuhttp.wrapper.parse

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.zxd.etuhttp.wrapper.exception.ExceptionHelper
import okhttp3.Response

/**
 * author: zxd
 * created on: 2021/6/24 15:42
 * description:
 */
class BitmapParser: Parser<Bitmap> {


    override fun onParser(response: Response): Bitmap {
        val body = ExceptionHelper.throwIfFatal(response)
        body.use {

            return BitmapFactory.decodeStream(it.byteStream())
        }

    }
}