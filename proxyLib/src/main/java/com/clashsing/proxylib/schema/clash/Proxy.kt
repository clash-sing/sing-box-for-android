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
    val skipCertVerify: Boolean = true,

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
    val disableMtuDiscovery: Boolean? = null,

    /** vless uuid */
    val uuid: String? = null,
    val alterId: Long? = null,
    val flow: String? = null,
    val tls: Boolean? = null,
    @SerialName("servername")
    val serverName: String? = null,

    /** shadowsocks/vless 加密方法 */
    val cipher: String? = null,
    @SerialName("reality-opts")
    val realityOpts: RealityOpts? = null
) {
    object Type {
        const val HYSTERIA2 = "hysteria2"
        const val HYSTERIA = "hysteria"
        const val ANYTLS = "anytls"
        const val TROJAN = "trojan"
        const val VLESS = "vless"
        const val SHADOWSOCKS = "ss"
    }
    @Serializable
    data class RealityOpts(
        @SerialName("public-key")
        val publicKey: String,
        @SerialName("short-id")
        val shortId: String? = null,
    )
}
