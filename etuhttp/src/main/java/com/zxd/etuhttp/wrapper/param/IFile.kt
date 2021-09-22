package com.zxd.etuhttp.wrapper.param

import androidx.annotation.NonNull
import com.zxd.etuhttp.wrapper.entity.UpFile
import java.io.File
import java.util.ArrayList

/**
 * author: zxd
 * created on: 2021/1/29 15:46
 * description:
 */
interface IFile<P : Param<P>> {

    fun addFile(key: String, file: File) :P{
        return addFile(UpFile(key, file))
    }

    fun addFile(key: String, filePath: String): P{
        return addFile(UpFile(key, filePath))
    }

    fun addFile(key: String, fileName: String, filePath: String):P{
        return addFile(UpFile(key, fileName, filePath))
    }

    fun addFile(key: String, filename: String, file: File): P {
        return addFile(UpFile(key, filename, file))
    }

    fun <T> addFiles(key: String, list: List<T>) :P{
        for (src in list){
            when (src) {
                is String -> {
                    addFile(UpFile(key, src.toString()))
                }
                is File -> {
                    addFile(UpFile(key, src))
                }
                else -> {
                    throw IllegalArgumentException("Incoming data type exception, it must be String or File")
                }
            }
        }
        return this as P
    }

    fun <T> addFiles(fileMap: Map<String, T>): P{
        for ((key, value) in fileMap) {
            when (value) {
                is String -> {
                    addFile(UpFile(key, value.toString()))
                }
                is File -> {
                    addFile(UpFile(key, (value as File)))
                }
                else -> {
                    throw java.lang.IllegalArgumentException("Incoming data type exception, it must be String or File")
                }
            }
        }
        return this as P
    }

    fun addFiles(upFileList: MutableList<out UpFile>): P {
        for (upFile in upFileList) {
            addFile(upFile)
        }
        return this as P
    }


    /**
     *
     * 添加文件对象
     *
     * @param upFile UpFile
     * @return Param
     */
    fun addFile(@NonNull upFile: UpFile): P
}