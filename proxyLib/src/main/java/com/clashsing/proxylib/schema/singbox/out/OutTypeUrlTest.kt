package com.clashsing.proxylib.schema.singbox.out

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class OutTypeUrlTest(
    override val type: String = OutType.URLTEST,
    override val tag: String,
    val url: String = "",
    /** 测试间隔。 默认使用 3m */
    val interval: String = "3m",
    /** 以毫秒为单位的测试容差。 默认使用 50 */
    val tolerance: Int = 50,
    val outbounds: List<String> = emptyList()
) : OutType, Parcelable
