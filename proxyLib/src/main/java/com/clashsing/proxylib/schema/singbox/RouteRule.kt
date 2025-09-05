package com.clashsing.proxylib.schema.singbox

import com.clashsing.proxylib.schema.StringOrStringList
import com.clashsing.proxylib.schema.StringOrStringListSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RouteRule(
    /** @see [Action] */
    val action: String = Action.ROUTE,

    /** @see [Mode] */
    val mode: String = Mode.AND,

    /** @see [ClashMode] */
    @SerialName("clash_mode")
    val clashMode: String? = null,

    /** @see [Outbound.tag] */
    val outbound: String? = null,

    /** @see [RouteRuleSet.tag] */
    @SerialName("rule_set")
    val ruleSet: List<String>? = null,

    @SerialName("ip_is_private")
    val ipIsPrivate: Boolean? = null,

    /** @see [Method] */
    val method: String? = null,

    @SerialName("no_drop")
    val noDrop: Boolean? = null,

    @Serializable(with = StringOrStringListSerializer::class)
    @SerialName("protocol")
    val protocols: StringOrStringList? = null
) {
    companion object {
        fun createRoute(outbound: String, clashMode: String?, ruleSet: List<String> = listOf(
            RouteRuleSet.Tag.GEOIP_CN, RouteRuleSet.Tag.GEOSITE_CN), ipIsPrivate: Boolean? = null,
        ): RouteRule = RouteRule(action = Action.ROUTE, outbound = outbound,
            clashMode = clashMode, ruleSet = ruleSet, ipIsPrivate = ipIsPrivate)
        fun createSniff(): RouteRule = RouteRule(action = Action.SNIFF)
        fun createReject(method: String = Method.DEFAULT, noDrop: Boolean? = null,
                         protocols: StringOrStringList = StringOrStringList.ListValue(
                             listOf(
                                 Protocol.QUIC
                             )
                         )
        ): RouteRule = RouteRule(
            action = Action.REJECT, method = method, noDrop = noDrop, protocols = protocols)
        fun createHijackDns(protocols: StringOrStringList = StringOrStringList.ListValue(listOf(Protocol.DNS))): RouteRule = RouteRule(
            action = Action.HIJACK_DNS, protocols = protocols)

        fun getStringList(value: StringOrStringList): List<String> {
            return when (value) {
                is StringOrStringList.StringValue -> listOf(value.value)
                is StringOrStringList.ListValue -> value.value
            }
        }
    }

    object Action {
        const val ROUTE = "route"
        const val REJECT = "reject"
        const val HIJACK_DNS = "hijack-dns"
        const val ROUTE_OPTIONS = "route-options"
        const val SNIFF = "sniff"
        const val RESOLVE = "resolve"
    }
    object Mode {
        const val AND = "and"
        const val OR = "or"
    }
    object ClashMode {
        const val DIRECT = "direct"
        const val GLOBAL = "global"
    }
    object Method {
        const val DEFAULT = "default"
        const val DROP = "drop"
        const val REPLY = "reply"
    }
    object Protocol {
        const val HTTP = "http"
        const val TLS = "tls"
        const val QUIC = "quic"
        const val STUN = "stun"
        const val DNS = "dns"
        const val BITTORRENT = "bittorrent"
        const val DTLS = "dtls"
        const val SSH = "ssh"
        const val RDP = "rdp"
        const val NTP = "ntp"
    }
}