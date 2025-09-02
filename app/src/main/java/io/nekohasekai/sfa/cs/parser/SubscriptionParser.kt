package io.nekohasekai.sfa.cs.parser

import io.nekohasekai.sfa.cs.Clash2SingBox
import io.nekohasekai.sfa.cs.ClashData
import io.nekohasekai.sfa.cs.ClashSingClient
import io.nekohasekai.sfa.cs.ClashSingWrapper
import io.nekohasekai.sfa.cs.SubscriptionUserinfo
import org.json.JSONObject

abstract class SubscriptionParser(val mapSingBox: Map<String, Any?>, val mapClashSing: Map<String, Any?>) {
    abstract fun getFixedContent(): String
    abstract fun setSubscriptionUserinfo(profileId: Long, wrapper: ClashSingWrapper)
}