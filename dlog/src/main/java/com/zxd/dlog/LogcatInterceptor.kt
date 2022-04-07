package com.zxd.dlog

import android.util.Log

/**
 * author: zxd
 * created on: 2022/3/18 16:52
 * description:
 */
class LogcatInterceptor : LogInterceptor {

    override fun log(priority: Int, tag: String, log: String, chain: Chain) {
        if (enable()) {
            var message = log
            val segmentSize = 1024
            val length: Long = message.length.toLong()

            if (length <= segmentSize) {
                Log.println(priority, tag, message)
            } else {
                while (message.length > segmentSize) {
                    val logContent: String = message.substring(0, segmentSize)
                    message = message.replace(logContent, "")
                    Log.println(priority, tag, logContent)
                }
            }
        }
        chain.process(priority, tag, log)
    }

    override fun enable(): Boolean {
       return true
    }
}