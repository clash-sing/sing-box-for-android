package com.clashsing.proxylib.schema.singbox.dns

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Dns(
    val servers: List<DnsServerType> = emptyList(),
    val rules: List<DnsRule> = emptyList(),
    @SerialName("independent_cache")
    val independentCache: Boolean = false,
    val final: String = DnsServerType.HTTPS,
    val strategy: String = PREFER_IPV6
) : Parcelable {
    companion object {
        const val PREFER_IPV4 = "prefer_ipv4"
        const val PREFER_IPV6 = "prefer_ipv6"
        const val IPV4_ONLY = "ipv4_only"
        const val IPV6_ONLY = "ipv6_only"
    }
}
