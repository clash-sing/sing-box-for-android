package com.clashsing.proxylib.schema.singbox.out

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class OutTypeHysteria(
    override val type: String = OutType.HYSTERIA,
    override val tag: String,
    val server: String,
    @SerialName("server_port")
    val serverPort: Int,
    /** @sample: ["2080:3000"] */
    @SerialName("server_ports")
    val serverPorts: List<String> = emptyList(),
    @SerialName("up_mbps")
    val upMbps: Int = 100,
    @SerialName("down_mbps")
    val downMbps: Int = 100,
    @SerialName("auth_str")
    val authStr: String = "",
    /** 强制为 Linux 和 Windows 以外的系统启用 */
    @SerialName("disable_mtu_discovery")
    val disableMtuDiscovery: Boolean = true,
    val tls: TlsOut = TlsOut(
        enabled = true,
        disableSni = true,
        insecure = true,
        serverName = "",
        alpn = listOf("h3")
    )
) : OutType, Parcelable