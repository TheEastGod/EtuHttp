package com.zxd.etuhttp_annotation

/**
 * author: zxd
 * created on: 2021/6/18 14:37
 * description:
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDomain(val name: String = "",val className: String = "") {
}