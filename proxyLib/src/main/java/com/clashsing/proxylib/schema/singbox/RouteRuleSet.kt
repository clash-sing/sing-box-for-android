package com.clashsing.proxylib.schema.singbox

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RouteRuleSet(
    val type: String,
    val tag: String,
    val format: String,
    val url: String,

    /**
     * 用于下载规则集的出站的标签。如果为空，将使用默认出站。
     * @see Outbound.tag
     */
    @SerialName("download_detour")
    val downloadDetour: String? = null,
) {
    object Type {
        const val INLINE = "inline"
        const val LOCAL = "local"
        const val REMOTE = "remote"
    }
    object Tag {
        const val GEOIP_CN = "geoip-cn"
        const val GEOSITE_CN = "geosite-cn"
    }
    object Format {
        const val BINARY = "binary"
        const val SOURCE = "source"
    }
    object Url {
        const val GEOIP_CN = "https://testingcf.jsdelivr.net/gh/SagerNet/sing-geoip@rule-set/geoip-cn.srs"
        const val GEOSITE_CN = "https://testingcf.jsdelivr.net/gh/SagerNet/sing-geosite@rule-set/geosite-cn.srs"
    }
}
