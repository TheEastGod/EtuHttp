package com.zxd.etuhttp.wrapper.param

import com.zxd.etuhttp.wrapper.entity.KeyValuePair
import com.zxd.etuhttp.wrapper.utils.BuildUtil
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * author: zxd
 * created on: 2021/3/31 15:50
 * description:
 */
class FormParam(url: String, method: Method) : AbstractBodyParam<FormParam>(url, method),IPart<FormParam>{

    private var multiType: MediaType? = null

    private var mBodyParam //Param list
            : MutableList<KeyValuePair>? = null

    private var partList //Part List
            : MutableList<MultipartBody.Part>? = null

    override fun add(key: String, value: Any): FormParam {
       return add(KeyValuePair(key, value))
    }

    fun addEncoded(key: String, value: Any?): FormParam {
        var mValue = value
        if (mValue == null) mValue = ""
        return add(KeyValuePair(key, mValue, true))
    }

    fun addAllEncoded(map: Map<String, *>): FormParam {
        for ((key, value) in map) {
            addEncoded(key, value)
        }
        return this
    }


    fun removeAllBody(key: String): FormParam {
        val bodyParam: MutableList<KeyValuePair> = mBodyParam ?: return this
        val iterator = bodyParam.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (next.equals(key)) iterator.remove()
        }
        return this
    }

    fun removeAllBody(): FormParam {
        val bodyParam: MutableList<KeyValuePair>? = mBodyParam
        bodyParam?.clear()
        return this
    }

    operator fun set(key: String?, value: Any): FormParam {
        removeAllBody(key!!)
        return add(key, value)
    }

    fun setEncoded(key: String, value: Any?): FormParam{
        removeAllBody(key)
        return addEncoded(key, value)
    }

    private fun add(keyValuePair: KeyValuePair): FormParam {
        var bodyParam: MutableList<KeyValuePair>? = mBodyParam
        if (bodyParam == null) {
            bodyParam = ArrayList()
            mBodyParam = bodyParam
        }
        bodyParam.add(keyValuePair)
        return this
    }

    //Set content-type to multipart/form-data
    fun setMultiForm(): FormParam {
        return setMultiType(MultipartBody.FORM)
    }

    //Set content-type to multipart/mixed
    fun setMultiMixed(): FormParam {
        return setMultiType(MultipartBody.MIXED)
    }

    //Set content-type to multipart/alternative
    fun setMultiAlternative(): FormParam {
        return setMultiType(MultipartBody.ALTERNATIVE)
    }

    //Set content-type to multipart/digest
    fun setMultiDigest(): FormParam {
        return setMultiType(MultipartBody.DIGEST)
    }

    //Set content-type to multipart/parallel
    fun setMultiParallel(): FormParam {
        return setMultiType(MultipartBody.PARALLEL)
    }

    //Set the MIME type
    fun setMultiType(multiType: MediaType?): FormParam {
        this.multiType = multiType
        return this
    }

    fun isMultipart(): Boolean {
        return multiType != null
    }


    override fun getRequestBody(): RequestBody? {
        TODO("Not yet implemented")
    }

    override fun addPart(part: MultipartBody.Part): FormParam {
         if (partList.isNullOrEmpty()){
             partList = ArrayList()
             if (!isMultipart()) setMultiForm()
         }
        partList!!.add(part)
        return this
    }

    fun getPartList(): MutableList<MultipartBody.Part>? {
        return partList
    }

    fun getBodyParam(): List<KeyValuePair?>? {
        return mBodyParam
    }

    override fun toString(): String {
        return BuildUtil.getHttpUrl(getSimpleUrl(), mBodyParam!!).toString()
    }
}