package io.nekohasekai.sfa.cs.parser

import io.nekohasekai.sfa.cs.ClashSingWrapper

abstract class SubscriptionParser(val mapSingBox: Map<String, Any?>, val mapClashSing: Map<String, Any?>) {
    abstract fun getFixedContent(): String
    abstract fun setSubscriptionUserinfo(profileId: Long, wrapper: ClashSingWrapper)
}