package com.zxd.etuhttp_compiler

import com.squareup.javapoet.*
import com.zxd.etuhttp_annotation.Parser
import java.io.IOException
import javax.annotation.processing.Filer
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypesException
import javax.lang.model.type.TypeMirror

/**
 * author: zxd
 * created on: 2021/11/3 11:10
 * description:
 */
class ParserAnnotatedClass {

    private val mElementMap = LinkedHashMap<String,TypeElement>()
    private val mTypeMap = LinkedHashMap<String,List<TypeMirror>>()

    fun add(typeElement: TypeElement){
        val annotation = typeElement.getAnnotation(Parser::class.java)
        val name : String = annotation.name

        require(name.isNotEmpty()){
            String.format("methodName() in @%s for class %s is null or empty! that's not allowed",
                Parser::class.java.simpleName, typeElement.qualifiedName.toString())
        }

        try {
            annotation.wrappers
        } catch (e: MirroredTypesException) {
            val typeMirror = e.typeMirrors
            mTypeMap[name] = typeMirror
        }

        mElementMap[name] = typeElement
    }


    fun getMethodList(filer: Filer):List<MethodSpec>{
        val t = TypeVariableName.get("T")
        val className = ClassName.get(Class::class.java)
        val classTName = ParameterizedTypeName.get(className, t)

        val listTName = ParameterizedTypeName.get(ClassName.get(List::class.java), t)
        val callName = ClassName.get("okhttp3", "Call")
        val responseName = ClassName.get("okhttp3", "Response")
        val requestName = ClassName.get("okhttp3", "Request")
        val parserName = ClassName.get("com.zxd.etuhttp.wrapper.parse", "Parser")
        val progressName = ClassName.get("com.zxd.etuhttp.wrapper.entity", "Progress")
        val logUtilName = ClassName.get("com.zxd.etuhttp.wrapper.utils", "LogUtil")
        val logTimeName = ClassName.get("com.zxd.etuhttp.wrapper.utils", "LogTime")
        val typeName = TypeName.get(String::class.java)
        val parserTName = ParameterizedTypeName.get(parserName, t)
        val simpleParserName = ClassName.get("com.zxd.etuhttp.wrapper.parse", "SimpleParser")
        val type = ClassName.get("java.lang.reflect", "Type")
        val parameterizedType = ClassName.get("com.zxd.etuhttp.wrapper.entity", "ParameterizedTypeImpl")
        val methodList = ArrayList<MethodSpec>()


        methodList.add(
            MethodSpec.methodBuilder("execute")
                .addModifiers(Modifier.PUBLIC)
                .addException(IOException::class.java)
                .addStatement("return newCall().execute()")
                .returns(responseName)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("execute")
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(t)
                .addException(IOException::class.java)
                .addParameter(parserName,"parser")
                .addStatement("return parser.onParse(execute())")
                .returns(t)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("executeString")
                .addModifiers(Modifier.PUBLIC)
                .addException(IOException::class.java)
                .addStatement("return executeClass(String.class)")
                .returns(typeName)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("executeClass")
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(t)
                .addException(IOException::class.java)
                .addParameter(classTName, "type")
                .addStatement("return execute(new \$T<T>(type))", simpleParserName)
                .returns(t)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("executeList")
            .addModifiers(Modifier.PUBLIC)
            .addTypeVariable(t)
            .addException(IOException::class.java)
            .addParameter(className,"type")
            .addStatement("\$T tTypeList = \$T.get(List.class, type)", type, parameterizedType)
            .addStatement("return execute(new \$T<List<T>>(tTypeList))", simpleParserName)
            .returns(listTName)
            .build())

        methodList.add(
            MethodSpec.methodBuilder("newCall")
                .addAnnotation(Override::class.java)
                .addModifiers(Modifier.PUBLIC,Modifier.FINAL)
                .addCode("""
                    Request request = buildRequest();
                    OkHttpClient okClient = getOkHttpClient();
                    return okClient.newCall(request);
                """.trimIndent())
                .returns(callName)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("buildRequest")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addCode(
                    """
                    boolean debug = ${"$"}T.isDebug();
                    if (request == null) {
                        doOnStart();
                        request = param.buildRequest();
                         if (debug) 
                          LogUtil.log(request, getOkHttpClient().cookieJar());
                }
                    if (debug) {
                     request = request.newBuilder()
                    .tag(LogTime.class, new ${"$"}T())
                    .build();
                }
                    return request;
                """.trimIndent(),logUtilName,logTimeName)
                .build())

        methodList.add(
            MethodSpec.methodBuilder("doOnStart")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .addJavadoc("请求开始前内部调用，用于添加默认域名等操作\n")
                .addStatement("setConverter(param)")
                .addStatement("addDefaultDomainIfAbsent(param)")
                .build())

        if (isDependenceRxJava()){

        }



        return methodList

    }

}