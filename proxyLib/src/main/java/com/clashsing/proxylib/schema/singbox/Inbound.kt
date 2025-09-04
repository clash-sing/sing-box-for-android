package com.clashsing.proxylib.schema.singbox

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Inbound(

    /** @see: [Type] */
    val type: String,
    val tag: String,
    val address: List<String>? = null,
    val mtu: Int? = null,

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
    val listenPort: Int? = null,

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
    val platform: Platform? = null

) : Parcelable {
    companion object {
        fun createTun(
            tag: String = "tun",
            address: List<String> = listOf("172.19.0.1/30", "fdfe:dcba:9876::1/126"),
            mtu: Int = 9000,
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
            listenPort: Int = 8890,
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
    @Parcelize
    @Serializable
    data class Platform(
        @SerialName("http_proxy")
        val httpProxy: HttpProxy = HttpProxy(),
    ) : Parcelable {
        @Parcelize
        @Serializable
        data class HttpProxy(
            val enabled: Boolean = false,
            val server: String = "127.0.0.1",
            @SerialName("server_port")
            val serverPort: Int = 8890
        ) : Parcelable
    }

}