package com.clashsing.proxylib.schema.singbox.route

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Route(
    val rules: List<RuleAction>,
    val ruleSet: List<RuleSet>,
    val final: String? = null

) : Parcelable {
    init {
        TODO ("未完成")
    }
}
