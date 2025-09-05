package com.clashsing.proxylib.schema.clash

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Proxy(
    val name: String,
    val type: String,
    val server: String,
    val port: Long,
    val ports: String? = null,
    val password: String? = null,

    @SerialName("skip-cert-verify")
    val skipCertVerify: Boolean,

    @SerialName("client-fingerprint")
    val clientFingerprint: String? = null,

    val udp: Boolean? = null,
    val tfo: Boolean? = null,
    val sni: String? = null,
    val network: String? = null,
    val up: Long? = null,
    val down: Long? = null,

    @SerialName("auth_str")
    val authStr: String? = null,

    val alpn: List<String>? = null,
    val protocol: String? = null,

    @SerialName("fast-open")
    val fastOpen: Boolean? = null,

    @SerialName("disable_mtu_discovery")
    val disableMtuDiscovery: Boolean? = null
)
