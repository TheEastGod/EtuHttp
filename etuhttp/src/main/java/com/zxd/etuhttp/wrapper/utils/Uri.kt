@file:JvmName("UriUtil")

package com.zxd.etuhttp.wrapper.utils

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.zxd.etuhttp.wrapper.entity.UriRequestBody
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileNotFoundException

@JvmOverloads
fun Uri.asRequestBody(
    context: Context,
    contentType: MediaType? = null
): RequestBody = UriRequestBody(context, this, contentType)

@JvmOverloads
fun Uri.asPart(
    context: Context,
    key: String,
    filename: String? = null,
    contentType: MediaType? = null
): MultipartBody.Part {
    val newFilename = filename ?: displayName(context)
    return MultipartBody.Part.createFormData(key, newFilename, asRequestBody(context, contentType))
}

fun Uri?.length(context: Context): Long {
    return length(context.contentResolver)
}

//return The size of the media item, return -1 if does not exist, might block.
fun Uri?.length(contentResolver: ContentResolver): Long {
    if (this == null) return -1L
    if (scheme == ContentResolver.SCHEME_FILE) {
        return File(path).length()
    }
    return try {
        contentResolver.openFileDescriptor(this, "r")?.statSize ?: -1L
    } catch (e: FileNotFoundException) {
        -1L
    }
}

internal fun Uri.displayName(context: Context): String? {
    if (scheme == ContentResolver.SCHEME_FILE) {
        return lastPathSegment
    }
    return getColumnValue(context.contentResolver, MediaStore.MediaColumns.DISPLAY_NAME)
}

//Return the value of the specified column，return null if does not exist
internal fun Uri.getColumnValue(contentResolver: ContentResolver, columnName: String): String? {
    return contentResolver.query(this, arrayOf(columnName),
        null, null, null)?.use {
        if (it.moveToFirst()) it.getString(0) else null
    }
}

//find the Uri by filename and relativePath, return null if find fail.  RequiresApi 29
fun Uri.query(context: Context, filename: String?, relativePath: String?): Uri? {
    if (filename.isNullOrEmpty() || relativePath.isNullOrEmpty()) return null
    val realRelativePath = relativePath.let {
        //Remove the prefix slash if it exists
        if (it.startsWith("/")) it.substring(1) else it
    }.let {
        //Suffix adds a slash if it does not exist
        if (it.endsWith("/")) it else "$it/"
    }
    val columnNames = arrayOf(
        MediaStore.MediaColumns._ID
    )
    return context.contentResolver.query(this, columnNames,
        "relative_path=? AND _display_name=?", arrayOf(realRelativePath, filename), null)?.use {
        if (it.moveToFirst()) {
            val uriId = it.getLong(0)
            ContentUris.withAppendedId(this, uriId)
        } else null
    }
}