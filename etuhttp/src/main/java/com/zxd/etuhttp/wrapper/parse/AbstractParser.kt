package com.zxd.etuhttp.wrapper.parse

import com.google.gson.internal.`$Gson$Preconditions`
import com.google.gson.internal.`$Gson$Types`
import com.zxd.etuhttp.wrapper.utils.TypeUtil
import java.lang.reflect.Type

/**
 * author: zxd
 * created on: 2021/2/7 16:41
 * description:
 */
abstract class AbstractParser<T> : Parser<T> {

    @JvmField
    protected var mType: Type

    constructor(){
        mType = TypeUtil.getActualTypeParameter(javaClass,0)
    }

    constructor(type: Type){
        mType = `$Gson$Types`.canonicalize(`$Gson$Preconditions`.checkNotNull(type))
    }



}