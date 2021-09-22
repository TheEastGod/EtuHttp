package com.zxd.etuhttp.wrapper.entity

import androidx.annotation.NonNull

/**
 * author: zxd
 * created on: 2021/2/1 16:56
 * description:
 */
class KeyValuePair @JvmOverloads constructor(
        val key: String,
        val value: Any,
        val isEncoded: Boolean = false
) {
    fun equals(inKey: String): Boolean {
        return inKey == key
    }

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is KeyValuePair) return false
        return other.key == key
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }
}