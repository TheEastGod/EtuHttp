package com.zxd.etuhttp.wrapper.param

import com.zxd.etuhttp.wrapper.entity.UpFile
import com.zxd.etuhttp.wrapper.utils.BuildUtil
import okhttp3.Headers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * author: zxd
 * created on: 2021/3/31 15:19
 * description:
 */
interface IPart<P : Param<P>> :IFile<P>{

    fun addPart(part: MultipartBody.Part): P

    fun addPart(body: RequestBody): P {
        return addPart(MultipartBody.Part.create(body))
    }

    fun addPart(contentType: MediaType, content: ByteArray): P {
        return addPart(RequestBody.create(contentType, content))
    }

    fun addPart(
        contentType: MediaType, content: ByteArray, offset: Int, byteCount: Int
    ): P {
        return addPart(RequestBody.create(contentType, content, offset, byteCount))
    }

    fun addPart(headers: Headers, body: RequestBody): P {
        return addPart(MultipartBody.Part.create(headers, body))
    }

    fun addFormDataPart(
        name: String,
        fileName: String,
        body: RequestBody
    ): P {
        return addPart(MultipartBody.Part.createFormData(name, fileName, body))
    }

    override fun addFile(upFile: UpFile): P {
        val file: File = upFile.file
        require(file.exists()) { "File '" + file.absolutePath + "' does not exist" }
        require(file.isFile) { "File '" + file.absolutePath + "' is not a file" }

        val requestBody: RequestBody = RequestBody.create(
            BuildUtil.getMediaType(upFile.getFilename()),
            file
        )
        return addPart(
            MultipartBody.Part.createFormData(
                upFile.key,
                upFile.getFilename(),
                requestBody
            )
        )
    }

}