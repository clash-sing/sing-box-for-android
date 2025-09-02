package io.nekohasekai.sfa.cs.parser

import com.google.gson.Gson
import io.nekohasekai.sfa.cs.ClashMetaOutboundType
import io.nekohasekai.sfa.cs.ClashProxyGroup
import io.nekohasekai.sfa.cs.ClashSingWrapper
import io.nekohasekai.sfa.cs.SingBoxOutboundType
import io.nekohasekai.sfa.cs.SubscriptionUserinfo
import io.nekohasekai.sfa.cs.SubscriptionUserinfoManager

class DefaultSubscriptionParserImpl(mapSingBox: Map<String, Any?>, mapClashSing: Map<String, Any?>) :
    SubscriptionParser(mapSingBox, mapClashSing) {

    companion object {
        /**
         * 用户的订阅信息
         * @sample: upload=4841978; download=345835144; total=214748364800; expire=1777514961
         */
        const val SUBSCRIPTION_USERINFO = "subscription-userinfo"
        /**
         * 代理服务提供商的网址
         * @sample: https://www.proxyXXX.com
         */
        const val PROFILE_WEB_PAGE_URL = "profile-web-page-url"

        /**
         * 通常用于保存代理服务提供商的名称
         * @sample: attachment;filename*=UTF-8''proxyXXX.com
         */
        const val CONTENT_DISPOSITION = "content-disposition"
    }

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

    override fun setSubscriptionUserinfo(profileId: Long, wrapper: ClashSingWrapper) {
        var usedBytes: Long? = null
        var totalBytes: Long? = null
        var expireTimestamp: Long? = null
        wrapper.headers[SUBSCRIPTION_USERINFO]?.let { subscription ->
            val subscriptionArray = subscription.split(";")
            if (subscriptionArray.size == 4) {
                val uploadBytes: Long? = getSubscriptionValue("upload=", subscriptionArray[0])
                val downloadBytes: Long? = getSubscriptionValue("download=", subscriptionArray[1])
                if (uploadBytes != null || downloadBytes != null) usedBytes = (uploadBytes ?: 0) + (downloadBytes ?: 0)
                totalBytes = getSubscriptionValue("total=", subscriptionArray[2])
                expireTimestamp = getSubscriptionValue("expire=", subscriptionArray[3])
                if (expireTimestamp != null && expireTimestamp.toString().length == 10) {
                    expireTimestamp *= 1000
                }
            }
        }
        val profileWebPageUrl: String = wrapper.headers[PROFILE_WEB_PAGE_URL] ?: ""
        val contentDisposition: String = wrapper.headers[CONTENT_DISPOSITION]?.let { disposition ->
            disposition.split("''", ignoreCase = true).takeIf { it.size == 2 }?.get(1)
        } ?: ""
        val userinfo = SubscriptionUserinfo(
            usedBytes = usedBytes,
            totalBytes = totalBytes,
            expireTimestamp = expireTimestamp,
            spUrl = profileWebPageUrl,
            spDisposition = contentDisposition
        )
        SubscriptionUserinfoManager.setUserinfo(profileId, userinfo)
    }

    private fun getSubscriptionValue(key: String, text: String): Long? {
        return text.let { value ->
            value.takeIf {
                it.contains(key, true)
            }?.replace(key, "", true)?.trim()?.toLongOrNull()
        }
    }
}