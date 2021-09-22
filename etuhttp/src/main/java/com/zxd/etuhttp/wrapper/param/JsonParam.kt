package com.zxd.etuhttp.wrapper.param

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.zxd.etuhttp.wrapper.utils.toAny
import com.zxd.etuhttp.wrapper.utils.toMap
import okhttp3.RequestBody
import kotlin.collections.set

/**
 * author: zxd
 * created on: 2021/4/1 11:06
 * description:
 */
class JsonParam(url: String, method: Method) : AbstractBodyParam<JsonParam>(url, method) {

    var mBodyParam : HashMap<String, Any?>? = null

    override fun add(key: String, value: Any?): JsonParam {
        initMap()
        mBodyParam!![key] = value
        return this
    }

    fun addAll(jsonObject: String): JsonParam{
        return addAll(JsonParser.parseString(jsonObject).asJsonObject)
    }

    fun addAll(jsonObject: JsonObject): JsonParam {
        return addAll(jsonObject.toMap())
    }

    override fun addAll(map: Map<String, *>): JsonParam {
        initMap()
        return super.addAll(map)
    }

    fun addJsonElement(key: String, jsonElement: String): JsonParam {
        val element = JsonParser.parseString(jsonElement)
        return add(key, element.toAny())
    }

    override fun getRequestBody(): RequestBody {
        val bodyParam: Map<String, Any?> =
           mBodyParam ?: return RequestBody.create(null, ByteArray(0))
        return convert(bodyParam)
    }

    private fun initMap(){
        if (mBodyParam == null) mBodyParam = LinkedHashMap()
    }
}