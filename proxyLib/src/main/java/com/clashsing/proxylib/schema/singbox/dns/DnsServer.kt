package com.clashsing.proxylib.schema.singbox.dns

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class DnsServer(
    /** @see: [Type] */
    val type: String,

    /** @see: [Tag] */
    val tag: String,

    /** @see: [ALI_DNS] */
    val server: String,

    /** @see: [InetRange] */
    @SerialName("inet4_range")
    val inet4Range: String,

    /** @see: [InetRange] */
    @SerialName("inet6_range")
    val inet6Range: String
) : Parcelable {
    companion object {
        const val ALI_DNS = "223.5.5.5"
    }
    object Type {
        const val LOCAL = "local"
        const val HTTPS = "https"
        const val FAKEIP = "fakeip"
    }
    object Tag {
        const val REMOTE_FOR_FAKEIP = "remote"
        const val SYSTEM_FOR_LOCAL = "system"
        const val ALIDOH_FOR_HTTPS = "alidoh"
    }
    object InetRange {
        const val INET4_RANGE = "198.18.0.0/15"
        const val INET6_RANGE = "fc00::/18"
    }
}
