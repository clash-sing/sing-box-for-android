package com.clashsing.proxylib.schema.singbox.out

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class OutTypeTrojan(
    override val type: String = OutType.Type.HYSTERIA2,
    override val tag: String,
    val server: String,
    @SerialName("server_port")
    val serverPort: Int,
    val password: String,
    val tls: TlsOut = TlsOut(),
    val transport: Transport = Transport()
) : OutType, Parcelable
