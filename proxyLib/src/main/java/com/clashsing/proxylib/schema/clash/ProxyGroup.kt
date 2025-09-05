package com.clashsing.proxylib.schema.clash

import kotlinx.serialization.Serializable

@Serializable
data class ProxyGroup(
    val name: String,
    /** @see Type */
    val type: String,
    val proxies: List<String>,
    val url: String? = null,
    val interval: Long? = null
) {
    object Type {
        const val SELECT = "select"
        const val FALLBACK = "fallback"
        const val URL_TEST = "url-test"
    }
}
