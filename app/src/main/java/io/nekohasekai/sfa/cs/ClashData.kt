package io.nekohasekai.sfa.cs

import okhttp3.Headers
import org.yaml.snakeyaml.Yaml

data class ClashData(
    val subscriptionUserinfo: SubscriptionUserinfo? = null,
    /** 节点分组 */
    val clashContent: Map<String, *> = emptyMap<String, Any>(),
) {
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

        fun create(
            headers: Headers,
            content: String
        ): ClashData {
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
            val profileWebPageUrl: String = headers[PROFILE_WEB_PAGE_URL] ?: ""
            val contentDisposition: String = headers[CONTENT_DISPOSITION]?.let { disposition ->
                disposition.split("''", ignoreCase = true).takeIf { it.size == 2 }?.get(1)
            } ?: ""
            return  ClashData(
                subscriptionUserinfo = SubscriptionUserinfo(
                    usedBytes = usedBytes,
                    totalBytes = totalBytes,
                    expireTimestamp = expireTimestamp,
                    spUrl = profileWebPageUrl,
                    spDisposition = contentDisposition
                ),
                clashContent = Yaml().load(content)
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
}