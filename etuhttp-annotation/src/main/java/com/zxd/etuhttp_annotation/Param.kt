package com.zxd.etuhttp_annotation

/**
 * author: zxd
 * created on: 2021/6/18 14:52
 * description:
 */
@Target(AnnotationTarget.TYPE)
@Retention(AnnotationRetention.BINARY)
annotation class Param (val methodName:String){
}