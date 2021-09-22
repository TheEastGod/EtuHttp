package com.zxd.etuhttp.wrapper.param

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.zxd.etuhttp.wrapper.utils.toAny
import com.zxd.etuhttp.wrapper.utils.toMap
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.ByteString.Companion.toByteString

/**
 * author: zxd
 * created on: 2021/4/6 14:17
 * description:
 */
class JsonArrayParam(url: String, method: Method) : AbstractBodyParam<JsonArrayParam>(url, method) {

    private var mBodyParam: MutableList<Any>? = null

    override fun add(key: String, value: Any?): JsonArrayParam {
        val map = HashMap<String,Any?>()
        map[key] = value
        return add(map)
    }

    fun add(any: Any): JsonArrayParam{
        initList()
        mBodyParam!!.add(any)
        return this
    }

    override fun getRequestBody(): RequestBody {
        val jsonArray = mBodyParam ?: return ByteArray(0).toByteString().toRequestBody(null)

        return convert(jsonArray)
    }

    private fun initList(){
        if (mBodyParam == null) mBodyParam = ArrayList()
    }

    fun addAll(jsonElement: String): JsonArrayParam? {
        val element = JsonParser.parseString(jsonElement)
        if (element.isJsonArray) {
            return addAll(element.asJsonArray)
        } else if (element.isJsonObject) {
            return addAll(element.asJsonObject)
        }
        return element.toAny()?.let { add(it) }
    }

    fun addAll(jsonObject: JsonObject): JsonArrayParam? {
        return addAll(jsonObject.toMap())
    }

    override fun addAll(map: Map<String, *>): JsonArrayParam {
        initList()
        return super.addAll(map)
    }

    fun addAll(jsonArray: JsonArray): JsonArrayParam {
        return addAll(jsonArray.toList())
    }

    fun addAll(list: List<*>): JsonArrayParam {
        initList()
        for (any in list) {
            if (any != null) {
                add(any)
            }
        }
        return this
    }

    fun addJsonElement(jsonElement: String): JsonArrayParam {
        val element = JsonParser.parseString(jsonElement)
        return add(element.toAny()!!)
    }

    fun addJsonElement(key: String, jsonElement: String): JsonArrayParam {
        val element = JsonParser.parseString(jsonElement)
        return add(key, element.toAny()!!)
    }

    fun getBodyParam():List<Any>{
        return mBodyParam!!
    }

}