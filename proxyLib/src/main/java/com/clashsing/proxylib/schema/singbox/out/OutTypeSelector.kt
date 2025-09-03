package com.clashsing.proxylib.schema.singbox.out

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class OutTypeSelector(
    override val type: String = OutType.SELECTOR,
    override val tag: String,
    val outbounds: List<String> = emptyList(),
) : OutType
