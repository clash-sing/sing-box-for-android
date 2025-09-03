package com.clashsing.proxylib.schema.singbox.out

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Transport(
    val type: String = WEB_SOCKET,
) : Parcelable {
    companion object {
        const val TYPE_HTTP = "http"
        const val WEB_SOCKET = "ws"
        const val QUIC = "quic"
        const val GRPC = "grpc"
        const val HTTP_UPGRADE = "httpupgrade"

    }
}
