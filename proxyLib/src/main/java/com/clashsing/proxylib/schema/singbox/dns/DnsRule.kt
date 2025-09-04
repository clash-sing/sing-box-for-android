package com.clashsing.proxylib.schema.singbox.dns

import android.os.Parcelable
import com.clashsing.proxylib.schema.singbox.route.RuleAction
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class DnsRule(

    /** @see [RuleAction] */
    val action: String = RuleAction.ROUTE,

    /** @see [Mode] */
    val mode: String = Mode.AND,

    /**
     * @see [DnsServer.Tag]
     */
    val server: String? = null,

    /** @see [ClashMode] */
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
    object ClashMode {
        const val DIRECT = "direct"
        const val GLOBAL = "global"
    }
    object Mode {
        const val AND = "and"
        const val OR = "or"
    }
}