package com.clashsing.proxylib.parser

import com.clashsing.proxylib.ProxyComponent
import com.clashsing.proxylib.SubUserinfo
import com.clashsing.proxylib.schema.SingBox
import com.clashsing.proxylib.schema.customJson
import okhttp3.Headers

abstract class SubParser(val srcContent: String, val headers: Headers) {
    abstract suspend fun getSingBox(): SingBox?
    abstract fun getSubUserInfo(): SubUserinfo?

    suspend fun getDefaultSingBox(): SingBox {
        val content = ProxyComponent.application.assets.open("singbox-config.json").bufferedReader().use { it.readText() }
        return  customJson.decodeFromString<SingBox>(content)
    }

}