package com.clashsing.proxylib.schema.singbox.out

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Outbound(
    /** @see [Type] */
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
    val url: String?,

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

    val tls: TlsOut? = null,
    val transport: Transport? = null

) : Parcelable {
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

}
