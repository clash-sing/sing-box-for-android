package com.clashsing.proxylib.schema.singbox.route

import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class RuleActionHijackDns(
    override val action: String = RuleAction.HIJACK_DNS,
    val protocol: String = RouteProtocol.DNS
) : RuleAction