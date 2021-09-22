package com.zxd.etuhttp.wrapper.callback

import com.zxd.etuhttp.wrapper.entity.Progress

/**
 * author: zxd
 * created on: 2021/2/23 11:19
 * description:
 */
interface ProgressCallback {

    fun onProgress(progress: Progress)

}