package com.zxd.etuhttp.wrapper.utils

import com.google.gson.internal.`$Gson$Types`
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * author: zxd
 * created on: 2021/2/7 16:53
 * description:
 */
class TypeUtil {


    companion object{

        /**
         * 获取泛型类型
         *
         * @param clazz 类类型
         * @param index 第几个泛型
         * @return Type
         */
        fun getActualTypeParameter(clazz: Class<*>,index: Int): Type {
            val superClass = clazz.genericSuperclass
            if (superClass !is ParameterizedType){
                throw RuntimeException("Missing type parameter.")
            }
            val parameter: ParameterizedType = superClass
            return `$Gson$Types`.canonicalize(parameter.actualTypeArguments[index])
        }
    }

}