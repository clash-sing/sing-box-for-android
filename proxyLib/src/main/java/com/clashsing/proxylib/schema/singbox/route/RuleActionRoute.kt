package com.clashsing.proxylib.schema.singbox.route

import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class RuleActionRoute(
    override val action: String = RuleAction.ROUTE,
    val clashMode: String? = null,
    /** @see [com.clashsing.proxylib.schema.singbox.out.Outbound.tag] */
    val outbound: String,
    val ruleSet: List<String>? = null,
    @SerialName("ip_is_private")
    val ipIsPrivate: Boolean? = null
) : RuleAction {
    companion object {
        const val CLASH_MODE_DIRECT = "direct"
        const val CLASH_MODE_GLOBAL = "global"
        val defaultRuleSet = listOf(RuleSetData.Tag.GEOIP_CN, RuleSetData.Tag.GEOSITE_CN)
    }
}
