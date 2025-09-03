package com.clashsing.proxylib.schema.singbox.dns

interface DnsServerType {
    val type: String
    val tag: String
    companion object {
        const val LOCAL = "local"
        const val HTTPS = "https"
        const val FAKEIP = "fakeip"
    }

}