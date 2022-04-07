package com.zxd.dlog

/**
 * author: zxd
 * created on: 2022/3/16 15:40
 * description:
 */
interface LogInterceptor {

    fun log(priority: Int, tag: String, log: String, chain: Chain)

    fun enable(): Boolean

}