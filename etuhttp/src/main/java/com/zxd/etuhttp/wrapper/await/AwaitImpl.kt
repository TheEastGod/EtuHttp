package com.zxd.etuhttp.wrapper.await

import com.zxd.etuhttp.wrapper.IAwait
import com.zxd.etuhttp.wrapper.IEtuHttp
import com.zxd.etuhttp.wrapper.parse.Parser
import com.zxd.etuhttp.wrapper.parse.SuspendParser
import com.zxd.etuhttp.wrapper.utils.await

/**
 * author: zxd
 * created on: 2021/6/24 14:47
 * description:
 */
internal class AwaitImpl<T> (
    private val iEtuHttp: IEtuHttp,
    private val parser: Parser<T>) : IAwait<T>{


    override suspend fun await(): T {
        val call = iEtuHttp.newCall()
        return try {
            if (parser is SuspendParser){
                parser.onSuspendParse(call.await())
            }else{
                call.await(parser)
            }

        }catch (t: Throwable){

            throw t
        }
    }

}