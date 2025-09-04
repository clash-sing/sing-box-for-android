package com.clashsing.proxylib.schema.singbox.route

import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class RuleActionReject(
    override val action: String = RuleAction.REJECT,
    val method: String = METHOD_DEFAULT,
    @SerialName("no_drop")
    val noDrop: Boolean?,
    @SerialName("protocol")
    val protocols: List<String> = listOf(RouteProtocol.QUIC)
) : RuleAction {
    companion object {
        const val METHOD_DEFAULT = "default"
        const val METHOD_DROP = "drop"
        const val METHOD_REPLY = "reply"
    }
}
