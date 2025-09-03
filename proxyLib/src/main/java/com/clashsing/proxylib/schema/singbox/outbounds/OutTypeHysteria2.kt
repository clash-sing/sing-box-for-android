package com.clashsing.proxylib.schema.singbox.outbounds

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class OutTypeHysteria2(
    override val type: String = OutType.Type.HYSTERIA2,
    override val tag: String,
    val server: String,
    @SerialName("server_port")
    val serverPort: Int,
    /** @sample: ["2080:3000"] */
    @SerialName("server_ports")
    val serverPorts: List<String> = emptyList(),
    val password: String,
    val tls: TlsOut = TlsOut().copy(alpn = listOf("h3"))
) : OutType, Parcelable