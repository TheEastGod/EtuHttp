package com.zxd.dlog

import java.lang.StringBuilder

/**
 * author: zxd
 * created on: 2022/3/18 15:00
 * description:
 */
class PreprocessingLogInterceptor : LogInterceptor{

    companion object{

        private const val HEADER =
            "┌──────────────────────────────────────────────────────────────────────────────────────────────────────"
        private const val FOOTER =
            "└──────────────────────────────────────────────────────────────────────────────────────────────────────"
        private const val LEFT_BORDER = '│'

        private val blackList = listOf(
            PreprocessingLogInterceptor::class.java.name,
            DLog::class.java.name,
            Chain::class.java.name,
        )
    }

    override fun log(priority: Int, tag: String, log: String, chain: Chain) {
        chain.process(priority, tag, HEADER)
        chain.process(priority, tag, "$LEFT_BORDER$log")

        getCallStack(blackList).forEach {
            val callStack = StringBuilder()
                .append(LEFT_BORDER)
                .append("\t${it}").toString()
            chain.process(priority, tag, callStack)
        }
        chain.process(priority, tag, FOOTER)
    }

    override fun enable(): Boolean {
       return true
    }

    private fun getCallStack(blackList: List<String>): List<String>{

        return Thread.currentThread()
            .stackTrace.drop(3)
            .filter { it.className !in blackList }
            .map { "${it.className}.${it.methodName}(${it.fileName}:${it.lineNumber})" }
    }

}