package io.nekohasekai.sfa.cs

import org.json.JSONArray
import org.json.JSONObject

class Clash2SingBox {
    private val clashData: ClashData
    private val singBox: JSONObject
    private val singBoxGroups = mutableListOf<JSONObject>()

    constructor(clash: ClashData, singBoxContent: String) {
        clashData = clash
        singBox = JSONObject(singBoxContent)
        val allOutbounds = (singBox.optJSONArray("outbounds") ?: JSONArray()).run {
            val list = mutableListOf<JSONObject>()
            for (i in (length() - 1) downTo 0) {
                if (this[i] is JSONObject) {
                    val jsonObject = this[i] as JSONObject
                    if (jsonObject.optJSONArray("outbounds") == null) {
                        list.add(0, jsonObject)
                    }
                }
            }
            list
        }
//        allOutbounds.forEach {
//            if (it.optJSONArray("outbounds") != null) {
//                allOutbounds.remove(it)
//            }
//        }
        @Suppress("UNCHECKED_CAST")
        val clashGroups = clashData.clashContent["proxy-groups"] as? List<Map<String, Any?>>
        clashGroups?.forEach { mapGroup ->
            ClashProxyGroup.create(mapGroup)?.let { clashProxyGroup ->
                val delIndex = clashProxyGroup.proxies.indexOf("\uD83D\uDD2F故障转移")
                if (delIndex != -1) {
                    clashProxyGroup.proxies.removeAt(delIndex)
                }
                val singBoxGroup = JSONObject()
                singBoxGroup.put("tag", clashProxyGroup.name)
                clashProxyGroup.proxies.let { proxies ->
                    val jsonArray = JSONArray()
                    proxies.forEach {
                        jsonArray.put(it)
                    }
                    singBoxGroup.put("outbounds", jsonArray)
                }
                if (clashProxyGroup.type == ClashMetaOutboundType.SELECT) {
                    singBoxGroup.put("type", SingBoxOutboundType.SELECTOR)
                    singBoxGroups.add(singBoxGroup)
                } else if (clashProxyGroup.type == ClashMetaOutboundType.URL_TEST) {
                    singBoxGroup.put("type", SingBoxOutboundType.URLTEST)
                    singBoxGroup.put("url", "https://www.gstatic.com/generate_204")
//                    singBoxGroup.put("url", "http:\\/\\/1.1.1.1")
                    singBoxGroup.put("interval", "3m")
                    singBoxGroup.put("tolerance", 50)
                    singBoxGroups.add(singBoxGroup)
                }
            }
        }
        println(singBoxGroups)
        allOutbounds.addAll(0, singBoxGroups)
        println(allOutbounds)
        singBox.remove("outbounds")
        allOutbounds.let { outbounds ->
            val jsonArray = JSONArray()
            outbounds.forEach {
                jsonArray.put(it)
            }
            singBox.put("outbounds", jsonArray)
        }
        singBox.optJSONObject("route")?.put("final", "")
        println(singBox)
    }

    fun getFixedSingBox(): JSONObject {
        return  singBox
    }
}