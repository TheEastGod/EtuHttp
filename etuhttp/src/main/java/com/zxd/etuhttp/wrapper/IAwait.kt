package com.zxd.etuhttp.wrapper

/**
 * author: zxd
 * created on: 2021/6/24 14:46
 * description:
 */
interface IAwait<T>{
    suspend fun await(): T
}

inline fun <T, R> IAwait<T>.newAwait(
    crossinline block: suspend IAwait<T>.() -> R
): IAwait<R> = object : IAwait<R> {

    override suspend fun await(): R {
        return this@newAwait.block()
    }
}

/**
 * Returns a IAwait containing the results of applying the given [map] function
 */
inline fun <T, R> IAwait<T>.map(
    crossinline map: suspend (T) -> R
): IAwait<R> = newAwait {
    map(await())
}