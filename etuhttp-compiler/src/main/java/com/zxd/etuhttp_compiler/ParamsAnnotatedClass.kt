package com.zxd.etuhttp_compiler

import com.squareup.javapoet.*
import com.zxd.etuhttp_annotation.Param
import java.lang.StringBuilder
import java.util.ArrayList
import java.util.LinkedHashMap
import javax.annotation.processing.Filer
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

/**
 * author: zxd
 * created on: 2021/7/30 13:59
 * description:
 */

class ParamsAnnotatedClass {

    private val mElementMap = LinkedHashMap<String, TypeElement>()

    fun add(typeElement: TypeElement){
        val annotation = typeElement.getAnnotation(Param::class.java)
        val name = annotation.methodName

        require(name.isNotEmpty()){
            String.format("methodName() in @%s for class %s is null or empty! that's not allowed",
                Param::class.java.simpleName, typeElement.qualifiedName.toString())
        }
        mElementMap[name] = typeElement
    }

    fun getMethodList(filer: Filer?):List<MethodSpec>{
        val etuHttp = r
        val t = TypeVariableName.get("T")
        val superT = WildcardTypeName.subtypeOf(t)
        val classTName = ParameterizedTypeName.get(ClassName.get(Class::class.java),superT)
        val headerName = ClassName.get("okhttp3","Headers")
        val headerBuilderName = ClassName.get("okhttp3","Headers.Builder")
        val cacheControlName = ClassName.get("okhttp3", "CacheControl")
        val paramName = ClassName.get(packageName, "Param")
        val cacheModeName = ClassName.get("com.zxd.etuhttp.wrapper.cahce", "CacheMode")
        val cacheStrategyName = ClassName.get("com.zxd.etuhttp.wrapper.cahce", "CacheStrategy")
        val downloadOffSizeName = ClassName.get("com.zxd.etuhttp.wrapper.entity", "DownloadOffSize")
        val stringName = TypeName.get(String::class.java)
        val subObject = WildcardTypeName.subtypeOf(TypeName.get(Any::class.java))
        val mapName = ParameterizedTypeName.get(ClassName.get(MutableMap::class.java),stringName,subObject)
        val mapStringName = ParameterizedTypeName.get(ClassName.get(MutableMap::class.java), stringName, stringName)
        val methodList = ArrayList<MethodSpec>()

        val methodMap = LinkedHashMap<String, String>()
        methodMap["get"] = "EtuHttpNoBodyParam"
        methodMap["head"] = "EtuHttpNoBodyParam"
        methodMap["postBody"] = "EtuHttpBodyParam"
        methodMap["putBody"] = "EtuHttpBodyParam"
        methodMap["patchBody"] = "EtuHttpBodyParam"
        methodMap["deleteBody"] = "EtuHttpBodyParam"
        methodMap["postForm"] = "EtuHttpFormParam"
        methodMap["putForm"] = "EtuHttpFormParam"
        methodMap["patchForm"] = "EtuHttpFormParam"
        methodMap["deleteForm"] = "EtuHttpFormParam"
        methodMap["postJson"] = "EtuHttpJsonParam"
        methodMap["putJson"] = "EtuHttpJsonParam"
        methodMap["patchJson"] = "EtuHttpJsonParam"
        methodMap["deleteJson"] = "EtuHttpJsonParam"
        methodMap["postJsonArray"] = "EtuHttpJsonArrayParam"
        methodMap["putJsonArray"] = "EtuHttpJsonArrayParam"
        methodMap["patchJsonArray"] = "EtuHttpJsonArrayParam"
        methodMap["deleteJsonArray"] = "EtuHttpJsonArrayParam"

        val method: MethodSpec.Builder

        val codeBlock = CodeBlock.builder().add("""
             For example:
                                                             
                    ```                                                  
                    EtuHttp.get("/service/%1${'$'}L/...?pageSize=%2${'$'}L", 1, 20)   
                        .asString()                                      
                        .subscribe()                                     
                    ```                                                  
                """.trimIndent(), "${'$'}s", "${'$'}s")
            .build()

        for ((key,value ) in methodMap){
            val methodBuilder = MethodSpec.methodBuilder(key)
            if (key == "get") {
                methodBuilder.addJavadoc(codeBlock)
            }

            methodList.add(methodBuilder.
            addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                .addParameter(String::class.java,"url")
                .addParameter(ArrayTypeName.of(Any::class.java),"formatArgs")
                .varargs()
                .addStatement("return new $value(\$T.\$L(format(url,formatArgs)))",paramName,key)
                .returns(ClassName.get(etuHttpPackage,value))
                .build()
            )
        }

        for ((key,typeElement) in mElementMap){
            val etuHttpTypeNames = ArrayList<TypeVariableName>()
            for ((i,parameterElement) in typeElement.typeParameters.withIndex()){
                val typeVariableName = TypeVariableName.get(parameterElement)
                etuHttpTypeNames.add(typeVariableName)
            }

            val param = ClassName.get(typeElement)
            val etuHttpName = "EtuHttp${typeElement.simpleName}"
            val etuHttpParamName = ClassName.get(etuHttpPackage,etuHttpName)
            val methodReturnType = if (etuHttpTypeNames.size > 0){
                ParameterizedTypeName.get(etuHttpParamName,*etuHttpTypeNames.toTypedArray())
            }else{
                etuHttpParamName
            }

            //遍历public构造方法
            getConstructorFun(typeElement).forEach{



            }


        }

        return methodList
    }


    //获取构造方法
    private fun getConstructorFun(typeElement: TypeElement): MutableList<ExecutableElement>{
        val funList = ArrayList<ExecutableElement>()
        typeElement.enclosedElements.forEach{
            if (it is ExecutableElement
                && it.kind == ElementKind.CONSTRUCTOR
                && it.modifiers.contains(Modifier.PUBLIC)){
                funList.add(it)
            }
        }
        return funList
    }

}