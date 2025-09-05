package com.clashsing.proxylib.schema.clash

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Clash(
    val proxies: List<Proxy>,

    @SerialName("proxy-groups")
    val proxyGroups: List<ProxyGroup>
)
