package io.nekohasekai.sfa.cs.parser

import com.google.gson.Gson
import io.nekohasekai.sfa.cs.ClashMetaOutboundType
import io.nekohasekai.sfa.cs.ClashProxyGroup
import io.nekohasekai.sfa.cs.SingBoxOutboundType
import io.nekohasekai.sfa.cs.SubscriptionUserinfo

class DefaultSubscriptionParserImpl(mapSingBox: Map<String, Any?>, mapClashSing: Map<String, Any?>) :
    SubscriptionParser(mapSingBox, mapClashSing) {
    override fun getFixedContent(): String {
        @Suppress("UNCHECKED_CAST")
        val allOutbounds = mapSingBox["outbounds"] as? MutableList<Map<String, Any?>> ?: mutableListOf()
        for (i in (allOutbounds.size - 1) downTo 0) {
            if (allOutbounds[i]["outbounds"] != null) {
                allOutbounds.removeAt(i)
            }
        }

        val singBoxGroups = mutableListOf<Map<String, Any?>>()
        @Suppress("UNCHECKED_CAST")
        val clashGroups = mapClashSing["proxy-groups"] as? List<Map<String, Any?>>
        clashGroups?.forEach { mapGroup ->
            ClashProxyGroup.create(mapGroup)?.let { clashProxyGroup ->
                val delIndex = clashProxyGroup.proxies.indexOf("\uD83D\uDD2F故障转移")
                if (delIndex != -1) {
                    clashProxyGroup.proxies.removeAt(delIndex)
                }
                val singBoxGroup = mutableMapOf<String, Any?>()
                singBoxGroup.put("tag", clashProxyGroup.name)
                singBoxGroup.put("outbounds", clashProxyGroup.proxies)
                if (clashProxyGroup.type == ClashMetaOutboundType.SELECT) {
                    singBoxGroup.put("type", SingBoxOutboundType.SELECTOR)
                    singBoxGroups.add(singBoxGroup)
                } else if (clashProxyGroup.type == ClashMetaOutboundType.URL_TEST) {
                    singBoxGroup.put("type", SingBoxOutboundType.URLTEST)
                    singBoxGroup.put("url", "https://www.gstatic.com/generate_204")
                    // singBoxGroup.put("url", "http:\\/\\/1.1.1.1")
                    singBoxGroup.put("interval", "3m")
                    singBoxGroup.put("tolerance", 50)
                    singBoxGroups.add(singBoxGroup)
                }
            }
        }

        println(singBoxGroups)
        allOutbounds.addAll(0, singBoxGroups)
        println(allOutbounds)
        val newMap = mapSingBox.toMutableMap().apply {
            remove("outbounds")
            put("outbounds", allOutbounds)
        }
        @Suppress("UNCHECKED_CAST")
        (newMap["route"] as? MutableMap<String, Any?>)?.let { route ->
            route["final"] = ""
        }
        return Gson().toJson(newMap)
    }

    override fun getSubscriptionUserinfo(): SubscriptionUserinfo? {
        TODO("Not yet implemented")
    }


}