package com.clashsing.proxylib.schema.singbox.route

import android.os.Parcelable
import com.clashsing.proxylib.schema.singbox.out.OutType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class RuleSetGeoSiteCN(
    override val tag: String = RuleSet.TAG_GEOSITE_CN,
    override val type: String = RuleSet.TYPE_REMOTE,
    val format: String = "binary",
    val url: String = "https://testingcf.jsdelivr.net/gh/SagerNet/sing-geosite@rule-set/geosite-cn.srs",
    @SerialName("download_detour")
    val downloadDetour: String? = null
) : RuleSet
