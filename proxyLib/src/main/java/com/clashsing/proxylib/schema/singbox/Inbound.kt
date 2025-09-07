package com.clashsing.proxylib.schema.singbox

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Inbound(

    /** @see: [Type] */
    val type: String,
    val tag: String? =  null,
    val address: List<String>? = null,
    val mtu: Long? = null,

    /**
     * TCP/IP 栈
     * 默认使用 mixed 栈如果 gVisor 构建标记已启用，否则默认使用 system 栈
     */
    val stack: String? = null,

    @SerialName("auto_route")
    val autoRoute: Boolean? = null,

    @SerialName("strict_route")
    val strictRoute: Boolean? = null,

    val listen: String? = null,

    @SerialName("listen_port")
    val listenPort: Long? = null,

    /**
     * Deprecated, @see https://sing-box.sagernet.org/zh/migration/#_3
     */
    @Deprecated("入站选项已被弃用，且可以被规则动作替代。")
    val sniff: Boolean? = null,

    /**
     * Deprecated, @see: https://sing-box.sagernet.org/zh/configuration/shared/listen/#sniff_override_destination
     * 在 Inbound.Tun (https://sing-box.sagernet.org/zh/configuration/inbound/tun/) 中无说明
     */
    @Deprecated("仅在【监听字段】中有说明，且已经在 v1.11.0 版本中废弃。")
    @SerialName("sniff_override_destination")
    val sniffOverrideDestination: Boolean? = null,
    val platform: Platform? = null,

    @SerialName("domain_strategy")
    val domainStrategy: String? = null,
    @SerialName("endpoint_independent_nat")
    val endpointIndependentNat: Boolean? = null,
    @SerialName("inet4_address")
    val inet4Address: String? = null,
    @SerialName("inet6_address")
    val inet6Address: String? = null,
    val users: List<String>? = null,



    ) {
    companion object {
        fun createTun(
            tag: String = "tun",
            address: List<String> = listOf("172.19.0.1/30", "fdfe:dcba:9876::1/126"),
            mtu: Long = 9000,
            stack: String = Stack.SYSTEM,
            autoRoute: Boolean = true,
            strictRoute: Boolean = true,
            sniff: Boolean = true,
            sniffOverrideDestination: Boolean = true,
            platform: Platform = Platform()
        ) : Inbound {
            return Inbound(
                type = Type.TUN,
                tag = tag,
                address = address,
                mtu = mtu,
                stack = stack,
                autoRoute = autoRoute,
                strictRoute = strictRoute,
                sniff = sniff,
                sniffOverrideDestination = sniffOverrideDestination,
                platform = platform
            )
        }
        fun createMixed(
            tag: String = "mixed",
            listen: String = "127.0.0.1",
            listenPort: Long = 8890,
            sniff: Boolean = true,
        ) : Inbound {
            return Inbound(
                type = Type.MIXED,
                tag = tag,
                listen = listen,
                listenPort = listenPort,
                sniff = sniff
            )
        }
    }
    object Type {
        const val DIRECT = "direct"
        const val MIXED = "mixed"
        const val SOCKS = "socks"
        const val HTTP = "http"
        const val SHADOWSOCKS = "shadowsocks"
        const val VMESS = "vmess"
        const val TROJAN = "trojan"
        const val NATIVE = "naive"
        const val HYSTERIA = "hysteria"
        const val SHADOWTLS = "shadowtls"
        const val TUIC = "tuic"
        const val HYSTERIA2 = "hysteria2"
        const val VLESS = "vless"
        const val ANYTLS = "anytls"
        const val TUN = "tun"
        const val REDIRECT = "redirect"
        const val TPROXY = "tproxy"
    }
    object Stack {
        const val SYSTEM = "system"
        const val GVISOR = "gvisor"
        const val MIXED = "mixed"
    }

    @Serializable
    data class Platform(
        @SerialName("http_proxy")
        var httpProxy: HttpProxy = HttpProxy(),
    ) {
        @Serializable
        data class HttpProxy(
            var enabled: Boolean = false,
            var server: String = "127.0.0.1",
            @SerialName("server_port")
            var serverPort: Long = 8890
        )
    }

}