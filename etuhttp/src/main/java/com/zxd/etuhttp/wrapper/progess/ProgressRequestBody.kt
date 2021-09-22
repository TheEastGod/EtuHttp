package com.zxd.etuhttp.wrapper.progess

import com.zxd.etuhttp.wrapper.callback.ProgressCallback
import com.zxd.etuhttp.wrapper.entity.Progress
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*
import java.io.IOException

/**
 * author: zxd
 * created on: 2021/2/23 15:36
 * description:
 */
class ProgressRequestBody constructor(requestBody: RequestBody,callback: ProgressCallback): RequestBody() {

    //实际的待包装请求体
    private val mRequestBody: RequestBody = requestBody

    //进度回调接口
    private val mCallback: ProgressCallback = callback

    //包装完成的BufferedSink
    private var bufferedSink: BufferedSink? = null

    fun getRequestBody() :RequestBody{
        return mRequestBody
    }


    override fun contentType(): MediaType? {
      return mRequestBody.contentType()
    }

    override fun contentLength(): Long {
        return mRequestBody.contentLength()
    }

    override fun writeTo(sink: BufferedSink) {

        //此行代码为兼容添加 HttpLoggingInterceptor 拦截器后，上传进度超过100%，达到200%问题
        if (sink is Buffer) return
        if (bufferedSink == null) {
            //包装
            bufferedSink = sink(sink)!!.buffer()
        }
        //写入
        mRequestBody.writeTo(bufferedSink!!)
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink!!.flush()

    }


    /**
     * 写入，回调进度接口
     *
     * @param sink Sink
     * @return Sink
     */
    private fun sink(sink: Sink): Sink? {
        return object : ForwardingSink(sink) {
            //当前写入字节数
            var bytesWritten = 0L

            //总字节长度，避免多次调用contentLength()方法
            var contentLength = 0L
            var lastProgress //上次回调进度
                    = 0

            @Throws(IOException::class)
            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                if (contentLength == 0L) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength()
                }
                //增加当前写入的字节数
                bytesWritten += byteCount
                val currentProgress = (bytesWritten * 100 / contentLength).toInt()
                if (currentProgress <= lastProgress) return  //进度较上次没有更新，直接返回
                lastProgress = currentProgress
                //回调, 更新进度
                updateProgress(lastProgress, bytesWritten, contentLength)
            }
        }
    }

    private fun updateProgress(
        progress: Int,
        currentSize: Long,
        totalSize: Long
    ) {
        if (mCallback == null) return
        val p = Progress(progress, currentSize, totalSize)
        mCallback.onProgress(p)
    }
}