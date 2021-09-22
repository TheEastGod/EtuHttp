package com.zxd.httpdemo

import java.util.concurrent.*

/**
 * author: zxd
 * created on: 2020/12/18 11:00
 * description:
 */
class ThreadPoolManager private constructor(){

    //根据cup核心数设置线程池数量
    private val corePoolSize = Runtime.getRuntime().availableProcessors()
    //最大线程池数量
    private val  maximumPoolSize = corePoolSize * 2 + 1
    //等待线程的存活时间
    private val keepAliveTime: Long = 10
    //等待线程存活时间的单位
    private val unit: TimeUnit = TimeUnit.SECONDS
    private val workQueue: BlockingQueue<Runnable> = LinkedBlockingQueue()
    private val threadFactory: ThreadFactory = Executors.defaultThreadFactory()
    /**
     * 取消策略，当超过等待线程池的数量后禁止添加了
     */
    private val handler: RejectedExecutionHandler = ThreadPoolExecutor.AbortPolicy()

    private val executor : ThreadPoolExecutor = ThreadPoolExecutor(
            corePoolSize,
            maximumPoolSize,
            keepAliveTime, unit,
            workQueue,
            threadFactory,
            handler)

    companion object {
        val instance = ThreadPoolManagerHolder.holder
    }


    private object ThreadPoolManagerHolder{
        val holder = ThreadPoolManager()
    }


    fun startThread(runnable: Runnable){
        executor.execute(runnable)
    }


    fun cancelThread(runnable: Runnable){
        executor.remove(runnable)
    }

}