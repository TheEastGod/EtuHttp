package com.zxd.etuhttp.wrapper.entity

import java.io.File

/**
 * author: zxd
 * created on: 2021/3/31 14:31
 * description:
 */
class UpFile( val key: String,
              private var filename: String?,
              val file: File
) {

    constructor(key: String, path: String) : this(key, null, File(path))
    constructor(key: String, file: File) : this(key, null, file)
    constructor(key: String, filename: String?, path: String) : this(key, filename, File(path))

    @Deprecated("", ReplaceWith("setFileName(fileName)"))
    fun setValue(filename: String) {
        setFilename(filename)
    }

    @Deprecated("", ReplaceWith("getFilename()"))
    fun getValue(): String {
        return getFilename()
    }

    fun setFilename(fileName: String) {
        this.filename = fileName
    }

    fun getFilename(): String {
        return filename ?: file.name
    }
}