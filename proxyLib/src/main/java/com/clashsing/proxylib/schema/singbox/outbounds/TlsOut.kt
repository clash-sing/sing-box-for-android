package com.clashsing.proxylib.schema.singbox.outbounds

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class TlsOut(
    val enabled: Boolean = true,
    @SerialName("disable_sni")
    val disableSni: Boolean = false,
    @SerialName("server_name")
    val serverName: String = "",
    val insecure: Boolean = false,
    val alpn: List<String> = emptyList(),
) : Parcelable
