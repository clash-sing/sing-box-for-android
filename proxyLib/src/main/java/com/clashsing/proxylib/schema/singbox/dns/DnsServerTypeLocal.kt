package com.clashsing.proxylib.schema.singbox.dns

import android.os.Parcelable
import com.clashsing.proxylib.schema.singbox.out.OutType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class DnsServerTypeLocal(
    override val type: String = DnsServerType.LOCAL,
    override val tag: String = "system"
) : DnsServerType, Parcelable
