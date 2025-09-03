package com.clashsing.proxylib.schema.singbox.dns

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class DnsServerTypeFakeIP(
    override val type: String = DnsServerType.FAKEIP,
    override val tag: String = "remote",
    @SerialName("inet4_range")
    val inet4Range: String = "198.18.0.0/15",
    @SerialName("inet6_range")
    val inet6Range: String = "fc00::/18",
) : DnsServerType, Parcelable
