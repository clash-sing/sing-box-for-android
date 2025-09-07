package com.clashsing.proxylib.parser

import com.clashsing.proxylib.ProxyComponent
import com.clashsing.proxylib.SubUserinfo
import com.clashsing.proxylib.schema.SingBox
import com.clashsing.proxylib.schema.customJson
import com.clashsing.proxylib.schema.singbox.Outbound
import okhttp3.Headers

abstract class SubParser(val originSingBox: SingBox?, val srcContent: String, val headers: Headers) {
    protected var _singBox: SingBox? = null
    val singBox: SingBox?
        get() = _singBox

    abstract suspend fun getSingBox(): SingBox?
    abstract fun getSubUserInfo(): SubUserinfo?

    suspend fun getDefaultSingBox(outboundType: String? = null): SingBox {
        val configFile = when (outboundType) {
            Outbound.Type.VLESS -> "singbox-vless-config.json"
            else -> "singbox-config.json"
        }
        val content = ProxyComponent.application.assets.open(configFile).bufferedReader().use { it.readText() }
        return  customJson.decodeFromString<SingBox>(content)
    }

}