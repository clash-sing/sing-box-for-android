package com.clashsing.proxylib.schema.singbox.route

import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class RuleActionSniff(
    override val action: String = RuleAction.SNIFF
) : RuleAction