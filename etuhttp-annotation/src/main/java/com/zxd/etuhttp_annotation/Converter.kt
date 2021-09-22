package com.zxd.etuhttp_annotation

/**
 * author: zxd
 * created on: 2021/6/17 17:17
 * description:
 */
@Target(AnnotationTarget.FIELD)
@kotlin.annotation.Retention(AnnotationRetention.BINARY)
annotation class Converter(val name: String, val className:String = "")