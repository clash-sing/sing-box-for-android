package com.clashsing.proxylib.schema.singbox.route

import android.os.Parcelable

interface RuleSet : Parcelable {
    val tag: String
    val type: String
    companion object {
        const val TYPE_INLINE = "inline"
        const val TYPE_LOCAL = "local"
        const val TYPE_REMOTE = "remote"
        const val TAG_GEOIP_CN = "geoip-cn"
        const val TAG_GEOSITE_CN = "geosite-cn"
    }
}