package com.clashsing.proxylib.schema.singbox

import android.os.Parcelable
import com.clashsing.proxylib.schema.singbox.dns.Dns
import com.clashsing.proxylib.schema.singbox.out.OutType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class SingBox(
    val dns: Dns,
    val outbounds: List<OutType>,
    val experimental: SingBoxExp = SingBoxExp(),
    val log: SingBoxLog = SingBoxLog()
) : Parcelable
