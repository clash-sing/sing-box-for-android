package com.clashsing.proxylib.schema.singbox.out

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class OutTypeAnyTls(
    override val type: String = OutType.ANYTLS,
    override val tag: String,
    val server: String,
    @SerialName("server_port")
    val serverPort: Int,
    val password: String,
    val tls: TlsOut = TlsOut(
        enabled = true,
        disableSni = false,
        serverName = "",
        insecure = true,
        utls = TlsOut.Utls()
    )
) : Parcelable, OutType