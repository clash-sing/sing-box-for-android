package com.clashsing.proxylib.parser

import com.clashsing.proxylib.schema.SingBox
import okhttp3.Headers

abstract class SubParser(val srcContent: String, val headers: Headers) {
    abstract fun getSingBox(): SingBox?
    abstract fun getSubUserInfo(): String?
}