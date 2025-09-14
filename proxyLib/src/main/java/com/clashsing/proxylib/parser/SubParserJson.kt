package com.clashsing.proxylib.parser

import android.net.Uri
import com.clashsing.proxylib.SubUserinfo
import com.clashsing.proxylib.parser.SubParserYaml.Companion.CONTENT_DISPOSITION
import com.clashsing.proxylib.parser.SubParserYaml.Companion.PROFILE_WEB_PAGE_URL
import com.clashsing.proxylib.parser.SubParserYaml.Companion.SUBSCRIPTION_USERINFO
import com.clashsing.proxylib.schema.SingBox
import okhttp3.Headers

class SubParserJson(originSingBox: SingBox?, srcContent: String, headers: Headers) : SubParser(originSingBox, srcContent, headers) {
    override suspend fun getSingBox(): SingBox? {
        this._singBox = originSingBox
        return singBox
    }

    override fun getSubUserInfo(): SubUserinfo? {
        var usedBytes: Long? = null
        var totalBytes: Long? = null
        var expireTimestamp: Long? = null
        headers[SUBSCRIPTION_USERINFO]?.let { subscription ->
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
        val profileWebPageUrl: String? = headers[PROFILE_WEB_PAGE_URL]
        val contentDisposition: String? = headers[CONTENT_DISPOSITION]?.let { disposition ->
            disposition.split("''", ignoreCase = true).takeIf { it.size == 2 }?.get(1)
        }
        if (usedBytes == null && totalBytes == null && expireTimestamp == null
            && profileWebPageUrl.isNullOrBlank() && contentDisposition.isNullOrBlank()) return null
        return SubUserinfo(
            usedBytes = usedBytes,
            totalBytes = totalBytes,
            expireTimestamp = expireTimestamp,
            spUrl = profileWebPageUrl,
            spDisposition = if (contentDisposition.isNullOrBlank()) null else Uri.decode(contentDisposition)
        )
    }
    private fun getSubscriptionValue(key: String, text: String): Long? {
        return text.let { value ->
            value.takeIf {
                it.contains(key, true)
            }?.replace(key, "", true)?.trim()?.toLongOrNull()
        }
    }

}