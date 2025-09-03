package com.clashsing.proxylib.schema.singbox.dns

import android.os.Parcelable

interface DnsServerType : Parcelable {
    val type: String
    val tag: String
    companion object {
        const val LOCAL = "local"
        const val HTTPS = "https"
        const val FAKEIP = "fakeip"
    }

}