package com.zxd.etuhttp_annotation
import kotlin.reflect.KClass

/**
 * author: zxd
 * created on: 2021/6/18 14:53
 * description:
 */
@Target(AnnotationTarget.TYPE)
@Retention(AnnotationRetention.BINARY)
annotation class Parser(
        /**
         * @return 解析器名称
         */
        val name: String,
        /**
         * 解析器泛型的包装类，通过该参数，可以生成任意个asXxx方法
         * @return Class数组
         */
        val wrappers: Array<KClass<*>> = [])
