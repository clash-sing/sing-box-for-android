package com.clashsing.proxylib.schema.singbox.dns

import android.os.Parcelable
import com.clashsing.proxylib.schema.singbox.out.OutType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class DnsServerTypeHttps(
    override val type: String = DnsServerType.HTTPS,
    override val tag: String = DnsServerType.HTTPS,
    val server: String = "223.5.5.5"
) : DnsServerType
