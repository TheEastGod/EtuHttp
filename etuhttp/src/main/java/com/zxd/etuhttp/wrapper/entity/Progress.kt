package com.zxd.etuhttp.wrapper.entity

/**
 * author: zxd
 * created on: 2021/2/23 14:09
 * description:
 */
data class Progress(var progress: Int,var currentSize: Long,var totalSize: Long) {


    override fun toString(): String {
        return "Progress(progress=$progress, currentSize=$currentSize, totalSize=$totalSize)"
    }
}