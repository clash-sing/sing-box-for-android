package com.clashsing.proxylib.schema.singbox

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Dns(
    val servers: List<DnsServer>,
    val rules: List<DnsRule>,
    @SerialName("independent_cache")
    var independentCache: Boolean = false,
    /** @see [DnsServer.Tag] */
    val final: String? = null,
    val strategy: String? = null
) {
    object Strategy {
        const val PREFER_IPV4 = "prefer_ipv4"
        const val PREFER_IPV6 = "prefer_ipv6"
        const val IPV4_ONLY = "ipv4_only"
        const val IPV6_ONLY = "ipv6_only"
    }
}
