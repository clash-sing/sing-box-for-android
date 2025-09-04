package com.clashsing.proxylib.schema.singbox.route

import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class RuleSetGeoIpCN(
    override val tag: String = RuleSet.TAG_GEOIP_CN,
    override val type: String = RuleSet.TYPE_REMOTE,
    val format: String = "binary",
    val url: String = "https://testingcf.jsdelivr.net/gh/SagerNet/sing-geoip@rule-set/geoip-cn.srs",
    @SerialName("download_detour")
    val downloadDetour: String? = null
) : RuleSet
