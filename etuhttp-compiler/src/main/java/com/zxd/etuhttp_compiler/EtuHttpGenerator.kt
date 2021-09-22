package com.zxd.etuhttp_compiler

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeVariableName

/**
 * author: zxd
 * created on: 2021/7/30 14:04
 * description:
 */
class EtuHttpGenerator {


}


const val ETUHttp_CLASS_NAME = "EtuHttp"
const val packageName = "etuhttp.wrapper.param"
var ETUHTTP = ClassName.get(etuHttpPackage, ETUHttp_CLASS_NAME)
private val paramName = ClassName.get(packageName, "Param")
var p = TypeVariableName.get("P", paramName)  //泛型P
var r = TypeVariableName.get("R", ETUHTTP)     //泛型R