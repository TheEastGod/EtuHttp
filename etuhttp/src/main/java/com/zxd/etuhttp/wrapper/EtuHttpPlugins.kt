package com.zxd.etuhttp.wrapper

import com.zxd.etuhttp.wrapper.callback.Function
import com.zxd.etuhttp.wrapper.param.Param

/**
 * author: zxd
 * created on: 2021/2/4 13:58
 * description: 插件类
 */
class EtuHttpPlugins {


    companion object{

        private var mOnParamAssembly : Function<in Param<*> , out Param<*>>? = null


        //设置公共参数装饰
        fun setOnParamAssembly(onParamAssembly: Function<in Param<*>, out Param<*>>) {
            mOnParamAssembly = onParamAssembly
        }

        /**
         * <P>对Param参数添加一层装饰,可以在该层做一些与业务相关工作，
         * <P>例如：添加公共参数/请求头信息
         *
         * @param source Param
         * @return 装饰后的参数
         */
        fun onParamAssembly(source: Param<*>) : Param<*>{
            if (!source.getAssemblyEnabled()) return source

            val f = mOnParamAssembly
            if (f != null){
                val p = apply(f,source)
            }

            return source
        }


        private fun <T, R> apply(f: Function<T, R>, t: T): R {
            return try {
                f.apply(t)
            } catch (ex: Throwable) {
                throw wrapOrThrow(ex)
            }
        }
    }


}

