package com.zxd.etuhttp.wrapper

import okhttp3.Call

/**
 * author: zxd
 * created on: 2021/7/9 15:57
 * description:
 */
interface IEtuHttp {

    fun newCall(): Call
}