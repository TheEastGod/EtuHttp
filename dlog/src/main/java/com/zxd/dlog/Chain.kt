package com.zxd.dlog

/**
 * author: zxd
 * created on: 2022/3/16 15:48
 * description:
 */
class Chain( private val interceptors: MutableList<LogInterceptor>,private val index : Int = 0) {

    fun process(priority: Int, tag: String, log: String){
        val next = Chain( interceptors,index + 1)
        val interceptor = interceptors.getOrNull(index)
        interceptor?.log(priority,tag,log,next)
    }

}