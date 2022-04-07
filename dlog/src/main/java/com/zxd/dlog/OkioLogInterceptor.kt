package com.zxd.dlog

import android.annotation.SuppressLint
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.widget.Switch
import okio.BufferedSink
import okio.appendingSink
import okio.buffer
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.sin

/**
 * author: zxd
 * created on: 2022/4/7 15:19
 * description:
 */
class OkioLogInterceptor private constructor(private var dir : String): LogInterceptor{

    private val handlerThread  = HandlerThread("log_to_file_thread")
    private val mHandler : Handler
    private var startTime = System.currentTimeMillis()
    private var bufferedSink: BufferedSink? = null
    private var logFile = File(getFileName())

    private val callback = Handler.Callback {
        val sink = checkSink()
        when(it.what){
            TYPE_FLUSH -> {
                sink.use {
                    it.flush()
                    bufferedSink = null
                }
            }
            TYPE_LOG ->{
                val log = it.obj as String
                sink.writeUtf8(log)
                sink.writeUtf8("\n")
            }
        }

        if (it.obj as? String == "work done") Log.v(
            "ttaylor1",
            "log() work is ok done=${System.currentTimeMillis() - startTime}"
        )
        false
    }

    init {
        handlerThread.start()
        mHandler = Handler(handlerThread.looper,callback)
    }

    companion object{

        private const val TYPE_FLUSH = -1
        private const val TYPE_LOG = 1
        private const val FLUSH_LOG_DELAY_MILLIS = 3000L

        @Volatile
        private var INSTANCE : OkioLogInterceptor? = null

        fun getInstance(dir: String): OkioLogInterceptor =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: OkioLogInterceptor(dir).apply { INSTANCE = this }
            }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getToday():String =
        SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)

    private fun getFileName() = "$dir${File.separator}${getToday()}.log"

    override fun log(priority: Int, tag: String, log: String, chain: Chain) {
        if (!handlerThread.isAlive) handlerThread.start()
        mHandler.run {
            removeMessages(TYPE_FLUSH)
            obtainMessage(TYPE_LOG,"[$tag] $log").sendToTarget()
            val flushMessage = mHandler.obtainMessage(TYPE_FLUSH)
            sendMessageDelayed(flushMessage, FLUSH_LOG_DELAY_MILLIS)
        }
        chain.process(priority, tag, log)
    }

    override fun enable(): Boolean {
        return true
    }

    private fun checkSink(): BufferedSink {
        if (bufferedSink == null) {
            bufferedSink = logFile.appendingSink().buffer()
        }
        return bufferedSink!!
    }
}