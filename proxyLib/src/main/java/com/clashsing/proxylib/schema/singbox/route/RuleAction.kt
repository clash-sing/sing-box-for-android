package com.clashsing.proxylib.schema.singbox.route

import android.os.Parcelable

interface RuleAction : Parcelable {
    val action: String
    companion object {
        const val ROUTE = "route"
        const val REJECT = "reject"
        const val HIJACK_DNS = "hijack-dns"
        const val ROUTE_OPTIONS = "route-options"
        const val SNIFF = "sniff"
        const val RESOLVE = "resolve"
    }
}