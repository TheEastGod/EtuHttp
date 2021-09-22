package com.zxd.etuhttp_compiler

import com.sun.java.browser.net.ProxyInfo
import com.zxd.etuhttp_annotation.*
import com.zxd.etuhttp_compiler.ClassHelper.generatorStaticClass
import net.ltgt.gradle.incap.IncrementalAnnotationProcessor
import net.ltgt.gradle.incap.IncrementalAnnotationProcessorType
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

lateinit var etuHttpPackage: String  //相关类的包名

/**
 * author: zxd
 * created on: 2021/6/23 11:20
 * description:
 */
@SupportedOptions(value = ["etuhttp_rxjava", "etuhttp_package"])
@IncrementalAnnotationProcessor(IncrementalAnnotationProcessorType.AGGREGATING)
open class AnnotationProcessor: AbstractProcessor() {

    /**
     * 生成文件的工具类
     */
    private lateinit var filer: Filer
    /**
     * 打印信息
     */
    private lateinit var messager: Messager
    /**
     * 元素相关
     */
    private lateinit var elementUtils:Elements
    private lateinit var typeUtils: Types
    private val proxyInfoMap: Map<String, ProxyInfo> = HashMap()

    private var processed = false


    @Synchronized
    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        filer = processingEnv.filer
        messager = processingEnv.messager
        elementUtils = processingEnv.elementUtils
        typeUtils = processingEnv.typeUtils
        val map =  processingEnv.options
        etuHttpPackage = map["etu_package"] ?: "etuhttp.wrapper.param"
        initRxJavaVersion(getRxJavaVersion(map))
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        val set = HashSet<String>()
        set.add(Converter::class.java.canonicalName)
        set.add(DefaultDomain::class.java.canonicalName)
        set.add(Domain::class.java.canonicalName)
        set.add(OkClient::class.java.canonicalName)
        set.add(Param::class.java.canonicalName)
        set.add(Parser::class.java.canonicalName)
        return set
    }

    open fun isAndroidPlatform() = true

    open fun getRxJavaVersion(map: Map<String,String>) = map["etuhttp_rxjava"]

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        if (annotations.isEmpty() || processed) return true

        generatorStaticClass(filer,isAndroidPlatform())


        return true
    }

}