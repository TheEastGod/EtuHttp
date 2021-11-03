package com.zxd.etuhttp_compiler

import com.squareup.javapoet.*
import com.zxd.etuhttp_annotation.Param
import java.lang.StringBuilder
import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.logging.Logger
import javax.annotation.processing.Filer
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeVariable

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

        var method: MethodSpec.Builder

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

        //生成Param相关类 和 EtuHttp的相关方法
        for ((key,typeElement) in mElementMap){

            val etuHttpTypeNames = ArrayList<TypeVariableName>()

            //获取泛型参数  按照声明顺序返回此类型元素的形式类型参数
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

            //遍历public构造方法 并etuHttp相应的方法
            getConstructorFun(typeElement).forEach{
                val parameterSpecs = ArrayList<ParameterSpec>() //构造方法参数
                val methodBody = StringBuilder("return new \$T(new \$T(")

                //取得方法参数列表
                for ((index,element) in it.parameters.withIndex()){
                    val parameterSpec = ParameterSpec.get(element)
                    parameterSpecs.add(parameterSpec)

                    if(index == 0 && parameterSpec.type.toString().contains("String")){
                        methodBody.append("format(" + parameterSpecs[0].name + ", formatArgs)")
                        continue
                    }else if (index > 0){
                        methodBody.append(", ")
                    }
                    methodBody.append(parameterSpec.name)
                }
                methodBody.append("))")

                val methodSpec = MethodSpec
                    //方法名
                    .methodBuilder(key)
                    //修饰符
                    .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                    //参数
                    .addParameters(parameterSpecs)
                    //泛型参数
                    .addTypeVariables(etuHttpTypeNames)
                    //返回类型
                    .returns(methodReturnType)

                if (parameterSpecs.size >0 && parameterSpecs[0].type.toString().contains("String")){
                     methodSpec.addParameter(ArrayTypeName.of(Any::class.java), "formatArgs").varargs()
                }
                methodSpec.addStatement(methodBody.toString(), etuHttpParamName, param)
                methodList.add(methodSpec.build())
            }

            //生成类
            val superclass = typeElement.superclass
            var prefix = "((" + param.simpleName() + ")param)."
            val etuHttpParam = when(superclass.toString()){
                "com.zxd.etuhttp.wrapper.param.BodyParam" -> ClassName.get(etuHttpPackage,"EtuHttpBodyParam")
                "com.zxd.etuhttp.wrapper.param.FormParam" -> ClassName.get(etuHttpPackage,"EtuHttpFormParam")
                "com.zxd.etuhttp.wrapper.param.JsonParam" -> ClassName.get(etuHttpPackage,"EtuHttpJsonParam")
                "com.zxd.etuhttp.wrapper.param.JsonArrayParam" -> ClassName.get(etuHttpPackage,"EtuHttpJsonArrayParam")
                "com.zxd.etuhttp.wrapper.param.NoBodyParam" -> ClassName.get(etuHttpPackage,"EtuHttpNoBodyParam")
                else -> {
                    val typeName = TypeName.get(superclass)
                    if ((typeName as? ParameterizedTypeName)?.rawType?.toString() == "com.zxd.etuhttp.wrapper.param.AbstractBodyParam") {
                        prefix = "param."
                        ClassName.get(etuHttpPackage,"EtuHttpAbstractBodyParam").let {
                            ParameterizedTypeName.get(it,param,etuHttpParamName)
                        }
                    }else{
                        prefix = "param."
                        ParameterizedTypeName.get(ETUHTTP,param,etuHttpParamName)
                    }
                }
            }

            val etuHttpPostCustomMethod = ArrayList<MethodSpec>()
            etuHttpPostCustomMethod.add(
                MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(param,"param")
                    .addStatement("super(param)")
                    .build()
            )

            for (enclosedElement in typeElement.enclosedElements){
                if (enclosedElement !is ExecutableElement
                    || enclosedElement.kind != ElementKind.METHOD //过滤非方法
                    || !enclosedElement.modifiers.contains(Modifier.PUBLIC) //过滤非public修饰符
                    || enclosedElement.getAnnotation(Override::class.java) != null)
                    continue

                var returnType = TypeName.get(enclosedElement.returnType) //方法返回值
                if (returnType.toString() == param.toString()){
                    returnType = etuHttpParamName
                }

                val parameterSpecs : MutableList<ParameterSpec> = ArrayList() // 方法参数
                val methodBody = StringBuilder(enclosedElement.simpleName.toString())  //方法体
                    .append("(")

                for(element in enclosedElement.parameters){
                    val parameterSpec = ParameterSpec.get(element)
                    parameterSpecs.add(parameterSpec)
                    methodBody.append(parameterSpec.name).append(",")
                }
                if (methodBody.toString().endsWith(",")) {
                    methodBody.deleteCharAt(methodBody.length - 1)
                }
                methodBody.append(")")

                val typeVariableNames : MutableList<TypeVariableName>  = ArrayList() //方法声明的泛型
                for (element in enclosedElement.typeParameters){
                    val typeVariableName = TypeVariableName.get(element.asType() as TypeVariable)
                    typeVariableNames.add(typeVariableName)
                }

                val throwTypeName: MutableList<TypeName> = ArrayList() //方法要抛出的异常
                for (mirror in enclosedElement.thrownTypes){
                    val typeName = TypeName.get(mirror)
                    throwTypeName.add(typeName)
                }

                method = MethodSpec.methodBuilder(enclosedElement.simpleName.toString())
                    .addModifiers(enclosedElement.modifiers)
                    .addTypeVariables(typeVariableNames)
                    .addExceptions(throwTypeName)
                    .addParameters(parameterSpecs)
                if (enclosedElement.isVarArgs){
                    method.varargs()
                }
                when {
                    returnType === etuHttpParamName -> {
                        method.addStatement(prefix + methodBody, param)
                            .addStatement("return this")
                    }
                    returnType.toString() == "void" -> {
                        method.addStatement(prefix + methodBody)
                    }
                    else -> {
                        method.addStatement("return $prefix$methodBody", param)
                    }
                }
                method.returns(returnType)
                etuHttpPostCustomMethod.add(method.build())
            }
            val etuHttpParamSpec = TypeSpec.classBuilder(etuHttpName)
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariables(etuHttpTypeNames)
                .superclass(etuHttpParam)
                .addMethods(etuHttpPostCustomMethod)
                .build()

            JavaFile.builder(etuHttpPackage,etuHttpParamSpec).build().writeTo(filer)

        }

        //生成etuHttp相应方法
        methodList.add(
            MethodSpec.methodBuilder("setUrl")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String::class.java,"url")
                .addStatement("param.setUrl(url)")
                .addStatement("return (R)this")
                .returns(etuHttp)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("removeAllQuery")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("param.removeAllQuery()")
                .addStatement("return (R)this")
                .returns(etuHttp)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("removeAllQuery")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String::class.java,"key")
                .addStatement("param.removeAllQuery(key)")
                .addStatement("return (R)this")
                .returns(etuHttp)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("addQuery")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String::class.java,"key")
                .addParameter(Any::class.java,"value")
                .addStatement("param.addQurey(key,value)")
                .addStatement("return (R)this")
                .returns(etuHttp)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("setQuery")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String::class.java, "key")
                .addParameter(Any::class.java, "value")
                .addStatement("param.setQuery(key,value)")
                .addStatement("return (R)this")
                .returns(etuHttp)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("addEncodedQuery")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String::class.java, "key")
                .addParameter(Any::class.java, "value")
                .addStatement("param.addEncodedQuery(key,value)")
                .addStatement("return (R)this")
                .returns(etuHttp)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("setEncodedQuery")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String::class.java, "key")
                .addParameter(Any::class.java, "value")
                .addStatement("param.setEncodedQuery(key,value)")
                .addStatement("return (R)this")
                .returns(etuHttp)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("addAllQuery")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(mapName, "map")
                .addStatement("param.addAllQuery(map)")
                .addStatement("return (R)this")
                .returns(etuHttp)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("setAllQuery")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(mapName, "map")
                .addStatement("param.setAllQuery(map)")
                .addStatement("return (R)this")
                .returns(etuHttp)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("addAllEncodedQuery")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(mapName, "map")
                .addStatement("param.addAllEncodedQuery(map)")
                .addStatement("return (R)this")
                .returns(etuHttp)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("setAllEncodedQuery")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(mapName, "map")
                .addStatement("param.setAllEncodedQuery(map)")
                .addStatement("return (R)this")
                .returns(etuHttp)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("addHeader")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String::class.java, "line")
                .addStatement("param.addHeader(line)")
                .addStatement("return (R)this")
                .returns(etuHttp)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("addHeader")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String::class.java, "line")
                .addParameter(Boolean::class.javaPrimitiveType, "isAdd")
                .beginControlFlow("if(isAdd)")
                .addStatement("param.addHeader(line)")
                .endControlFlow()
                .addStatement("return (R)this")
                .returns(etuHttp)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("addNonAsciiHeader")
                .addJavadoc("Add a header with the specified name and value. Does validation of header names, allowing non-ASCII values.")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String::class.java, "key")
                .addParameter(String::class.java, "value")
                .addStatement("param.addNonAsciiHeader(key,value)")
                .addStatement("return (R)this")
                .returns(etuHttp)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("setNonAsciiHeader")
                .addJavadoc("Set a header with the specified name and value. Does validation of header names, allowing non-ASCII values.")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String::class.java, "key")
                .addParameter(String::class.java, "value")
                .addStatement("param.setNonAsciiHeader(key,value)")
                .addStatement("return (R)this")
                .returns(etuHttp)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("addHeader")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String::class.java, "key")
                .addParameter(String::class.java, "value")
                .addStatement("param.addHeader(key,value)")
                .addStatement("return (R)this")
                .returns(etuHttp)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("addHeader")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String::class.java, "key")
                .addParameter(String::class.java, "value")
                .addParameter(Boolean::class.javaPrimitiveType, "isAdd")
                .beginControlFlow("if(isAdd)")
                .addStatement("param.addHeader(key,value)")
                .endControlFlow()
                .addStatement("return (R)this")
                .returns(etuHttp)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("addAllHeader")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(mapStringName, "headers")
                .addStatement("param.addAllHeader(headers)")
                .addStatement("return (R)this")
                .returns(etuHttp)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("addAllHeader")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(headerName, "headers")
                .addStatement("param.addAllHeader(headers)")
                .addStatement("return (R)this")
                .returns(etuHttp)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("setHeader")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String::class.java, "key")
                .addParameter(String::class.java, "value")
                .addStatement("param.setHeader(key,value)")
                .addStatement("return (R)this")
                .returns(etuHttp)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("setAllHeader")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(mapStringName, "headers")
                .addStatement("param.setAllHeader(headers)")
                .addStatement("return (R)this")
                .returns(etuHttp)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("setRangeHeader")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Long::class.javaPrimitiveType, "startIndex")
                .addStatement("return setRangeHeader(startIndex, -1, false)")
                .returns(etuHttp)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("setRangeHeader")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Long::class.javaPrimitiveType, "startIndex")
                .addParameter(Long::class.javaPrimitiveType, "endIndex")
                .addStatement("return setRangeHeader(startIndex, endIndex, false)")
                .returns(etuHttp).build())

        methodList.add(
            MethodSpec.methodBuilder("setRangeHeader")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Long::class.javaPrimitiveType, "startIndex")
                .addParameter(Boolean::class.javaPrimitiveType, "connectLastProgress")
                .addStatement("return setRangeHeader(startIndex, -1, connectLastProgress)")
                .returns(etuHttp).build())

        methodList.add(
            MethodSpec.methodBuilder("setRangeHeader")
                .addJavadoc("""
                    设置断点下载开始/结束位置
                    @param startIndex 断点下载开始位置
                    @param endIndex 断点下载结束位置，默认为-1，即默认结束位置为文件末尾
                    @param connectLastProgress 是否衔接上次的下载进度，该参数仅在带进度断点下载时生效
                """.trimIndent())
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Long::class.javaPrimitiveType, "startIndex")
                .addParameter(Long::class.javaPrimitiveType, "endIndex")
                .addParameter(Boolean::class.javaPrimitiveType, "connectLastProgress")
                .addCode("""
                    param.setRangeHeader(startIndex, endIndex);                         
                    if (connectLastProgress)                                            
                      param.tag(DownloadOffSize.class, new ${'$'}T(startIndex));
                    return (R) this;                                                    
                """.trimIndent(), downloadOffSizeName)
                .returns(etuHttp).build())

        methodList.add(
            MethodSpec.methodBuilder("removeAllHeader")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String::class.java, "key")
                .addStatement("param.removeAllHeader(key)")
                .addStatement("return (R)this")
                .returns(etuHttp)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("setHeadersBuilder")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(headerBuilderName, "builder")
                .addStatement("param.setHeadersBuilder(builder)")
                .addStatement("return (R)this")
                .returns(etuHttp)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("setAssemblyEnabled")
                .addJavadoc("""
                    设置单个接口是否需要添加公共参数,
                    即是否回调通过{@link #setOnParamAssembly(Function)}方法设置的接口,默认为true
                """.trimIndent())
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Boolean::class.javaPrimitiveType, "enabled")
                .addStatement("param.setAssemblyEnabled(enabled)")
                .addStatement("return (R)this")
                .returns(etuHttp)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("setDecoderEnabled")
                .addJavadoc("""
                    设置单个接口是否需要对Http返回的数据进行解码/解密,
                    即是否回调通过{@link #setResultDecoder(Function)}方法设置的接口,默认为true
                """.trimIndent())
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Boolean::class.javaPrimitiveType, "enabled")
                .addStatement("param.addHeader(\$T.DATA_DECRYPT,String.valueOf(enabled))", paramName)
                .addStatement("return (R)this")
                .returns(etuHttp)
                .build())

        methodList.add(MethodSpec.methodBuilder("isAssemblyEnabled")
            .addModifiers(Modifier.PUBLIC)
            .addStatement("return param.isAssemblyEnabled()")
            .returns(Boolean::class.javaPrimitiveType)
            .build())

        methodList.add(
            MethodSpec.methodBuilder("getUrl")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("addDefaultDomainIfAbsent(param)")
                .addStatement("return param.getUrl()")
                .returns(String::class.java)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("getSimpleUrl")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return param.getSimpleUrl()")
                .returns(String::class.java)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("getHeader")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String::class.java, "key")
                .addStatement("return param.getHeader(key)")
                .returns(String::class.java).build())

        methodList.add(
            MethodSpec.methodBuilder("getHeaders")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return param.getHeaders()")
                .returns(headerName)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("getHeadersBuilder")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return param.getHeadersBuilder()")
                .returns(headerBuilderName).build())

        methodList.add(
            MethodSpec.methodBuilder("tag")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Any::class.java, "tag")
                .addStatement("param.tag(tag)")
                .addStatement("return (R)this")
                .returns(etuHttp)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("tag")
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(t)
                .addParameter(classTName, "type")
                .addParameter(t, "tag")
                .addStatement("param.tag(type,tag)")
                .addStatement("return (R)this")
                .returns(etuHttp).build())


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