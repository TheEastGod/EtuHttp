package com.zxd.dlog

import android.os.Debug
import java.io.PrintWriter
import java.io.StringWriter
import java.net.UnknownHostException

/**
 * author: zxd
 * created on: 2022/3/16 15:41
 * description:
 */
object DLog {

    /**
     * Priority constant for the println method; use Log.v.
     */
    private const val VERBOSE = 2

    /**
     * Priority constant for the println method; use Log.d.
     */
    private const val DEBUG = 3

    /**
     * Priority constant for the println method; use Log.i.
     */
    private const val INFO = 4

    /**
     * Priority constant for the println method; use Log.w.
     */
    private const val WARN = 5

    /**
     * Priority constant for the println method; use Log.e.
     */
    private const val ERROR = 6

    /**
     * Priority constant for the println method.
     */
    private const val ASSERT = 7

    private val interceptors = ArrayList<LogInterceptor>()

    fun v(tag: String, message: String, vararg args: Any) {
        log(VERBOSE,message,tag,args)
    }

    fun v(tag: String, message: String, vararg args: Any,throwable: Throwable?) {
        log(VERBOSE,message,tag,args,throwable = throwable)
    }

    fun d(tag: String, message: String, vararg args: Any) {
        log(DEBUG,message,tag,args)
    }

    fun d(tag: String, message: String, vararg args: Any,throwable: Throwable?) {
        log(DEBUG,message,tag,args,throwable = throwable)
    }

    fun i(tag: String, message: String, vararg args: Any) {
        log(INFO,message,tag,args)
    }

    fun i(tag: String, message: String, vararg args: Any,throwable: Throwable?) {
        log(INFO,message,tag,args,throwable = throwable)
    }

    fun w(tag: String, message: String, vararg args: Any) {
        log(WARN,message,tag,args)
    }

    fun w(tag: String, message: String, vararg args: Any,throwable: Throwable?) {
        log(WARN,message,tag,args,throwable = throwable)
    }

    fun e(tag: String, message: String, vararg args: Any,throwable: Throwable?) {
        log(ERROR,message,tag,args,throwable = throwable)
    }

    fun e(tag: String, message: String, vararg args: Any) {
        log(ERROR,message,tag,args)
    }

    fun a(tag: String, message: String, vararg args: Any) {
        log(ASSERT,message,tag,args)
    }

    fun a(tag: String, message: String, vararg args: Any,throwable: Throwable?) {
        log(ASSERT,message,tag,args,throwable = throwable)
    }

    fun init(){
        interceptors.add(PreprocessingLogInterceptor())
        interceptors.add(LogcatInterceptor())
    }

    @Synchronized
    private fun log(
        priority: Int,
        message: String,
        tag: String,
        vararg args: Any,
        throwable: Throwable? = null
    ) {
        var logMessage = message.format(*args)

        if(throwable != null){
            logMessage += getStackTraceString(throwable)
        }

        Chain(interceptors).process(priority,tag,logMessage)
    }

    private fun String.format(vararg args: Any) =
        if (args.isNullOrEmpty()) this else String.format(this, *args)

    // 读取堆栈
    private fun getStackTraceString(tr:Throwable?) : String{
        if (tr == null){
            return ""
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        var t = tr
        while (t != null){
            if (t is UnknownHostException){
                return ""
            }
            t = t.cause
        }
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        tr.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }

}