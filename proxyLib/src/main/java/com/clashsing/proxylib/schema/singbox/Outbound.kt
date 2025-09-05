package com.clashsing.proxylib.schema.singbox

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Outbound(
    /** @see Type */
    val type: String,

    val tag: String,

    /**
     * 出站群组
     * 仅当 [type] = [Type.SELECTOR] 或 [Type.URLTEST] 时有效
     */
    val outbounds: List<String>? = null,

    /**
     * 测试地址
     * 仅当 [type] = [Type.URLTEST] 时有效
     */
    val url: String? =  null,

    /**
     * 测试间隔。 默认使用 3m
     * 仅当 [type] = [Type.URLTEST] 时有效
     */
    val interval: String? = null,

    /**
     * 以毫秒为单位的测试容差。 默认使用 50
     * 仅当 [type] = [Type.URLTEST] 时有效
     */
    val tolerance: Int? = null,

    val server: String? = null,

    @SerialName("server_port")
    val serverPort: Int? = null,

    /** @sample: ["2080:3000"] */
    @SerialName("server_ports")
    val serverPorts: List<String>? = null,

    val password: String? = null,

    @SerialName("auth_str")
    val authStr: String? = null,

    @SerialName("up_mbps")
    val upMbps: Int? = null,

    @SerialName("down_mbps")
    val downMbps: Int? = null,

    /** 强制为 Linux 和 Windows 以外的系统启用 */
    @SerialName("disable_mtu_discovery")
    val disableMtuDiscovery: Boolean? = null,

    val tls: Tls? = null,
    val transport: Transport? = null

) {
    companion object {
        fun direct(tag: String = Type.DIRECT) = Outbound(
            type = Type.DIRECT,
            tag = tag
        )
        fun block(tag: String = Type.BLOCK) = Outbound(
            type = Type.BLOCK,
            tag = tag
        )
        fun selector(tag: String = Type.SELECTOR, outbounds: List<String>) = Outbound(
            type = Type.SELECTOR,
            tag = tag,
            outbounds = outbounds
        )
        fun urltest(tag: String = Type.URLTEST, outbounds: List<String>,
                    url: String = "https://www.gstatic.com/generate_204", interval: String = "3m",
                    tolerance: Int = 50) = Outbound(
            type = Type.URLTEST,
            tag = tag,
            outbounds = outbounds,
            url = url,
            interval = interval,
            tolerance = tolerance
        )
        fun trojan(tag: String, server: String, serverPort: Int, password: String,
                   tls: Tls = Tls(enabled = true, disableSni = true, serverName = "", insecure = true),
                   transport: Transport = Transport()) = Outbound(
            type = Type.TROJAN,
            tag = tag,
            server = server,
            serverPort = serverPort,
            password = password,
            tls = tls,
            transport = transport
        )
        fun hysteria(tag: String, server: String, serverPort: Int, serverPorts: List<String>,
                    upMbps: Int = 100, downMbps: Int = 100, authStr: String, disableMtuDiscovery: Boolean = true,
                     tls: Tls = Tls(enabled = true, disableSni = true,
                         serverName = "", insecure = true, alpn = listOf("h3"))) = Outbound(
            type = Type.HYSTERIA,
            tag = tag,
            server = server,
            serverPort = serverPort,
            serverPorts = serverPorts,
            upMbps = upMbps,
            downMbps = downMbps,
            authStr = authStr,
            disableMtuDiscovery = disableMtuDiscovery,
            tls = tls
        )
        fun hysteria2(tag: String, server: String, serverPort: Int, serverPorts: List<String>,
                      upMbps: Int? = null, downMbps: Int? = null, password: String,
                      tls: Tls = Tls(enabled = true, disableSni = true,
                          serverName = "", insecure = true, alpn = listOf("h3"))) = Outbound(
            type = Type.HYSTERIA2,
            tag = tag,
            server = server,
            serverPort = serverPort,
            serverPorts = serverPorts,
            upMbps = upMbps,
            downMbps = downMbps,
            password = password,
            tls = tls
        )
        fun anyTls(tag: String, server: String, serverPort: Int, password: String,
                   tls: Tls = Tls(enabled = true, disableSni = false,
                         serverName = "", insecure = true, utls = Tls.Utls())) = Outbound(
            type = Type.ANYTLS,
            tag = tag,
            server = server,
            serverPort = serverPort,
            password = password,
            tls = tls
        )
    }
    object Type {
        const val DIRECT = "direct"
        const val BLOCK = "block"
        const val SOCKS = "socks"
        const val HTTP = "http"
        const val SHADOWSOCKS = "shadowsocks"
        const val VMESS = "vmess"
        const val TROJAN = "trojan"
        const val WIREGUARD = "wireguard"
        const val HYSTERIA = "hysteria"
        const val VLESS = "vless"
        const val SHADOWTLS = "shadowtls"
        const val TUIC = "tuic"
        const val HYSTERIA2 = "hysteria2"
        const val ANYTLS = "anytls"
        const val TOR = "tor"
        const val SSH = "ssh"
        const val DNS = "dns"
        const val SELECTOR = "selector"
        const val URLTEST = "urltest"
    }

    @Serializable
    data class Tls(
        val enabled: Boolean = true,
        @SerialName("disable_sni")
        val disableSni: Boolean = false,
        @SerialName("server_name")
        val serverName: String = "",
        val insecure: Boolean = false,
        val alpn: List<String>? = null,
        val utls: Utls? = null
    ) {

        @Serializable
        data class Utls(
            val enabled: Boolean = true,
            val fingerprint: String = CHROME,
        ) {
            companion object {
                const val CHROME = "chrome"
                const val FIREFOX = "firefox"
                const val EDGE = "edge"
                const val SAFARI = "safari"
                const val QQ = "qq"
                const val IOS = "ios"
                const val ANDROID = "android"
                const val RANDOM = "random"
                const val RANDOMIZED = "randomized"
            }
        }
    }

    @Serializable
    data class Transport(
        val type: String = WEB_SOCKET,
    ) {
        companion object {
            const val TYPE_HTTP = "http"
            const val WEB_SOCKET = "ws"
            const val QUIC = "quic"
            const val GRPC = "grpc"
            const val HTTP_UPGRADE = "httpupgrade"

        }
    }

}