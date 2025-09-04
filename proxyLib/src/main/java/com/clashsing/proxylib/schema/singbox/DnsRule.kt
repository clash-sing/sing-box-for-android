package com.clashsing.proxylib.schema.singbox

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class DnsRule(

    /** @see [RouteRule.Action] */
    val action: String = RouteRule.Action.ROUTE,

    /** @see [RouteRule.Mode] */
    val mode: String = RouteRule.Mode.AND,

    /**
     * @see [DnsServer.Tag]
     */
    val server: String? = null,

    /** @see [RouteRule.ClashMode] */
    @SerialName("clash_mode")
    val clashMode: String? = null,

    @SerialName("rule_set")
    val ruleSet: List<String>? = null,

    @SerialName("query_type")
    val queryType: List<String>? = null,

    ) : Parcelable {
    companion object {
        const val GEOIP_CN = "geoip-cn"
        const val GEOSITE_CN = "geosite-cn"
        const val QUERY_TYPE_A = "A"
        const val QUERY_TYPE_AAAA = "AAAA"
        val defaultRuleSet = listOf<String>(GEOIP_CN, GEOSITE_CN)
        val defaultQueryType = listOf<String>(QUERY_TYPE_A, QUERY_TYPE_AAAA)
    }
}