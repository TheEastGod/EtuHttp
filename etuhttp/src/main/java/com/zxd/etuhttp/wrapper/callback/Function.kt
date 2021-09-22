package com.zxd.etuhttp.wrapper.callback

/**
 * author: zxd
 * created on: 2021/2/5 15:01
 * description:
 */
interface Function<T,R> {


    fun apply(t:T) : R

}